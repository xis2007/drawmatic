package com.justinlee.drawmatic.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.justinlee.drawmatic.R;
import com.justinlee.drawmatic.objects.InnerInstructionsData;
import com.ramotion.garlandview.TailAdapter;
import com.ramotion.garlandview.header.HeaderItem;
import com.ramotion.garlandview.inner.InnerLayoutManager;
import com.ramotion.garlandview.inner.InnerRecyclerView;

import java.util.ArrayList;

public class InstructionsOuterAdapter extends TailAdapter<InstructionsOuterAdapter.InstructionsOuterItem> {
    private final ArrayList<ArrayList<InnerInstructionsData>> mInstructionsList;

    public InstructionsOuterAdapter(ArrayList<ArrayList<InnerInstructionsData>> instructionsList) {
        mInstructionsList = instructionsList;

    }

    @NonNull
    @Override
    public InstructionsOuterItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new InstructionsOuterItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionsOuterItem holder, int position) {
        holder.setContent(mInstructionsList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mInstructionsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_outer_instructions;
    }



    /** *****************************************************************************************
     * InstructionsOuterItem
     * ******************************************************************************************
     */
    public class InstructionsOuterItem extends HeaderItem {
        private View mHeader;
        private View mHeaderAlpha;
        private TextView mHeaderTitle;
        private ImageView mHeaderImage;

        private InnerRecyclerView mInnerRecyclerView;
        private InstructionsInnerAdapter mInstructionsInnerAdapter;
        private InnerLayoutManager mInnerLayoutManager;
        private boolean mIsScrolling;

        public InstructionsOuterItem(View itemView) {
            super(itemView);

            initHeader(itemView);
            initInnerRecyclerView(itemView);
        }

        private void initHeader(View itemView) {
            mHeader = itemView.findViewById(R.id.layout_header_instructions);
            mHeaderAlpha = itemView.findViewById(R.id.header_instructions_alpha);
            mHeaderTitle = itemView.findViewById(R.id.header_instructions);
            mHeaderImage = itemView.findViewById(R.id.image_header_instructions);
        }

        private void initInnerRecyclerView(View itemView) {
            mInnerRecyclerView = itemView.findViewById(R.id.recyclerView_inner);
            mInstructionsInnerAdapter = new InstructionsInnerAdapter();
            mInnerRecyclerView.setAdapter(mInstructionsInnerAdapter);

            mInnerLayoutManager = new InnerLayoutManager();
            mInnerLayoutManager.generateDefaultLayoutParams();
            mInnerRecyclerView.setLayoutManager(new InnerLayoutManager());

            mInnerRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    mIsScrolling = newState != RecyclerView.SCROLL_STATE_IDLE;
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                onItemScrolled(recyclerView, dx, dy);
                }
            });

//        mInnerRecyclerView.addItemDecoration(new HeaderDecorator(5, 20));
        }


        @Override
        public boolean isScrolling() {
            return mIsScrolling;
        }

        @Override
        public InnerRecyclerView getViewGroup() {
            return mInnerRecyclerView;
        }

        @Override
        public View getHeader() {
            return mHeader;
        }

        @Override
        public View getHeaderAlphaView() {
            return mHeaderAlpha;
        }

        public void setContent(@NonNull ArrayList<InnerInstructionsData> innerDataList, int position) {
            mInstructionsInnerAdapter.addData(innerDataList);

            mHeaderTitle.setText(innerDataList.get(0).getInstructionTitle());
            mHeaderImage.setBackground(mHeaderImage.getResources().getDrawable(Integer.valueOf(innerDataList.get(0).getImageSource())));

            if (position == 0) {
                mHeaderTitle.setTextColor(mHeaderTitle.getResources().getColor(R.color.colorGrey));
            }
        }
    }
}
