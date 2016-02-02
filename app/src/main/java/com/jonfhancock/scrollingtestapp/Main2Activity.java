package com.jonfhancock.scrollingtestapp;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

public class Main2Activity extends AppCompatActivity implements ItemFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ArgbEvaluator mColorEvaluator;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ImageView mHeroImage;
    private Toolbar mToolbar;

    public int adjustAlpha(int color, float factor) {
            int alpha = Math.round(Color.alpha(color) * factor);
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
        int argb = Color.argb(alpha, red, green, blue);
        return argb;
        }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mColorEvaluator = new ArgbEvaluator();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setBackgroundColor(adjustAlpha(getResources().getColor(android.R.color.holo_blue_dark),0));

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mHeroImage = (ImageView) findViewById(R.id.hero_image);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
//        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
//                float bgColorPercent = 0;
//                if(-i >= mHeroImage.getHeight()-getSupportActionBar().getHeight()){
//                    bgColorPercent = 1;
//                }else if(-i <= getSupportActionBar().getHeight()){
//                    bgColorPercent = 0;
//                }else{
//                    int total = mHeroImage.getHeight()-getSupportActionBar().getHeight()*2;
//                    Log.d("Scrolling","Total = " + total);
//                    bgColorPercent =  (((float)(-i - getSupportActionBar().getHeight()))/total);
//                }
////                if(-i > mHeroImage.getHeight()-toolbar.getHeight()){
////                    bgColorPercent = getSupportActionBar().getHeight() / -i;
////                }
//                mToolbar.setBackgroundColor(adjustAlpha(getResources().getColor(android.R.color.holo_blue_dark),bgColorPercent));
//                Log.d("Scrolling","100% target i = " + (mHeroImage.getHeight()-getSupportActionBar().getHeight()));
//                Log.d("Scrolling","i = " + i + " imageHeight = "+ mHeroImage.getHeight() + " toolbarheight = " + getSupportActionBar().getHeight() + " bgColorPercent = " + bgColorPercent);
//            }
//        });

        setTitle(null);
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);
        SlidingTabLayout tabLayout = (SlidingTabLayout) findViewById(R.id.indicator);
        tabLayout.setViewPager(mViewPager);

        RequestManager mPicasso = Glide.with(this);
        mPicasso.load("http://lorempixel.com/1280/500/?1").placeholder(R.drawable.hero_placeholder).into(mHeroImage);

        tabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d(TAG,"onPageScrolled position =" + position + " positionOffset = " + positionOffset + " positionOffsetPixels = " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
//                Log.d(TAG, "onPageSelected position = " + position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                mPagerIsSwiping = state == 1;
//                Log.d(TAG,"onPageScrollStateChanged state = " + state);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String uri) {

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main2, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends CacheFragmentStatePagerAdapter {
        private int count = 10;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected ItemFragment createItem(int position) {
            return ItemFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Item " + position;
        }

        @Override
        public ItemFragment getItemAt(int position) {
            return (ItemFragment) super.getItemAt(position);
        }
    }
}
