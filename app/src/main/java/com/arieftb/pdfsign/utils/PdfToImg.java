package com.arieftb.pdfsign.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.arieftb.pdfsign.R;

import org.vudroid.core.DecodeServiceBase;
import org.vudroid.pdfdroid.codec.PdfContext;
import org.vudroid.pdfdroid.codec.PdfPage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfToImg extends AsyncTask<File, Void, List<String>> {

    private String TAG = PdfToImg.class.getSimpleName();
    private Context context;
    private List<String> fileImgPDF;

    public PdfToImg(Context context, AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    public interface AsyncResponse {
        void processFinish(List<String> output);
    }

    public AsyncResponse delegate = null;

    @Override
    protected List<String> doInBackground(File... files) {
//        return null;

        fileImgPDF = new ArrayList<>();

        try {
            // select a document and get bytes
            DecodeServiceBase decodeService = new DecodeServiceBase(new PdfContext());
            decodeService.setContentResolver(context.getContentResolver());

            File pdfile = files[0];
            String pdfFileName = pdfile.getName().substring(0, pdfile.getName().lastIndexOf("."));
// a bit long running
            decodeService.open(Uri.fromFile(pdfile));


            int pageCount = decodeService.getPageCount();


            File outputDir = new File(Environment.getExternalStorageDirectory()  + "/" + context.getString(R.string.app_name) + "/"+ pdfFileName);


            if (!outputDir.exists()) {
                if (outputDir.mkdir()) {
                    Log.d(TAG, "doInBackground: MAKEDIR : " + "True");

                    for (int i = 0; i < pageCount; i++) {
                        PdfPage page = (PdfPage) decodeService.getPage(i);
                        RectF rectF = new RectF(0, 0, 1, 1);

                        Bitmap bitmap = page.renderBitmap(page.getWidth(), page.getHeight(), rectF);

                        String pageNum = String.valueOf(i + 1);

                        try {

                            File outputFile = new File(outputDir,   pdfFileName + "page("+ pageNum + ")" + ".jpg");
                            FileOutputStream outputStream = new FileOutputStream(outputFile);

                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                            outputStream.close();

                            fileImgPDF.add(outputFile.getAbsolutePath());
                        } catch (IOException e) {
                            Log.d(TAG, "doInBackground: Img Fail " + e.getMessage());
                            return  null;
                        }
                    }
                } else {
                    return null;
                }
            } else {
                if (deleteDir(outputDir)) {
                    if (outputDir.mkdir()) {
                        Log.d(TAG, "doInBackground: MAKEDIR : " + "True");

                        for (int i = 0; i < pageCount; i++) {
                            PdfPage page = (PdfPage) decodeService.getPage(i);
                            RectF rectF = new RectF(0, 0, 1, 1);

                            Bitmap bitmap = page.renderBitmap(page.getWidth(), page.getHeight(), rectF);

                            String pageNum = String.valueOf(i + 1);

                            try {

                                File outputFile = new File(outputDir,   pdfFileName + "page("+ pageNum + ")" + ".jpg");
                                FileOutputStream outputStream = new FileOutputStream(outputFile);

                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                                outputStream.close();

                                fileImgPDF.add(outputFile.getAbsolutePath());
                            } catch (IOException e) {
                                Log.d(TAG, "doInBackground: Img Fail " + e.getMessage());
                                return  null;
                            }
                        }
                    } else {
                        return null;
                    }
                } else {
                    Log.d(TAG, "doInBackground: DELETEDIR FALSE");
                    return null;
                }
            }
        } catch (Exception e) {
            Log.d("CounterA", e.toString());
        }

        if (fileImgPDF.size() > 0) {
            return fileImgPDF;
        } else {
            return null;
        }
    }

    private boolean deleteDir(File outputDir) {
        if (outputDir.isDirectory()) {
            String[] children = outputDir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(outputDir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return outputDir.delete();
    }


    @Override
    protected void onPostExecute(List<String> strings) {
        super.onPostExecute(strings);
        if (strings != null) {
            delegate.processFinish(strings);
        } else {
            delegate.processFinish(strings);
        }
    }
}
