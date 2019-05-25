package com.neuq.hyf.Utils.SpeechUtils;

import android.app.Application;
import android.content.Context;

import com.neuq.hyf.Utils.SpeechUtils.SpeechUtil;

/**
 * Created by WangJi.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        SpeechUtil.getInstance().init();
    }

    public static Context getContext() {
        return context;
    }
}
