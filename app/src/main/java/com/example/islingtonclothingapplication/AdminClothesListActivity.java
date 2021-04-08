package com.example.islingtonclothingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.islingtonclothingapplication.Adapter.ClothesAdapter;
import com.example.islingtonclothingapplication.Common.Common;
import com.example.islingtonclothingapplication.Remote.IMyAPI;
import com.example.islingtonclothingapplication.model.Clothes;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AdminClothesListActivity extends AppCompatActivity {

    IMyAPI mService;

    RecyclerView recycler_clothes;

    TextView txt_banner_name;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_clothes_list);

        mService = Common.getAPI();
        recycler_clothes=(RecyclerView)findViewById(R.id.recycler_admin_clothes_list);
        recycler_clothes.setLayoutManager(new GridLayoutManager(this,2));
        recycler_clothes.setHasFixedSize(true);


        loadListClothes(Common.currentCategory.getId());

    }

    private void loadListClothes(String id) {
        compositeDisposable.add(mService.getClothes(id).observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Consumer<List<Clothes>>() {
            @Override
            public void accept(List<Clothes> clothes) throws Exception {
                displayClothesList(clothes);
            }
        }));
    }

    private void displayClothesList(List<Clothes> clothes) {
        ClothesAdapter adapter = new ClothesAdapter(this,clothes);
        recycler_clothes.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        loadListClothes(Common.currentCategory.getId());
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}