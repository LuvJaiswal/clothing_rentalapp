package com.example.islingtonclothingapplication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.islingtonclothingapplication.Interface.IItemClickListener;
import com.example.islingtonclothingapplication.R;
import com.example.islingtonclothingapplication.model.Clothes;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ClothesAdapter extends RecyclerView.Adapter<ClothesViewHolder> {

    Context context;
    List<Clothes> clothesList;

    IItemClickListener IItemClickListener;

    public ClothesAdapter(Context context, List<Clothes> clothesList) {
        this.context = context;
        this.clothesList = clothesList;
    }

    @NonNull
    @Override
    public ClothesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.clothes_item_layout, null);
        return new ClothesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClothesViewHolder holder, final int position) {

        holder.txt_price.setText(new StringBuilder("$").append(clothesList.get(position).Price).toString());
        holder.txt_clothe_name.setText(clothesList.get(position).Name);

        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddToCartDialog(position);
            }
        });
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

    private void showAddToCartDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.add_to_cart_layout, null);


        //View

        ImageView img_product_dialog = (ImageView) itemView.findViewById(R.id.img_cart_product);

        ElegantNumberButton txt_count = (ElegantNumberButton) itemView.findViewById(R.id.txt_count);
        TextView txt_product_dialg = (TextView) itemView.findViewById(R.id.edt_comment);

        RadioButton rdi_day = (RadioButton) itemView.findViewById(R.id.rdi_timeDay);

        RadioButton rdi_4day = (RadioButton)itemView.findViewById(R.id.rdi_4Day);

        RadioButton rdi_week = (RadioButton)itemView.findViewById(R.id.rdi_timeWeek);


        //Set data

        Picasso.with(context)
                .load(clothesList.get(position).Link)
                .into(img_product_dialog);
        txt_product_dialg.setText(clothesList.get(position).Name);

        builder.setView(itemView);
        builder.setNegativeButton("Add To Cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.show();

    }

    @Override
    public int getItemCount() {
        return clothesList.size();
    }
}
