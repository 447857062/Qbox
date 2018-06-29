package kelijun.com.qbox.module.me.weather.weather;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kelijun.com.qbox.R;
import kelijun.com.qbox.module.me.weather.android.util.TimingLogger;
import kelijun.com.qbox.module.me.weather.android.util.UiUtil;
import kelijun.com.qbox.module.me.weather.dynamicweather.DynamicWeatherView;
import kelijun.com.qbox.module.me.weather.mxxedgeeffect.widget.MxxFragmentPagerAdapter;
import kelijun.com.qbox.module.me.weather.mxxedgeeffect.widget.MxxViewPager;
import kelijun.com.qbox.module.me.weather.weather.api.ApiManager;

public class WeatherActivity extends FragmentActivity {
    public static Typeface typeface;
    private DynamicWeatherView weatherView;
    private MxxViewPager viewPager;

    public SimpleFragmentPagerAdapter mFragmentPagerAdapter;
    public List<BaseWeatherFragment> mFragmentList;
    public static Typeface getTypeface(Context context) {
        return typeface;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimingLogger logger = new TimingLogger("WeatherActivity.onCreate");
        if (typeface == null) {
            typeface = Typeface.createFromAsset(getAssets(), "fonts/mxx_font2.ttf");
        }
        logger.addSplit("Typeface.createFromAsset");
        setContentView(R.layout.activity_weather);
        logger.addSplit("setContentView");
        viewPager = (MxxViewPager) findViewById(R.id.main_viewpager);
        if (Build.VERSION.SDK_INT >= 19) {
            viewPager.setPadding(0, UiUtil.getStatusBarHeight(), 0, 0);
        }

        alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(260);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.BLACK));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
        viewPager.setAnimation(alphaAnimation);
        logger.addSplit("findViewPager");
        weatherView = (DynamicWeatherView) findViewById(R.id.main_dynamicweatherview);
        logger.addSplit("findWeatherView");
        loadAreaToViewPager();
        logger.addSplit("loadAreaToViewPager");
        logger.dumpToLog();
    }
    public void loadAreaToViewPager() {
        final ArrayList<ApiManager.Area> selectedAreas = ApiManager.loadSelectedArea(this);
        final BaseWeatherFragment[] fragments = new BaseWeatherFragment[selectedAreas.size()];
        for (int i = 0; i < selectedAreas.size(); i++) {
            final ApiManager.Area area = selectedAreas.get(i);
            fragments[i] = WeatherFragment.makeInstance(area, ApiManager.loadWeather(this, area.id));
        }
        mFragmentList = Arrays.asList(fragments);
        mFragmentPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);
        viewPager.setAdapter(mFragmentPagerAdapter);
        viewPager.setOnPageChangeListener(new MxxViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                weatherView.setDrawerType(((SimpleFragmentPagerAdapter) viewPager.getAdapter()).getItem(
                        position).getDrawerType());
            }
        });
    }
    AlphaAnimation alphaAnimation;

    @Override
    protected void onResume() {
        super.onResume();
        weatherView.onResume();
    }

    @Override
    protected void onPause() {
        weatherView.onPause();
        viewPager.clearAnimation();
        alphaAnimation.cancel();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        weatherView.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void updateCurDrawerType() {
        weatherView.setDrawerType(((SimpleFragmentPagerAdapter) viewPager.getAdapter()).getItem(viewPager.getCurrentItem()).getDrawerType());
    }

    public static class SimpleFragmentPagerAdapter extends MxxFragmentPagerAdapter {
        private List<BaseWeatherFragment>fragments;

        public SimpleFragmentPagerAdapter(FragmentManager fm,List<BaseWeatherFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public BaseWeatherFragment getItem(int position) {
            BaseWeatherFragment fragment = fragments.get(position);
            fragment.setRetainInstance(true);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((Fragment) object).getView());
            super.destroyItem(container, position, object);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return fragments==null?0:fragments.size();
        }
    }
}
