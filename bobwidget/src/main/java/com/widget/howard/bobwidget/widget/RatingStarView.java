package com.widget.howard.bobwidget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.widget.howard.bobwidget.R;
import com.widget.howard.bobwidget.util.DensityUtil;

/**
 * Created by howard on 2016/8/17.
 */
public class RatingStarView extends LinearLayout {
    private int countNum;// 共有几个星星
    private int countSelected;
    private int stateResId;
    private float widthAndHeight;
    private float dividerWidth;
    private boolean canEdit;
    private boolean differentSize;

    public RatingStarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingStarView);
        countNum = typedArray.getInt(R.styleable.RatingStarView_starCount, 5);
        countSelected = typedArray.getInt(R.styleable.RatingStarView_countSelected, 0);
        canEdit = typedArray.getBoolean(R.styleable.RatingStarView_canEdit, false);
        differentSize = typedArray.getBoolean(R.styleable.RatingStarView_differentSize, false);
        widthAndHeight = typedArray.getDimension(R.styleable.RatingStarView_widthAndHeight, DensityUtil.dp2px(context, 0));
        dividerWidth = typedArray.getDimension(R.styleable.RatingStarView_dividerWidth, DensityUtil.dp2px(context, 0));
        stateResId = typedArray.getResourceId(R.styleable.RatingStarView_stateResId, -1);
        typedArray.recycle();
        initView();
    }

    public void setStateResId(int stateResId) {
        this.stateResId = stateResId;
        initView();
    }

    public int getCountNum() {
        return countNum;
    }

    public void setCountNum(int countNum) {
        this.countNum = countNum;
        initView();
    }

    public int getCountSelected() {
        return countSelected;
    }

    public void setCountSelected(int countSelected) {
        if (countSelected > countNum) {
            return;
        }
        this.countSelected = countSelected;
        initView();
    }


    private void initView() {
        removeAllViews();
        for (int i = 0; i < countNum; i++) {
            CheckBox cb = new CheckBox(getContext());
            LayoutParams layoutParams;
            if (widthAndHeight == 0) {
                layoutParams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            } else {
                layoutParams = new LayoutParams(
                        (int) widthAndHeight, (int) widthAndHeight);
            }
            if (differentSize && countNum % 2 != 0) {
                Log.e("xxx", layoutParams.width + "");
                int index = i;
                if (index > countNum / 2) {
                    index = countNum - 1 - index;
                }
                float scale = (index + 1) / (float) (countNum / 2 + 1);
                layoutParams.width = (int) (layoutParams.width * scale);
                layoutParams.height = layoutParams.width;
            }
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            if (i != 0 && i != countNum - 1) {
                layoutParams.leftMargin = (int) dividerWidth;
                layoutParams.rightMargin = (int) dividerWidth;
            } else if (i == 0) {
                layoutParams.rightMargin = (int) dividerWidth;
            } else if (i == countNum - 1) {
                layoutParams.leftMargin = (int) dividerWidth;
            }
            cb.setLayoutParams(layoutParams);
            addView(cb);
            if (stateResId == -1) {
                stateResId = R.drawable.selector_rating_star;
            }
            cb.setBackgroundResource(android.R.color.transparent);
            cb.setButtonDrawable(stateResId);
            if (i + 1 <= countSelected) {
                cb.setChecked(true);
            }
            cb.setEnabled(canEdit);
            cb.setOnClickListener(new MyClickListener(i));
        }
    }

    private class MyClickListener implements OnClickListener {
        int position;

        public MyClickListener(int position) {
            super();
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            countSelected = position + 1;

            for (int i = 0; i < countNum; i++) {
                CheckBox cb = (CheckBox) getChildAt(i);

                if (i <= position) {
                    cb.setChecked(true);
                } else if (i > position) {
                    cb.setChecked(false);
                }
            }
            if (mOnRatingChangeListener != null) {
                mOnRatingChangeListener.onChange(countSelected);
            }
        }

    }

    private OnRatingChangeListener mOnRatingChangeListener;

    public OnRatingChangeListener getOnRatingChangeListener() {
        return mOnRatingChangeListener;
    }

    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        mOnRatingChangeListener = onRatingChangeListener;
    }

    public interface OnRatingChangeListener {
        /**
         * @param countSelected 星星选中的个数
         */
        void onChange(int countSelected);
    }

    private int dp2Px(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }
}
