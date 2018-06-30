package kelijun.com.qbox.module.setting;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import kelijun.com.qbox.R;
import kelijun.com.qbox.base.BaseCommonActivity;

public class AboutMeActivity extends BaseCommonActivity {
    private List<Fragment> mFragmentList;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    @Override
    public void initContentView() {
        setContentView(R.layout.activity_about_me);
    }

    @Override
    public void initView() {
        initToolbar();
        initViewPager();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void initPresenter() {

    }
    private void initViewPager() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(AboutQboxFragment.newInstance());
        mFragmentList.add(AboutStoryFragment.newInstance());
        mFragmentList.add(AboutMeFragment.newInstance());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(mFragmentList.size()-1);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList==null?0:mFragmentList.size();
        }
    }

}
