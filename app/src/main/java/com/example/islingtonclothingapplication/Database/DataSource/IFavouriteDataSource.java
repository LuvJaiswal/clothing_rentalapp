package com.example.islingtonclothingapplication.Database.DataSource;

import androidx.room.Delete;
import androidx.room.Query;

import com.example.islingtonclothingapplication.Database.ModelDB.Favourite;

import java.util.List;

import io.reactivex.Flowable;

public interface IFavouriteDataSource {


    Flowable<List<Favourite>> getFavItems();


    int isfavourite(int itemid);


    void delete(Favourite favourite);


    void insertFav(Favourite...favourite);
}
