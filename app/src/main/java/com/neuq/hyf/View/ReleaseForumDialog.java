package com.neuq.hyf.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.neuq.hyf.Model.UserInfo;
import com.neuq.hyf.R;
import com.neuq.hyf.Threads.WriteToForumDocumentsThread;
import com.neuq.hyf.Utils.SpeechUtils.VoiceRecognition;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sloop.fonts.FontsManager;
import com.suke.widget.SwitchButton;

import org.bson.Document;

import java.io.IOException;

public class ReleaseForumDialog extends Dialog implements View.OnClickListener {
    private Context context = null;
    private View view = null;
    private SwitchButton switchButton = null;
    Button btn_voice, btn_cancel, btn_publish;
    MaterialEditText materialEditText;
    private VoiceRecognition voiceRecognition = null;
    private Document document = null;
    private UserInfo userInfo = UserInfo.getInstance();
    private WriteToForumDocumentsThread writeToForumDocumentsThread = null;

    public ReleaseForumDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.release_forum_dialog, null, false);
        btn_voice = view.findViewById(R.id.buttonVoice);
        btn_voice.setOnClickListener(this);
        btn_cancel = view.findViewById(R.id.cancel);
        btn_cancel.setOnClickListener(this);
        btn_publish = view.findViewById(R.id.publish);
        btn_publish.setOnClickListener(this);
        switchButton = view.findViewById(R.id.switch_button);
        materialEditText = view.findViewById(R.id.edit_text2);
        voiceRecognition = new VoiceRecognition(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.view);
        //把此Acitivity的字体设置成隶书
        FontsManager.initFormAssets(context, "fonts/lishu.ttf");
        FontsManager.changeFonts(this.getWindow().getDecorView());
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                this.dismiss();
                break;
            case R.id.buttonVoice:
                voiceRecognition.beginListen(materialEditText);
                break;
            case R.id.publish:
                writeToForumDocumentsThread = new WriteToForumDocumentsThread();
                gengerateDocument();
                writeToForumDocumentsThread.document = getDocument();
                writeToForumDocumentsThread.start();
                this.dismiss();
                break;
        }
    }

    private void gengerateDocument() {
        document = new Document();
        document.append("时间", System.currentTimeMillis());
        document.append("内容", materialEditText.getText().toString());
        document.append("机主设备信息", userInfo.getMachineInfo().toJson());
        document.append("浏览人数", String.valueOf(1));
        document.append("回复数量", String.valueOf(0));
        document.append("提问者账号",UserInfo.getInstance().getAccountNumber());
    }

    public Document getDocument() {
        return document;
    }

    public View getView() {
        return view;
    }
}
