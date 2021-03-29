package com.example.islingtonclothingapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.islingtonclothingapplication.Database.ModelDB.Favourite;
import com.example.islingtonclothingapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    Context context;
    List<Favourite> favouriteList;

    public FavouriteAdapter(Context context, List<Favourite> favouriteList) {
        this.context = context;
        this.favouriteList = favouriteList;
    }



    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(context).inflate(R.layout.fav_item_layout,parent,false);
        return new FavouriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {

        Picasso.with(context).load(favouriteList.get(position).link).into(holder.fav_img_product);
        holder.fav_txt_product_price.setText(new StringBuilder("$").append(favouriteList.get(position).price).toString());
        holder.fav_txt_product_name.setText(favouriteList.get(position).name);


    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    class FavouriteViewHolder extends RecyclerView.ViewHolder  {

        ImageView fav_img_product;
        TextView fav_txt_product_name, fav_txt_product_price;

        public FavouriteViewHolder(View itemView){
            super(itemView);
            fav_img_product = (ImageView)itemView.findViewById(R.id.fav_img_product);
            fav_txt_product_name = (TextView) itemView.findViewById(R.id.fav_txt_product_name);
            fav_txt_product_price = (TextView) itemView.findViewById(R.id.fav_txt_product_price);

        }
    }
}
