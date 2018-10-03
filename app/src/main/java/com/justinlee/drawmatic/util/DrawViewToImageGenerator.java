package com.justinlee.drawmatic.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import java.io.ByteArrayOutputStream;

public class DrawViewToImageGenerator {

    Bitmap mGeneratedBitmap;

    public DrawViewToImageGenerator() {
    }

    public Bitmap generateFrom(View drawView) {
        Bitmap bitmap = Bitmap.createBitmap( drawView.getLayoutParams().width, drawView.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawView.layout(drawView.getLeft(), drawView.getTop(), drawView.getRight(), drawView.getBottom());
        drawView.draw(canvas);

        mGeneratedBitmap = bitmap;

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
