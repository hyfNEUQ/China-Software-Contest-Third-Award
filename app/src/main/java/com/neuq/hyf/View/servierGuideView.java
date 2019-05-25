package com.neuq.hyf.View;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.SeekBar;

import com.hanks.htextview.evaporate.EvaporateTextView;
import com.neuq.hyf.Acitivity.BaseActivity;
import com.neuq.hyf.Acitivity.customerServiceAcivity;
import com.neuq.hyf.R;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.markushi.ui.CircleButton;

public class servierGuideView extends BaseActivity {
    private Context context = null;
    private View view = null;
    private CircleButton circleButton = null;
    private EvaporateTextView evaporateTextView1, evaporateTextView2;
    private Timer timerEvaporateTextView1, timerEvaporateTextView2 = null;

    public servierGuideView(Context context) {
        this.context = context;
        this.view = LayoutInflater.from(
                context).inflate(R.layout.servier_guide_view, null, false);
        this.circleButton = view.findViewById(R.id.circlebutton);
        evaporateTextView1 = view.findViewById(R.id.eva1);
        evaporateTextView1.setOnClickListener(new ClickListener());
        evaporateTextView1.animateText("智能问答");
        evaporateTextView1.setProgress((float) 0.52);
        evaporateTextView1.setAnimationListener(new SimpleAnimationListener(this));
        evaporateTextView2 = view.findViewById(R.id.eva2);
        evaporateTextView2.setOnClickListener(new ClickListener());
        evaporateTextView2.animateText("专注数控机床维修");
        evaporateTextView2.setProgress((float) 0.52);
        evaporateTextView2.setAnimationListener(new SimpleAnimationListener(this));
        initUi();
    }

    private void initUi() {
        timerEvaporateTextView1 = new Timer();
        timerEvaporateTextView1.schedule(new TimerTask() {
            @Override
            public void run() {
                if (evaporateTextView1.getText().toString().equals("专注数控机床维修")) {
                    evaporateTextView1.animateText("多引擎高效检索答案");
                } else {
                    evaporateTextView1.animateText("专注数控机床维修");
                }
            }
        }, 0, 3000);
        timerEvaporateTextView2 = new Timer();
        timerEvaporateTextView2.schedule(new TimerTask() {
            @Override
            public void run() {
                if (evaporateTextView2.getText().toString().equals("智能问答")) {
                    evaporateTextView2.animateText("准确高效");
                } else {
                    evaporateTextView2.animateText("智能问答");
                }
            }
        }, 0, 3000);
        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, customerServiceAcivity.class);
                context.startActivity(intent);
            }
        });
        //evaporateTextView1.
    }

    public View getView() {
        return view;
    }
}

