package com.example.islingtonclothingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.islingtonclothingapplication.Adapter.CategoryAdapter;
import com.example.islingtonclothingapplication.Common.Common;
import com.example.islingtonclothingapplication.Remote.IMyAPI;
import com.example.islingtonclothingapplication.model.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class  ServerActivity extends AppCompatActivity {


    private static final int REQUEST_PERMISSION_CODE = 1111 ;
    RecyclerView recycler_menu;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyAPI mService;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case  REQUEST_PERMISSION_CODE:
            {
                if (grantResults.length > 0&& grantResults [0] ==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }

            break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new);

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
//
//      ActionBarDrawerToggle  toggle = new ActionBarDrawerToggle(
//              this, drawer, toolbar, R.string.open, R.string.close);
//       drawer.addDrawerListener(toggle);
//       toggle.syncState();


//        NavigatigationView navigatigationView = (NavigationView)findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        },REQUEST_PERMISSION_CODE);


        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"Replace with your action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //View

        recycler_menu = (RecyclerView) findViewById(R.id.recycler_menu);
        recycler_menu.setLayoutManager(new GridLayoutManager(this,2));
        recycler_menu.setHasFixedSize(true);

        mService = Common.getAPI();

        getadminCategory();



    }

    private void getadminCategory() {
        compositeDisposable.add(mService.getCategory().observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Consumer<List<Category>>() {
            @Override
            public void accept(List<Category> categories) throws Exception {
               displaCategoryList(categories);
            }
        }));
    }

    private void displaCategoryList(List<Category> categories) {
        CategoryAdapter adapter = new CategoryAdapter(this,categories);
        recycler_menu.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getadminCategory();
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