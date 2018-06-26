package kelijun.com.qbox.module.news;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by ${kelijun} on 2018/6/26.
 */

public class NewsPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    public NewsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public NewsPagerAdapter(FragmentManager fm, List<Fragment> mFragmentList) {
        super(fm);
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        int count=mFragmentList==null?0:mFragmentList.size();
        Logger.i("count="+count);
        return count;
    }
}
