package com.neuq.hyf.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.neuq.hyf.R;

public class ChatUser {
    private int id = 1;
    private Context context = null;
    private Bitmap icon = null;
    private String name = "man";

    public ChatUser(Context context) {
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getIcon() {
        if (icon == null)
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.man);
        else return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

