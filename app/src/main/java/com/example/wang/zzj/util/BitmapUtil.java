package com.example.wang.zzj.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wang on 2016/1/8.
 */
public class BitmapUtil {

    private static BitmapUtil sBitmapUtil;
    private Context context;

    public static synchronized BitmapUtil getInstance(Context context) {
        if (sBitmapUtil == null) {
            sBitmapUtil = new BitmapUtil(context);
        }
        return sBitmapUtil;
    }

    private BitmapUtil(Context context) {
        this.context = context;
    }




    public void save(Bitmap bitmap, String fileName){
        if (fileName==null){
            return;
        }
        File file = new File(fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);// 把数据写入文件
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取本地保存地址和文件名
    public String getFileName(String dir_path){
        String fileName;
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            NotificationHelper.toastDanger(context, "手机存储卡读取失败");
            return null;
        }


        File file = new File(dir_path);
        if(!file.exists()){
            file.mkdirs(); //创建文件夹
        }
        //以时间来命名文件
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss", Locale.getDefault());
        fileName = dir_path + sdf.format(new Date()) + ".jpg";
        return fileName;
    }


    /**
     * 按大小压缩
     * @param bitmap 要压缩的图片
     *@param newWidth 要压缩到的图片的新宽度
     *@param newHeigth 要压缩到的图片的新高度
     * **/
    public Bitmap compressWithSize(Bitmap bitmap, float newWidth, float newHeigth){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024 > 2 * 1024) {//判断如果图片大于2M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (width > height && width > newWidth) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (width / newWidth);
        } else if (width < height && height > newHeigth) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (height / newHeigth);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, newOpts);
    }

    /**
     * 按质量压缩图片
     * @param bitmap 要压缩的图片
     * @param mega 压缩后的大小单位kb
     * **/
    public Bitmap compressWithQuality(Bitmap bitmap , int mega) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024 >=mega  && options > 0) {
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);
    }
}
