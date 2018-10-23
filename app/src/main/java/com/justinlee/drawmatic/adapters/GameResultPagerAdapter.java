package com.justinlee.drawmatic.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.justinlee.drawmatic.R;

import java.util.ArrayList;

public class GameResultPagerAdapter extends PagerAdapter {
    private Context mContext;

    private boolean mIsInOfflineMode;

    // Online Mode Data
    private ArrayList<String> mResultStrings;
    private ArrayList<String> mAuthorStrings;

    // Offline Mode Data
    private ArrayList<Object> mResultObjects;

//    private ArrayList<View> mViewList;


    /**
     * Constructor for Online Mode
     */
    public GameResultPagerAdapter(Context context, ArrayList<String> resultStrings, ArrayList<String> authorStrings) {
        mContext = context;
        mIsInOfflineMode = false;

        mResultStrings = resultStrings;
        mAuthorStrings = authorStrings;
//        mViewList = new ArrayList<>();
    }

    /**
     * Constructor for Offline Mode
     */
    public GameResultPagerAdapter(Context context, ArrayList<Object> resultObjects) {
        mContext = context;
        mIsInOfflineMode = true;

        mResultObjects = resultObjects;
    }

    @Override
    public int getCount() { //必须实现
        if(mIsInOfflineMode) {
            return mResultObjects.size();
        } else {
            return mResultStrings.size() + 1;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {//必须实现
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {//必须实现，实例化
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_game_result_image, container, false);
        ImageView resultImage = view.findViewById(R.id.image_game_result);
        TextView resultText = view.findViewById(R.id.text_game_result);
        TextView authorText = view.findViewById(R.id.text_author_game_result);

        if(mIsInOfflineMode) {
            authorText.setVisibility(View.GONE);

            if(position % 2 == 0) {
                resultImage.setVisibility(View.GONE);
                resultText.setText((String) mResultObjects.get(position));

            } else {
                resultText.setVisibility(View.GONE);
                Glide.with(mContext).load((Bitmap) mResultObjects.get(position)).into(resultImage);
            }

        } else {

            // Online Mode
            if(position == 2) {
                // for ad
                view = LayoutInflater.from(mContext).inflate(R.layout.item_banner_ad, container, false);
                insertAdToView(view);

            } else if (position < 2){
                // for results before ad
                if(position % 2 == 0) {
                    resultImage.setVisibility(View.GONE);
                    resultText.setText(mResultStrings.get(position));
                    authorText.setText("by " + mAuthorStrings.get(position));

                } else {
                    resultText.setVisibility(View.GONE);
                    Glide.with(mContext).load(mResultStrings.get(position)).into(resultImage);
                    authorText.setText("by " + mAuthorStrings.get(position));
                }
            } else {
                // for results after ad
                if(position % 2 == 0) {
                    resultText.setVisibility(View.GONE);
                    Glide.with(mContext).load(mResultStrings.get(position - 1)).into(resultImage);
                    authorText.setText("by " + mAuthorStrings.get(position - 1));

                } else {
                    resultImage.setVisibility(View.GONE);
                    resultText.setText(mResultStrings.get(position - 1));
                    authorText.setText("by " + mAuthorStrings.get(position - 1));
                }
            }


        }

//        container.removeAllViewsInLayout();
//        container.removeAllViews();
        container.addView(view);
        return view;
    }

    private void insertAdToView(View view) {
        AdView adView = view.findViewById(R.id.bannerAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//必须实现，销毁
//        container.removeView(mViewList.get(position));
    }
}
