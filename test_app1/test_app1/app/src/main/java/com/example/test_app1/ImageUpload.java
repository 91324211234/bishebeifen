package com.example.test_app1;

import static com.example.test_app1.ResultActivity.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageUpload{

    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    private final static int READ_TIMEOUT = 100;

    private final static int CONNECT_TIMEOUT = 60;

    private final static int WRITE_TIMEOUT = 60;

    private static final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(32,5,TimeUnit.MINUTES));
    private static final OkHttpClient client = clientBuilder.build();
    public static String tmp;
    static String tmp_s = new String();

    public static void run(File f) throws Exception {
        final File file=f;

        new Thread() {
            @Override
            public void run() {
                //子线程需要做的工作
                tmp = DateUtil.getCurrentDate();
                tmp = tmp +".png";

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file",tmp,
                                RequestBody.create(MEDIA_TYPE_JPG, file))
                        .addFormDataPart("id","1")
                        .build();
                //设置为自己的ip地址
                Request request = new Request.Builder()

                        .url("http://59.72.109.216:8010/upload")
                        .post(requestBody)
                        .build();
                try(Response response = client.newCall(request).execute()){
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    else {
                        String responseData = response.body().string();
                        String data = null;
                        String pred = null;

                        JSONObject jsonObject = JSON.parseObject(responseData);
                        data = jsonObject.getString("data");
                        pred = jsonObject.getString("pred");
                        Log.e("_______pred________", pred);

                        if (pred.equals("-1")|| pred.equals("-2")) {
                            tmp_s = data;
                            Log.e("_______test1________", tmp_s);
                            Message message = Message.obtain();
                            message.obj = tmp_s;
                            message.what = 0;
                            handler.sendMessage(message);
                        }
                        else{
                            Log.e("data",data);

                            Message message1 = Message.obtain();
                            message1.obj = stringToBitmap(data);
                            message1.what = 1;
                            handler.sendMessage(message1);
                            if(pred.equals("1"))
                            {

                                Message message = Message.obtain();
                                message.obj ="患者适用于该治疗方案";
                                message.what = 0;
                                handler.sendMessage(message);
                            }
                            if(pred.equals("0"))
                            {
                                Message message = Message.obtain();
                                message.obj ="患者不适用于该治疗方案";
                                message.what = 0;
                                handler.sendMessage(message);
                            }
                        }
                    }

//                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }
    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap readInputStreamToBitmap(InputStream ins, int fileSize) {
        if (ins == null) {
            return null;
        }
        byte[] b;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int size = -1;
            int len = 0;// 已经接收长度
            size = ins.read(buffer);
            while (size != -1) {
                len = len + size;//
                bos.write(buffer, 0, size);
                if (fileSize == len) {// 接收完毕
                    break;
                }
                size = ins.read(buffer);
            }
            b = bos.toByteArray();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }
    private static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            // commons-io
            //IOUtils.copy(inputStream, outputStream);

        }

    }

}