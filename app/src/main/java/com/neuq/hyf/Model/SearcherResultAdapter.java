package com.neuq.hyf.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.neuq.hyf.R;

import java.util.ArrayList;
import java.util.List;

public class SearcherResultAdapter extends BaseAdapter {
    private Context context;
    private List<SearcherResult> myList = new ArrayList<>();

    public SearcherResultAdapter(Context context, List<SearcherResult> list) {
        this.context = context;
        this.myList = list;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int i) {
        return myList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SearcherResultItem searcherResultItem = null;
        if (view == null) {
            view = LayoutInflater.from(
                    context).inflate(R.layout.searcher_result_item, viewGroup, false);
            searcherResultItem = new SearcherResultItem();
            searcherResultItem.question = view.findViewById(R.id.question);
            searcherResultItem.answer = view.findViewById(R.id.answer);
            searcherResultItem.sourceAndScore = view.findViewById(R.id.sourceAndScore);
            searcherResultItem.shineButton = view.findViewById(R.id.shinebutton);
            searcherResultItem.shineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "关联性+5%", Toast.LENGTH_SHORT).show();
                }
            });
            view.setTag(searcherResultItem);
        } else {
            searcherResultItem = (SearcherResultItem) view.getTag();
        }
        searcherResultItem.question.setText(myList.get(i).question);
        searcherResultItem.answer.setText(myList.get(i).answer);
        searcherResultItem.sourceAndScore.setText("来源: " + myList.get(i).source + "    相关度" + myList.get(i).score + "%");
        return view;
    }
}
