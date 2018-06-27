package kelijun.com.qbox.module.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import kelijun.com.qbox.R;
import kelijun.com.qbox.base.BaseCustomActivity;
import kelijun.com.qbox.model.entities.RefreshNewsFragmentEvent;
import kelijun.com.qbox.module.find.FindFragment;
import kelijun.com.qbox.module.me.MeFragment;
import kelijun.com.qbox.module.news.NewsFragment;
import kelijun.com.qbox.module.news_category.CategoryActivity;
import kelijun.com.qbox.module.wechat.WechatFragment;
import kelijun.com.qbox.utils.StateBarTranslucentUtils;
import kelijun.com.qbox.widget.TabBar_Mains;

public class MainsActivity extends BaseCustomActivity {

    private static final String NEWS_FRAGMENT = "NEWS_FRAGMENT";
    private static final String WECHAT_FRAGMENT = "WECHAT_FRAGMENT";
    private static final String FIND_FRAGMENT = "FIND_FRAGMENT";
    public static final String ME_FRAGMENT = "ME_FRAGMENT";
    @BindView(R.id.framelayout_mains)
    FrameLayout sFramelayoutMains;
    @BindView(R.id.recommend_mains)
    TabBar_Mains sRecommendMains;
    @BindView(R.id.cityfinder_mains)
    TabBar_Mains sCityfinderMains;
    @BindView(R.id.findtravel_mains)
    TabBar_Mains sFindtravelMains;
    @BindView(R.id.me_mains)
    TabBar_Mains sMeMains;
    private FragmentManager sBaseFragmentManager;
    public MeFragment mMeFragment;
    public NewsFragment mNewsFragment;
    public WechatFragment mWechatFragment;
  public FindFragment mFindFragment;
    private String mCurrentIndex;
    boolean isRestart = false;

    @Override
    public void initContentView() {
        StateBarTranslucentUtils.setStateBarTranslucent(this);
        Logger.i("initContentView");
        setContentView(R.layout.activity_mains);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRefreshNewsFragmentEvent(RefreshNewsFragmentEvent event) {
        startActivityForResult(new Intent(MainsActivity.this, CategoryActivity.class), event.getMark_code());
    }
    @OnClick({R.id.recommend_mains, R.id.cityfinder_mains, R.id.findtravel_mains, R.id.me_mains})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recommend_mains:
                if (!mCurrentIndex.equals(NEWS_FRAGMENT))
                    switchToFragment(NEWS_FRAGMENT);
                break;
            case R.id.cityfinder_mains:
                if (!mCurrentIndex.equals(WECHAT_FRAGMENT))
                    switchToFragment(WECHAT_FRAGMENT);
                break;
            case R.id.findtravel_mains:
                if (!mCurrentIndex.equals(FIND_FRAGMENT))
                    switchToFragment(FIND_FRAGMENT);
                break;
            case R.id.me_mains:
                if (!mCurrentIndex.equals(ME_FRAGMENT))
                    switchToFragment(ME_FRAGMENT);
                break;
        }
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        sBaseFragmentManager = getFragmentmanager();
        String startPage = NEWS_FRAGMENT;
        if (savedInstanceState != null) {
            initByRestart(savedInstanceState);
        } else {
            switchToFragment(startPage);
            mCurrentIndex = startPage;
        }

        //订阅事件
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {

        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initByRestart(Bundle savedInstanceState) {

        mCurrentIndex = savedInstanceState.getString("mCurrentIndex");

        isRestart = true;
        Logger.e("恢复状态：" + mCurrentIndex);
        mMeFragment = (MeFragment) sBaseFragmentManager.findFragmentByTag(ME_FRAGMENT);
        mNewsFragment = (NewsFragment) sBaseFragmentManager.findFragmentByTag(NEWS_FRAGMENT);
      mWechatFragment = (WechatFragment) sBaseFragmentManager.findFragmentByTag(WECHAT_FRAGMENT);
      mFindFragment = (FindFragment) sBaseFragmentManager.findFragmentByTag(FIND_FRAGMENT);

        switchToFragment(mCurrentIndex);
    }

    private void switchToFragment(String index) {
         hideAllFragment();
        switch (index) {
            case NEWS_FRAGMENT:
                if (sRecommendMains.getVisibility() == View.VISIBLE) {
                    showNewsFragment();
                    Logger.e("newsopen:101");
                }
                break;
            case WECHAT_FRAGMENT:
                showWechatFragment();
                break;
            case FIND_FRAGMENT:
               showFindFragment();
                break;
            case ME_FRAGMENT:
                showMeFragment();
                break;
            default:

                break;
        }
        mCurrentIndex = index;
    }
    private void showFindFragment() {
        if (false == sFindtravelMains.isSelected()) {
            sFindtravelMains.setSelected(true);
        }
        if (mFindFragment == null) {
            mFindFragment = FindFragment.newInstance("", "");
            addFragment(R.id.framelayout_mains, mFindFragment, FIND_FRAGMENT);
        } else if (isRestart = true) {
            isRestart = false;
            getFragmentTransaction().show(mFindFragment).commit();
        } else {
            showFragment(mFindFragment);
        }

    }

    private void showWechatFragment() {
        if (false == sCityfinderMains.isSelected()) {
            sCityfinderMains.setSelected(true);
        }
        if (mWechatFragment == null) {
            mWechatFragment = mWechatFragment.newInstance("", "");
            addFragment(R.id.framelayout_mains, mWechatFragment, WECHAT_FRAGMENT);
        } else if (isRestart = true) {
            isRestart = false;
            getFragmentTransaction().show(mWechatFragment).commit();
        } else {
            showFragment(mWechatFragment);
        }

    }
    private void showNewsFragment() {
        if (false == sRecommendMains.isSelected()) {
            sRecommendMains.setSelected(true);
        }
        if (mNewsFragment == null) {
            Logger.e("恢复状态：" + "null");
            mNewsFragment = NewsFragment.newInstance("a", "b");
            addFragment(R.id.framelayout_mains, mNewsFragment, NEWS_FRAGMENT);
        } else if (isRestart = true) {
            isRestart = false;
            getFragmentTransaction().show(mNewsFragment).commit();
        } else {
            showFragment(mNewsFragment);
        }

    }

    private void showMeFragment() {
        if (false == sMeMains.isSelected())
            sMeMains.setSelected(true);
        if (mMeFragment == null) {
            mMeFragment = MeFragment.newInstance();
            addFragment(R.id.framelayout_mains, mMeFragment, ME_FRAGMENT);
        } else if (isRestart = true) {
            getFragmentTransaction().show(mMeFragment).commit();
            isRestart = false;
        } else {
            showFragment(mMeFragment);
        }
    }

    @Override
    public void initPresenter() {

    }

    private void hideAllFragment() {
        if (mNewsFragment != null) {
            hideFragment(mNewsFragment);
        }
       if (mWechatFragment != null) {
            hideFragment(mWechatFragment);
        }
        if (mFindFragment != null) {
            hideFragment(mFindFragment);
        }
        if (mMeFragment != null) {
            hideFragment(mMeFragment);
        }
        if (sRecommendMains.getVisibility() == View.VISIBLE) {
            sRecommendMains.setSelected(false);
        }
        sFindtravelMains.setSelected(false);
        sCityfinderMains.setSelected(false);
        sMeMains.setSelected(false);
    }
}
