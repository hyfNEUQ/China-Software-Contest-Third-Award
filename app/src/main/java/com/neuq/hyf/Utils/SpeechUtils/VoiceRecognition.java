package com.neuq.hyf.Utils.SpeechUtils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class VoiceRecognition {
    private Context context = null;
    private String result = null;

    public VoiceRecognition(Context context) {
        this.context = context;
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=5ba238bb");
    }

    public void beginListen(final TextView view) {
        result = "";
        RecognizerDialog dialog = new RecognizerDialog(context, null);
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        dialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                result += parseIatResult(recognizerResult.getResultString());
                view.setText(result);
            }

            @Override
            public void onError(SpeechError speechError) {
            }
        });
        dialog.show();
    }

    public String getResult() {
        return this.result;
    }

    private static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }
}
