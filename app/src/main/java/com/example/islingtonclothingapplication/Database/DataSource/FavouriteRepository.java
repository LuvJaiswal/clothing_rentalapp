package com.example.islingtonclothingapplication.Database.DataSource;

import com.example.islingtonclothingapplication.Database.ModelDB.Favourite;

import java.util.List;

import io.reactivex.Flowable;

public class FavouriteRepository implements IFavouriteDataSource {
    private IFavouriteDataSource favouriteDataSource;

    public FavouriteRepository(IFavouriteDataSource favouriteDataSource) {
        this.favouriteDataSource = favouriteDataSource;
    }

    private static FavouriteRepository instance;

    public static FavouriteRepository getInstance(IFavouriteDataSource favouriteDataSource) {

        if (instance == null)
            instance = new FavouriteRepository(favouriteDataSource);
        return instance;

    }


    @Override
    public Flowable<List<Favourite>> getFavItems() {
        return favouriteDataSource.getFavItems();
    }

    @Override
    public int isfavourite(int itemid) {
        return favouriteDataSource.isfavourite(itemid);
    }

    @Override
    public void delete(Favourite favourite) {
        favouriteDataSource.delete(favourite);
    }
}
