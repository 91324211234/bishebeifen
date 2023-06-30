package com.example.test_app1;

import static android.util.Base64.encodeToString;
import static java.text.DateFormat.DEFAULT;
import static java.util.Base64.*;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

//import org.apache.hc.client5.http.classic.HttpClient;
//import org.apache.hc.client5.http.classic.methods.HttpPost;
//import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
//import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
//import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
//import org.apache.hc.client5.http.impl.classic.HttpClients;
//import org.apache.hc.core5.http.HttpResponse;
//import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
//import org.apache.hc.core5.http.io.entity.EntityUtils;
//import org.apache.hc.core5.http.io.entity.StringEntity;
//import org.apache.hc.core5.http.message.BasicHeader;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.Closeable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConfirmActivity extends AppCompatActivity {
    private ImageView imageView;
    public static Uri savedUri;
    private Uri tmpUri;
    private int code = 1;
    private Button mbutton;
    private Button mbutton2;
    public static int tmp_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_confirm);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

//        Exception();
        imageView = findViewById(R.id.imageView4);
        mbutton = findViewById(R.id.button10);
        mbutton2 = findViewById(R.id.button);
        savedUri = ShotActivity.tmp_uri1;
//        Toast.makeText(ConfirmActivity.this, savedUri.getPath(), Toast.LENGTH_SHORT).show();
        if(MainActivity.pic_flag == 1){
            Bitmap tmpbit = compressImage(MainActivity.tmp_bitmap);
            imageView.setImageBitmap(tmpbit);

            tmp_size = getBitmapSize(tmpbit);

            savedUri =Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),tmpbit, DateUtil.getCurrentDate(),null));
//            startPhotoZoom(savedUri);
            MainActivity.pic_flag = 0;
        }
        else{
            if(MainActivity.click_flag == 1)
            {
                startPhotoZoom(savedUri);
                MainActivity.click_flag = 0;
            }

        }





        mbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                View cv = getWindow().getDecorView();
                File f = uriToFileApiQ(savedUri,ConfirmActivity.this);
                try {
                    ImageUpload.run(f);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent(ConfirmActivity.this,ResultActivity.class);
                startActivity(intent);


                Log.e("------TAG2-----", "onCreate: wait for long time " + String.valueOf(code));
            }
        });
        mbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmActivity.this,ShotActivity.class);
                startActivity(intent);
            }
        });
    }
    public static boolean isDestroy(Activity mActivity) {
        if (mActivity == null ||
                mActivity.isFinishing() ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public void startPhotoZoom(Uri uri){
        if(uri == null)
        {
            finish();
            return;
        }
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) +"cut.jpg";
        File CropPhoto = new File(getExternalCacheDir(), name);
        //m_ResultImagePath = CropPhoto.getPath();
        Uri mPhotoUri = Uri.fromFile(CropPhoto);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            CropPhoto = uriToFileApiQ(uri, getApplicationContext());
            mPhotoUri = FileProvider.getUriForFile(getApplicationContext(),
                    getPackageName() + ".FileProvider", CropPhoto);
        }
        String headPath = CropPhoto.getPath();

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            intent.setDataAndType(mPhotoUri, "image/*");
        }
        else
        {
            intent.setDataAndType(uri, "image/*");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION ); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.putExtra("crop", "true");
        intent.putExtra("scale", false);
        if(Build.MANUFACTURER.equals("HUAWEI")) {
            intent.putExtra("aspectX", 512);
            intent.putExtra("aspectY", 512);
        }
        else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }

        intent.putExtra("outputX", 512);
        intent.putExtra("outputY", 512);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, savedUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //intent.putExtra("noFaceDetection", true); // no face detection

        try {
            startActivityForResult(intent, code);//这里的RESULT_REQUEST_CODE是在startActivityForResult里使用的返回值。


//            while(code == 1)
//                savedUri = mPhotoUri;

        } catch (Exception e) {
            // 解决截取后部分机型秒退
            e.printStackTrace();
        }

    }
    @TargetApi(Build.VERSION_CODES.Q)
    public static File uriToFileApiQ(Uri uri, Context context) {
        File file = null;
        if(uri == null) return file;
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
            String displayName = System.currentTimeMillis()+ Math.round((Math.random() + 1) * 1000)
                    +"."+ MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri));

//            注释掉的方法可以获取到原文件的文件名，但是比较耗时
//            Cursor cursor = contentResolver.query(uri, null, null, null, null);
//            if (cursor.moveToFirst()) {
//                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));}

            try {
                InputStream is = contentResolver.openInputStream(uri);
                File cache = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(), displayName);
                FileOutputStream fos = new FileOutputStream(cache);
                FileUtils.copy(is, fos);
                file = cache;
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                Bitmap bitmap = BitmapFactory.decodeFile(savedUri.getPath());
                imageView.setImageBitmap(bitmap);
//                Log.e("------TAG-----", "onCreate: wait for long time " + String.valueOf(bitmap.getWidth()));
        }
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 10, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 5000) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


}

