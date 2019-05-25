package com.neuq.hyf.Threads;

import com.neuq.hyf.Utils.HttpUtils.HttpUtils;

import org.bson.Document;

import java.io.IOException;

public class UserInfoQueryThread extends Thread {
    public String accountNumber, password;
    public Document document;

    public void run() {
        try {
            document = HttpUtils.queryUserInfo(accountNumber, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}