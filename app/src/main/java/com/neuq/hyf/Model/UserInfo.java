package com.neuq.hyf.Model;

import com.neuq.hyf.Threads.UpdateUserInfoThread;
import com.neuq.hyf.Threads.UserInfoQueryThread;
import com.neuq.hyf.Utils.HttpUtils.HttpUtils;

import org.bson.Document;

import java.io.IOException;

public class UserInfo {
    //整个用户信息在数据库中存的文档
    private Document document = null;
    //实例
    private static UserInfo userInfo = null;
    //账号
    private String accountNumber;
    //密码
    private String password;
    //机床信息
    private Document machineInfo;
    //数据库_id
    private long _id;
    //用户查询线程
    private UserInfoQueryThread userInfoQueryThread = null;

    public static UserInfo getInstance() {
        if (userInfo == null) userInfo = new UserInfo();
        return userInfo;
    }

    public String getAccountNumber() {
        return accountNumber;
    }


    public String getPassword() {
        return password;
    }

    public void setAccountAndPassword(String accountNumber, String password) {
        this.accountNumber = accountNumber;
        this.password = password;
        userInfoQueryThread = new UserInfoQueryThread();
        userInfoQueryThread.accountNumber = accountNumber;
        userInfoQueryThread.password = password;
        userInfoQueryThread.start();
        do {
        } while (userInfoQueryThread.isAlive());
        this._id = Long.valueOf(userInfoQueryThread.document.get("_id").toString());
        if (userInfoQueryThread.document.get("机床信息") != null)
            this.machineInfo = Document.parse(userInfoQueryThread.document.get("机床信息").toString().replace("Document", ""));
        else this.machineInfo = null;
    }

    public Document getMachineInfo() {
        return machineInfo;
    }

    public void setMachineInfo(Document machineInfo) throws IOException {
        this.machineInfo = machineInfo;
        this.updateToServicer();
    }

    //更新到服务器
    private void updateToServicer() throws IOException {
        document = new Document();
        document.append("账号", accountNumber);
        document.append("密码", password);
        document.append("_id", _id);
        document.append("机床信息", machineInfo.toJson().replace("Document", ""));
        UpdateUserInfoThread updateUserInfoThread = new UpdateUserInfoThread();
        updateUserInfoThread.document = document;
        updateUserInfoThread.start();
    }

}
