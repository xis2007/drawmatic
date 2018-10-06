package com.justinlee.drawmatic.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GameResultPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> mResultStrings;

    private ArrayList<View> mViewList;

    public GameResultPagerAdapter(Context context, ArrayList<String> resultStrings) {
        mContext = context;
        mResultStrings = resultStrings;
        mViewList = new ArrayList<>();
    }

    @Override
    public int getCount() {//必须实现
        return mResultStrings.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {//必须实现
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {//必须实现，实例化
        View viewToShow;
        if(position % 2 == 0) { // meaning that it is a text
            viewToShow = new TextView(mContext);
            ((TextView) viewToShow).setText(mResultStrings.get(position));
            ((TextView) viewToShow).setTextSize(60f);
            ((TextView) viewToShow).setGravity(Gravity.CENTER);

        } else { // meaning that it is a url
            viewToShow = new ImageView(mContext);
            Glide.with(mContext).load(mResultStrings.get(position)).into((ImageView) viewToShow);

        }

//        mViewList.add(viewToShow);
        container.addView(viewToShow);

        return viewToShow;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//必须实现，销毁
//        container.removeView(mViewList.get(position));
    }
}
