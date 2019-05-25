package com.neuq.hyf.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.neuq.hyf.R;

import java.util.ArrayList;
import java.util.List;

public class CrackHistoryAdapter extends BaseAdapter {
    public List<CrackHistoryItemData> crackHistoryItemDataList = new ArrayList<>();
    private Context context;

    public CrackHistoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return crackHistoryItemDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CrackHistoryItem crackHistoryItem;
        if (view == null) {
            crackHistoryItem = new CrackHistoryItem();
            view = LayoutInflater.from(context).inflate(R.layout.crack_history_item, viewGroup, false);
            crackHistoryItem.time = view.findViewById(R.id.time);
            crackHistoryItem.materialEditText1 = view.findViewById(R.id.edit_text1);
            crackHistoryItem.materialEditText2 = view.findViewById(R.id.edit_text2);
            view.setTag(crackHistoryItem);
        } else {
            crackHistoryItem = (CrackHistoryItem) view.getTag();
        }
        CrackHistoryItemData crackHistoryItemData = crackHistoryItemDataList.get(i);
        crackHistoryItem.time.setText(crackHistoryItemData.time);
        crackHistoryItem.materialEditText1.setText(crackHistoryItemData.crackInfo);
        crackHistoryItem.materialEditText2.setText(crackHistoryItemData.method);
        return view;
    }
}
