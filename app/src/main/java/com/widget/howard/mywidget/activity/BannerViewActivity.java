package com.widget.howard.mywidget.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.widget.howard.bobwidget.widget.BannerView;
import com.widget.howard.mywidget.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BannerViewActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.bv_banner_view)
    BannerView bvBannerView;

    private ArrayList<String> imgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_view);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        imgList.clear();
        imgList.add("http://pic33.nipic.com/20130916/3420027_192919547000_2.jpg");
        imgList.add("http://4493bz.1985t.com/uploads/allimg/150127/4-15012G52133.jpg");
        imgList.add("http://img3.imgtn.bdimg.com/it/u=4271053251,2424464488&fm=21&gp=0.jpg");
        bvBannerView.setImageUrl(imgList, 2);

    }

}
