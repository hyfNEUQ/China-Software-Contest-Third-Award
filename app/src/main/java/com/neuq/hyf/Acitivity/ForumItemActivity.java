package com.neuq.hyf.Acitivity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.neuq.hyf.R;
import com.neuq.hyf.Threads.UpdateForumDataThread;
import com.neuq.hyf.View.DialogFactory;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ForumItemActivity extends AppCompatActivity implements View.OnClickListener {
    private TitleBar titleBar = null;
    private TextView crack = null;
    private ExpandableTextView expandableTextView = null;
    private ExpandableHeightListView expandableHeightListView = null;
    private Document document;
    private Context context;
    private Handler handler = null;
    private Button submitButton = null;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.CHINESE);
    private MaterialEditText editText = null;
    private UpdateForumDataThread updateForumDataThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_item);
        context = this;
        //获取传来的参数
        Intent i = getIntent();
        document = Document.parse(i.getBundleExtra("Bundle").get("文档").toString());
        titleBar = findViewById(R.id.tilebar);
        crack = findViewById(R.id.crack);
        expandableTextView = findViewById(R.id.answer);
        expandableHeightListView = findViewById(R.id.searcher_expandable_listview_comment);
        submitButton = findViewById(R.id.submitbutton);
        editText = findViewById(R.id.edit_text2);
        submitButton.setOnClickListener(this);
        initUi();
    }

    public void initUi() {
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {
            }

            @Override
            public void onRightClick(View v) {
            }
        });
        crack.setText("主题：" + document.get("内容").toString());
        String machineInfo = "";
        Iterator itx = Document.parse(document.get("机主设备信息").toString()).entrySet().iterator();
        while (itx.hasNext()) {
            Map.Entry map = (Map.Entry) itx.next();
            String key = map.getKey().toString();
            String value = map.getValue().toString();
            if (key.equals("_id")) continue;
            machineInfo += key + " :" + value + "\n";
        }
        expandableTextView.setText("机主设备信息：" + "\n" + machineInfo);
        List<String> list = new ArrayList<>();
        try {
            Document doc = Document.parse(document.get("评论列表").toString());
            Iterator it = doc.entrySet().iterator();
            while (it.hasNext()) {
                String content = "";
                Map.Entry entry = (Map.Entry) it.next();
                Document d = Document.parse(entry.getValue().toString());
                {
                    Iterator ite = d.entrySet().iterator();
                    while (ite.hasNext()) {
                        Map.Entry map = (Map.Entry) ite.next();
                        content += map.getKey() + ":  ";
                        if (map.getKey().toString().equals("时间"))
                            content += timeFormat.format(Long.valueOf(map.getValue().toString())) + "\n";
                        else content += map.getValue() + "\n";
                    }
                }
                list.add(content);
            }
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
            expandableHeightListView.setAdapter(itemsAdapter);
        }
        catch (Exception e)
        {

        }
    }

    private void handleSubmitButton() {
        updateForumDataThread = new UpdateForumDataThread();
        Document docOrigin = this.document;
        Document docNew = new Document();
        Iterator ito = docOrigin.entrySet().iterator();
        while (ito.hasNext()) {
            Map.Entry entry = (Map.Entry) ito.next();
            switch (entry.getKey().toString()) {
                case "回复数量":
                    docNew.append("回复数量", String.valueOf(Integer.valueOf(entry.getValue().toString()) + 1));
                    break;
                case "评论列表":
                    Document documentList = Document.parse(entry.getValue().toString());
                    Document docNewComment = new Document();
                    docNewComment.append("时间", String.valueOf(System.currentTimeMillis()));
                    docNewComment.append("解决方法", editText.getText().toString());
                    docNewComment.append("回答者", "匿名用户");
                    documentList.append(String.valueOf(documentList.entrySet().size()+1), docNewComment.toJson());
                    docNew.append("评论列表", documentList.toJson());
                    break;
                default:
                    docNew.append(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        updateForumDataThread.docOrigin = docOrigin;
        updateForumDataThread.docNew = docNew;
        updateForumDataThread.start();
        DialogFactory.getSuccessCommentDialog(context).showDialog();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitbutton:
                handleSubmitButton();
                break;
        }
    }
}
