package com.example.test_app1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    TextView textView;
    WordDao wordDao;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_history);
        textView = (TextView) findViewById(R.id.textView_history);
        wordDao =MainActivity.wordDatabase.getWordDao();
        updateView();

    }
    void updateView(){
        List<Word> list = wordDao.getAllWords();
        String text = "";
        for(int i = 0;i<list.size();i++)
        {
            Word word = list.get(i);
            Date date = new Date();
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");

            text += (list.size()-i) + "次查询结果:" + word.getNum() + "\n";
            text += "查询时间:" + word.getDate()+ "\n";
            text += "————————————\n";


        }
        textView.setText(text);
    }

    public void delete_all(View view) {
        List<Word> list = wordDao.getAllWords();

        if(list.size()==0){
            Toast.makeText(HistoryActivity.this, "当前历史纪录为空！",Toast.LENGTH_SHORT).show();

        }
        else{
            AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
            alertdialogbuilder.setMessage("确认要删除全部记录？");
            alertdialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    wordDao.deleteAllWords();
                    updateView();
                }
            });
            alertdialogbuilder.setNeutralButton("取消", null);
            final AlertDialog alertdialog1 = alertdialogbuilder.create();
            alertdialog1.show();
        }


    }

    public void delete_one(View view) {
        List<Word> list = wordDao.getAllWords();


        if(list.size()==0){
            Toast.makeText(HistoryActivity.this, "当前历史纪录为空！",Toast.LENGTH_SHORT).show();

        }
        else{
            int i = 0;
            String tmp = String.valueOf(i);
            Log.e("________test______",tmp);
            Word word = list.get(i);
            wordDao.deleteWords(word);
            updateView();
        }

    }
}