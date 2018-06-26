package kelijun.com.qbox.module.me;

import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import kelijun.com.qbox.R;
import kelijun.com.qbox.base.BaseFragment;

/**
 * Created by ${kelijun} on 2018/6/26.
 */

public class MeFragment extends BaseFragment {
    @BindView(R.id.homebg_me)
    ImageView mHomebgMe;
    @BindView(R.id.motto_me)
    TextView mMottoMe;
    @BindView(R.id.userhead_me)
    CircleImageView mUserheadMe;
    @BindView(R.id.username_me)
    TextView mUsernameMe;
    @BindView(R.id.rili_me)
    LinearLayout mRiliMe;
    @BindView(R.id.tianqi_me)
    LinearLayout mTianqiMe;
    @BindView(R.id.led_me)
    LinearLayout mLedMe;
    @BindView(R.id.sdt_me)
    LinearLayout mSdtMe;
    @BindView(R.id.erweima_me)
    LinearLayout mErweimaMe;
    @BindView(R.id.setting_me)
    LinearLayout mSettingMe;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    public MeFragment() {
    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_me_new;
    }

    @Override
    public void initView() {

    }

    @Override
    protected void managerArguments() {

    }
}
