package com.arieftb.pdfsign.ui;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.arieftb.pdfsign.R;
import com.arieftb.pdfsign.utils.ImgRealPath;
import com.arieftb.pdfsign.utils.RealPathUtil;
import com.arieftb.pdfsign.utils.SignView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = MainActivity.class.getSimpleName();
    private ImageButton btnSignAdd, btnSignSave, btnSignClear;
    private ImageView ivSignAdd;
    private SignView signView;
    private String pathSign;
    private Bitmap myBitmap;
    private Uri pathImage, pathPdf;
    private int GALLERY_REQUEST = 200;
    private int MANAGER_REQUEST = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Tanda Tangan");

        signView = findViewById(R.id.signv_main);
        ivSignAdd = findViewById(R.id.iv_sign_add);
        btnSignAdd = findViewById(R.id.btn_sign_add);
        btnSignSave = findViewById(R.id.btn_sign_save);
        btnSignClear = findViewById(R.id.btn_sign_clear);

        btnSignAdd.setOnClickListener(this);
        btnSignSave.setOnClickListener(this);
        btnSignClear.setOnClickListener(this);

        checkLocationPermission();
    }

    @Override
    public void onClick(View v) {
        if (v == btnSignSave) {
            saveSign();
        } else if (v == btnSignClear) {
            clearSign();
        } else if (v == btnSignAdd) {
            addSign();
        }
    }

    private void saveSign() {
        if (String.valueOf(pathImage).isEmpty()) {
            pathSign = signView.save(signView);
            checkSign(pathSign);
            getPdf();
        } else {
            getPdf();
        }
    }

    private void clearSign() {
        if (pathImage == null || String.valueOf(pathImage).isEmpty()) {
            signView.clear();
            ivSignAdd.setVisibility(View.GONE);
        } else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private void addSign() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Pilih Tanda Tangan Melalui"), GALLERY_REQUEST);
    }

    private void getPdf() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Pilih Dokumen Melalui"), MANAGER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pathImage = data.getData();
            setUriPhoto(pathImage);
        } else if (requestCode == MANAGER_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //            pathPdf = data.getData();
            pathPdf = Uri.parse(RealPathUtil.getRealPath(getApplicationContext(), data.getData()));
            previewPDF(pathPdf);
        } else {
            Toast.makeText(getApplicationContext(), "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
        }
    }

    private void previewPDF(Uri pathPdf) {
        createParentDir();
        Intent intent = new Intent(getApplicationContext(), PreviewPDFActivity.class);
        intent.putExtra("PATHPDF", String.valueOf(pathPdf));
        intent.putExtra("PATHSIGN", pathSign);
        startActivity(intent);
    }

    private void setUriPhoto(Uri pathImage) {
        Log.d(TAG, "setUriPhoto: " + pathImage);

        pathSign = String.valueOf(pathImage);

        ivSignAdd.setVisibility(View.VISIBLE);
        signView.setVisibility(View.GONE);

        Glide.with(this)
                .load(pathImage)
                .apply(new RequestOptions().fitCenter())
                .into(ivSignAdd);
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
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {
                checkLocationPermission();
            }
        }
    }

    private void createParentDir() {
        File outputDir = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));

        if (outputDir.mkdir()) {
            Log.d(TAG, "createParentDir: true");
        }
    }
}
