package com.example.islingtonclothingapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.islingtonclothingapplication.ClothesActivity;
import com.example.islingtonclothingapplication.Common.Common;
import com.example.islingtonclothingapplication.HomeActivity;
import com.example.islingtonclothingapplication.Interface.IItemClickListener;
import com.example.islingtonclothingapplication.R;
import com.example.islingtonclothingapplication.UpdateCategoryActivity;
import com.example.islingtonclothingapplication.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ServerCategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    Context context;
    List<Category> categories;

    IItemClickListener IItemClickListener;

    public ServerCategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.server_category_menu_layout, null);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {

        //Load Image

        Picasso.with(context)
                .load(categories.get(position).Link)
                .into(holder.img_product);

        holder.txt_category_name.setText(categories.get(position).Name);


        //EVENT

        holder.setItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v, boolean isLongClick) {
                if (isLongClick) {
                    Common.currentCategory = categories.get(position);
                    context.startActivity(new Intent(context, UpdateCategoryActivity.class));
                }
                else {

                }
            }
        });

//            @Override
//            public void onClick(View v) {
//
//                Context context = v.getContext();
//                Common.currentCategory = categories.get(position);
//
//                context.startActivity(new Intent(context, UpdateCategoryActivity.class));
//
//            }
//        });

        }

        @Override
        public int getItemCount () {
            return categories.size();
        }
    }
