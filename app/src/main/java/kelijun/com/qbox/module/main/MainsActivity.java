package kelijun.com.qbox.module.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import kelijun.com.qbox.R;
import kelijun.com.qbox.app.BaseApplication;
import kelijun.com.qbox.base.BaseCustomActivity;
import kelijun.com.qbox.config.Const;
import kelijun.com.qbox.model.entities.RefreshNewsFragmentEvent;
import kelijun.com.qbox.module.find.FindFragment;
import kelijun.com.qbox.module.me.MeFragment;
import kelijun.com.qbox.module.news.NewsFragment;
import kelijun.com.qbox.module.news_category.CategoryActivity;
import kelijun.com.qbox.module.wechat.WechatFragment;
import kelijun.com.qbox.update.AppUtils;
import kelijun.com.qbox.update.UpdateChecker;
import kelijun.com.qbox.utils.SPUtils;
import kelijun.com.qbox.utils.StateBarTranslucentUtils;
import kelijun.com.qbox.utils.inputmethodmanager_leak.IMMLeaks;
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

    public static List<String> logList = new CopyOnWriteArrayList<String>();

    @Override
    protected void onResume() {
        super.onResume();
        refreshLogInfo();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mCurrentIndex", mCurrentIndex);
        Logger.e("保存状态");
    }
    /**
     * 监听用户按返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    private boolean mIsExit;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                this.finish();
            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mIsExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 用于优雅的退出程序(当从其他地方退出应用时会先返回到此页面再执行退出)
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra(Const.TAG_EXIT, false);
            if (isExit) {
                finish();
            }
        }
    }
    public void refreshLogInfo() {
        String AllLog = "";
        for (String log : logList) {
            AllLog = AllLog + log + "\n\n";
        }
        Logger.e(AllLog);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.NEWSFRAGMENT_CATEGORYACTIVITY_REQUESTCODE
                && resultCode == Const.NEWSFRAGMENT_CATEGORYACTIVITY_RESULTCODE) {
            mNewsFragment.initView();
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
        int qbox_version = (int) SPUtils.get(this, Const.QBOX_NEW_VERSION, 0);
        if (qbox_version != 0 && qbox_version > AppUtils.getVersionCode(this)) {
            UpdateChecker.checkForNotification(this); //通知提示升级
        }
        //订阅事件
        EventBus.getDefault().register(this);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onDestroy() {

        EventBus.getDefault().unregister(this);
        for (Fragment fragment :
                getBaseFragmentManager().getFragments()) {
            getFragmentTransaction().remove(fragment);
        }
        super.onDestroy();
        BaseApplication.setMainActivity(null);
        IMMLeaks.fixFocusedViewLeak(getApplication());//解决 Android 输入法造成的内存泄漏问题。
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
        sRecommendMains.setSelected(false);
        sFindtravelMains.setSelected(false);
        sCityfinderMains.setSelected(false);
        sMeMains.setSelected(false);
    }
}
