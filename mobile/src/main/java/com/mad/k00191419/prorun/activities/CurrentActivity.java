package com.mad.k00191419.prorun.activities;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.mad.k00191419.prorun.R;

public class CurrentActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);

        // get references to private fields
        setupReferences();

        CurrentActivityPagerAdapter adapter = new CurrentActivityPagerAdapter();
        mViewPager.setAdapter(adapter);
    }

    private void setupReferences() {
        mViewPager = (ViewPager)findViewById(R.id.vpCurrentActivity);

    }

    public class CurrentActivityPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int layoutId = 0;
            switch(position){
                case 0:
                    layoutId = R.id.layoutCurrentStats;
                    break;
                case 1:
                    layoutId = R.id.layoutCurrentMap;
                    break;
            }

            return findViewById(layoutId);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
