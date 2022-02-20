package com.niceplaces.niceplaces.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.adapters.ViewPagerAdapter;

public class IntroActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TextView[] mDots;
    private LinearLayout mLayoutDots;
    private Button mButtonNext, mButtonSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final IntroActivity thisActivity = this;
        setContentView(R.layout.activity_intro);
        getSupportActionBar().hide();
        final int[] layouts = new int[]{
                R.layout.activity_intro1,
                R.layout.activity_intro2,
                R.layout.activity_intro3,
                R.layout.activity_intro4,
                R.layout.activity_intro5,
                R.layout.activity_intro6,
                R.layout.activity_intro7,
        };
        mLayoutDots = findViewById(R.id.layout_dots);
        mViewPager = findViewById(R.id.view_pager);
        mButtonNext = findViewById(R.id.button_next);
        mButtonSkip = findViewById(R.id.button_skip);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, layouts);
        mViewPager.setAdapter(adapter);
        addBottomDots(0, layouts.length);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position, layouts.length);
                if (position == layouts.length - 1){
                    mButtonNext.setText("Vai!");
                    mButtonSkip.setVisibility(View.GONE);
                } else {
                    mButtonNext.setText("Avanti");
                    mButtonSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mButtonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(thisActivity, MenuActivity.class);
                startActivity(i);
                finish();
            }
        });
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int next = mViewPager.getCurrentItem() + 1;
                if (next < layouts.length){
                    mViewPager.setCurrentItem(next);
                } else {
                    Intent i = new Intent(thisActivity, MenuActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    public void addBottomDots(int position, int number){
        mDots = new TextView[number];
        mLayoutDots.removeAllViews();
        for (int i = 0; i < mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            if (i == position){
                mDots[i].setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                mDots[i].setTextColor(getResources().getColor(R.color.colorPrimaryAlpha));
            }
            mLayoutDots.addView(mDots[i]);
        }
    }

}
