package com.neuq.hyf.Acitivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;

import com.kongzue.dialog.listener.InputDialogOkButtonClickListener;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.v2.InputDialog;
import com.neuq.hyf.Model.UserInfo;
import com.neuq.hyf.R;
import com.neuq.hyf.Threads.MachineQueryThread;
import com.neuq.hyf.View.ProductView;

import org.bson.Document;

import java.io.IOException;

public class ProductAcivity extends AppCompatActivity {
    private ProductView productView = null;
    private String machineInfo = null;
    private Context context = null;
    public static ProductAcivity productAcivity;
    private UserInfo userInfo = UserInfo.getInstance();

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view);
        context = this;
        productAcivity = this;
        if (userInfo.getMachineInfo() == null) inputMachineInfo();
        else {
            try {
                productView = new ProductView(context, userInfo.getMachineInfo());
            } catch (IOException e) {
                e.printStackTrace();
            }
            setContentView(productView.getView());
        }
    }

    private void inputMachineInfo() {
        InputDialog.show(this, "添加数控机床", "请出入机床品牌和型号（输入格式样例：卫汉数控-CK）：", new InputDialogOkButtonClickListener() {
            @Override
            public void onClick(Dialog dialog, String inputText) {
                dialog.dismiss();
                Document document = null;
                try {
                    document = getDocument(inputText);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (document != null) {
                    try {
                        productView = new ProductView(context, document);
                        setContentView(productView.getView());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).setInputInfo(new InputInfo().setMAX_LENGTH(20).setInputType(InputType.TYPE_NULL));
    }

    public Document getDocument(String documentInfo) throws IOException {
        if (documentInfo == null) return null;
        else {
            String brand = null;
            String type = null;
            if (documentInfo.startsWith("-")) {
                type = documentInfo.replace("-", "");
            } else if (documentInfo.endsWith("-") || !documentInfo.contains("-")) {
                brand = documentInfo.replace("-", "");
            } else {
                String[] a = documentInfo.split("-");
                brand = a[0];
                type = a[1];
            }
            MachineQueryThread machineQueryThread = new MachineQueryThread();
            machineQueryThread.type = type;
            machineQueryThread.brand = brand;
            machineQueryThread.start();
            while (machineQueryThread.isAlive()) {
            }
            return machineQueryThread.document;
        }
    }

}
