package kelijun.com.qbox.module.find.joke;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kelijun.com.qbox.R;
import kelijun.com.qbox.base.BaseCommonActivity;

public class JokeActivity extends BaseCommonActivity {
    public static final int JOKESTYLE_NEW = 1;
    public static final int JOKESTYLE_RANDOM = 2;
    public int mJokestyle;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private List<String> mTitleStrings;
    private List<Fragment> mFragmentList;
    @Override
    public void initContentView() {
        setContentView(R.layout.activity_joke);
    }

    @Override
    public void initView() {
        mJokestyle = JOKESTYLE_RANDOM;

        initToolbar();
        initViewPager();
    }

    private void initViewPager() {
        mTitleStrings = Arrays.asList("故事","趣图");
        mFragmentList = new ArrayList<>();
        for (int i = 0; i < mTitleStrings.size(); i++) {
            switch (mTitleStrings.get(i)) {
                case "故事":
                    mFragmentList.add(i, TextJokeFragment.newInstance("",""));
                    break;
                case "趣图":
                    mFragmentList.add(i, ImgJokeFragment.newInstance("",""));
                    break;
                default:
                    break;
            }
        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this, mFragmentList, mTitleStrings);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    public int getJokestyle() {
        return mJokestyle;
    }
    @Override
    public void initPresenter() {

    }

}
