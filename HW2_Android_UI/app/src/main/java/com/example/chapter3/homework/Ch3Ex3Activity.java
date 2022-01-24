package com.example.chapter3.homework;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用 ViewPager 和 Fragment 做一个简单版的好友列表界面
 * 1. 使用 ViewPager 和 Fragment 做个可滑动界面
 * 2. 使用 TabLayout 添加 Tab 支持
 * 3. 对于好友列表 Fragment，使用 Lottie 实现 Loading 效果，在 5s 后展示实际的列表，要求这里的动效是淡入淡出
 */
public class Ch3Ex3Activity extends AppCompatActivity {

    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;

    private TabLayout mTab;
    private String[] titles=new String[]{"Classmates","Boyfriends","Girlfriends"};
    private String[][] namelists = new String[][]{
            {"Yihang Li","Jack Ma","Jeff Dean","Steve Jobs"},
            {"Albedo","Kaedehara Kazuha","Venti"},
            {"Yoimiya","Sucrose","Eula","Ganyu"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ch3ex3);

        // TODO: ex3-1. 添加 ViewPager 和 Fragment 做可滑动界面
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);

        // TODO: ex3-2, 添加 TabLayout 支持 Tab
        mTab=(TabLayout)findViewById(R.id.tab_layout);
        mTab.setTabMode(TabLayout.MODE_FIXED);
        for (String tab:titles){
            mTab.addTab(mTab.newTab().setText(tab));
        }
        mTab.setupWithViewPager(mPager);

    }



    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            PlaceholderFragment myFragment= new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putStringArray("namelist",namelists[position]);
            Log.d("position",Integer.toString(position));
            Log.d("write",namelists[position][0]);
            myFragment.setArguments(args);
            return myFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
