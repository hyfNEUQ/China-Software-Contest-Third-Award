package com.neuq.hyf.Threads;

import android.os.Handler;
import android.os.Message;

import com.neuq.hyf.Utils.HttpUtils.HttpUtils;

import org.bson.Document;

import java.io.IOException;

public class GetForumDocumentThread extends Thread{
    public Document documentQuery;
    public Document documentResult;
    public Handler refreshHandler;
    public void run()
    {
        try {
            documentResult=HttpUtils.getForumDocument(documentQuery);
            Message message=new Message();
            refreshHandler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
