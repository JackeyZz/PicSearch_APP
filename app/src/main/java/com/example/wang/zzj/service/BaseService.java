package com.example.wang.zzj.service;

import android.content.Context;
import android.os.Environment;

/**
 * Created by wang on 2016/1/22.
 */
public abstract class BaseService {

    protected Context mContext;

    public static String DIR_PATH = Environment.getExternalStorageDirectory() + "/zzjIMG";

    public BaseService(Context mContext){
        this.mContext = mContext;
    }
}
