package com.example.islingtonclothingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.example.islingtonclothingapplication.Database.DataSource.FavouriteRepository;
import com.example.islingtonclothingapplication.Database.Local.CartDatabase;
import com.example.islingtonclothingapplication.Database.Local.FavouriteDataSource;
import com.example.islingtonclothingapplication.Database.ModelDB.Favourite;
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
import com.nex3z.notificationbadge.NotificationBadge;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SliderLayout sliderLayout;
    IMyAPI mService;

    RecyclerView lst_category;


    private static final String TAG = "HomeActivity";

    //Rxjjava
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    //for toggle navigation

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;

    //Notification badge

    NotificationBadge badge;

    ImageView cart_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //for navigation

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        nav = (NavigationView) findViewById(R.id.navmenu);
        nav.setNavigationItemSelectedListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//                switch (menuItem.getItemId()) {
//                    case R.id.menu_home:
//                        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
//                        startActivity(intent);
//                        Log.d(TAG, "homeclicked");
//                        break;
//
//
//                    case R.id.cart_icon:
//                        Toast.makeText(getApplicationContext(), "Cart clicked", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//
//                drawerLayout.closeDrawer(GravityCompat.START);
//                return true;
//            }
//        });


        mService = Common.getAPI();

        lst_category = (RecyclerView)findViewById(R.id.categoryList);

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
        Common.favouriteRepository = FavouriteRepository.getInstance(FavouriteDataSource.getInstance(Common.cartDatabase.favouriteDAO()));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        View view = menu.findItem(R.id.cart_menu).getActionView();
        badge = (NotificationBadge)view.findViewById(R.id.badges);

        cart_icon = (ImageView)view.findViewById(R.id.cart_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Cart activity clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this,CartActivity.class));
            }
        });

        updateCartCount();
        return true;
    }



    private void updateCartCount() {
        if (badge == null)
            return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Common.cartRepository.countCartItems() == 0)
                    badge.setVisibility(View.INVISIBLE);
                else {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(String.valueOf(Common.cartRepository.countCartItems()));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cart_menu) {
            return true;
        }
        if (id == R.id.fav_menu) {
           startActivity(new Intent(HomeActivity.this,FavouriteListActivity.class));
           return true;
        }

        if (id == R.id.search_menu) {
            startActivity(new Intent(HomeActivity.this,SearchActivity.class));
            return true;
        }

        if (id == R.id.admin_category) {
            startActivity(new Intent(HomeActivity.this,ServerActivity.class));
            return true;
        }

        if (id == R.id.logout) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to Exit");
            builder.setCancelable(true);
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        }
        if (id == R.id.review) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setData(Uri.parse("email"));
                String[] s = {"Uaayusha@gmail.com"};
                i.putExtra(Intent.EXTRA_EMAIL, s);
                i.putExtra(Intent.EXTRA_SUBJECT, "'Write your Subject here'");
                i.putExtra(Intent.EXTRA_TEXT, "Your FeedBack please");
                i.setType("message/rfc822");
                Intent chooser = Intent.createChooser(i, "Give your Feedback from Gmail");
                startActivity(chooser);
                return true;
        }







        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
//            case R.id.menu_home:
//                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
//                startActivity(intent);
//                Log.d(TAG, "homeclicked");
//                break;
//
//
//            case R.id.cart_icon:
//                Toast.makeText(getApplicationContext(), "Cart clicked", Toast.LENGTH_SHORT).show();
//                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }
}
