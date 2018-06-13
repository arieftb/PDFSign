package com.arieftb.pdfsign.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.arieftb.pdfsign.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by arieftebe on 18/01/18.
 */

public class SignView extends View {
    public SignView(Context context) {
        super(context);
        init();
    }

    public SignView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SignView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private int pathIndex = 0;
    private ArrayList<Path> pathLists = new ArrayList<>();
    private ArrayList<Paint> paintLists = new ArrayList<>();
    private int drawerColor = Color.BLACK;

    private void init() {
        pathLists.add(new Path());
        paintLists.add(createPaint());
        pathIndex++;
    }

    private Paint createPaint() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5F);
        paint.setColor(drawerColor);
        return paint;
    }

    private Path createPath(MotionEvent event) {
        Path path = new Path();
        path.moveTo(event.getX(), event.getY());
        return path;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                updateIndex(createPath(event));
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                Path path = pathLists.get(pathIndex - 1);
                path.lineTo(x, y);
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    private void updateIndex(Path path) {
        if (pathIndex == pathLists.size()) {
            pathLists.add(path);
            paintLists.add(createPaint());
            pathIndex++;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int index = 0; index < pathIndex; index++) {
            Path path = pathLists.get(index);
            Paint paint = paintLists.get(index);
            canvas.drawPath(path, paint);
        }
    }

    public void setDrawerColor(int drawerColor) {
        this.drawerColor = drawerColor;
    }

    public String save(View v) {
        Bitmap mBitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_4444);
//        Bitmap mBitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(mBitmap);

        String pathImage = null;
        FileOutputStream mFileOutStream = null;

        try {
            pathImage = String.valueOf(getDir());
            mFileOutStream = new FileOutputStream(pathImage);
            v.draw(canvas);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 70, mFileOutStream);

            mFileOutStream.flush();
            mFileOutStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pathImage;
    }

    public File getDir() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getContext().getString(R.string.app_name) + "/TTD");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + getContext().getString(R.string.app_name) + "_" + timeStamp + ".png");
    }

    public void clear() {
        pathLists.clear();
        paintLists.clear();
        pathIndex = 0;
        invalidate();
    }

}
