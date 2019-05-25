package com.neuq.hyf.Acitivity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dd.processbutton.iml.ActionProcessButton;
import com.neuq.hyf.Model.UserInfo;
import com.neuq.hyf.R;
import com.neuq.hyf.Threads.UserInfoQueryThread;
import com.neuq.hyf.Threads.UserRegisterThread;
import com.neuq.hyf.Utils.HttpUtils.HttpUtils;
import com.neuq.hyf.View.DialogFactory;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sloop.fonts.FontsManager;

import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialEditText editText1, editText2;
    private ActionProcessButton loginButton, registerAndLoginButton;
    private Context context;
    private UserInfo userInfo = UserInfo.getInstance();
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editText1 = findViewById(R.id.edit_text1);
        editText2 = findViewById(R.id.edit_text2);
        loginButton = findViewById(R.id.btnSignIn);
        registerAndLoginButton = findViewById(R.id.btnRegisterAndLogin);
        loginButton.setOnClickListener(this);
        registerAndLoginButton.setOnClickListener(this);
        context = this;
        //把此Acitivity的字体设置成隶书
        FontsManager.initFormAssets(context, "fonts/st.TTF");
        FontsManager.changeFonts(this.getWindow().getDecorView());
        initPermission();
    }

    //处理登陆的情况
    private void handleLogin() {
        String accountNumber = editText1.getText().toString();
        String password = editText2.getText().toString();
        UserInfoQueryThread userInfoQueryThread = new UserInfoQueryThread();
        userInfoQueryThread.accountNumber = accountNumber;
        userInfoQueryThread.password = password;
        userInfoQueryThread.start();
        do {
        } while (userInfoQueryThread.isAlive());
        Document userInfo = userInfoQueryThread.document;
        if (userInfo.get("账号") == null) {
            //账号未注册的提示
            DialogFactory.getAccountISNotExist(context).showDialog();
        } else {
            if (userInfo.get("密码").equals(password)) {
                //登陆成功
                DialogFactory.getSuccessLoginDialog(context).showDialog();
                enterMainActivity();
                this.userInfo.setAccountAndPassword(accountNumber, password);
            } else {
                //密码错误
                DialogFactory.getPsswordErrorDialog(context).showDialog();
            }
        }
    }

    //处理注册并且登陆
    public void handleRegisterAndLogin() {
        String accountNumber = editText1.getText().toString();
        String password = editText2.getText().toString();
        UserInfoQueryThread userInfoQueryThread = new UserInfoQueryThread();
        userInfoQueryThread.accountNumber = accountNumber;
        userInfoQueryThread.password = password;
        userInfoQueryThread.start();
        do {
        } while (userInfoQueryThread.isAlive());
        Document userInfo = userInfoQueryThread.document;
        if (userInfo.get("账号") == null) {
            //账号未注册的提示
            UserRegisterThread userRegisterThread = new UserRegisterThread();
            userRegisterThread.accountNumber = accountNumber;
            userRegisterThread.password = password;
            userRegisterThread.start();
            do {
            } while (userRegisterThread.isAlive());
            if (userRegisterThread.result) DialogFactory.getSuccessRegister(context).showDialog();
        } else {
            if (userInfo.get("密码").equals(password)) {
                //登陆成功
                DialogFactory.getSuccessLoginDialog(context).showDialog();
                this.userInfo.setAccountAndPassword(accountNumber, password);
                enterMainActivity();
            } else {
                //密码错误
                DialogFactory.getAccountAlreadyRegister(context).showDialog();
            }
        }
    }

    public void enterMainActivity() {
        Intent intent = new Intent();
        intent.setClass(context, MainAcitivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegisterAndLogin:
                handleRegisterAndLogin();
                break;
            case R.id.btnSignIn:
                handleLogin();
                break;
        }
    }
    //查看权限
    private void initPermission()
    {
        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
        permissionItems.add(new PermissionItem(Manifest.permission_group.MICROPHONE, "录音", R.drawable.micro));
        permissionItems.add(new PermissionItem(Manifest.permission_group.STORAGE, "存储权限", R.drawable.save));
        permissionItems.add(new PermissionItem(Manifest.permission.INTERNET, "网络", R.drawable.wifi));
        HiPermission.create(LoginActivity.this)
                .permissions(permissionItems)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        Log.i(TAG, "onClose");
                        //showToast(getString(R.string.permission_on_close));
                    }

                    @Override
                    public void onFinish() {
                        //showToast(getString(R.string.permission_completed));
                    }

                    @Override
                    public void onDeny(String permission, int position) {
                        Log.i(TAG, "onDeny");
                    }

                    @Override
                    public void onGuarantee(String permission, int position) {
                        Log.i(TAG, "onGuarantee");
                    }
                });
    }
}



