package com.neuq.hyf.View;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.neuq.hyf.Acitivity.CrackHistoryActivity;
import com.neuq.hyf.Acitivity.PDFViewActivity;
import com.neuq.hyf.Acitivity.PDFViewActivity_;
import com.neuq.hyf.Acitivity.ProductAcivity;
import com.neuq.hyf.R;

import java.util.ArrayList;
import java.util.List;

import id.yuana.itemsettingview.ItemSettingView;

public class ConfigView {
    private Context context = null;
    private View view = null;
    private Button addProduct = null;
    private ItemSettingView faultCodeItem = null;
    private ItemSettingView crackHistory = null;

    public ConfigView(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.config_view, null, false);
        addProduct = view.findViewById(R.id.setMachineButton);
        faultCodeItem = view.findViewById(R.id.faulttable);
        crackHistory = view.findViewById(R.id.crackhistory);
        initUi();
    }

    private void initUi() {
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, ProductAcivity.class);
                context.startActivity(intent);
            }
        });
        faultCodeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, PDFViewActivity_.class);
                context.startActivity(intent);
            }
        });
        crackHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, CrackHistoryActivity.class);
                context.startActivity(intent);
            }
        });
    }

    public View getView() {
        return view;
    }
}
