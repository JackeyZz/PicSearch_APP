package com.example.wang.zzj.viewmodel;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.wang.zzj.exception.ViewNotExistException;
import com.example.wang.zzj.interfaces.CallBack;
import com.example.wang.zzj.service.ImageService;
import com.example.wang.zzj.service.ServiceManager;

import java.lang.ref.WeakReference;

/**
 * Created by wang on 2016/1/22.
 */
public class ResultActivityViewModel {

    String TAG = ResultActivityViewModel.class.getName();

    WeakReference<IView> view;

    Context context;

    ImageService imageService;

    public ResultActivityViewModel(Context context, IView view){
        this.context = context;
        this.view = new WeakReference<>(view);
        imageService = (ImageService) ServiceManager.getInstance(context).getService(ServiceManager.IMAGE_SERVICE);

    }


    public <T> void loadBitmap(T url, ContentResolver cr, ImageView imageView){
        imageService.loadBitmap(url, cr, imageView, new CallBack<Bitmap>() {

            @Override
            public void success(Bitmap bitmap) {
                try {
                    IView v = getViewInstance();
                    v.getBitmapSuccess();
                } catch (ViewNotExistException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error() {
                try {
                    IView v = getViewInstance();
                    v.getBitmapError();
                } catch (ViewNotExistException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    IView getViewInstance() throws ViewNotExistException{

        IView v = view.get();
        if (v == null){
            throw new ViewNotExistException(TAG);
        }
        return v;
    }

    public interface IView{

        void getBitmapSuccess();

        void getBitmapError();
    }
}
