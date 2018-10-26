package com.justinlee.drawmatic.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.justinlee.drawmatic.MainActivity;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.in_game_result.GameResultPresenter;

import java.util.ArrayList;

public class ResultAsBitmapsUtil {
    private static final String TAG = "drawingggggg";

    private Context mContext;
    private GameResultPresenter mGameResultPresenter;
    private ArrayList<Bitmap> mResults;

    public ResultAsBitmapsUtil(Context context) {
        mContext = context;
    }

    public ResultAsBitmapsUtil(Context context, GameResultPresenter gameResultPresenter) {
        mContext = context;
        mGameResultPresenter = gameResultPresenter;
        mResults = new ArrayList<>();
    }

    public ArrayList<Bitmap> generateFrom(ArrayList<String> resultStrings) {
        for (int i = 0; i < resultStrings.size(); i++) {
            mResults.clear();
            mResults.add(null);
        }

        for (int i = 0; i < resultStrings.size(); i++) {
            if (i % 2 == 0) { // even numbered items in the list means it is guessing strings
                textToResId(i, resultStrings.get(i), 36, R.color.colorGrey);
            } else {         // else, odd numbered items in the list means it is image url
                urlToBitmap(i, resultStrings);
            }
        }
        return mResults;
    }

    private Bitmap textToResId(int index, String text, float textSize, int textColor) {
        if (text == null) {
            text = "";
        }
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
//        int width = (int) (paint.measureText(text) + 0.5f); // round
//        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);

        mResults.set(index, image);

        return image;
    }

    public Bitmap textToResId(String text, float textSize, int textColor) {
//        if (text == null) {
//            text = "";
//        }
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setTextSize(textSize);
//        paint.setColor(textColor);
//        paint.setTextAlign(Paint.Align.CENTER);
//        float baseline = -paint.ascent(); // ascent() is negative
////        int width = (int) (paint.measureText(text) + 0.5f); // round
////        int height = (int) (baseline + paint.descent() + 0.5f);
//        Bitmap image = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(image);
//        canvas.drawText(text, 0, baseline, paint);
//
//        return image;


        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(text, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);

            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            Log.d(TAG, "textToResId: bitmap is: " + null);
            return null;
        }

//        Log.d(TAG, "textToResId: text is: " + text);
//
//
//        String toPrint = "Yes~";
//        int resId = mContext.getResources().getIdentifier(toPrint, "drawable", mContext.getPackageName());
//        Log.d(TAG, "textToResId: resource id: " + resId);
//
//        //        imgView.setImageResource(resID);
//        return mContext.getResources().getDrawable(resId);
    }

    private void urlToBitmap(int index, ArrayList<String> resultStrings) {
        final int finalIndex = index;
        Glide
                .with((MainActivity) mContext)
                .asBitmap()
                .load(resultStrings.get(index))
                .into(new Target<Bitmap>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) { }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) { }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) { }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) { }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) { }

                    @Override
                    public void setRequest(@Nullable Request request) { }

                    @Nullable
                    @Override
                    public Request getRequest() {
                        return null;
                    }

                    @Override
                    public void onStart() { }

                    @Override
                    public void onStop() { }

                    @Override
                    public void onDestroy() { }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mResults.set(finalIndex, resource);
                    }
                });
    }

    public ArrayList getResults() {
        return mResults;
    }
}
