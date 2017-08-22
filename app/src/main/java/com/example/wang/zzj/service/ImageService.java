package com.example.wang.zzj.service;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.wang.zzj.interfaces.CallBack;
import com.example.wang.zzj.util.BitmapUtil;
import com.example.wang.zzj.util.ImageRequestManager;

/**
 * Created by wang on 2016/1/22.
 */
public class ImageService extends BaseService {

    BitmapUtil mBitmapUtil;
    ImageRequestManager mImageRequestManager;

    public ImageService(Context context){
        super(context);
        mBitmapUtil = BitmapUtil.getInstance(context);
        mImageRequestManager = ImageRequestManager.getInstance(context);
    }

    public String getImgName(){
        return mBitmapUtil.getFileName(DIR_PATH);
    }

    public <T> void loadBitmap(T url, ContentResolver cr, ImageView imageView, CallBack<Bitmap> callBack){
        mImageRequestManager.loadBitmap(url, cr, imageView, callBack);
    }
}
