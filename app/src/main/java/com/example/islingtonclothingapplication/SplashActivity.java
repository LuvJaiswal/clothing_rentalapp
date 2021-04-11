package com.example.islingtonclothingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ///////status bar is hidden///////////
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //////////End of status bar//////////


        ////Setting the handler splash timing delay///
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);

    }
}