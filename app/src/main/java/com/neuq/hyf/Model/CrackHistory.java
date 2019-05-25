/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: CrackHistory
 * Author:   Administrator
 * Date:     2019/5/24 21:34
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * HYF              修改时间           版本号              描述
 */
package com.neuq.hyf.Model;

import org.bson.Document;

public class CrackHistory {
    public int id;
    public String time;
    public String accountNumber;
    public String crackDescription;
    public String crackSolveMethod;
    public String crackCause;

    public Document toDocument() {
        Document doc = new Document();
        doc.append("id", id);
        doc.append("time", time);
        doc.append("accountNumber", accountNumber);
        doc.append("crackDescription", crackDescription);
        doc.append("crackSolveMethod", crackSolveMethod);
        doc.append("crackCause", crackCause);
        return doc;
    }
}
