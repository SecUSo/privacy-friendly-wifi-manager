package org.secuso.privacyfriendlywifi.view.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import secuso.org.privacyfriendlywifi.R;

/**
 *
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable divider;

    public DividerItemDecoration(Context context) {
        this.divider = ContextCompat.getDrawable(context, R.drawable.simple_rectangle);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + this.divider.getIntrinsicHeight();

            this.divider.setBounds(child.getLeft(), top, child.getRight(), bottom);
            this.divider.draw(c);
        }
    }
}