package com.example.islingtonclothingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.islingtonclothingapplication.Adapter.FavouriteAdapter;
import com.example.islingtonclothingapplication.Common.Common;
import com.example.islingtonclothingapplication.Database.ModelDB.Favourite;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavouriteListActivity extends AppCompatActivity {

    RecyclerView recycler_fav;

    CompositeDisposable compositeDisposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

        compositeDisposable = new CompositeDisposable();

        recycler_fav = (RecyclerView)findViewById(R.id.recycler_fav);
        recycler_fav.setLayoutManager(new LinearLayoutManager(this));
        recycler_fav.setHasFixedSize(true);

        loadFavouritesItem();
        
    }

    private void loadFavouritesItem() {
        compositeDisposable.add(Common.favouriteRepository.getFavItems()
        .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Favourite>>() {
                    @Override
                    public void accept(List<Favourite> favourites) throws Exception {
                        displayFavouriteItem(favourites);
                    }
                })
        );
    }

    private void displayFavouriteItem(List<Favourite> favourites) {
        FavouriteAdapter favouriteAdapter = new FavouriteAdapter(this,favourites);
        recycler_fav.setAdapter(favouriteAdapter);

    }
}