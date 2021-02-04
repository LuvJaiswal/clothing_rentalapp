package com.example.islingtonclothingapplication;

import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterActivity extends AppCompatActivity {

    EditText et_name,et_email,et_password;
    Button btn_register;

    IMyAPI mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //Init service
        mService = Common.getAPI();


        //Init views
        et_name = (EditText)findViewById(R.id.register_name);
        et_email = (EditText)findViewById(R.id.register_email);
        et_password=(EditText)findViewById(R.id.register_password);

        btn_register = (Button) findViewById(R.id.RegisterBtn);

        //EVENT

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewUser(et_name.getText().toString(),et_email.getText().toString(),et_password.getText().toString());
            }
        });

    }

    private void createNewUser(String name, String email, String password) {
        mService.registerUser(name,email,password)
                .enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        APIResponse result = response.body();
                        if (result.isError()){
                            Toast.makeText(RegisterActivity.this,result.getError_msg(), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "User Created Successfully: " +result.getUid(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
