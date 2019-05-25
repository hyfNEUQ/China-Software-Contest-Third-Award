package com.neuq.hyf.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.neuq.hyf.Acitivity.ForumItemActivity;
import com.neuq.hyf.Model.ForumAdapter;
import com.neuq.hyf.Model.ForumItemData;
import com.neuq.hyf.R;
import com.neuq.hyf.Threads.GetForumDocumentThread;

import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ForumView implements View.OnClickListener {
    private Context context = null;
    private View view;
    private ExpandableHeightListView expandableHeightListView = null;
    private ReleaseForumDialog releaseForumDialog = null;
    private Button add, refresh;
    private RadioButton time, heat;
    private GetForumDocumentThread getForumDocumentThread = null;
    private Handler refreshHandler;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.CHINESE);
    private ForumAdapter forumAdapter = null;
    private List<ForumItemData> forumItemDataList = null;

    public ForumView(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.forum_view, null, false);
        expandableHeightListView = view.findViewById(R.id.forum_expandable_listview);
        add = view.findViewById(R.id.add);
        add.setOnClickListener(this);
        refresh = view.findViewById(R.id.refresh);
        refresh.setOnClickListener(this);
        time = view.findViewById(R.id.radioTimeButton);
        heat = view.findViewById(R.id.radioHeatButton);
        initHandler();
        handleRefresh();
        setListItemOnclickListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                releaseForumDialog = new ReleaseForumDialog(this.context);
                releaseForumDialog.show();
                break;
            case R.id.refresh:
                handleRefresh();
                break;
        }
    }

    private void handleRefresh() {
        getForumDocumentThread = new GetForumDocumentThread();
        Document doc = new Document();
        if (time.isChecked()) {
            doc.append("类型", "时间");
        } else {
            doc.append("类型", "热度");
        }
        doc.append("页", 1);
        getForumDocumentThread.documentQuery = doc;
        getForumDocumentThread.refreshHandler = refreshHandler;
        getForumDocumentThread.start();
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        refreshHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                forumItemDataList = new ArrayList<>();
                Document doc = getForumDocumentThread.documentResult;
                Iterator it = doc.entrySet().iterator();
                while (it.hasNext()) {
                    ForumItemData forumItemData = new ForumItemData();
                    Map.Entry map = (Map.Entry) it.next();
                    Document d = Document.parse(map.getValue().toString());
                    forumItemData.time = timeFormat.format(Long.valueOf(d.get("时间").toString()));
                    forumItemData.question = "问题描述:" + d.get("内容").toString();
                    forumItemData.viewNumberTextView = d.get("浏览人数").toString() + "人浏览过";
                    forumItemData.viewNumber = BitmapFactory.decodeResource(context.getResources(), R.drawable.view);
                    forumItemData.answerNumber = BitmapFactory.decodeResource(context.getResources(), R.drawable.comment);
                    forumItemData.answerNumberTextview = d.get("回复数量").toString() + "条回复";
                    forumItemData.state = BitmapFactory.decodeResource(context.getResources(), R.drawable.help);
                    forumItemDataList.add(forumItemData);
                }
                forumAdapter = new ForumAdapter(forumItemDataList, context);
                expandableHeightListView.setAdapter(forumAdapter);
            }
        };
    }

    private void setListItemOnclickListener() {
        expandableHeightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                handelItemClick(i);
            }
        });
    }

    private void handelItemClick(int id) {
        Document doc = getForumDocumentThread.documentResult;
        if (doc == null) return;
        doc = Document.parse(doc.get(String.valueOf(id)).toString());
        Intent intent = new Intent();
        intent.setClass(context, ForumItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("文档", doc.toJson());
        intent.putExtra("Bundle", bundle);
        context.startActivity(intent);
    }

    public View getView() {
        return view;
    }
}
