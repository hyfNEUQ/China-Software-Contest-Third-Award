package com.neuq.hyf.Utils.HttpUtils; /**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: HttpUtils
 * Author:   Administrator
 * Date:     2019/4/8 15:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * HYF              修改时间           版本号              描述
 */


import com.neuq.hyf.Model.CrackHistory;
import com.neuq.hyf.Utils.PrinterUtils.Printer;

import org.bson.Document;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class HttpUtils {
    public static String serverAddressPrefix = "http://106.13.96.243:8080/QA_war_exploded/";

    //获取指定网址的回应
    public static String getResponse(String url, Document document) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.connect();
        Iterator it = document.entrySet().iterator();
        String out = "";
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            out += "&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8");
        }
        //log.info("Document:"+out);
        OutputStream ops = httpURLConnection.getOutputStream();
        ops.write(out.getBytes());
        ops.flush();
        ops.close();
        InputStream ins = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins));
        String response = "";
        String line = bufferedReader.readLine();
        while (line != null) {
            response += line;
            line = bufferedReader.readLine();
        }
        return response;
    }

    private final static String split = "-\\*-";

    //测试百度知道的远程方法
    public static String getBaiDuAnswer(String question) throws IOException {
        String url = HttpUtils.serverAddressPrefix + "answerFromBaiDuKnows";
        Document doc = new Document();
        doc.append("question", question);
        return HttpUtils.getResponse(url, doc);
    }

    //测试查询机器信息的方法
    public static List<Document> machineQuery(String type, String brand) throws IOException {
        String url = HttpUtils.serverAddressPrefix + "Mongodb/MachineQuery";
        Document doc = new Document();
        if (type != null && !type.equals("")) doc.append("type", type);
        if (brand != null && !brand.equals("")) doc.append("brand", brand);
        String[] as = HttpUtils.getResponse(url, doc).replace("Document", "").split(split);
        List<Document> docList = new ArrayList<>();
        for (String a : as) {
            a = a.replace(split, "");
            if (a.equals("")) break;
            Document result = Document.parse(a);
            // log.info(result.toString());
            docList.add(result);
        }
        return docList;
    }

    //更新MachineInfo
    public static void upDateMachineInfo(Document docOrigin, Document docNew) throws IOException {
        String url = HttpUtils.serverAddressPrefix + "Mongodb/MachineUpdate";
        Document document = new Document();
        document.append("Document_origin", docOrigin.toJson());
        document.append("Document_new", docNew.toJson());
        String response = HttpUtils.getResponse(url, document);
        //log.info(response);
    }

    //查询用户信息
    public static Document queryUserInfo(String accountNumber, String password) throws IOException {
        String url = HttpUtils.serverAddressPrefix + "Mongodb/UserQuery";
        Document document = new Document();
        document.append("账号", accountNumber);
        String response = HttpUtils.getResponse(url, document);
        return Document.parse(response.replace("Document", ""));
    }

    //用户注册
    public static boolean registerUser(String accountNumber, String password) throws IOException {
        String url = HttpUtils.serverAddressPrefix + "Mongodb/RegisterUser";
        Document document = new Document();
        document.append("账号", accountNumber);
        document.append("密码", password);
        Document doc = new Document();
        doc.append("注册信息", document.toJson());
        String response = HttpUtils.getResponse(url, doc);
        return response.equals("true");
    }

    //更新用户信息
    public static boolean updateUserInfo(Document document) throws IOException {
        String url = HttpUtils.serverAddressPrefix + "Mongodb/UpdateUserInfo";
        Document doc = new Document();
        doc.append("用户信息", document.toJson());
        String result = HttpUtils.getResponse(url, doc);
        return result.equals("true");
    }

    //用于查询百度知道的问答对
    public static Document getQuestionAnswerOnline(String question, String pn) throws IOException {
        String url = HttpUtils.serverAddressPrefix + "Mongodb/GetQuestionOnline";
        Document document = new Document();
        document.append("问题", question);
        document.append("pn", pn);
        return Document.parse(HttpUtils.getResponse(url, document));
    }

    //用于查询数据库中合适的答案
    public static Document getDataBaseAnswer(String question) throws IOException {
        String url = HttpUtils.serverAddressPrefix + "Mongodb/GetAnswerFromDataBase";
        Document document = new Document();
        document.append("问题", question);
        return Document.parse(HttpUtils.getResponse(url, document));
    }

    //获取问答平台中的答案
    public static Document getOnlineAnswer(String question) throws IOException {
        String url = HttpUtils.serverAddressPrefix + "Mongodb/GetAnswerFromOnline";
        Document doc = new Document();
        doc.append("问题", question);
        return Document.parse(HttpUtils.getResponse(url, doc));
    }

    //按条件查取论坛的数据
    public static Document getForumDocument(Document document) throws IOException {
        String url = HttpUtils.serverAddressPrefix + "Mongodb/QueryDocumentByCondition";
        Document doc = new Document();
        doc.append("条件文档", document.toJson());
        return Document.parse(HttpUtils.getResponse(url, doc));
    }

    //把论坛数据插入到数据库中
    public static void insertForumToDataBase(Document document) throws IOException {
        String url = HttpUtils.serverAddressPrefix + "Mongodb/WriteToForumDocuments";
        Document doc = new Document();
        doc.append("文档", document.toJson());
        HttpUtils.getResponse(url, doc);
    }

    //更新论坛数据中的评论
    public static void updateForumDataBase(Document documentOrigin, Document documentNew) throws IOException {
        String url = HttpUtils.serverAddressPrefix + "Mongodb/UpdateForumDocument";
        Document doc = new Document();
        doc.append("docOrigin", documentOrigin.toJson());
        doc.append("docNew", documentNew.toJson());
        HttpUtils.getResponse(url, doc);
    }

    //聊天机器人
    public static String getResponse(String question) throws IOException {
        String url = "http://106.13.96.243:8080/QA_war_exploded/" + "ChatController/GetResponse";
        Document doc = new Document();
        doc.append("用户的话", question);
        return (HttpUtils.getResponse(url, doc));
    }

    //将故障记录插入到云服务器
    public static String insertCrackHistory(CrackHistory crackHistory) throws IOException {
        String url = HttpUtils.serverAddressPrefix + "CrackHistoryContoller/InsertCrackHistory";
        Document doc = new Document();
        doc.append("CrackHistory", crackHistory.toDocument().toJson());
        return (HttpUtils.getResponse(url, doc));
    }

    //按照用户查询写入的故障记录
    public static  List<CrackHistory> getCrackHistoryListByAccountNumber(String accountNumber) throws IOException {
        List<CrackHistory> list = new ArrayList<>();
        String url = HttpUtils.serverAddressPrefix + "CrackHistoryContoller/GetCrackHistoryListByAccountNumber";
        Document doc = new Document();
        doc.append("accountNumber", accountNumber);
        Document documentList = Document.parse(HttpUtils.getResponse(url, doc));
        Iterator it = documentList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry map = (Map.Entry) it.next();
            Document document = Document.parse(map.getValue().toString());
            CrackHistory crackHistory = new CrackHistory();
            crackHistory.id = Integer.valueOf(document.get("id").toString());
            crackHistory.time = document.get("time").toString();
            crackHistory.accountNumber = document.get("accountNumber").toString();
            crackHistory.crackDescription = document.get("crackDescription").toString();
            crackHistory.crackCause = document.get("crackCause").toString();
            crackHistory.crackSolveMethod = document.get("crackSolveMethod").toString();
            list.add(crackHistory);
        }
        return list;
    }

}

