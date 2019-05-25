package com.neuq.hyf.Utils.AnotherUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeUtils {
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.CHINESE);
    public static String getCurrentTime()
    {
        return timeFormat.format(System.currentTimeMillis());
    }
}
