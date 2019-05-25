package com.neuq.hyf.Threads;

import com.neuq.hyf.Utils.HttpUtils.HttpUtils;

import org.bson.Document;

import java.io.IOException;
import java.util.List;

public class MachineQueryThread extends Thread {
    public Document document = null;
    public String type, brand;

    public void run() {
        List<Document> machineList = null;
        try {
            machineList = HttpUtils.machineQuery(type, brand);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (machineList != null && machineList.size() > 0) document = machineList.get(0);
        else document = null;
    }
}
