package com.widget.howard.mywidget.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.widget.howard.mywidget.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_banner_view)
    Button btnBannerView;
    @Bind(R.id.btn_expand_filter)
    Button btnExpandFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setClickListener();

    }

    private void setClickListener() {
        btnBannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BannerViewActivity.class));
            }
        });
        btnExpandFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ExpandFilterActivity.class));
            }
        });
    }

}
