package kelijun.com.qbox.module.setting;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.BindView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import kelijun.com.qbox.R;
import kelijun.com.qbox.base.BaseCommonActivity;

public class AccountActivity extends BaseCommonActivity implements Handler.Callback,PlatformActionListener,CompoundButton.OnCheckedChangeListener{
    private String[] sBindPhoneText;
    private Drawable[] sBindPhoneImg;
    private int[] sBindPhoneTextColor;

    public static final String QQ_NAME = "QQ";
    public static final String WECHAT_NAME = "Wechat";
    public static final String SINAWEIBO_NAME = "SinaWeibo";

    @BindView(R.id.toolbar_account)
    Toolbar mToolbarAccount;
    @BindView(R.id.phone_account)
    LinearLayout mPhoneAccount;
    @BindView(R.id.changepassword_account)
    LinearLayout mChangepasswordAccount;
    @BindView(R.id.weiboname_account)
    TextView mWeibonameAccount;
    @BindView(R.id.switch_weibo_account)
    Switch mSwitchWeiboAccount;
    @BindView(R.id.weibo_account)
    LinearLayout mWeiboAccount;
    @BindView(R.id.wechatname_account)
    TextView mWechatnameAccount;
    @BindView(R.id.switch_wechat_account)
    Switch mSwitchWechatAccount;
    @BindView(R.id.wechat_account)
    LinearLayout mWechatAccount;
    @BindView(R.id.qqname_account)
    TextView mQqnameAccount;
    @BindView(R.id.switch_qq_account)
    Switch mSwitchQqAccount;
    @BindView(R.id.qq_account)
    LinearLayout mQqAccount;
    @BindView(R.id.phonebindtext_account)
    TextView mPhonebindtextAccount;
    @BindView(R.id.phonebindimg_account)
    ImageView mPhonebindimgAccount;
    @BindView(R.id.phonebindinfo_account)
    TextView mPhonebindinfoAccount;
    @Override
    public void initContentView() {
        setContentView(R.layout.activity_account);
    }

    @Override
    public void initView() {
        sBindPhoneText = new String[]{getString(R.string.noverification),
                getString(R.string.verification)};
        sBindPhoneImg = new Drawable[]{getResources().getDrawable(R.drawable.certification_no),
                getResources().getDrawable(R.drawable.certification_yes)};
        sBindPhoneTextColor = new int[]{getResources().getColor(R.color.secondary_text),
                getResources().getColor(R.color.colorAccent)};
        initToolbar();
        initSwitch();
        initPhone();
        Toast.makeText(this, "账号系统未开通，本页面的所有信息只会保存在本地！", Toast.LENGTH_LONG).show();
    }

    private void initPhone() {

    }

    private void initSwitch() {

    }

    private void initToolbar() {
        setSupportActionBar(mToolbarAccount);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initPresenter() {

    }


    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
