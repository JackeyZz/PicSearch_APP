package com.example.wang.zzj.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.wang.zzj.R;
import com.example.wang.zzj.interfaces.OnRecylerItemClickListener;
import com.example.wang.zzj.model.RecyclerViewItemArray;
import com.example.wang.zzj.util.Constant;
import com.example.wang.zzj.viewholder.EmptyViewHolder;

/**
 * Created by wang on 2016/1/22.
 */
public class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerViewItemArray itemArray;
    private OnRecylerItemClickListener listener;

    public ResultAdapter(RecyclerViewItemArray itemArray, OnRecylerItemClickListener listener){
        this.itemArray = itemArray;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case Constant.Type.RESULT:{
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
                return new ResultViewHolder(itemView);
            }

            case Constant.Type.EMPTY:{
                return new EmptyViewHolder(parent, -1);
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)){

            case Constant.Type.RESULT:{
                ResultViewHolder vh = (ResultViewHolder) holder;

                break;
            }

            case Constant.Type.EMPTY:{

                EmptyViewHolder vh = (EmptyViewHolder) holder;

                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemArray.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemArray.get(position).getDataType();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        ImageView resultImg;
        public ResultViewHolder(View itemView) {
            super(itemView);

            resultImg = (ImageView) itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(itemArray.get(getAdapterPosition()).getDataType(), getAdapterPosition());
                }
            });
        }
    }
}
