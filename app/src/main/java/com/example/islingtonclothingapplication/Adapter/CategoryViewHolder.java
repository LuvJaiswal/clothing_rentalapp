package com.example.islingtonclothingapplication.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.islingtonclothingapplication.Interface.IItemClickListener;
import com.example.islingtonclothingapplication.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
    ImageView img_product;
    TextView txt_category_name;

    IItemClickListener itemClickListener;

    public void setItemClickListener(IItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        img_product = (ImageView)itemView.findViewById(R.id.image_product);
        txt_category_name = (TextView)itemView.findViewById(R.id.txt_category_name);

     itemView.setOnClickListener(this);
     itemView.setOnLongClickListener(this);


    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,false);
    }

    @Override
    public boolean onLongClick(View v) {
       itemClickListener.onClick(v,true);
       return true;
    }
}
