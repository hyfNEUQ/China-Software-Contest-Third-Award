package com.neuq.hyf.Threads;

import android.os.Handler;
import android.os.Message;

import com.neuq.hyf.Model.CrackHistory;
import com.neuq.hyf.Utils.HttpUtils.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CrackHistoryThread extends Thread{
    private CrackHistory crackHistory = null;
    private String accountNumber = null;
    private List<CrackHistory> list = new ArrayList<>();
    private Handler handler = null;

    public CrackHistoryThread(CrackHistory crackHistory) {
        this.crackHistory = crackHistory;
    }

    public CrackHistoryThread(String accountNumber, Handler handler) {
        this.accountNumber = accountNumber;
        this.handler = handler;
    }

    public void run() {
        if (accountNumber != null) {
            try {
                list = HttpUtils.getCrackHistoryListByAccountNumber(accountNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }
            handler.sendMessage(new Message());
        } else if (crackHistory != null) {
            try {
                HttpUtils.insertCrackHistory(crackHistory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return;
        }
    }

    public List<CrackHistory> getList() {
        return list;
    }
}
