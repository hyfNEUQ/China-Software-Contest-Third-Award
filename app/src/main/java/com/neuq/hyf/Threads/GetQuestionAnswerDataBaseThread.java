package com.neuq.hyf.Threads;

import android.os.Handler;
import android.os.Message;

import com.neuq.hyf.Utils.HttpUtils.HttpUtils;

import org.bson.Document;

import java.io.IOException;

public class GetQuestionAnswerDataBaseThread extends Thread {
    public String question;
    public Document document;
    public Handler updateListViewHandler;
    public final int arg1 = 1;

    public void run() {
        try {
            document = HttpUtils.getDataBaseAnswer(question);
            Message message = new Message();
            message.arg1=this.arg1;
            updateListViewHandler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
