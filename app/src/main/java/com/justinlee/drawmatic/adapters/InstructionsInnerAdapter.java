package com.justinlee.drawmatic.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.objects.InnerInstructionsData;
import com.ramotion.garlandview.inner.InnerAdapter;
import com.ramotion.garlandview.inner.InnerItem;

import java.util.ArrayList;

public class InstructionsInnerAdapter extends InnerAdapter<InstructionsInnerAdapter.InstructionsInnerItem> {
    private ArrayList<InnerInstructionsData> mInnerInstructionsDataList = new ArrayList<>();

    public InstructionsInnerAdapter() {

    }

    @NonNull
    @Override
    public InstructionsInnerItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new InstructionsInnerItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionsInnerItem holder, int position) {
        holder.setContent(mInnerInstructionsDataList.get(position + 1));
    }

    @Override
    public int getItemCount() {
        return mInnerInstructionsDataList.size() - 1;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_inner_instructions;
    }

    public void addData(@NonNull ArrayList<InnerInstructionsData> innerInstructionsList) {
        mInnerInstructionsDataList = innerInstructionsList;
        notifyDataSetChanged();
    }

    public void clearData() {
        mInnerInstructionsDataList.clear();
        notifyDataSetChanged();
    }

    /** *****************************************************************************************
     * InstructionsInnerItem
     * ******************************************************************************************
     */
    public class InstructionsInnerItem extends InnerItem {
        private final View mInnerLayout;

        private final TextView mTextTitle;
        private final TextView mTextContent;
        private final CardView mCardView;
        private final ImageView mImageView;

        public InstructionsInnerItem(View itemView) {
            super(itemView);

            mInnerLayout = itemView.findViewById(R.id.layout_inner_item);
            mTextTitle = itemView.findViewById(R.id.text_title_inner);
            mTextContent = itemView.findViewById(R.id.text_content_inner);
            mCardView = itemView.findViewById(R.id.cardView_inner);
            mImageView = itemView.findViewById(R.id.image_inner);
        }

        @Override
        protected View getInnerLayout() {
            return mInnerLayout;
        }

        public void setContent(InnerInstructionsData data) {
            mTextTitle.setText(data.getInstructionTitle());
            mTextContent.setText(data.getInstructionContent());

            if(data.getImageSource().equals("") || data.getImageSource().isEmpty()) {
                mCardView.setVisibility(View.GONE);
            } else {
                mCardView.setVisibility(View.VISIBLE);
                Glide.with(mImageView.getContext())
                        .load(data.getImageSource())
                        .into(mImageView);
            }
        }
    }
}
