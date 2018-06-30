package kelijun.com.qbox.module.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
import kelijun.com.qbox.R;
import kelijun.com.qbox.base.BaseCommonActivity;
import kelijun.com.qbox.config.Const;
import kelijun.com.qbox.module.main.MainsActivity;
import kelijun.com.qbox.update.AppUtils;
import kelijun.com.qbox.update.UpdateChecker;
import kelijun.com.qbox.utils.DataCleanManager;
import kelijun.com.qbox.utils.FileInfoUtils;
import kelijun.com.qbox.utils.SPUtils;

public class SettingActivity extends BaseCommonActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.switch_sllms_setting)
    Switch switchSllmsSetting;
    @BindView(R.id.sllms_setting)
    LinearLayout sllmsSetting;
    @BindView(R.id.text_qchc_setting)
    TextView textQchcSetting;
    @BindView(R.id.qchc_setting)
    LinearLayout qchcSetting;
    @BindView(R.id.qlcz_setting)
    LinearLayout qlczSetting;
    @BindView(R.id.weatheraddress_setting)
    LinearLayout weatheraddressSetting;
    @BindView(R.id.switch_xxts_setting)
    Switch switchXxtsSetting;
    @BindView(R.id.xxts_setting)
    LinearLayout xxtsSetting;
    @BindView(R.id.switch_zdyts_setting)
    Switch switchZdytsSetting;
    @BindView(R.id.zdyts_setting)
    LinearLayout zdytsSetting;
    @BindView(R.id.zhyaq_setting)
    LinearLayout zhyaqSetting;
    @BindView(R.id.kyxk_setting)
    LinearLayout kyxkSetting;
    @BindView(R.id.version_setting)
    TextView versionSetting;
    @BindView(R.id.jcgx_setting)
    LinearLayout jcgxSetting;
    @BindView(R.id.sm_setting)
    TextView smSetting;
    @BindView(R.id.yjfk_setting)
    TextView yjfkSetting;
    @BindView(R.id.aboutefall_setting)
    TextView aboutefallSetting;
    @BindView(R.id.share_setting)
    TextView shareSetting;
    @BindView(R.id.exit_setting)
    TextView exitSetting;
    @BindView(R.id.author_setting)
    TextView authorSetting;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void initView() {
        initToolbar();
        initSwitch();
        initVersion();
    }

    private void initVersion() {
        versionSetting.setText(new StringBuffer("当前版本:")
                .append(AppUtils.getVersionName(getApplicationContext()))
                .append("(")
                .append(AppUtils.getVersionCode(getApplicationContext()))
                .append(")")
                .toString()
        );
    }

    private void initSwitch() {
        showCacheSize();
        switchSllmsSetting.setChecked((Boolean) SPUtils.get(this, Const.SLLMS, false));
        switchXxtsSetting.setChecked((Boolean) SPUtils.get(this, Const.XXTS, false));
        switchZdytsSetting.setChecked((Boolean) SPUtils.get(this, Const.ZDYTS, false));

        switchSllmsSetting.setOnCheckedChangeListener(this);
        switchXxtsSetting.setOnCheckedChangeListener(this);
        switchZdytsSetting.setOnCheckedChangeListener(this);
    }

    private void showCacheSize() {
        long externalCacheSize = 0;
        long cacheSize = 0;
        long filesSize = 0;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                externalCacheSize = getFileSize(getExternalCacheDir());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            cacheSize = getFileSize(SettingActivity.this.getCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            filesSize = FileInfoUtils.getFileSize(getFilesDir());
        } catch (Exception e) {
            e.printStackTrace();
        }

        textQchcSetting.setText(
                FileInfoUtils.FormetFileSize(externalCacheSize + cacheSize + filesSize));
    }

    // 递归
    public static long getFileSize(File f) throws Exception// 取得文件夹大小
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.sllms_setting, R.id.qchc_setting, R.id.xxts_setting, R.id.qlcz_setting,
            R.id.zdyts_setting, R.id.zhyaq_setting, R.id.kyxk_setting, R.id.jcgx_setting,
            R.id.sm_setting, R.id.yjfk_setting, R.id.aboutefall_setting, R.id.share_setting,
            R.id.exit_setting, R.id.weatheraddress_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sllms_setting:
                switchSllmsSetting.setChecked(!switchSllmsSetting.isChecked());
                break;
            case R.id.qchc_setting:
                DataCleanManager.cleanInternalCache(this);
                DataCleanManager.cleanExternalCache(this);
                DataCleanManager.cleanFiles(this);
                showCacheSize();
                break;
            case R.id.xxts_setting:
                switchXxtsSetting.setChecked(!switchXxtsSetting.isChecked());
                break;
            case R.id.qlcz_setting:
                Snackbar.make(appBar, "重置完成后，将会退出软件一次！", Snackbar.LENGTH_LONG)
                        .setAction("知道了，开始清理", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clearAllData();
                            }
                        }).show();
                break;
            case R.id.zdyts_setting:
                switchZdytsSetting.setChecked(!switchZdytsSetting.isChecked());
                break;
            case R.id.zhyaq_setting:
                startActivity(new Intent(this, AccountActivity.class));
                break;
            case R.id.kyxk_setting:
                Bundle bundle = new Bundle();
                bundle.putString("url", "https://github.com/OCNYang/QBox/blob/master/README.md");
                bundle.putString("title", "开源许可");
                Intent intent = new Intent(this, WebPageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.jcgx_setting:
                UpdateChecker.checkForDialog(this);
                break;
            case R.id.sm_setting:
                startActivity(new Intent(this, StatementActivity.class));
                break;
            case R.id.yjfk_setting:
                Bundle bundle1 = new Bundle();
                bundle1.putString("url", "https://weibo.com/ocnyang");
                bundle1.putString("title", "意见反馈（微博私信）");
                Intent intent1 = new Intent(this, WebPageActivity.class);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;
            case R.id.aboutefall_setting:
                  startActivity(new Intent(this, AboutMeActivity.class));
                break;
            case R.id.share_setting:
                showShare();
                break;
            case R.id.exit_setting:
                exitApp();
                break;
            case R.id.weatheraddress_setting:
                 startActivity(new Intent(this,WeatherAddressActivity.class));
                break;
            default:
                break;
        }
    }

    private void clearAllData() {
        new AlertDialog.Builder(this)
                .setTitle("友情提示")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("此操作你将失去小秋魔盒中所有的数据和设置，回到初始安装状态，确定要这样做吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataCleanManager.cleanApplicationData(SettingActivity.this);
                        //关闭App
                        exitApp();
                    }
                })
                .setNegativeButton("取消", null)
                .create().show();

    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("小秋魔盒");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://ocnyang.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("给你推荐一个我淘到的很不错的App");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://ocnyang.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("功能很强大，设计的也很不错，也很纯净。");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://ocnyang.com");

        // 启动分享GUI
        oks.show(this);
    }

    private void exitApp() {
        Intent intent = new Intent(this, MainsActivity.class);
        intent.putExtra(Const.TAG_EXIT, true);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_sllms_setting:
                SPUtils.put(this, Const.SLLMS, isChecked);
                break;
            case R.id.switch_xxts_setting:
                if (isChecked) {
                    SPUtils.put(this, Const.XXTS, isChecked);
                } else {
                    closeNoti();
                }
                break;
            case R.id.switch_zdyts_setting:
                SPUtils.put(this, Const.ZDYTS, isChecked);
                break;
            default:
                break;
        }
    }

    private void closeNoti() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setIcon(R.drawable.love_guide)
                .setMessage("“本软件是个人开发，不用于商业用途，绝不会向使用者推送广告等性质的消息。\n\n 本功能主要是为了作者和使用者之间沟通和共享一些有趣的信息，请大家尽量不要关闭。\n\n 如果你真的收到了烦扰的消息再关闭也不迟呀！”")
                .setNegativeButton("相信真爱", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switchXxtsSetting.setChecked(true);
                    }
                })
                .setPositiveButton("确定关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPUtils.put(SettingActivity.this, Const.XXTS, false);
                    }
                })
                .setCancelable(false)//点击其他地方不起作用，强制点击按钮
                .create();

        alertDialog.show();
    }
}
