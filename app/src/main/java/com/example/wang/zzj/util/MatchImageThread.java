package com.example.wang.zzj.util;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.example.wang.zzj.R;
import com.example.wang.zzj.activity.ResultSwitcherActivity;
import com.googlecode.javacv.cpp.opencv_core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MatchImageThread extends AsyncTask<String, Integer, List<ImageItem>> {

    Context c;
    public AlertDialog dialog;
    public AlertDialog.Builder builder;

    public MatchImageThread(Context c) {
        this.c = c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showDialog("检索系统");
    }

    @Override
    protected List<ImageItem> doInBackground(String... urls) {
        List<ImageItem> list = new ArrayList<ImageItem>();
        Vector<String> vector = Filepath.GetTestXlsFileName(Constant.ORG_PATH);
        if (vector != null) {
            int len = vector.size();


            opencv_core.CvMat tem;
            tem = SIFTMatch.featureDetect(Constant.ORG_PATH + "/tem.t");
            for (int i = 0; i < len; i++) {
                ImageItem item = new ImageItem();
                opencv_core.CvMat tem2;
                tem2 = SIFTMatch.featureDetect(Constant.ORG_PATH + "/" + vector.get(i));
                item.setPath(Constant.ORG_PATH + "/" + vector.get(i));

                item.setResult(0 - SIFTMatch.match(tem, tem2, vector.get(i)));

                list.add(item);
            }

            selectSortSIFT(list);

            for (int i = 0; i < len; i++) {
                ImageItem item = list.get(i);

//                item.setPath(Constant.ORG_PATH + "/" + vector.get(i));
                item.setResult(HSVMatch.doit(item.getPath()));


            }


            selectSortHSV(list);
            selectSort(list);

        }


        return list;

    }

    @Override
    protected void onPostExecute(List<ImageItem> list) {
        dialog.dismiss();
        if (list != null) {
            Intent intent = new Intent();
            intent.setClass(c, ResultSwitcherActivity.class);
            intent.putExtra(Constant.Key.LIST_RESULT, (Serializable) list);
            intent.putExtra("type", "综合匹配");
            c.startActivity(intent);
        }
    }

    public static void selectSortSIFT(List<ImageItem> list) {
        int size = list.size();
        ImageItem temp;
        for (int i = 0; i < size; i++) {
            int k = i;
            for (int j = size - 1; j > i; j--) {
                if (list.get(j).getResult() < list.get(k).getResult())
                    k = j;
            }
            temp = list.get(i);
            list.set(i, list.get(k));
            list.set(k, temp);
            list.get(i).setSiftsort(i);
        }
    }
    public static void selectSortHSV(List<ImageItem> list) {
        int size = list.size();
        ImageItem temp;
        for (int i = 0; i < size; i++) {
            int k = i;
            for (int j = size - 1; j > i; j--) {
                if (list.get(j).getResult() < list.get(k).getResult())
                    k = j;
            }
            temp = list.get(i);
            list.set(i, list.get(k));
            list.set(k, temp);
            list.get(i).setHsvsort(i);
        }
    }
    public static void selectSort(List<ImageItem> list) {
        int size = list.size();
        ImageItem temp;
        for (int i = 0; i < size; i++) {
            int k = i;
            for (int j = size - 1; j > i; j--) {
                if ((list.get(j).getHsvsort()+ list.get(j).getSiftsort())< (list.get(k).getHsvsort()+ list.get(k).getSiftsort()))
                    k = j;
            }
            temp = list.get(i);
            list.set(i, list.get(k));
            list.set(k, temp);

        }
    }
    public void showDialog(String res) {

        builder = new AlertDialog.Builder(c);

        builder.setTitle(res);
        // 通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View viewd = LayoutInflater.from(c).inflate(
                R.layout.dialog_layout, null);
        // 设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(viewd);
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }
}