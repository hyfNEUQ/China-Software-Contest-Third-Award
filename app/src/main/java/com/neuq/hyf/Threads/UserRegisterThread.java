package com.neuq.hyf.Threads;

import com.neuq.hyf.Utils.HttpUtils.HttpUtils;

import org.bson.Document;

import java.io.IOException;

public class UserRegisterThread extends Thread {
    public String accountNumber, password;
    public Document document;
    public boolean result;

    public void run() {
        try {
            result = HttpUtils.registerUser(accountNumber, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
