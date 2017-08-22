package com.example.wang.zzj.widget;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.wang.zzj.R;

/**
 * Created by wang on 2016/1/22.
 */
public class SelectMethodDialog extends AppCompatDialogFragment implements DialogInterface.OnKeyListener{

    OnSelectListener listener;

    OnBackListener backListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((AppCompatDialog)getDialog()).supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setOnKeyListener(this);
        setCancelable(false);
        View view = inflater.inflate(R.layout.dialog_select_method, container);
        view.findViewById(R.id.take_photo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    dismiss();
                    listener.cameraClick();
                }

            }
        });

        view.findViewById(R.id.to_album_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    dismiss();
                    listener.albumClick();
                }
            }
        });

        Log.d("test", "lalalla");

        return view;
    }

    public void setSelectListener(OnSelectListener listener){
        this.listener = listener;
    }

    public void setBackListener(OnBackListener backListener) {
        this.backListener = backListener;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        Log.d("test", "good");
        if (keyCode == KeyEvent.KEYCODE_BACK){
            dismiss();
            Log.d("test", "aaaaa");
            if (backListener != null){
                backListener.backClick();
            }
            return true;
        }

        return false;
    }

    public interface OnSelectListener{
        void cameraClick();

        void albumClick();
    }

    public interface OnBackListener{

        void backClick();
    }
}
