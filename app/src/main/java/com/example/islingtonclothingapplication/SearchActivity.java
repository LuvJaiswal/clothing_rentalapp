package com.example.islingtonclothingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.islingtonclothingapplication.Adapter.ClothesAdapter;
import com.example.islingtonclothingapplication.Common.Common;
import com.example.islingtonclothingapplication.Remote.IMyAPI;
import com.example.islingtonclothingapplication.model.Clothes;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    List<String> suggestList = new ArrayList<>();
    List<Clothes> localDataSource = new ArrayList<>();
    MaterialSearchBar searchBar;

    IMyAPI mService;

    RecyclerView rec_search;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    ClothesAdapter searchAdapter, adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Init service

        mService = Common.getAPI();

        rec_search = (RecyclerView) findViewById(R.id.rec_search);
        searchBar =(MaterialSearchBar)findViewById(R.id.searchBar);

        rec_search.setLayoutManager(new GridLayoutManager(this, 2));
        searchBar.setHint("Enter Your Cloth");

        loadAllClothes();


        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                List<String> suggest = new ArrayList<>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                searchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                    rec_search.setAdapter(adapter); //restpring the who
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });


    }

    private void startSearch(CharSequence text) {
        List<Clothes> result = new ArrayList<>();

        for (Clothes clothes:localDataSource)
            if (clothes.Name.contains(text))
                result.add(clothes);

            searchAdapter = new ClothesAdapter(this,result);
            rec_search.setAdapter(searchAdapter);

    }


    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    /***
     *Search work
     */


    private void loadAllClothes() {
        compositeDisposable.add(mService.getAllClothes().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Clothes>>() {
                    @Override
                    public void accept(List<Clothes> clothes) throws Exception {
                        displayListCloth(clothes);
                        buildSuggestList(clothes);
                    }
                }));
    }

    private void buildSuggestList(List<Clothes> clothes) {

        for (Clothes clothes1 : clothes)
            suggestList.add(clothes1.Name);
        searchBar.setLastSuggestions(suggestList);
    }

    private void displayListCloth(List<Clothes> clothes) {
        localDataSource = clothes;
        adapter = new ClothesAdapter(this, clothes);
        rec_search.setAdapter(adapter);
    }
}