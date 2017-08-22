package com.example.wang.zzj.viewmodel;

import android.content.Context;

import com.example.wang.zzj.exception.ViewNotExistException;
import com.example.wang.zzj.service.ImageService;
import com.example.wang.zzj.service.ServiceManager;

import java.lang.ref.WeakReference;

/**
 * Created by wang on 2016/1/22.
 */
public class MainActivityViewModel {

    String TAG = MainActivityViewModel.class.getName();

    WeakReference<IView> view;   //弱引

    Context context;

    ImageService imageService;

    public MainActivityViewModel(Context context, IView view){
        this.context = context;
        this.view = new WeakReference<>(view);
        imageService = (ImageService) ServiceManager.getInstance(context).getService(ServiceManager.IMAGE_SERVICE);

    }

    public String getImgName(){
        return imageService.getImgName();
    }


    IView getViewInstance() throws ViewNotExistException{

        IView v = view.get();
        if (v == null){
            throw new ViewNotExistException(TAG);
        }
        return v;
    }

    public interface IView{

    }

}
