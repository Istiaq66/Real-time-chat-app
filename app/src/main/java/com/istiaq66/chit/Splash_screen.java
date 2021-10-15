package com.istiaq66.chit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class Splash_screen extends AppCompatActivity {

    ImageView Logo;
    LottieAnimationView lottieAnimationView;
    TextView txt;
    private  static int SPLASH_TIME_OUT = 5300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Logo = findViewById(R.id.img);
        txt = findViewById(R.id.textView);
        lottieAnimationView = findViewById(R.id.lottie);
        getSupportActionBar().hide();




        lottieAnimationView.animate().translationY(-2000).setDuration(1000).setStartDelay(4000);
        txt.animate().translationY(-2000).setDuration(1000).setStartDelay(4000);
        Logo.animate().translationY(-2600).setDuration(1000).setStartDelay(4000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_screen.this,login.class);
                startActivity(intent);
            }
        },SPLASH_TIME_OUT);
    }
}