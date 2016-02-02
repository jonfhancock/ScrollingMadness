package com.jonfhancock.scrollingtestapp;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;


public class MainActivity extends ActionBarActivity implements ItemFragment.OnFragmentInteractionListener {

    private ImageView mHeroImage;


    private Toolbar mToolbar;
    private View mHeader;
    private SlidingTabLayout mIndicator;
    private ViewPager mPager;
    private ItemPagerAdapter mPagerAdapter;
    private FrameLayout mTouchInterceptor;
    private ViewGroup mContainer;
    private View mContent;
    private ArgbEvaluator mColorEvaluator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContent = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(null);
        mHeader = findViewById(R.id.header);
        mHeroImage = (ImageView) findViewById(R.id.hero_image);
        mIndicator = (SlidingTabLayout) findViewById(R.id.indicator);
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mTouchInterceptor = (FrameLayout) findViewById(R.id.touch_intercepter);
        mContainer = (ViewGroup) findViewById(R.id.container);
        mPagerAdapter = new ItemPagerAdapter(getSupportFragmentManager());

        mPager.setAdapter(mPagerAdapter);
        mIndicator.setViewPager(mPager);
        RequestManager mPicasso = Glide.with(this);
        mPicasso.load("http://lorempixel.com/1280/500/?1").placeholder(R.drawable.hero_placeholder).into(mHeroImage);

        final GestureDetectorCompat detector = new GestureDetectorCompat(this, new MyGestureListener());

        mColorEvaluator = new ArgbEvaluator();

        mTouchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return mContainer.dispatchTouchEvent(event) && detector.onTouchEvent(event) ;
            }
        });
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            startTy = (int) mIndicator.getTranslationY();
            startRootViewHeight = mContainer.getHeight();
            return true;
        }

        int startTy;
        int startRootViewHeight;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(DEBUG_TAG,"onFling velX = " + velocityX + " vely = " + velocityY);
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceX) > Math.abs(distanceY)) {
                return super.onScroll(e1,e2,distanceX,distanceY);
            }

            int scroll_diff = (int) (e1.getY() - e2.getY());
            int toolbarTranslationY = 0;
            int headerheight;
            int toolbarTranslucent = adjustAlpha(getResources().getColor(android.R.color.holo_blue_dark),0f);
            int toolbarColor = Color.parseColor("#55FFFFFF");
            int finalToolbarColor = getResources().getColor(android.R.color.holo_blue_dark);
            boolean movingUp = scroll_diff > 0;
            int targetTranslationY = (int) (startTy - scroll_diff);

            if (movingUp) {
                if (-targetTranslationY <= mHeader.getHeight()) {
                    headerheight = startRootViewHeight - targetTranslationY;
                    if (-targetTranslationY >= mHeader.getHeight() - mToolbar.getHeight()) {
                        toolbarTranslationY = mHeader.getHeight() + targetTranslationY - mToolbar.getHeight();
                        toolbarColor = finalToolbarColor;

                    }else{
                        float colorPercentage = 1.1f-(mHeader.getHeight()+ (float)targetTranslationY - mToolbar.getHeight() )/mIndicator.getHeight();
                        if(colorPercentage < 0){
                            toolbarColor = toolbarTranslucent;
                        }else if(colorPercentage <=1){
                           toolbarColor = (Integer) mColorEvaluator.evaluate(colorPercentage, toolbarTranslucent,finalToolbarColor);
                        }else{
                            toolbarColor = finalToolbarColor;
                        }
                    }


                } else {
                    targetTranslationY = -mHeader.getHeight();
                    toolbarTranslationY = targetTranslationY;
                    headerheight = mContent.getHeight() + mHeader.getHeight();
                    toolbarColor = finalToolbarColor;
                }

            } else {
                //moving down
                if (targetTranslationY <= 0) {
                    headerheight = startRootViewHeight - targetTranslationY;
                    if (mHeader.getHeight() + targetTranslationY <= mToolbar.getHeight()) {
                        toolbarTranslationY = mHeader.getHeight() + targetTranslationY - mToolbar.getHeight();
                        toolbarColor = finalToolbarColor;

                    }else{
                        float colorPercentage = 1.1f-(mHeader.getHeight()+ (float)targetTranslationY - mToolbar.getHeight() )/mIndicator.getHeight();
                        if(colorPercentage < 0){
                            toolbarColor = toolbarTranslucent;
                        }else if(colorPercentage <=1){
                            toolbarColor = (Integer) mColorEvaluator.evaluate(colorPercentage, toolbarTranslucent,finalToolbarColor);
                        }else{
                            toolbarColor = finalToolbarColor;
                        }
                    }
                } else {
                    targetTranslationY = 0;
                    toolbarColor = toolbarTranslucent;
                    headerheight = ViewGroup.LayoutParams.MATCH_PARENT;
                }
            }
            updateViews(targetTranslationY, toolbarTranslationY, headerheight,toolbarColor);

            return true;
        }
    }
    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int argb = Color.argb(alpha, red, green, blue);
        return argb;
    }
    private void updateViews(int targetTranslationY, int toolbarTranslationY, int headerheight,int toolbarColor) {
        mToolbar.setTranslationY(toolbarTranslationY);
        mToolbar.setBackgroundColor(toolbarColor);

        mIndicator.setTranslationY(targetTranslationY);
        mHeader.setTranslationY(targetTranslationY);
        mPager.setTranslationY(targetTranslationY);
        ViewGroup.LayoutParams rootParams = mContainer.getLayoutParams();
        rootParams.height = headerheight;
        mContainer.setLayoutParams(rootParams);
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
        Toast.makeText(this, "on clicked " + uri, Toast.LENGTH_SHORT).show();
    }

    public class ItemPagerAdapter extends CacheFragmentStatePagerAdapter {
        private int count = 10;

        public ItemPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment createItem(int position) {
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
    }
}
