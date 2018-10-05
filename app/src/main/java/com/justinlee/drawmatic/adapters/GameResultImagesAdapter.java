package com.justinlee.drawmatic.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.in_game_result.GameResultPresenter;

import java.util.ArrayList;

public class GameResultImagesAdapter extends RecyclerView.Adapter {
    private static final String TAG = "justinxxxxx";

    private Context mContext;
    private GameResultPresenter mGameResultPresenter;
    private ArrayList<String> mResultStrings;

    public GameResultImagesAdapter(Context context, GameResultPresenter gameResultPresenter) {
        mContext = context;
        mGameResultPresenter = gameResultPresenter;
        mResultStrings = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_result_image, parent, false);
        return new ResultImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position % 2 == 0) {
            ((ResultImageViewHolder) holder).getTextResult().setText(mResultStrings.get(position));
            ((ResultImageViewHolder) holder).getImageResult().setVisibility(View.INVISIBLE);

        } else {
            Glide.with(mContext).load(mResultStrings.get(position)).into(((ResultImageViewHolder) holder).getImageResult());
            ((ResultImageViewHolder) holder).getTextResult().setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return mResultStrings.isEmpty() ? 0 : mResultStrings.size();
    }

    public void swapList(ArrayList<String> resultStrings) {
        if (mResultStrings != resultStrings) {
            Log.d(TAG, "swapList: result strings is: " + resultStrings.toString());
            mResultStrings = resultStrings;
            notifyDataSetChanged();
        }
    }

    public class ResultImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageResult;
        private TextView mTextResult;


        public ResultImageViewHolder(View itemView) {
            super(itemView);

            mImageResult = itemView.findViewById(R.id.image_game_result);
            mTextResult = itemView.findViewById(R.id.text_game_result);
        }

        public ImageView getImageResult() {
            return mImageResult;
        }

        public TextView getTextResult() {
            return mTextResult;
        }
    }
}
