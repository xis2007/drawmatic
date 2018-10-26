package com.justinlee.drawmatic.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public GridItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        boolean isLeftColumn = position % 2 == 0;

        outRect.top = space * 2;

        if (isLeftColumn) {
            outRect.left = space * 2;
            outRect.right = space;
        } else {
            outRect.left = space;
            outRect.right = space * 2;
        }
    }
}
