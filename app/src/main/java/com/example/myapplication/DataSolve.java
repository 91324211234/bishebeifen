package com.example.myapplication;

import android.graphics.Bitmap;

public class DataSolve {
    public Bitmap mBitmap;
    private static DataSolve datasolve;
    public Bitmap getmBitmap(){return mBitmap;}

    public void setmBitmap(Bitmap mBitmap){this.mBitmap=mBitmap;};
    public static DataSolve getInstance(){
        if(datasolve==null){
            synchronized (DataSolve.class){
                if(datasolve==null){
                    datasolve=new DataSolve();
                }
            }
        }
        return datasolve;
    }
}
