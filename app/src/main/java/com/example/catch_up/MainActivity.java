package com.example.catch_up;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request no title feature before setting the content view
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Set the activity to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the status bar color to transparent
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_main);

        // Ensure no action bar is shown
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Delay execution for 3 seconds and then start SignUpActivity or HomeActivity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
//                For testing purposes
//                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                }
                finish();
            }
        }, 3000);
    }
}
