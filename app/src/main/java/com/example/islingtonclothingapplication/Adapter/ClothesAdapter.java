package com.example.islingtonclothingapplication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.islingtonclothingapplication.Common.Common;
import com.example.islingtonclothingapplication.Database.ModelDB.Cart;
import com.example.islingtonclothingapplication.Database.ModelDB.Favourite;
import com.example.islingtonclothingapplication.Interface.IItemClickListener;
import com.example.islingtonclothingapplication.R;
import com.example.islingtonclothingapplication.model.Clothes;
import com.google.gson.Gson;
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
    public void onBindViewHolder(@NonNull final ClothesViewHolder holder, final int position) {

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

        //for favourites working system

        if (Common.favouriteRepository.isfavourite(Integer.parseInt(clothesList.get(position).ID)) == 1)
            holder.btn_favourites.setImageResource(R.drawable.ic_baseline_favorite_24);
        else
            holder.btn_favourites.setImageResource(R.drawable.ic_baseline_favorite_border_white_24);

        holder.btn_favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.favouriteRepository.isfavourite(Integer.parseInt(clothesList.get(position).ID)) != 1){
                    addOrRemovefavourite(clothesList.get(position),true);
                    holder.btn_favourites.setImageResource(R.drawable.ic_baseline_favorite_24);
                }

                else{
                    addOrRemovefavourite(clothesList.get(position),false);
                    holder.btn_favourites.setImageResource(R.drawable.ic_baseline_favorite_border_white_24);
                }
            }
        });

    }

    private void addOrRemovefavourite(Clothes clothes, boolean isAdd) {
        Favourite favourite = new Favourite();
        favourite.id = clothes.ID;
        favourite.link = clothes.Link;
        favourite.name = clothes.Name;
        favourite.price = clothes.Price;
        favourite.clothesid= clothes.CategoryId;

        if (isAdd)
            Common.favouriteRepository.insertFav(favourite);
        else
            Common.favouriteRepository.delete(favourite);
    }



    private void showAddToCartDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.add_to_cart_layout, null);


        //View

        ImageView img_product_dialog = (ImageView) itemView.findViewById(R.id.img_cart_product);
        final ElegantNumberButton txt_count = (ElegantNumberButton) itemView.findViewById(R.id.txt_count);
        TextView txt_product_dialg = (TextView) itemView.findViewById(R.id.edt_comment);

        RadioButton rdi_day = (RadioButton) itemView.findViewById(R.id.rdi_timeDay);

        RadioButton rdi_4day = (RadioButton) itemView.findViewById(R.id.rdi_4Day);

        RadioButton rdi_week = (RadioButton) itemView.findViewById(R.id.rdi_timeWeek);


        rdi_day.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.daysfor_rent = 0;
                }
            }
        });
        rdi_4day.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.daysfor_rent = 0;
                }
            }
        });

        rdi_week.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Common.daysfor_rent = 1;
                }
            }
        });


        //Recycle work for topping

        RecyclerView recycler_top = (RecyclerView) itemView.findViewById(R.id.recycler_top);
        recycler_top.setLayoutManager(new LinearLayoutManager(context));
        recycler_top.setHasFixedSize(true);

        MultiChoiceAdapter adapter = new MultiChoiceAdapter(context, Common.toppingList);
        recycler_top.setAdapter(adapter);


        //Set data

        Picasso.with(context)
                .load(clothesList.get(position).Link)
                .into(img_product_dialog);
        txt_product_dialg.setText(clothesList.get(position).Name);

        builder.setView(itemView);
        builder.setNegativeButton("Add To Cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Common.daysfor_rent == -1) {
                    Toast.makeText(context, "Please choose the number of days to be rented", Toast.LENGTH_SHORT).show();
                    return;
                }
                showConfirmDialog(position, txt_count.getNumber());
                dialog.dismiss();
            }
        });

        builder.show();

    }

    private void showConfirmDialog(final int position, final String number) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.confirm_add_to_cart_layout, null);

        //View

        ImageView img_product_dialog = (ImageView) itemView.findViewById(R.id.img_product);
        final TextView txt_product_dialog = (TextView) itemView.findViewById(R.id.txt_confirm_cart_product_name);

        TextView txt_product_price = (TextView) itemView.findViewById(R.id.txt_confirm_cart_product_price);

        //Set data

        Picasso.with(context).load(clothesList.get(position).Link).into(img_product_dialog);
        txt_product_dialog.setText(new StringBuilder(clothesList.get(position).Name).append("   x   ")
                .append(number).append(" for ")
                .append(Common.daysfor_rent == 0 ? "Day 2" : "1 Week").toString());

        double price = (Double.parseDouble(clothesList.get(position).Price) * Double.parseDouble(number)) + Common.topPrice;

        if (Common.daysfor_rent == 1) {
            price += 100;
        }
        if (Common.daysfor_rent == 2) {
            price += 200;
        }
        if (Common.daysfor_rent == 1) {
            price += 300;
        }

        StringBuilder topp_final_comment = new StringBuilder("");
        for (String line : Common.toppingAdded)
            topp_final_comment.append(line).append("\n");

        //left smthing to explain


        final double finalPrice = price;
        builder.setNegativeButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();
                try {
                    //Add to SQLITE
                    //Implement late in next part

                    Cart cartItem = new Cart();
                    cartItem.name = txt_product_dialog.getText().toString();
                    cartItem.amount = Integer.parseInt(number);
                    cartItem.price = finalPrice;
                    cartItem.link = clothesList.get(position).Link;

                    //Added to DB

                    Common.cartRepository.insertToCart(cartItem);
                    Log.d("Clothing_Debug", new Gson().toJson(cartItem));
                    Toast.makeText(context, "Save item to cart success", Toast.LENGTH_SHORT).show();

                } catch (Exception ex) {
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }


        });

        builder.setView(itemView);
        builder.show();

    }

    @Override
    public int getItemCount() {
        return clothesList.size();
    }
}
