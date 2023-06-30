package com.example.test_app1;

import static com.example.test_app1.ConfirmActivity.tmp_size;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {

    public static TextView mtextview_result;
    public static ImageView imageView;
    static WordDao wordDao;



    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0)
            {
                super.handleMessage(msg);
                String tmp = (String) msg.obj;

                mtextview_result.setText("经检测，"+tmp);
                String date = DateUtil.getCurrentDate();

                Word word = new Word(tmp, date);
                wordDao.insertWords(word);
            }
            else if(msg.what == 1)
            {
                super.handleMessage(msg);
                Bitmap bitmap = (Bitmap) msg.obj;
                if (bitmap == null)
                {
                    Log.e("kongzhizhen","null!");
                }
                else {
                    imageView.setImageBitmap(bitmap);
                }


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_result);
        MainActivity.click_flag = 0;
        if(tmp_size>1024*1024)
        {
            Toast.makeText(ResultActivity.this,"您上传的图片大小为"+ (tmp_size)/(1024)+"KB，图片较大，请耐心等候",Toast.LENGTH_LONG).show();
        }
        mtextview_result = findViewById(R.id.textView_result);
        imageView = findViewById(R.id.imageView5);

        wordDao = MainActivity.wordDatabase.getWordDao();

    }

    public void back(View view) {
        ConfirmActivity.isDestroy(ResultActivity.this);

        Intent intent = new Intent(ResultActivity.this,MainActivity.class);
        startActivity(intent);
    }
}