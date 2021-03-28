package com.example.islingtonclothingapplication.Database.ModelDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Favourite")
public class Favourite {

    @NonNull
    @PrimaryKey

    @ColumnInfo(name = "id")
    public String id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "link")
    public String link;


    @ColumnInfo(name = "price")
    public String price;

    @ColumnInfo(name = "clothesid")
    public String clothesid;


}
