/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: HttpTest
 * Author:   Administrator
 * Date:     2019/4/7 10:00
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * HYF              修改时间           版本号              描述
 */
package com.neuq.hyf.Utils.HttpUtils;


import com.neuq.hyf.Utils.PrinterUtils.Printer;


import org.bson.Document;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HttpTest {
    //private static final Logger log = LoggerFactory.getLogger(HttpTest.class);
    private final static String split = "-\\*-";

    public static void main(String[] args) throws IOException {
        Document doc2=new Document();
        doc2.append("账号","1234");
        doc2.append("密码","789");
        Printer.print(HttpUtils.updateUserInfo(doc2));;
    }
}
