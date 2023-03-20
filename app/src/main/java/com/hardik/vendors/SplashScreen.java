package com.hardik.vendors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        try {
            Thread.sleep(3000);
            Intent i = new Intent(SplashScreen.this,Map.class);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}