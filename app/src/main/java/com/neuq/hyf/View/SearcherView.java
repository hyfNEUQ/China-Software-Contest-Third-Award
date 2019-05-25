package com.neuq.hyf.View;

import android.annotation.SuppressLint;
import android.content.Context;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.neuq.hyf.Model.SearcherResult;
import com.neuq.hyf.Model.SearcherResultAdapter;
import com.neuq.hyf.Model.SearcherResultItem;
import com.neuq.hyf.R;
import com.neuq.hyf.Threads.GetQuestionAnswerDataBaseThread;
import com.neuq.hyf.Threads.GetQustionAnswerOnlineThread;
import com.neuq.hyf.Utils.PrinterUtils.Printer;

import org.bson.Document;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class SearcherView {
    private View view;
    private Context context;
    private MaterialSearchView searchView;
    private ExpandableHeightListView expandableHeightListView;
    private SearcherResultAdapter searcherResultAdapter = null;
    private Map<String, Integer> searchermap = new HashMap<>();
    private Handler updateListViewHandler = null;
    private GetQustionAnswerOnlineThread getQustionAnswerOnlineThread = null;
    private GetQuestionAnswerDataBaseThread getQuestionAnswerDataBaseThread = null;
    private SpinKitView spin_kit = null;
    private List<SearcherResult> list = null;
    private DecimalFormat df = new DecimalFormat("#.00");

    public SearcherView(View view, Context context) {
        this.view = view;
        this.context = context;
        searchView = view.findViewById(R.id.search_view);
        expandableHeightListView = view.findViewById(R.id.searcher_expandable_listview);
        spin_kit = view.findViewById(R.id.spin_kit);
        initHandler();
        setListener();
    }

    public void setListener() {
        searchView.setVoiceSearch(true);
        searchView.setCursorDrawable(R.drawable.color_cursor_white);
        //searchView.setSuggestions(context.getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleQueryAnnoation(query, "刷新");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }

    //处理查询动作
    private void handleQueryAnnoation(String query, String type) {
        list = new ArrayList<>();
        int pn = 0;
        if (searchermap.get(query) == null) {
            searchermap.put(query, 0);
        } else {
            switch (type) {
                case "刷新":
                    pn++;
                    searchermap.put(query, pn);
                    break;
            }
        }
        getQustionAnswerOnlineThread = new GetQustionAnswerOnlineThread();
        getQustionAnswerOnlineThread.question = query;
        getQustionAnswerOnlineThread.pn = String.valueOf(pn);
        getQustionAnswerOnlineThread.updateListViewHandler = updateListViewHandler;
        getQustionAnswerOnlineThread.start();
        getQuestionAnswerDataBaseThread = new GetQuestionAnswerDataBaseThread();
        getQuestionAnswerDataBaseThread.question = query;
        getQuestionAnswerDataBaseThread.updateListViewHandler = updateListViewHandler;
        getQuestionAnswerDataBaseThread.start();
        spin_kit.setVisibility(View.VISIBLE);
    }

    //初始化Handler
    @SuppressLint("HandlerLeak")
    private void initHandler() {
        //此handler专用于更新listview
        updateListViewHandler = new Handler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void handleMessage(Message msg) {
                switch (msg.arg1) {
                    case 1:
                        Document documentDataBaseResult = getQuestionAnswerDataBaseThread.document;
                        for (int i = 0; i < 5; i++) {
                            try {
                                Document doc = Document.parse(documentDataBaseResult.get(String.valueOf(i)).toString());
                                String crack = doc.get("故障").toString();
                                //String question = doc.get("原因").toString();
                                String method = doc.get("解决方法").toString();
                                SearcherResult searcherResult = new SearcherResult();
                                searcherResult.question = "问题描述：" + crack;
                                if (doc.get("原因") != null)
                                    searcherResult.answer = "原因：" + doc.get("原因").toString() + "\n" + "解决方法：" + method;
                                else searcherResult.answer = "解决方法：" + method;
                                searcherResult.source = "知识库";
                                searcherResult.score = 100 * Double.valueOf(df.format(Double.valueOf((doc.get("分数").toString()))));
                                list.add(searcherResult);
                            } catch (Exception e) {

                            }
                        }
                        break;
                    case 2:
                        Document documentResult = getQustionAnswerOnlineThread.document;
                        for (int i = 0; i <= 9; i++) {
                            try {
                                Document doc = Document.parse(documentResult.get(String.valueOf(i)).toString());
                                Printer.print(doc);
                                SearcherResult searcherResult = new SearcherResult();
                                searcherResult.question = "问题描述：" + doc.get("故障").toString();
                                searcherResult.answer = "推荐答案：" + doc.get("答案").toString();
                                searcherResult.source = "问答平台";
                                searcherResult.score = 100 * Double.valueOf(df.format(Double.valueOf((doc.get("分数").toString()))));
                                list.add(searcherResult);
                            }
                            catch (Exception e)
                            {
                                continue;
                            }
                        }
                }
                list.sort(new Comparator<SearcherResult>() {
                    @Override
                    public int compare(SearcherResult searcherResult, SearcherResult t1) {
                        if(searcherResult.score>t1.score) return -1;
                        else if(searcherResult.score<t1.score) return 1;
                        else return 0;
                    }
                });
                searcherResultAdapter = new SearcherResultAdapter(context, list);
                expandableHeightListView.setAdapter(searcherResultAdapter);
                if (!(getQuestionAnswerDataBaseThread.isAlive() && getQuestionAnswerDataBaseThread.isAlive()))
                    spin_kit.setVisibility(View.GONE);
            }
        };
    }
}
