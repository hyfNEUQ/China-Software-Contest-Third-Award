package com.neuq.hyf.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.neuq.hyf.Model.CrackHistory;
import com.neuq.hyf.Model.UserInfo;
import com.neuq.hyf.R;
import com.neuq.hyf.Threads.CrackHistoryThread;
import com.neuq.hyf.Utils.AnotherUtils.TimeUtils;
import com.neuq.hyf.Utils.SpeechUtils.VoiceRecognition;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;

public class ReleaseCrackDialog extends Dialog implements View.OnClickListener {
    private View view = null;
    private Context context = null;
    private MaterialEditText materialEditText1 = null;
    private MaterialEditText materialEditText2 = null;
    private Button buttonAdd = null;
    private Button buttonVoice = null;
    private VoiceRecognition voiceRecognition = null;
    private UserInfo userInfo = UserInfo.getInstance();
    private static Handler insertHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.view);
    }

    public ReleaseCrackDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.release_crack_dialog, null);
        materialEditText1 = view.findViewById(R.id.edit_text1);
        materialEditText2 = view.findViewById(R.id.edit_text2);
        buttonAdd = view.findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(this);
        buttonVoice = view.findViewById(R.id.buttonVoice);
        buttonVoice.setOnClickListener(this);
        voiceRecognition = new VoiceRecognition(context);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add:
                CrackHistory crackHistory = new CrackHistory();
                crackHistory.time = TimeUtils.getCurrentTime();
                crackHistory.accountNumber = userInfo.getAccountNumber();
                crackHistory.crackDescription = materialEditText1.getText().toString();
                crackHistory.crackSolveMethod = materialEditText2.getText().toString();
                CrackHistoryThread crackHistoryThread = new CrackHistoryThread(crackHistory);
                crackHistoryThread.start();
                this.dismiss();
                break;
            case R.id.buttonVoice:
                voiceRecognition.beginListen(materialEditText2);
                break;
        }
    }
}
