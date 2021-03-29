package com.example.islingtonclothingapplication.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {
    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class FavouriteViewHolder extends RecyclerView.ViewHolder  {

        public FavouriteViewHolder(View itemView){
            super(itemView);
        }
    }
}
