package com.example.islingtonclothingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.islingtonclothingapplication.Adapter.FavouriteAdapter;
import com.example.islingtonclothingapplication.Common.Common;
import com.example.islingtonclothingapplication.Common.RecyclerItemTouchHelper;
import com.example.islingtonclothingapplication.Common.RecyclerItemTouchHelperListener;
import com.example.islingtonclothingapplication.Database.ModelDB.Favourite;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavouriteListActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recycler_fav;

    RelativeLayout rootLayout;

    CompositeDisposable compositeDisposable;

    FavouriteAdapter favouriteAdapter;
    List<Favourite>localFavourites = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);


        compositeDisposable = new CompositeDisposable();

        recycler_fav = (RecyclerView)findViewById(R.id.recycler_fav);
        recycler_fav.setLayoutManager(new LinearLayoutManager(this));
        recycler_fav.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_fav);


        loadFavouritesItem();
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavouritesItem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
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
        localFavourites = favourites;
        favouriteAdapter = new FavouriteAdapter(this,favourites);
        recycler_fav.setAdapter(favouriteAdapter);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof FavouriteAdapter.FavouriteViewHolder){
            String name = localFavourites.get(viewHolder.getAdapterPosition()).name;
            final Favourite deletedItem = localFavourites.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            //Dlete item from

            favouriteAdapter.removeItem(deletedIndex);

            //Delete from roomdatabase

            Common.favouriteRepository.delete(deletedItem);

            Snackbar snackbar = Snackbar.make(rootLayout, new StringBuilder(name).append("removed from favourite list").toString(),
                    Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    favouriteAdapter.restoreItem(deletedItem,deletedIndex);
                    Common.favouriteRepository.insertFav(deletedItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }

}