package com.widget.howard.bobwidget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.widget.howard.bobwidget.R;
import com.widget.howard.bobwidget.util.DensityUtil;

/**
 * Created by howard on 2016/8/3.
 */
public class RatingBarView extends LinearLayout {
    private int countNum;// 共有几个星星
    private int stateResId;
    private float widthAndHeight;
    private float dividerWidth;

    public RatingBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBarView);
        countNum = typedArray.getInt(R.styleable.RatingBarView_levelCount, 5);
        widthAndHeight = typedArray.getDimension(R.styleable.RatingBarView_levelWidthAndHeight, DensityUtil.dp2px(context, 0));
        dividerWidth = typedArray.getDimension(R.styleable.RatingBarView_levelDividerWidth, DensityUtil.dp2px(context, 0));
        stateResId = typedArray.getResourceId(R.styleable.RatingBarView_levelStateResId, -1);
        typedArray.recycle();
        initView();
    }

    public void setStateResId(int stateResId) {
        this.stateResId = stateResId;
        initView();
    }

    public void setCountNum(int countNum) {
        this.countNum = countNum;
        initView();
    }

    private void initView() {
        removeAllViews();
        for (int i = 0; i < countNum; i++) {
            ImageView view = new ImageView(getContext());
            LayoutParams layoutParams;
            if (widthAndHeight == 0) {
                layoutParams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            } else {
                layoutParams = new LayoutParams(
                        (int) widthAndHeight, (int) widthAndHeight);
            }
            if (i != 0) {
                layoutParams.leftMargin = (int) dividerWidth;
            }
            view.setLayoutParams(layoutParams);
            addView(view);
            if (stateResId == -1) {
                stateResId = R.drawable.ic_star_full;
            }
            view.setImageDrawable(ContextCompat.getDrawable(getContext(), stateResId));
        }
    }
}
