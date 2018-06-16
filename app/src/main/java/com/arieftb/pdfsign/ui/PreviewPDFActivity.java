package com.arieftb.pdfsign.ui;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.arieftb.pdfsign.R;
import com.arieftb.pdfsign.utils.PdfToImg;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import org.vudroid.core.DecodeServiceBase;
import org.vudroid.pdfdroid.codec.PdfContext;
import org.vudroid.pdfdroid.codec.PdfPage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class PreviewPDFActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnErrorListener, View.OnClickListener, PdfToImg.AsyncResponse {

    private String TAG = PreviewPDFActivity.class.getSimpleName();
    private PDFView pdfView;
    private FloatingActionButton fabConvertPdf;
    private String pathPDF, pathSign;
    private File pdfFile;
    private Integer pageNumber = 0;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_pdf);

        pathPDF = getIntent().getStringExtra("PATHPDF");
        pathSign = getIntent().getStringExtra("PATHSIGN");


        pdfView = findViewById(R.id.pdf_view);
        fabConvertPdf = findViewById(R.id.fab_convert_pdf);

        previewPDF(pathPDF);
    }

    private void previewPDF(String pathPDF) {

//        progressDialog = ProgressDialog.show(PreviewPDFActivity.this, "", "Opening ...");

        pdfFile = new File(pathPDF);

        getSupportActionBar().setTitle(pdfFile.getName());

        pdfView.fromFile(pdfFile)
                .defaultPage(pageNumber)
                .enableSwipe(true)
                .onPageChange(this)
                .onError(this)
                .onLoad(this)
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }

    @Override
    public void loadComplete(int nbPages) {
//        progressDialog.dismiss();
        fabConvertPdf.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == fabConvertPdf) {
            convertPdfToImg(pdfFile);
        }
    }

    private void convertPdfToImg(File pdfFile) {
        progressDialog = ProgressDialog.show(PreviewPDFActivity.this, "", "Converting ...");
        new PdfToImg(this, this).execute(pdfFile);
    }

    @Override
    public void processFinish(List<String> output) {
        progressDialog.dismiss();

        if (output != null) {
            for (int i = 0; i < output.size(); i++) {
                Log.d(TAG, "processFinish: photo " + output.get(i));
            }
        }
    }

    @Override
    public void onError(Throwable t) {
        Toast.makeText(this, "Error Load PDF", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onError: Load PDF " + t.getMessage());
    }
}