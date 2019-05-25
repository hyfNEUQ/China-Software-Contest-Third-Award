package com.neuq.hyf.Threads;

import com.neuq.hyf.Utils.HttpUtils.HttpUtils;

import org.bson.Document;

import java.io.IOException;

public class WriteToForumDocumentsThread extends Thread {
    public Document document;

    public void run() {
        try {
            HttpUtils.insertForumToDataBase(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
