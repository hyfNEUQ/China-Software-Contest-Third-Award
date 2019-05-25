package com.neuq.hyf.Threads;

import com.neuq.hyf.Utils.HttpUtils.HttpUtils;

import org.bson.Document;

import java.io.IOException;

public class UpdateForumDataThread extends Thread {
    public Document docOrigin,docNew;
    public void run() {
        try {
            HttpUtils.updateForumDataBase(docOrigin,docNew);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
