package kelijun.com.qbox.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.AttributeSet;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import kelijun.com.qbox.R;
import kelijun.com.qbox.utils.webviewutils.NetStatusUtil;

/**
 * Created by ${kelijun} on 2018/6/27.
 */

public class Html5WebView extends WebView {
    private ProgressBar mProgressBar;

    private WebsiteChangeListener mWebsiteChangeListener;
    private Context mContext;
    public Html5WebView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public Html5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public Html5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public Html5WebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init();
    }

    public Html5WebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        mContext = context;
        init();
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        mProgressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 8);
        mProgressBar.setLayoutParams(layoutParams);

        Drawable drawable = getResources().getDrawable(R.drawable.web_progress_bar_states);
        mProgressBar.setProgressDrawable(drawable);

        addView(mProgressBar);

        WebSettings webSettings = getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setLoadsImagesAutomatically(true);

        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);

        //缓存数据
        saveData(webSettings);
        //新建窗口
        newWin(webSettings);

        setWebChromeClient(webChromeClient);
        setWebViewClient(webViewClient);
    }
    WebChromeClient webChromeClient=new WebChromeClient(){
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);//注意个函数，第二个参数就是是否同意定位权限，第三个是是否希望内核记住
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            HitTestResult result = view.getHitTestResult();
            String data = result.getExtra();
            view.loadUrl(data);
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(GONE);
            } else {
                if (mProgressBar.getVisibility() == GONE)
                    mProgressBar.setVisibility(VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mWebsiteChangeListener != null) {
                mWebsiteChangeListener.onWebsiteChange(title);
            }
        }

    };
    WebViewClient webViewClient=new WebViewClient(){
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                String url = request.getUrl().toString().toLowerCase();
                if (!ADFilterTool.hasAd(url)) {
                    return super.shouldInterceptRequest(view, request);
                } else {
                    return new WebResourceResponse(null, null, null);
                }
            } else {
                return super.shouldInterceptRequest(view, request);
            }
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            int lastLen = url.length() > 40 ? 40 : url.length();
            String adUrl = url.substring(0, lastLen).toLowerCase();
            if (!ADFilterTool.hasAd(adUrl)) {
                return super.shouldInterceptRequest(view, url);
            } else {
                return new WebResourceResponse(null, null, null);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            if (mWebsiteChangeListener!=null) {
                mWebsiteChangeListener.onUrlChange(url);

            }
            return true;
        }

    };
    private void newWin(WebSettings webSettings) {
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

    }

    /**
     * html5 数据存储
     * @param webSettings
     */
    private void saveData(WebSettings webSettings) {
        if (NetStatusUtil.isConnected(mContext)) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        }

        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);

        String appCachePath = mContext.getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);

    }

    public interface WebsiteChangeListener {
        void onWebsiteChange(String title);

        void onUrlChange(String url);
    }

    public void setmWebsiteChangeListener(WebsiteChangeListener mWebsiteChangeListener) {
        this.mWebsiteChangeListener = mWebsiteChangeListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) mProgressBar.getLayoutParams();
        lp.x=l;
        lp.y=t;
        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
