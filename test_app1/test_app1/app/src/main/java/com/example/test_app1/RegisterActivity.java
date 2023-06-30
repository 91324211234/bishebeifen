package com.example.test_app1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RegisterActivity extends AppCompatActivity {
    private EditText Account;
    private EditText passwd;
    private Button Register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        Account = findViewById(R.id.Account);
        passwd = findViewById(R.id.passwd);
        Register_button = findViewById(R.id.button8);
    }

    public void register(View view) throws Exception {
        RegisterUtil.run(Account.getText().toString(),passwd.getText().toString());
    }
}