package com.example.islingtonclothingapplication.Database.Local;

import androidx.room.Delete;
import androidx.room.Query;

import com.example.islingtonclothingapplication.Database.ModelDB.Favourite;

import java.util.List;

import io.reactivex.Flowable;

public interface FavouriteDAO {

    @Query("SELECT * FROM Favourite")
    Flowable<List<Favourite>>getFavItems();

    @Query("SELECT EXISTS (SELECT 1 FROM Favourite WHERE id=:itemid)")
    int isfavourite(int itemid);

    @Delete
    void delete(Favourite favourite);



}
