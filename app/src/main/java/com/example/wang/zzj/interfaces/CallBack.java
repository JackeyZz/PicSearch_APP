package com.example.wang.zzj.interfaces;

/**
 * Created by wang on 2016/1/22.
 */
public interface CallBack<T> {

    void success(T t);

    void error();
}
