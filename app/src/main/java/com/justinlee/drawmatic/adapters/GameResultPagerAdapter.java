package com.justinlee.drawmatic.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.justinlee.drawmatic.R;

import java.util.ArrayList;

public class GameResultPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> mResultStrings;
    private ArrayList<String> mAuthorStrings;

//    private ArrayList<View> mViewList;


    public GameResultPagerAdapter(Context context, ArrayList<String> resultStrings, ArrayList<String> authorStrings) {
        mContext = context;
        mResultStrings = resultStrings;
        mAuthorStrings = authorStrings;
//        mViewList = new ArrayList<>();
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
//        LinearLayout linearLayout = new LinearLayout(mContext);
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        View resultViewToShow;
//        View authorViewToShow;
//
//        if(position % 2 == 0) { // meaning that it is a text
//            resultViewToShow = new TextView(mContext);
//            authorViewToShow = new TextView(mContext);
//
//            ((TextView) resultViewToShow).setText(mResultStrings.get(position));
//            ((TextView) resultViewToShow).setTextSize(60f);
//            ((TextView) resultViewToShow).setGravity(Gravity.CENTER);
//
//            ((TextView) authorViewToShow).setText(mAuthorStrings.get(position));
//            ((TextView) authorViewToShow).setTextSize(12f);
//            ((TextView) authorViewToShow).setGravity(Gravity.BOTTOM|Gravity.CENTER);
//
//            linearLayout.addView((TextView) resultViewToShow);
//            linearLayout.addView((TextView) authorViewToShow);
//        } else { // meaning that it is a url
//            resultViewToShow = new ImageView(mContext);
//            authorViewToShow = new TextView(mContext);
//
//            Glide.with(mContext).load(mResultStrings.get(position)).into((ImageView) resultViewToShow);
////            ((ImageView) resultViewToShow).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//
//            ((TextView) authorViewToShow).setText("by " + mAuthorStrings.get(position));
//            ((TextView) authorViewToShow).setTextSize(12f);
//            ((TextView) authorViewToShow).setGravity(Gravity.BOTTOM|Gravity.CENTER);
//            ((TextView) authorViewToShow).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//
//            linearLayout.addView((ImageView) resultViewToShow);
//            linearLayout.addView((TextView) authorViewToShow);
//        }

//        mViewList.add(viewToShow);
//        container.addView(resultViewToShow);

//        container.removeAllViewsInLayout();
//        container.addView(linearLayout);




        // method 2
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_game_result_image, container, false);
        ImageView resultImage = view.findViewById(R.id.image_game_result);
        TextView resultText = view.findViewById(R.id.text_game_result);
        TextView authorText = view.findViewById(R.id.text_author_game_result);

        if(position % 2 == 0) {
            resultImage.setVisibility(View.GONE);

            resultText.setText(mResultStrings.get(position));
            authorText.setText("by " + mAuthorStrings.get(position));
        } else {
            resultText.setVisibility(View.GONE);

            Glide.with(mContext).load(mResultStrings.get(position)).into(resultImage);
            authorText.setText("by " + mAuthorStrings.get(position));
        }

//        container.removeAllViewsInLayout();
//        container.removeAllViews();
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//必须实现，销毁
//        container.removeView(mViewList.get(position));
    }
}
