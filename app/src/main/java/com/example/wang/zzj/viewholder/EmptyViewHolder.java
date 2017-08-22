package com.example.wang.zzj.viewholder;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.wang.zzj.R;


/**
 * Created by wang on 12/17/15. recylerView的列表为空时的显示
 */
public class EmptyViewHolder extends RecyclerView.ViewHolder {

    public EmptyViewHolder(ViewGroup parent, int image) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false));

        if(image != -1){
            ((ImageView) itemView.findViewById(R.id.image_view)).setImageResource(image);
        }

    }
}
