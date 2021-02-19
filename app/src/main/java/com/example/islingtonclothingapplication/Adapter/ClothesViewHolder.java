package com.example.islingtonclothingapplication.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.islingtonclothingapplication.Interface.IItemClickListener;
import com.example.islingtonclothingapplication.R;

public class ClothesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView img_product;
    TextView txt_clothe_name, txt_price;

    IItemClickListener itemClickListener;

    public void setItemClickListener(IItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ClothesViewHolder(@NonNull View itemView) {
        super(itemView);

        img_product = (ImageView) itemView.findViewById(R.id.image_product);
        txt_clothe_name = (TextView) itemView.findViewById(R.id.txt_cloth_name);
        txt_price = (TextView) itemView.findViewById(R.id.txt_price);


    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v);
    }
}
