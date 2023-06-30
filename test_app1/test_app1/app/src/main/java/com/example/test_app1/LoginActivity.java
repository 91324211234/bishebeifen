package com.example.test_app1;

import static android.util.Base64.encodeToString;
import static java.text.DateFormat.DEFAULT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mPhoneNumberEditText;
    private EditText mPassWordEditText;
    private Button mLoginButton;
    private Uri savedUri;
    //用于接收Http请求的servlet的URL地址，请自己定义
    private String originAddress = "http://59.72.109.216:8030/wld";
    //用于处理消息的Handler
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";

            if ("OK".equals(msg.obj.toString())){
                result = "success";
            }else if ("Wrong".equals(msg.obj.toString())){
                result = "fail";
            }else {
                result = msg.obj.toString();
            }
            Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initView() {
        mPhoneNumberEditText =  findViewById(R.id.phoneNumberEditText);
        mPassWordEditText =  findViewById(R.id.passwordEditText);
        mLoginButton =  findViewById(R.id.loginButton);
    }

    private void initEvent() {
        mLoginButton.setOnClickListener(this);
    }

    public void login() throws IOException {
        //检查用户输入的账号和密码的合法性
        if (!isInputValid()){
            return;
        }
        //构造HashMap
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("me","login");
        params.put(User.PHONENUMBER, mPhoneNumberEditText.getText().toString());
        params.put(User.PASSWORD, mPassWordEditText.getText().toString());
        try {
            //构造完整URL
            String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
            //发送请求
            HttpUtil.sendHttpRequest(compeletedURL, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Message message = new Message();
                    message.obj = response;
                    mHandler.sendMessage(message);
                }

                @Override
                public void onError(Exception e) {
                    Message message = new Message();
                    message.obj = e.toString();
                    mHandler.sendMessage(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isInputValid() {
        //检查用户输入的合法性，这里暂且默认用户输入合法
        return true;
    }


    @Override
    public void onClick(View v) {

    }


    public void register(View view) {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
}