package com.example.islingtonclothingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    TextView txt_register;
    EditText et_email, et_password;
    Button btn_login;

    IMyAPI mService;  //to access the api


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init service
        mService = Common.getAPI();


        //Init views
        txt_register = (TextView) findViewById(R.id.txt_register);
        et_email = (EditText) findViewById(R.id.login_email);
        et_password = (EditText) findViewById(R.id.login_password);

        btn_login = (Button) findViewById(R.id.LoginBtn);


        //Event

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));

            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser(et_email.getText().toString(), et_password.getText().toString());
            }
        });
    }

    private void authenticateUser(String email, String password) {
        mService.loginUser(email, password)
                .enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        APIResponse result = response.body();
                        if (result.isError()) {
                            Toast.makeText(MainActivity.this, result.getError_msg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }


                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {

                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
