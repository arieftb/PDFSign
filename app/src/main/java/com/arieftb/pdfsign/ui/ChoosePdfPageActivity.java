package com.arieftb.pdfsign.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.arieftb.pdfsign.R;

import java.util.List;

public class ChoosePdfPageActivity extends AppCompatActivity {

    private String TAG = ChoosePdfPageActivity.class.getSimpleName();
    private List<String> pagePDF;
    private String pathSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pdf_page);

        pagePDF = getIntent().getStringArrayListExtra("PAGELIST");
        pathSign = getIntent().getStringExtra("PATHSIGN");

        for (int i = 0; i < pagePDF.size(); i++) {
            Log.d(TAG, "onCreate: pagePDF " + pagePDF.get(i));
        }
    }
}
