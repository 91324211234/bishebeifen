package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView logoimage;
    Bitmap logobit;
    int code_album=001;
    int code_camera=002;
    Button mButtonCamera;
    Button mButtonAlbum;
    Button mButtonRealtime;
    Button mButtonPattern;
    Uri camerafileurl;
    public static String assetPattern="yolov5s6.torchscript.ptl";
    public static String namePattern="yolo";
    String[] stor = new String[10];
    String[] name = new String[10];
    int strCnt;
    int nameCnt;
    int now;


    private final String[] REQUEST_PERMISSIONS = new String[] {"android.permission.CAMERA"};
    private int REQUEST_CODE_PERMISSIONS=101;

    private boolean check_permissions(){
        for(String permission:REQUEST_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission)!= PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoimage=(ImageView) findViewById(R.id.imageview_logo);
        try {
            logobit= BitmapFactory.decodeStream(getAssets().open("OIP-C.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logoimage.setImageBitmap(logobit);
        mButtonCamera=(Button) findViewById(R.id.camera);
        mButtonAlbum=(Button) findViewById(R.id.album);
        mButtonRealtime=(Button) findViewById(R.id.realtime);
        mButtonPattern=(Button) findViewById(R.id.switchPattern);
        //按钮绑定

        mButtonCamera.setOnClickListener(this);
        mButtonAlbum.setOnClickListener(this);
        mButtonRealtime.setOnClickListener(this);
        mButtonPattern.setOnClickListener(this);
        //监听功能

        stor[0]="yolov5s6.torchscript.ptl";
        name[0]="yolo";
        stor[1]="bestBIFPN.torchscript.ptl";
        name[1]="BiFPN";
        stor[2]="bestCA.torchscript.ptl";
        name[2]="CA";
        stor[3]="bestNWD.torchscript.ptl";
        name[3]="NWD";


        strCnt=4;
        nameCnt=4;
        now=0;
        //模型预装载
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.camera:
                Toast.makeText(this, "打开摄像头", Toast.LENGTH_SHORT).show();
                Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                String f=System.currentTimeMillis()+".jpg";
                //利用时间戳防止命名重复
                File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),f);
                camerafileurl= FileProvider.getUriForFile(MainActivity.this,getPackageName()+".fileprovider",file);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,camerafileurl);
                startActivityForResult(cameraIntent,code_camera);

                break;
            case R.id.album:
                Toast.makeText(this, "打开相册", Toast.LENGTH_SHORT).show();

                Intent chooseIntent=new Intent(Intent.ACTION_GET_CONTENT);
                chooseIntent.setType("image/*");
                chooseIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                startActivity(chooseIntent);
                startActivityForResult(chooseIntent,code_album);
                break;
            case R.id.realtime:
                Toast.makeText(this, "启动实时监测", Toast.LENGTH_SHORT).show();

                if(!check_permissions()){
                    ActivityCompat.requestPermissions(this,REQUEST_PERMISSIONS,REQUEST_CODE_PERMISSIONS);
                }
                Intent realtimeIntent=new Intent(MainActivity.this,LiveActivity.class);
                startActivity(realtimeIntent);

                break;
            case R.id.switchPattern:
                now = (now + 1) % strCnt;
                assetPattern = stor[now];
                namePattern = name[now];
                Toast.makeText(this, "切换模型为"+namePattern, Toast.LENGTH_SHORT).show();

        }
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Intent detect_result_activity=new Intent(this,DetectResult.class);
        //从this跳到DetectResult
        Bitmap mBitmap;
        if(requestCode==code_album){
            try{
                mBitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
                //取出图片
                DataSolve instance=DataSolve.getInstance();
                instance.setmBitmap(mBitmap);
                //传递图片给下级页面
                setResult(Activity.RESULT_OK,detect_result_activity);
                startActivity(detect_result_activity);
            } catch (IOException e){
                e.printStackTrace();
            }



        }
        else if(requestCode==code_camera){
            try{
                mBitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),camerafileurl);
                //取出图片
                DataSolve instance=DataSolve.getInstance();
                instance.setmBitmap(mBitmap);
                //传递图片给下级页面
                setResult(Activity.RESULT_OK,detect_result_activity);
                startActivity(detect_result_activity);
            } catch (IOException e){
                e.printStackTrace();
            }



        }
    }
}