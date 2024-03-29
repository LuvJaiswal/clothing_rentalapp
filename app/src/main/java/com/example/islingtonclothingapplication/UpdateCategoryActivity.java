package com.example.islingtonclothingapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.islingtonclothingapplication.Common.Common;
import com.example.islingtonclothingapplication.Common.ProgressRequestBody;
import com.example.islingtonclothingapplication.Common.UploadCallBack;
import com.example.islingtonclothingapplication.Remote.IMyAPI;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCategoryActivity extends AppCompatActivity implements UploadCallBack {

    private static final int PICK_FILE_REQUEST = 111111;
    ImageView img_browser;
    EditText edt_name;
    Button btn_update, btn_delete;

    IMyAPI mService;
    CompositeDisposable compositeDisposable;

    Uri selectedUri=null;
    String uploaded_img_path="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);

        btn_delete =(Button)findViewById(R.id.btn_Delete);
        btn_update =(Button)findViewById(R.id.btn_Update);
        edt_name=(EditText)findViewById(R.id.update_category_name);
        img_browser =(ImageView)findViewById(R.id.update_img_browser);

        mService = Common.getAPI();

        compositeDisposable = new CompositeDisposable();


        displayData();

        img_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(), "Select a file"),
                        PICK_FILE_REQUEST);
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCategory();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategory();
            }
        });


    }

    private void deleteCategory() {
        compositeDisposable.add(mService.deleteCategory(Common.currentCategory.getId()).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        Toast.makeText(UpdateCategoryActivity.this, s, Toast.LENGTH_SHORT).show();
                        uploaded_img_path="";
                        selectedUri=null;
                        Common.currentCategory =null;
                        finish();
                    }
                }));
    }

    private void updateCategory() {
        if (!edt_name.getText().toString().isEmpty()){
            compositeDisposable.add(mService.updateCategory(Common.currentCategory.getId(),
                    edt_name.getText().toString(),
                    uploaded_img_path).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {

                    Toast.makeText(UpdateCategoryActivity.this, s, Toast.LENGTH_SHORT).show();
                    uploaded_img_path="";
                    selectedUri=null;
                    Common.currentCategory = null;
                    finish();
                }
            }));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data != null) {
                    selectedUri = data.getData();
                    if (selectedUri != null && !selectedUri.getPath().isEmpty()) {
                        img_browser.setImageURI(selectedUri);
                        uploadFileToServer();
                    }
                    else
                        Toast.makeText(this, "cannot upload image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadFileToServer() {

        if (selectedUri != null) {
            File file = FileUtils.getFile(this, selectedUri);

            String fileName = new StringBuilder(UUID.randomUUID().toString())
                    .append(FileUtils.getExtension(file.toString())).toString();


            ProgressRequestBody requestFile = new ProgressRequestBody(file, this);

            final MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded file", fileName, requestFile);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    mService.uploadCategoryFile(body)
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {

                                    uploaded_img_path = new StringBuilder(Common.BASE_URL)
                                            .append("server/category/category_img/")
                                            .append(response.body())
                                            .toString();

                                    //.toString(); was not supported here so may be error

                                    Log.d("IMGPath", uploaded_img_path);

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(UpdateCategoryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }).start();

        }

    }

    private void displayData() {
        if (Common.currentCategory !=null){
            Picasso.with(this)
                    .load(Common.currentCategory.getLink())
                    .into(img_browser);

            edt_name.setText(Common.currentCategory.getName());
            uploaded_img_path = Common.currentCategory.getLink();


        }
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