package com.example.islingtonclothingapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.islingtonclothingapplication.Adapter.ClothesAdapter;
import com.example.islingtonclothingapplication.Common.Common;
import com.example.islingtonclothingapplication.Common.ProgressRequestBody;
import com.example.islingtonclothingapplication.Common.UploadCallBack;
import com.example.islingtonclothingapplication.Remote.IMyAPI;
import com.example.islingtonclothingapplication.model.Clothes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.io.File;
import java.util.List;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminClothesListActivity extends AppCompatActivity implements UploadCallBack {

    private static final int PICK_FILE_REQUEST = 11111;
    private static final int REQUEST_PERMISSION_CODE = 2222;
    IMyAPI mService;

    RecyclerView recycler_clothes;


    CompositeDisposable compositeDisposable = new CompositeDisposable();


    FloatingActionButton btn_add;

    ImageView img_browser;
    EditText edt_cloth_name, edt_cloth_price;

    Uri selected_uri;

    String uploaded_img_path;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data != null) {
                    selected_uri = data.getData();
                    if (selected_uri != null && !selected_uri.getPath().isEmpty()) {
                        img_browser.setImageURI(selected_uri);
                        uploadFileToServer();
                    } else
                        Toast.makeText(this, "Cannot upload File to the server", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Product");

        View view = LayoutInflater.from(this).inflate(R.layout.admin_user_add_new_product, null);

        edt_cloth_name = view.findViewById(R.id.edt_cloth_name);
        img_browser = view.findViewById(R.id.product_img_browser);
        edt_cloth_price = view.findViewById(R.id.edt_cloth_price);


        img_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(), "Select a file"),
                        PICK_FILE_REQUEST);
            }
        });

        //Setview

        builder.setView(view);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                uploaded_img_path = "";
                selected_uri = null;

            }
        }).setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (edt_cloth_name.getText().toString().isEmpty()) {
                    Toast.makeText(AdminClothesListActivity.this, "Please enter the name of the cloth name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edt_cloth_price.getText().toString().isEmpty()) {
                    Toast.makeText(AdminClothesListActivity.this, "Please enter the cloth price ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (uploaded_img_path.isEmpty()) {
                    Toast.makeText(AdminClothesListActivity.this, "Select cloth image", Toast.LENGTH_SHORT).show();
                    return;
                }
                compositeDisposable.add(mService.addNewProduct(edt_cloth_name.getText().toString(),
                        uploaded_img_path,
                        edt_cloth_price.getText().toString(),
                        Common.currentCategory.Id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Toast.makeText(AdminClothesListActivity.this, s, Toast.LENGTH_SHORT).show();
                                loadListClothes(Common.currentCategory.getId());

//                                           uploaded_img_path = "";
//                                           selected_uri = null;


                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(AdminClothesListActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }));
            }
        }).show();


    }

    private void uploadFileToServer() {

        if (selected_uri != null) {
            File file = FileUtils.getFile(this, selected_uri);

            String fileName = new StringBuilder(UUID.randomUUID().toString())
                    .append(FileUtils.getExtension(file.toString())).toString();


            ProgressRequestBody requestFile = new ProgressRequestBody(file, this);

            final MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", fileName, requestFile);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    mService.uploadProductFile(body)
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {

                                    try {
                                        if (response.body() != null) {
                                            uploaded_img_path = new StringBuilder(Common.BASE_URL)
                                                    .append("server/product/product_img/")
                                                    .append(response.body()) //.toString() not working here extra
                                                    .toString();
                                            Log.d("IMGPath", uploaded_img_path);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(AdminClothesListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }).start();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_clothes_list);

        mService = Common.getAPI();


        btn_add = findViewById(R.id.add_product_fab);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProductDialog();
            }
        });

        recycler_clothes = findViewById(R.id.recycler_admin_clothes_list);
        recycler_clothes.setLayoutManager(new GridLayoutManager(this, 2));
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
        ClothesAdapter adapter = new ClothesAdapter(this, clothes);
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

    @Override
    public void onProgressUpdate(int percentage) {

    }
}