package com.arieftb.pdfsign.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.arieftb.pdfsign.R;
import com.arieftb.pdfsign.utils.SignView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = MainActivity.class.getSimpleName();
    private Button btnSignSave, btnSignClear;
    private SignView signView;
    private String pathSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signView = findViewById(R.id.signv_main);
        btnSignSave = findViewById(R.id.btn_sign_save);
        btnSignClear = findViewById(R.id.btn_sign_clear);

        btnSignSave.setOnClickListener(this);
        btnSignClear.setOnClickListener(this);

        checkLocationPermission();
    }

    @Override
    public void onClick(View v) {
        if (v == btnSignSave) {
            pathSign = signView.save(signView);
            checkSign(pathSign);
        } else if (v == btnSignClear) {
            signView.clear();
        }
    }

    private void checkSign(String pathSign) {
        if (pathSign != null) {
            Toast.makeText(this, "Tanda Tangan Tersimpan", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
            }, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }
    }
}
