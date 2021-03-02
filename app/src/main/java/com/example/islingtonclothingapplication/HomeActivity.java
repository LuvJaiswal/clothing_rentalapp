package com.example.islingtonclothingapplication;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
<<<<<<< HEAD
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
||||||| merged common ancestors
=======
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
>>>>>>> Room_Database

import android.os.Bundle;
import android.widget.LinearLayout;


import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.islingtonclothingapplication.Adapter.CategoryAdapter;
import com.example.islingtonclothingapplication.Common.Common;
import com.example.islingtonclothingapplication.Database.DataSource.CartDataSource;
import com.example.islingtonclothingapplication.Database.DataSource.CartRepository;
import com.example.islingtonclothingapplication.Database.Local.CartDatabase;
import com.example.islingtonclothingapplication.Remote.IMyAPI;
import com.example.islingtonclothingapplication.model.Banner;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import com.daimajia.slider.library.SliderLayout;
import com.example.islingtonclothingapplication.model.Category;
import com.example.islingtonclothingapplication.model.Clothes;

public class HomeActivity extends AppCompatActivity {

    SliderLayout sliderLayout;
    private DrawerLayout drawer;
    IMyAPI mService;
    Toolbar toolbar;

    RecyclerView lst_category;


    //Rxjjava
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mService = Common.getAPI();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        lst_category = (RecyclerView) findViewById(R.id.categoryList);

        lst_category.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        lst_category.setHasFixedSize(true);

        sliderLayout = (SliderLayout) findViewById(R.id.slider);

        //Get banner

        getBannerImage();


        //Get menu
        getMenu();

        //save newestToppingList
        getToppingList();

        //InitDatabase
        initDB();
    }

    private void initDB() {
        Common.cartDatabase = CartDatabase.getInstance(this);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.cartDatabase.cartDAO()));

    }

    private void getToppingList() {
        compositeDisposable.add(mService.getClothes(Common.TOPPING_CLOTHES_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Clothes>>() {
                    @Override
                    public void accept(List<Clothes> clothes) throws Exception {
                        Common.toppingList = clothes;
                    }
                }));
    }

    private void getMenu() {
        compositeDisposable.add(mService.getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Category>>() {
                    @Override
                    public void accept(List<Category> categories) throws Exception {

                        displayCategory(categories);
                    }
                }));

    }

    private void displayCategory(List<Category> categories) {
        CategoryAdapter adapter = new CategoryAdapter(this,categories);
        lst_category.setAdapter(adapter);
    }

    private void getBannerImage() {
        compositeDisposable.add(mService.getBanners()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Banner>>() {
                    @Override
                    public void accept(List<Banner> banners) throws Exception {

                        displayImage(banners);
                    }
                }));
    }

    //ctrl+o


    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void displayImage(List<Banner> banners) {

        HashMap<String, String> bannerMap = new HashMap<>();
        for (Banner item : banners)
            bannerMap.put(item.getName(), item.getLink());

        for (String name : bannerMap.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView.description(name)
                    .image(bannerMap.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            sliderLayout.addSlider(textSliderView);

        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
