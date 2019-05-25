package com.neuq.hyf.Threads;

import android.os.Handler;
import android.os.Message;

import com.neuq.hyf.Utils.HttpUtils.HttpUtils;

import java.io.IOException;

public class ChatThread extends Thread {
    public String answer = null;
    public String question;
    public Handler getResponseHandler;

    public void run() {
        try {
            answer = HttpUtils.getResponse(question);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (answer != null) {
            getResponseHandler.sendMessage(new Message());
        }
    }
}
