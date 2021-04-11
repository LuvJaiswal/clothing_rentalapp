package com.example.islingtonclothingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.islingtonclothingapplication.Common.Common;
import com.example.islingtonclothingapplication.Remote.IMyAPI;
import com.example.islingtonclothingapplication.model.APIResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText et_name, et_email, et_password, et_phone;
    Button btn_register;

    IMyAPI mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //Init service
        mService = Common.getAPI();


        //Init views
        et_name = (EditText) findViewById(R.id.register_name);
        et_email = (EditText) findViewById(R.id.register_email);
        et_password = (EditText) findViewById(R.id.register_password);
        et_phone = (EditText) findViewById(R.id.register_phone);

        btn_register = (Button) findViewById(R.id.RegisterBtn);

        //EVENT

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_name.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "enter your name",
                            Toast.LENGTH_SHORT).show();}

                    if (TextUtils.isEmpty(et_email.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "enter your email",
                                Toast.LENGTH_SHORT).show();}

                        if (TextUtils.isEmpty(et_password.getText().toString())) {
                            Toast.makeText(RegisterActivity.this, "enter your password",
                                    Toast.LENGTH_SHORT).show();}

                            if (TextUtils.isEmpty(et_phone.getText().toString())) {
                                Toast.makeText(RegisterActivity.this, "enter your phone number",
                                        Toast.LENGTH_SHORT).show();}
                            else {
                                createNewUser(et_name.getText().toString(), et_email.getText().toString(), et_password.getText().toString(), et_phone.getText().toString());
                            }
                                }
                            });


                        }

                        private void createNewUser (String name, String email, String
                        password, String phone){
                            mService.registerUser(name, email, password, phone)
                                    .enqueue(new Callback<APIResponse>() {
                                        @Override
                                        public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                                            APIResponse result = response.body();
                                            if (result.isError()) {
                                                Toast.makeText(RegisterActivity.this, result.getError_msg(), Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "User Created Successfully: " + result.getUid(), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<APIResponse> call, Throwable t) {
                                            Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    }
