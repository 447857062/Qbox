package kelijun.com.qbox.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import butterknife.ButterKnife;

public abstract class BaseCommonActivity extends AppCompatActivity implements IBaseView{


    private ProgressDialog mProgressDialog;


    /**
     * 初始化布局
     */
    public abstract void initContentView();

    /**
     * 初始化控件
     */
    public abstract void initView();

    /**
     * 初始化控制中心
     */
    public abstract void initPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
        // 初始化View注入
        ButterKnife.bind(this);
        initPresenter();
        initView();
    }

    @Override
    public void finish() {
        // 清除网络请求队列
//        AsyncHttpNetCenter.getInstance().clearRequestQueue(this);
        super.finish();
    }

    /**
     * 显示单选对话框
     *
     * @param title           标题
     * @param message         提示信息
     * @param checkedItem     默认选中
     * @param strings         选项数组
     * @param onClickListener 点击事件的监听
     */
    public void showRadioButtonDialog(String title, String message, String[] strings, int checkedItem, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        builder.setSingleChoiceItems(strings, checkedItem, onClickListener);
        builder.create();
        builder.show();
    }

    @Override
    public void showProgress(boolean flag, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(flag);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(message);
        }

        mProgressDialog.show();
    }

    @Override
    public void showProgress(String message) {
        showProgress(true, message);
    }


    @Override
    public void showProgress(boolean flag) {
        showProgress(flag, "");
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog == null)
            return;

        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showToast(int resId) {
        showToast(getString(resId));
    }

    @Override
    public void showToast(String msg) {
        if (!isFinishing()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void close() {
        finish();
    }



}
