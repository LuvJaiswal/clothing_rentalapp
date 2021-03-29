package com.example.islingtonclothingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.GridLayout;
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

public class ClothesActivity extends AppCompatActivity {

    IMyAPI mService;

    RecyclerView lst_clothes;

    TextView txt_banner_name;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        mService = Common.getAPI();

        lst_clothes = (RecyclerView) findViewById(R.id.rec_clothes_list);
        lst_clothes.setLayoutManager(new GridLayoutManager(this, 2));
        lst_clothes.setHasFixedSize(true);

        txt_banner_name = (TextView) findViewById(R.id.banner_name);
        txt_banner_name.setText(Common.currentCategory.Name);

        loadListClothes(Common.currentCategory.Id);
    }

    private void loadListClothes(String Id) {
        compositeDisposable.add(mService.getClothes(Id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Clothes>>() {
                    @Override
                    public void accept(List<Clothes> clothes) throws Exception {

                        displayClothesList(clothes);
                    }
                }));
    }

    private void displayClothesList(List<Clothes> clothes) {
        ClothesAdapter adapter = new ClothesAdapter(this, clothes);
        lst_clothes.setAdapter(adapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
