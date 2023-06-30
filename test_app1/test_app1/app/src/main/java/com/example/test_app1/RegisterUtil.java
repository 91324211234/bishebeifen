package com.example.test_app1;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RegisterUtil {

    private static final OkHttpClient client = new OkHttpClient();

    public static void run(String userid,String passwd) throws Exception {

        new Thread() {
            @Override
            public void run() {
                //子线程需要做的工作
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder().add("userid",userid).add("passwd",passwd).build();
                Request request = new Request.Builder()
                        .post(body)
                        .url("http://lgds.work/wld/register").
                                build();

                try(Response response = client.newCall(request).execute()){
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
