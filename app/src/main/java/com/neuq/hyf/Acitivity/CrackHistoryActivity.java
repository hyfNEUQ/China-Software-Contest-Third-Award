package com.neuq.hyf.Acitivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.neuq.hyf.Model.CrackHistory;
import com.neuq.hyf.Model.CrackHistoryAdapter;
import com.neuq.hyf.Model.CrackHistoryItem;
import com.neuq.hyf.Model.CrackHistoryItemData;
import com.neuq.hyf.Model.UserInfo;
import com.neuq.hyf.R;
import com.neuq.hyf.Threads.CrackHistoryThread;
import com.neuq.hyf.View.ReleaseCrackDialog;

import java.util.ArrayList;
import java.util.List;

public class CrackHistoryActivity extends Activity implements View.OnClickListener {
    private View view = null;
    private Context context = null;
    private ExpandableHeightListView expandableListView = null;
    private FloatingActionButton floatingActionButton = null;
    private ReleaseCrackDialog releaseCrackDialog = null;
    private static Handler getCrackHistoryHandler = null;
    private CrackHistoryThread crackHistoryThread = null;
    private UserInfo userInfo = UserInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        view = LayoutInflater.from(context).inflate(R.layout.crack_history, null, false);
        setContentView(view);
        expandableListView = view.findViewById(R.id.crackList);
        floatingActionButton = view.findViewById(R.id.addCrackHistory);
        floatingActionButton.setOnClickListener(this);
        handleHandlerMessage();
        crackHistoryThread = new CrackHistoryThread(userInfo.getAccountNumber(), getCrackHistoryHandler);
        crackHistoryThread.start();
    }

    //初始化UI
    private void updateUi(List<CrackHistory> list) {
        if(list.size()<=0) return;
        //更新ListView
        CrackHistoryAdapter crackHistoryAdapter = new CrackHistoryAdapter(context);
        List<CrackHistoryItemData> crackHistoryItemDataList = new ArrayList<>();
        for (CrackHistory l : list) {
            CrackHistoryItemData crackHistoryItemData = new CrackHistoryItemData();
            crackHistoryItemData.crackInfo=l.crackDescription;
            crackHistoryItemData.method=l.crackSolveMethod;
            crackHistoryItemData.time=l.time;
            crackHistoryItemDataList.add(crackHistoryItemData);
        }
        crackHistoryAdapter.crackHistoryItemDataList = crackHistoryItemDataList;
        expandableListView.setAdapter(crackHistoryAdapter);
        //
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addCrackHistory:
                releaseCrackDialog = new ReleaseCrackDialog(this);
                releaseCrackDialog.show();
        }
    }

    //处理handler回调时间
    @SuppressLint("HandlerLeak")
    private void handleHandlerMessage() {
        getCrackHistoryHandler = new Handler() {
            public void handleMessage(Message message) {
                if (!crackHistoryThread.isAlive() && crackHistoryThread.getList() != null) {
                    updateUi(crackHistoryThread.getList());
                }
            }
        };
    }
}
