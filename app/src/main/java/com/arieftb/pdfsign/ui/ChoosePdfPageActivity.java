package com.arieftb.pdfsign.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.arieftb.pdfsign.R;
import com.arieftb.pdfsign.adapter.PDFPageRecAdapter;

import java.util.List;

public class ChoosePdfPageActivity extends AppCompatActivity {

    private String TAG = ChoosePdfPageActivity.class.getSimpleName();
    private List<String> pagePDF;
    private String pathSign;
    private RecyclerView rvPagePdf;
    private PDFPageRecAdapter pdfPageRecAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pdf_page);

        pagePDF = getIntent().getStringArrayListExtra("PAGELIST");
        pathSign = getIntent().getStringExtra("PATHSIGN");

        rvPagePdf = findViewById(R.id.rv_page_pdf);

//        for (int i = 0; i < pagePDF.size(); i++) {
//            Log.d(TAG, "onCreate: pagePDF " + pagePDF.get(i));
//        }

        pdfPageRecAdapter = new PDFPageRecAdapter(pagePDF, this);

        rvPagePdf.setLayoutManager(new LinearLayoutManager(this));
        rvPagePdf.setAdapter(pdfPageRecAdapter);
    }
}
