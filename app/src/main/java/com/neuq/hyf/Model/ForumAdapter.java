package com.neuq.hyf.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.neuq.hyf.R;

import java.util.ArrayList;
import java.util.List;

public class ForumAdapter extends BaseAdapter {
    private List<ForumItemData> forumItemDataList = new ArrayList<>();
    private Context context;

    public ForumAdapter(List<ForumItemData> forumItemDataList, Context context) {
        this.context = context;
        this.forumItemDataList = forumItemDataList;
    }

    @Override
    public int getCount() {
        return forumItemDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return forumItemDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ForumItem forumItem;
        if (view == null) {
            forumItem = new ForumItem();
            view = LayoutInflater.from(context).inflate(R.layout.forum_item, viewGroup, false);
            forumItem.state = view.findViewById(R.id.state);
            forumItem.answerNumber = view.findViewById(R.id.answerNumber);
            forumItem.answerNumberTextview = view.findViewById(R.id.answerNumberTextview);
            forumItem.question = view.findViewById(R.id.questionx);
            forumItem.time = view.findViewById(R.id.time);
            forumItem.viewNumber = view.findViewById(R.id.viewNumber);
            forumItem.viewNumberTextView = view.findViewById(R.id.viewNumberTextView);
            view.setTag(forumItem);
        } else {
            forumItem = (ForumItem) view.getTag();
        }
        ForumItemData forumItemData = forumItemDataList.get(i);
        forumItem.viewNumberTextView.setText(forumItemData.viewNumberTextView);
        forumItem.viewNumber.setImageBitmap(forumItemData.viewNumber);
        forumItem.time.setText(forumItemData.time);
        forumItem.question.setText(forumItemData.question);
        forumItem.answerNumberTextview.setText(forumItemData.answerNumberTextview);
        forumItem.state.setImageBitmap(forumItemData.state);
        forumItem.answerNumber.setImageBitmap(forumItemData.answerNumber);
        return view;
    }
}
