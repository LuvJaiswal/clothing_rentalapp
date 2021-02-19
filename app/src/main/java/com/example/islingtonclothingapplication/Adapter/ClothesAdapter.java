package com.example.islingtonclothingapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.islingtonclothingapplication.Interface.IItemClickListener;
import com.example.islingtonclothingapplication.R;
import com.example.islingtonclothingapplication.model.Clothes;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ClothesAdapter extends RecyclerView.Adapter<ClothesViewHolder> {

    Context context;
    List<Clothes>clothesList;

    IItemClickListener IItemClickListener;

    public ClothesAdapter(Context context, List<Clothes> clothesList) {
        this.context = context;
        this.clothesList = clothesList;
    }

    @NonNull
    @Override
    public ClothesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.clothes_item_layout,null);
        return new ClothesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClothesViewHolder holder, int position) {

        holder.txt_price.setText(new StringBuilder("$").append(clothesList.get(position).Price).toString());
        holder.txt_clothe_name.setText(clothesList.get(position).Name);

        Picasso.with(context)
                .load(clothesList.get(position).Link)
                .into(holder.img_product);

        holder.setItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked the item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return clothesList.size();
    }
}
