package com.justinlee.drawmatic.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import java.io.ByteArrayOutputStream;

public class DrawViewToImageGenerator {


    public DrawViewToImageGenerator() {
    }

    public Bitmap generateBitmapFrom(View drawView) {
        Bitmap bitmap = Bitmap.createBitmap(drawView.getMeasuredWidth(), drawView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(255, 255, 255, 255);
        drawView.layout(drawView.getLeft(), drawView.getTop(), drawView.getRight(), drawView.getBottom());
        drawView.draw(canvas);

        return bitmap;
    }

    public byte[] generateToData(View drawView) {
        Bitmap bitmap = Bitmap.createBitmap(drawView.getMeasuredWidth(), drawView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(255, 255, 255, 255);
        drawView.layout(drawView.getLeft(), drawView.getTop(), drawView.getRight(), drawView.getBottom());
        drawView.draw(canvas);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        return data;
    }
}
