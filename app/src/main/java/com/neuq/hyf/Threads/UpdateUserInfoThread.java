package com.neuq.hyf.Threads;

import com.neuq.hyf.Utils.HttpUtils.HttpUtils;

import org.bson.Document;

import java.io.IOException;

public class UpdateUserInfoThread extends Thread {
    public Document document;
    public void run()
    {
        try {
            HttpUtils.updateUserInfo(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
