package com.juqueen.flatshare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class flatActivity extends FragmentActivity {

    flatData flatData;

    int NUMBER_SCREENS =3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new myPageAdapter(getSupportFragmentManager()));
        viewPager.setPageTransformer(true, new zoomOutPageTransformer());
        flatData = getIntent().getParcelableExtra("selectedflat");

    }

    class myPageAdapter extends FragmentPagerAdapter {

        public myPageAdapter(FragmentManager fm) {
            super(fm);
        }


        public Fragment getItem(int position) {
            switch(position) {

                case 0: return flatHome.newInstance(flatData);
                case 1: return flatFriends.newInstance("This is the friends acivity");
                case 2: return flatTimeline.newInstance("This is the timeline activity");
                default: return flatHome.newInstance(flatData);
            }
        }


        public int getCount() {
            return NUMBER_SCREENS;
        }
    }

}
