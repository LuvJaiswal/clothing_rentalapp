package com.example.islingtonclothingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    SliderLayout sliderLayout;
    IMyAPI mService;

    RecyclerView lst_category;

    private static final String TAG = "HomeActivity";

    //Rxjjava
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    //for toggle navigation

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //for navigation

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView =(NavigationView)findViewById(R.id.navmenu);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.menu_home:
                        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                        startActivity(intent);
                        Log.d(TAG,"homeclicked");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.cart_icon :
                        Toast.makeText(getApplicationContext(), "Cart clicked", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });


        mService = Common.getAPI();

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
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
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
}
