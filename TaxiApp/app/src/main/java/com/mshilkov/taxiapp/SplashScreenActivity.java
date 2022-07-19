package com.mshilkov.taxiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sceen);
        Thread thread=new Thread(){
            @Override
            public void run()
            {
                try {
                    sleep(5000);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                finally {
                    startActivity(new Intent(SplashScreenActivity.this, ChooseModeActivity.class));

                }
            }


        };
        thread.start();


    }
}