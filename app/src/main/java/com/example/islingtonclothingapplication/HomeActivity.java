package com.example.islingtonclothingapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


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

import java.io.File;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
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
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int PICK_FILE_REQUEST = 1222 ;
    SliderLayout sliderLayout;
    IMyAPI mService;

    RecyclerView lst_category;

    private static final String TAG = "HomeActivity";

    //for avatar and nav name phone
    CircleImageView img_avatar;

    Uri selectedFileUri;

    TextView txt_name,txt_email;


    //Rxjjava
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    //for toggle navigation
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    //Notification badge

    NotificationBadge badge;

    ImageView cart_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //for navigation

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        drawerLayout.bringToFront();
        toggle.syncState();
        nav = findViewById(R.id.nav_menu);
        nav.setNavigationItemSelectedListener(HomeActivity.this);

        mService = Common.getAPI();

//        lst_category = (RecyclerView)findViewById(R.id.categoryList);
//
//        lst_category.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        lst_category.setHasFixedSize(true);


       // sliderLayout = findViewById(R.id.slider);


        /****
         * p
        for avatar only
         *
         */


        //updating the navbar -- may be wrong approach

        View headerView = nav.getHeaderView(0);
        txt_name = headerView.findViewById(R.id.txt_avatar_name);
        txt_email =headerView.findViewById(R.id.txt_avatar_email);
        img_avatar = headerView.findViewById(R.id.img_avatar);
        txt_name.setText("Bikas");


        //event of avatar

//        img_avatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                chooseImagee();
//            }
//        });


        //set Info
//
//        txt_name.setText(Common.currentuser.getName());
//        txt_email.setText(Common.currentuser.getEmail());
//
//
//        //set avatar
//

//        if(!TextUtils.isEmpty(Common.currentuser.getAvatarUrl())){
//            Picasso.with(this)
//                    .load(new StringBuilder(Common.BASE_URL)
//                    .append("user_avatar/")
//                    .append(Common.currentuser.getAvatarUrl()).toString())
//                    .into(img_avatar);
//        }



        //Get banner

      // getBannerImage();



        //save newestToppingList
       // getToppingList();

        //InitDatabase
       // initDB();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.iconmenu, menu);

                View view = menu.findItem(R.id.cart_menu).getActionView();
        badge = view.findViewById(R.id.badges);

        cart_icon = view.findViewById(R.id.cart_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Cart activity clicked", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(HomeActivity.this, CartActivity.class);
                startActivity(i);
            }
        });

        //updateCartCount();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_home:
                 Toast.makeText(getApplicationContext(),"Test 123232",Toast.LENGTH_LONG).show();
                return true;
            case R.id.cart_icon:
                Toast.makeText(getApplicationContext(),"Test Cart",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }







    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_cart:
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    /***
     * AVATAR WORKING UPLOAD FILE
     */

//
//    private void chooseImagee() {
//        startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(),"Select a file"),
//                PICK_FILE_REQUEST);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == PICK_FILE_REQUEST)
            {
                if (data!=null){
                    selectedFileUri = data.getData();

                    if (selectedFileUri !=null && !selectedFileUri.getPath().isEmpty()){
                        img_avatar.setImageURI(selectedFileUri);
                        uploadFile();
                    }
                    else
                        Toast.makeText(this, "cannot be uploaded to the server", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    private void uploadFile() {
        if (selectedFileUri != null){
//            File file = FileUtils.getFile(this,selectedFileUri);
//
//            String fileName = new StringBuilder(Common.currentuser.getEmail())
//                    .append(FileUtils.getEx)

        }

    }

//
//
//
//
//    private void initDB() {
//        Common.cartDatabase = CartDatabase.getInstance(this);
//        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.cartDatabase.cartDAO()));
//
//    }
//
//    private void getToppingList() {
//        compositeDisposable.add(mService.getClothes(Common.TOPPING_CLOTHES_ID)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<Clothes>>() {
//                    @Override
//                    public void accept(List<Clothes> clothes) throws Exception {
//                        Common.toppingList = clothes;
//                    }
//                }));
//    }
//
////    private void getMenu() {
////        compositeDisposable.add(mService.getCategory()
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(new Consumer<List<Category>>() {
////                    @Override
////                    public void accept(List<Category> categories) throws Exception {
////
////                        displayCategory(categories);
////                    }
////                }));
////
////    }
//
////    private void displayCategory(List<Category> categories) {
////        CategoryAdapter adapter = new CategoryAdapter(this, categories);
////        lst_category.setAdapter(adapter);
////    }
////
////
//    private void getBannerImage() {
//        compositeDisposable.add(mService.getBanners()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<Banner>>() {
//                    @Override
//                    public void accept(List<Banner> banners) throws Exception {
//
//                        displayImage(banners);
//                    }
//                }));
//    }

//    //ctrl+o
//
//



//
//    private void displayImage(List<Banner> banners) {
//
//        HashMap<String, String> bannerMap = new HashMap<>();
//        for (Banner item : banners)
//            bannerMap.put(item.getName(), item.getLink());
//
//        for (String name : bannerMap.keySet()) {
//            TextSliderView textSliderView = new TextSliderView(this);
//            textSliderView.description(name)
//                    .image(bannerMap.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.Fit);
//
//            sliderLayout.addSlider(textSliderView);
//
//        }
//    }
//


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
//

//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }
//

    @Override
    protected void onResume() {
        super.onResume();
       // updateCartCount();
    }

}
