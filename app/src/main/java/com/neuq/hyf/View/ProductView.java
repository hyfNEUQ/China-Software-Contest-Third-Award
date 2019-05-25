package com.neuq.hyf.View;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.kongzue.dialog.listener.InputDialogOkButtonClickListener;
import com.kongzue.dialog.v2.InputDialog;
import com.lid.lib.LabelImageView;
import com.neuq.hyf.Acitivity.ProductAcivity;
import com.neuq.hyf.Model.UserInfo;
import com.neuq.hyf.R;
import com.neuq.hyf.Threads.ClawBaiDuPicture;

import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.kongzue.dialog.v2.DialogSettings.STYLE_IOS;

public class ProductView extends Thread {
    private ProductAcivity productAcivity = ProductAcivity.productAcivity;
    public Document document = null;
    private View view = null;
    private Context context = null;
    public String type, brand;
    private ExpandableHeightListView expandableListView = null;
    private LabelImageView smartImageView = null;
    private Button button = null;
    private AddProductViewDialog addProductViewDialog = null;
    private static ProductView productView = null;
    private UserInfo userInfo = UserInfo.getInstance();
    private Handler updateListViewHandler = null;
    private Handler updateImageViewHandler = null;
    private ClawBaiDuPicture clawBaiDuPicture = null;

    public ProductView(Context context, Document document) throws IOException {
        this.context = context;
        this.document = document;
        view = LayoutInflater.from(
                context).inflate(R.layout.product_view, null, false);
        expandableListView = (ExpandableHeightListView) view.findViewById(R.id.expandable_listview);
        addProductViewDialog = new AddProductViewDialog(context);
        expandableListView.setExpanded(true);
        smartImageView = view.findViewById(R.id.image);
        button = view.findViewById(R.id.add_new_attribute_button);
        setButton();
        handlerInit();
        if (document != null) {
            updateImageView();
            updateListView();
        }
        productView = this;
    }

    @SuppressLint("HandlerLeak")
    private void handlerInit() {
        updateListViewHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        updateImageViewHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                smartImageView.setImageBitmap(clawBaiDuPicture.bitmap);
            }
        };
    }


    public void updateListView() throws IOException {
        userInfo.setMachineInfo(document);
        Iterator it = document.entrySet().iterator();
        final List<String> myList = new ArrayList<>();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            if (key.equals("_id") || value.equals("")) continue;
            String item = key + ": " + value;
            if (key.equals("imagelist")) continue;
            myList.add(item);
        }
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, myList);
        expandableListView.setAdapter(itemsAdapter);
        expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                InputDialog.build(context, "修改设备参数", "请输入要修改的设备参数", "确定更改", new InputDialogOkButtonClickListener() {
                    @Override
                    public void onClick(Dialog dialog, String inputText) {
                        String[] a = myList.get(i).split(": ");
                        String key = a[0];
                        document.put(key, inputText);
                        try {
                            updateListView();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        hideInput();
                    }
                }, "取消更改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).setDialogStyle(STYLE_IOS).showDialog();
            }
        });
    }

    private void setButton() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductViewDialog.show();
            }
        });
    }

    private void updateImageView() {
        clawBaiDuPicture = new ClawBaiDuPicture();
        clawBaiDuPicture.updateImageViewHandler = updateImageViewHandler;
        if (document.get("品牌") != null) {
            clawBaiDuPicture.keyword = document.get("品牌").toString();
            clawBaiDuPicture.start();
        }
    }

    public View getView() {
        return this.view;
    }

    public static ProductView getProductView() {
        return productView;
    }

    private void hideInput() {
        InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputmanger != null) inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
