package com.example.islingtonclothingapplication.Database.Local;

import com.example.islingtonclothingapplication.Database.DataSource.IFavouriteDataSource;
import com.example.islingtonclothingapplication.Database.ModelDB.Favourite;

import java.util.List;

import io.reactivex.Flowable;

public class FavouriteDataSource implements IFavouriteDataSource {

    private FavouriteDAO favouriteDAO;
    private static FavouriteDataSource instance;


    public FavouriteDataSource(FavouriteDAO favouriteDAO) {
        this.favouriteDAO = favouriteDAO;
    }


    public static FavouriteDataSource getInstance(FavouriteDAO favouriteDAO){
        if (instance == null)
            instance = new FavouriteDataSource(favouriteDAO);
        return instance;
    }

    @Override
    public Flowable<List<Favourite>> getFavItems() {
        return favouriteDAO.getFavItems();
    }

    @Override
    public int isfavourite(int itemid) {
        return favouriteDAO.isfavourite(itemid);
    }

    @Override
    public void delete(Favourite favourite) {
        favouriteDAO.delete(favourite);
    }
}
