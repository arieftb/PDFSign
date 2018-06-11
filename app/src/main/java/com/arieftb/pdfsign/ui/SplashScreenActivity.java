package com.arieftb.pdfsign.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.arieftb.pdfsign.BuildConfig;
import com.arieftb.pdfsign.R;

public class SplashScreenActivity extends AppCompatActivity {

    private TextView tvAppversion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        tvAppversion = findViewById(R.id.tv_app_version);

        tvAppversion.setText(String.format(getString(R.string.app_version), BuildConfig.VERSION_NAME));

        new Handler().postDelayed(new Thread(){
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }, 1500);
    }
}
