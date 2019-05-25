package com.neuq.hyf.View;

import android.content.Context;

import com.kongzue.dialog.v2.TipDialog;

public class DialogFactory {
    //获取成功登陆的提示框
    public static TipDialog getSuccessLoginDialog(Context context) {
        return TipDialog.build(context, "登陆成功", 20, TipDialog.TYPE_FINISH);
    }

    //获取密码错误的提示框
    public static TipDialog getPsswordErrorDialog(Context context) {
        return TipDialog.build(context, "密码错误", 20, TipDialog.TYPE_ERROR);
    }

    //获取账号不存在的提示框
    public static TipDialog getAccountISNotExist(Context context) {
        return TipDialog.build(context, "账号不存在", 20, TipDialog.TYPE_ERROR);
    }

    //获取注册成功的提示框
    public static TipDialog getSuccessRegister(Context context) {
        return TipDialog.build(context, "注册成功", 20, TipDialog.TYPE_FINISH);
    }

    //获取账号已经被注册
    public static TipDialog getAccountAlreadyRegister(Context context) {
        return TipDialog.build(context, "账号已经被注册，您的密码错误", 20, TipDialog.TYPE_ERROR);
    }
    //发表评论成功
    public static TipDialog getSuccessCommentDialog(Context context) {
        return TipDialog.build(context, "发表成功", 20, TipDialog.TYPE_FINISH);
    }
}
