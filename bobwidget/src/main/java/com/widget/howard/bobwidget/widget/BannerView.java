package com.widget.howard.bobwidget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.widget.howard.bobwidget.R;
import com.widget.howard.bobwidget.util.DensityUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by howard on 2016/7/27.
 */
public class BannerView extends RelativeLayout {

    private ImageView placeHolder;
    private ViewPager mBanner;
    private BannerViewAdapter mBannerAdapter;
    private Timer mTimer = new Timer();

    private int mBannerPosition = 0;
    private int FAKE_BANNER_SIZE;
    private int DEFAULT_BANNER_SIZE;
    private boolean mIsUserTouched = false;
    private ArrayList<String> mBannerList;
    private RadioGroup rg_dots;
    private Context mContext;
    private BannerClickListener mBannerClickListener;
    private ScalingUtils.ScaleType scaleType;//默认状态
    private Handler mHandler = new Handler();

    private int placeholderImage = -1;
    private int intScaleType = 0;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BannerView);
        placeholderImage = typedArray.getResourceId(R.styleable.BannerView_placeholderImage, -1);
        intScaleType = typedArray.getInteger(R.styleable.BannerView_scaleType, 0);
        typedArray.recycle();
        //在构造函数中将Xml中定义的布局解析出来。
        LayoutInflater.from(context).inflate(R.layout.view_banner, this, true);
    }

    private TimerTask mTimerTask;

    private void initView() {
        placeHolder = (ImageView) findViewById(R.id.iv_place_holder);
        rg_dots = (RadioGroup) findViewById(R.id.rg_dots);
        mBanner = (ViewPager) findViewById(R.id.banner);
        rg_dots.removeAllViews();
        placeHolder.setImageResource(placeholderImage == -1 ? R.color.transparency : placeholderImage);
        switch (intScaleType) {
            case 0:
                scaleType = ScalingUtils.ScaleType.CENTER_CROP;//默认状态
                break;
            case 1:
                scaleType = ScalingUtils.ScaleType.FIT_START;
                break;
            case 2:
                scaleType = ScalingUtils.ScaleType.FIT_CENTER;
                break;
            case 3:
                scaleType = ScalingUtils.ScaleType.FIT_END;
                break;
            case 4:
                scaleType = ScalingUtils.ScaleType.CENTER;
                break;
            case 5:
                scaleType = ScalingUtils.ScaleType.CENTER_INSIDE;
                break;
            case 6:
                scaleType = ScalingUtils.ScaleType.FIT_XY;
                break;
            case 7:
                scaleType = ScalingUtils.ScaleType.FOCUS_CROP;
                break;
        }
        DEFAULT_BANNER_SIZE = mBannerList.size();
        FAKE_BANNER_SIZE = DEFAULT_BANNER_SIZE * 2 + 1;
        for (int i = 0; i < DEFAULT_BANNER_SIZE; i++) {
            RadioButton dot = new RadioButton(mContext);
            dot.setButtonDrawable(R.drawable.selector_dot);
            dot.setPadding(8, 0, 8, 0);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(DensityUtil.dp2px(mContext, 8), DensityUtil.dp2px(mContext, 8));
            params.setMargins(DensityUtil.dp2px(mContext, 6), 0, DensityUtil.dp2px(mContext, 6), 0);
            dot.setLayoutParams(params);
            rg_dots.addView(dot);
        }
        mBannerAdapter = new BannerViewAdapter();
        mBanner.setAdapter(mBannerAdapter);
        mBanner.setOnPageChangeListener(mBannerAdapter);
        mBanner.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
                    mIsUserTouched = true;
                } else if (action == MotionEvent.ACTION_UP) {
                    mIsUserTouched = false;
                }
                return false;
            }
        });
    }

    private class BannerViewAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        @Override
        public int getCount() {
            return FAKE_BANNER_SIZE;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position %= DEFAULT_BANNER_SIZE;
            SimpleDraweeView imageView = new SimpleDraweeView(mContext);
            imageView.setBackgroundResource(R.color.white);
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
            GenericDraweeHierarchy draweeHierarchy = builder
                    .setActualImageScaleType(scaleType)
                    .setFailureImage(ContextCompat.getDrawable(getContext(), placeholderImage == -1 ? R.color.transparency : placeholderImage))
                    .setPlaceholderImage(ContextCompat.getDrawable(getContext(), placeholderImage == -1 ? R.color.transparency : placeholderImage))
                    .build();
            imageView.setHierarchy(draweeHierarchy);
            imageView.setImageURI(Uri.parse(mBannerList.get(position)));
            final int pos = position;
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBannerClickListener != null)
                        mBannerClickListener.onItemClick(pos);
                }
            });
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            int position = mBanner.getCurrentItem();
            if (position == 0) {
                position = DEFAULT_BANNER_SIZE;
                mBanner.setCurrentItem(position, false);
            } else if (position == FAKE_BANNER_SIZE - 1) {
                position = DEFAULT_BANNER_SIZE;
                mBanner.setCurrentItem(position, false);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mBannerPosition = position;
            setIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private void setIndicator(int position) {
        position %= DEFAULT_BANNER_SIZE;
        ((RadioButton) rg_dots.getChildAt(position)).setChecked(true);
    }

    public void setImageUrl(ArrayList<String> urlList, int time) {
        if (urlList.size() != 0) {
            mBannerList = new ArrayList<>();
            mBannerList.clear();
            mBannerList = urlList;
            initView();
            placeHolder.setVisibility(GONE);
            if (time > 0) {
                if (mTimer != null) {
                    mTimer.cancel();
                    if (mTimerTask != null) {
                        mTimerTask.cancel();  //将原任务从队列中移除
                    }
                }
                mTimer = new Timer();
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!mIsUserTouched) {
                                    mBannerPosition = (mBannerPosition + 1) % FAKE_BANNER_SIZE;

                                    if (mBannerPosition == FAKE_BANNER_SIZE - 1) {
                                        mBanner.setCurrentItem(DEFAULT_BANNER_SIZE - 1, false);
                                        mBannerPosition = DEFAULT_BANNER_SIZE;
                                        mBanner.setCurrentItem(mBannerPosition);
                                    } else {
                                        mBanner.setCurrentItem(mBannerPosition);
                                    }
                                }
                            }
                        });
                    }
                };
                mTimer.schedule(mTimerTask, time * 1000, time * 1000);
            }
            setCurrentItem(0);
        } else {
            placeHolder.setVisibility(VISIBLE);
        }
    }

    public void setImageScaleType(ScalingUtils.ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    public interface BannerClickListener {
        void onItemClick(int position);
    }

    public void setBannerClickListener(BannerClickListener bannerClickListener) {
        this.mBannerClickListener = bannerClickListener;
    }

    public void setCurrentItem(int mCurrentItem) {
        mBanner.setCurrentItem(mCurrentItem);
        mBannerPosition = mBanner.getCurrentItem();
    }
}
