package com.example.wang.zzj.service;

import android.content.Context;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by wang on 2016/1/22.
 */
public class ServiceManager {

    private static ServiceManager sInstance;
    private Context mContext;
    Map<String, BaseService> mServiceCache;


    public static final String IMAGE_SERVICE = "image_service";

    public static synchronized ServiceManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ServiceManager(context.getApplicationContext());
        }
        return sInstance;
    }

    public ServiceManager(Context context) {
        mContext = context;
        mServiceCache = new WeakHashMap<>();
    }

    public BaseService getService(String serviceName){
        BaseService service = mServiceCache.get(serviceName);
        if (service == null) {
            if (IMAGE_SERVICE.equals(serviceName)) {
                service = new ImageService(mContext);
            }
            mServiceCache.put(serviceName, service);
        }
        return service;
    }


}
