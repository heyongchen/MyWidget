package com.widget.howard.mywidget.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.widget.howard.bobwidget.model.FilterData;
import com.widget.howard.bobwidget.widget.FilterView;
import com.widget.howard.mywidget.R;
import com.widget.howard.mywidget.model.ExpandList;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExpandFilterActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fv_filter_view)
    FilterView fvFilterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_filter);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        final TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams);
        fvFilterView.setDefaultMenu(ExpandList.getList(), textView);
        fvFilterView.setMenuClickListener(new FilterView.MenuClickListener() {
            @Override
            public void onItemClick(List<FilterData> key) {
                for (int i = 0; i < key.size(); i++) {
                    textView.setText(i == 0 ? key.get(i).value : (textView.getText().toString() + " : " + key.get(i).value));
                }
            }
        });
    }
}
