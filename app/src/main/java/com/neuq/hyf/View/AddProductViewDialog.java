package com.neuq.hyf.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.neuq.hyf.Model.UserInfo;
import com.neuq.hyf.R;
import com.neuq.hyf.Utils.SpeechUtils.VoiceRecognition;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;

public class AddProductViewDialog extends Dialog implements View.OnClickListener {
    private View view = null;
    private Context context = null;
    private MaterialEditText materialEditText1 = null;
    private MaterialEditText materialEditText2 = null;
    private Button buttonAdd = null;
    private Button buttonVoice = null;
    private VoiceRecognition voiceRecognition = null;
    private ProductView productView = null;
    private UserInfo userInfo = UserInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.view);
        context = this.context;
        productView = ProductView.getProductView();
    }

    public AddProductViewDialog(Context context) {
        super(context);
        this.context = context;
        this.view = LayoutInflater.from(
                context).inflate(R.layout.add_product_diglog, null, false);
        materialEditText1 = view.findViewById(R.id.edit_text1);
        materialEditText2 = view.findViewById(R.id.edit_text2);
        buttonAdd = view.findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(this);
        buttonVoice = view.findViewById(R.id.buttonVoice);
        buttonVoice.setOnClickListener(this);
        voiceRecognition = new VoiceRecognition(context);
    }

    public View getView() {
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add:
                productView.document.append(materialEditText1.getText().toString(), materialEditText2.getText().toString());
                try {
                    productView.updateListView();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.dismiss();
                break;
            case R.id.buttonVoice:
                voiceRecognition.beginListen(materialEditText2);
                break;
        }
    }
}
