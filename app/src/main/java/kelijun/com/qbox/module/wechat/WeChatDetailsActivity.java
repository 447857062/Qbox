package kelijun.com.qbox.module.wechat;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import cn.sharesdk.onekeyshare.OnekeyShare;
import kelijun.com.qbox.R;
import kelijun.com.qbox.base.BaseCommonActivity;
import kelijun.com.qbox.config.Const;
import kelijun.com.qbox.model.entities.WechatItem;
import kelijun.com.qbox.utils.SPUtils;
import kelijun.com.qbox.widget.Html5WebView;

public class WeChatDetailsActivity extends BaseCommonActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.ivImage)
    ImageView mImageView;
    @BindView(R.id.nestedscrollview_wechat)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.webview_wechat)
    Html5WebView mWebviewWechat;

    WechatItem.ResultBean.ListBean mWechat;
    @Override
    public void initContentView() {
        setContentView(R.layout.activity_we_chat_details);
    }
    @Override
    public void initView() {
        initToolbar();
        initDataByGetIntent();
        initFAB();
        initWebView();
    }
    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initDataByGetIntent() {
        mWechat = getIntent().getParcelableExtra("wechat");
        boolean isNotLoad = (boolean) SPUtils.get(this, Const.SLLMS, false);
        if (!isNotLoad) {
            Glide.with(this)
                    .load(mWechat.getThumbnails())
                    .placeholder(R.drawable.lodingview)
                    .error(R.drawable.errorview)
                    .crossFade(1000)
                    .into(mImageView);
        }
        getSupportActionBar().setTitle(mWechat.getTitle());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initFAB() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWechat != null) {
                    showShare();
                }
            }
        });
    }
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(mWechat.getTitle()+"");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(mWechat.getSourceUrl()+"");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("文章标题："+mWechat.getTitle()+"\n地址："+mWechat.getSourceUrl()+"\n-来自：小秋魔盒");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(mWechat.getSourceUrl()+"");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("很喜欢这篇文章，写的很不错。");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://ocnyang.com");

        // 启动分享GUI
        oks.show(this);
    }
    private void initWebView() {
        mWebviewWechat.loadUrl(mWechat.getSourceUrl());
    }
    @Override
    public void initPresenter() {

    }

}
