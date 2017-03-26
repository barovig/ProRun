package com.mad.k00191419.prorun.activities;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mad.k00191419.prorun.R;

public class DetailsActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // get references to private fields
        setupReferences();

        DetailsActivityPagerAdapter adapter = new DetailsActivityPagerAdapter();
        mViewPager.setAdapter(adapter);
    }

    private void setupReferences() {
        mViewPager = (ViewPager)findViewById(R.id.vpDetailsActivity);

    }

    public class DetailsActivityPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int layoutId = 0;
            switch(position){
                case 0:
                    layoutId = R.id.layoutSummary;
                    break;
                case 1:
                    layoutId = R.id.layoutGraphSpeed;
                    break;
                case 2:
                    layoutId = R.id.layoutGraphElevation;
                    break;
                case 3:
                    layoutId = R.id.layoutRouteMap;
                    break;
            }

            //View layout = findViewById(layoutId);

            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            ViewGroup layoutGroup = (ViewGroup)inflater.inflate(R.layout.activity_details_pages, container);
            View layout = layoutGroup.findViewById(layoutId);
            container.addView(layout);
            return layout;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
