package com.neuq.hyf.Threads;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClawBaiDuPicture extends Thread {
    public String keyword;
    public String url;
    public Bitmap bitmap = null;
    public Handler updateImageViewHandler;

    public String drawPicture(String keyword) throws IOException {
        String url = "http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=" + keyword + "&cg=star&pn=" + 0 + "&rn=30&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm=" + Integer.toHexString(0);
        org.jsoup.nodes.Document document = Jsoup.connect(url).data("query", "Java")//请求参数
                .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")//设置urer-agent  get();
                .timeout(5000)
                .get();
        String xmlSource = document.toString();
        xmlSource = StringEscapeUtils.unescapeHtml3(xmlSource);
        String reg = "objURL\":\"http://.+?\\.jpg";
        Pattern pattern = Pattern.compile(reg);
        Matcher m = pattern.matcher(xmlSource);
        while (m.find()) {
            return m.group().substring(9);
        }
        return null;
    }

    public Bitmap getBitmap(String url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
        InputStream fis = httpURLConnection.getInputStream();
        return BitmapFactory.decodeStream(fis);
    }

    public void run() {
        try {
            url = drawPicture(keyword);
            bitmap = getBitmap(url);
            Message message = new Message();
            if (updateImageViewHandler != null) updateImageViewHandler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
