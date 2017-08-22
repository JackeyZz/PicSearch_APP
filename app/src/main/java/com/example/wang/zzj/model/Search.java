package com.example.wang.zzj.model;

import java.io.Serializable;

/**
 * Created by wang on 2016/1/22.
 */
public class Search implements Serializable {

    public enum SearchMethod{
        Color, Texture, All;

        public static String[] NAME = new String[]{"颜色特征", "纹理特征", "颜色和纹理特征"};

        public String getName(){
            return NAME[ordinal()];
        }

    }

    String fileName;


    SearchMethod method;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }


    public void setMethod(SearchMethod method) {
        this.method = method;
    }

    public SearchMethod getMethod() {
        return method;
    }
}
