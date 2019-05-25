package com.neuq.hyf.Acitivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.neuq.hyf.Model.CandidateResponse;
import com.neuq.hyf.Model.ChatUser;
import com.neuq.hyf.R;
import com.neuq.hyf.Threads.ChatThread;
import com.neuq.hyf.Threads.replyThread;

import org.bson.Document;
import org.bson.json.JsonParseException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class customerServiceAcivity extends Activity {
    private Context context = null;
    private ChatView chatView = null;
    private ChatUser customer = null;
    private ChatUser servicer = null;
    private ChatThread chatThread = null;
    private Handler getResponseHandler = null;
    private com.github.bassaer.chatmessageview.model.ChatUser c, s;
    private List<CandidateResponse> candidateResponseList = null;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_service_view);
        servicer = new ChatUser(this);
        customer = new ChatUser(this);
        chatView = findViewById(R.id.chat_view);
        initUi();
        initHandler();
    }

    private void initUi() {
        //view setting
        //user setting
        servicer.setName("智能客服");
        servicer.setId(0);
        servicer.setIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.girl_icon));
        customer.setName("尊贵的您");
        customer.setId(1);
        //chatView.setRightBubbleColor(ContextCompat.getColor(context, R.color.green500));
        chatView.setLeftBubbleColor(Color.WHITE);
        //chatView.setBackgroundColor(ContextCompat.getColor(context, R.color.blueGray500));
        // chatView.setSendButtonColor(ContextCompat.getColor(context, R.color.purple_pressed));
        //chatView.setSendIcon(R.drawable.ic_action_send);
        chatView.setRightMessageTextColor(Color.WHITE);
        chatView.setLeftMessageTextColor(Color.BLACK);
        chatView.setUsernameTextColor(Color.WHITE);
        chatView.setSendTimeTextColor(Color.WHITE);
        chatView.setDateSeparatorColor(Color.WHITE);
        chatView.setInputTextHint("");
        chatView.setMessageMarginTop(5);
        chatView.setMessageMarginBottom(5);
        c = new com.github.bassaer.chatmessageview.model.ChatUser(customer.getId(), customer.getName(), customer.getIcon());
        s = new com.github.bassaer.chatmessageview.model.ChatUser(servicer.getId(), servicer.getName(), servicer.getIcon());
        chatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = chatView.getInputText();
                Message message = new Message.Builder()
                        .setUser(c)
                        .setRight(true)
                        .setText(inputText)
                        .hideIcon(false)
                        .setUserIconVisibility(true)
                        .build();
                chatView.send(message);
                chatView.setInputText("");
                handleChat(inputText);
                /*
                replyThread is used for answer user question
                 */
            }
        });
        Message messageReply = new Message.Builder()
                .setUser(s)
                .setRight(false)
                .setText("您好，有什么问题需要咨询呢")
                .hideIcon(false)
                .setUserIconVisibility(true)
                .build();
        chatView.receive(messageReply);
        chatView.setOnIconClickListener(
                new Message.OnIconClickListener() {
                    @Override
                    public void onIconClick(@NotNull Message message) {

                    }
                }
        );
    }

    private void handleChat(String inputText) {
        if (isNumber(inputText) && candidateResponseList != null && Integer.valueOf(inputText) >= 1 && Integer.valueOf(inputText) <= candidateResponseList.size()) {
            for (CandidateResponse c : candidateResponseList) {
                if (c.id.equals(inputText)) {
                    Message messageReply = new Message.Builder()
                            .setUser(s)
                            .setRight(false)
                            .setText(c.content)
                            .hideIcon(false)
                            .setUserIconVisibility(true)
                            .build();
                    chatView.receive(messageReply);
                }
            }
        } else {
            chatThread = new ChatThread();
            chatThread.question = inputText;
            chatThread.getResponseHandler = getResponseHandler;
            chatThread.start();
        }
    }

    private boolean isNumber(String numberText) {
        try {
            Integer.valueOf(numberText);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        getResponseHandler = new Handler() {
            public void handleMessage(android.os.Message message) {
                Message messageReply = null;
                try {
                    candidateResponseList = new ArrayList<>();
                    Document result = Document.parse(chatThread.answer);
                    String response = result.get("答案列表").toString();
                    result = Document.parse(response);
                    Iterator it = result.entrySet().iterator();
                    response = "系统分析您可能想知道以下选项中的一项，如果是您想知道的请输入选项前的数字号，不是的话请您输入更详细的信息";
                    response += "\n";
                    while (it.hasNext()) {
                        try {
                            CandidateResponse candidateResponse = new CandidateResponse();
                            Map.Entry map = (Map.Entry) it.next();
                            String number = map.getKey().toString();
                            candidateResponse.id = number;
                            Document doc = Document.parse(map.getValue().toString());
                            String title = doc.get("主题").toString();
                            candidateResponse.title = title;
                            String content = doc.get("内容").toString();
                            candidateResponse.content = content;
                            response += number + ":" + title + "\n";
                            candidateResponseList.add(candidateResponse);
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    messageReply = new Message.Builder()
                            .setUser(s)
                            .setRight(false)
                            .setText(response)
                            .hideIcon(false)
                            .setUserIconVisibility(true)
                            .build();
                } catch (JsonParseException e) {
                    messageReply = new Message.Builder()
                            .setUser(s)
                            .setRight(false)
                            .setText(chatThread.answer)
                            .hideIcon(false)
                            .setUserIconVisibility(true)
                            .build();
                }
                chatView.receive(messageReply);
            }
        };
    }
}
