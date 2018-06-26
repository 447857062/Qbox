package kelijun.com.qbox.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by ${kelijun} on 2018/6/26.
 */

public abstract class BaseFragment extends Fragment implements IBaseView{
    private BaseActivity baseActivity;

    public abstract int getLayoutRes();

    public abstract void initView();

    protected abstract void managerArguments();

    private View mLayoutView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            managerArguments();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mLayoutView != null) {

        } else {
            mLayoutView = inflater.inflate(getLayoutRes(), container, false);
            ButterKnife.bind(this, mLayoutView);
            initView();
        }
        return mLayoutView;
    }

    private boolean getStatu() {
        return isAdded() && !isRemoving();
    }
    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void showProgress(boolean flag, String message) {
        if (getStatu()) {
            BaseActivity activity = getBaseActivity();
            if (activity != null) {
                activity.showProgress(flag, message);
            }
        }
    }

    @Override
    public void showProgress(String message) {
        showProgress(true,message);
    }

    @Override
    public void showProgress(boolean flag) {
        showProgress(flag, "");
    }

    @Override
    public void hideProgress() {
        if (getStatu()) {
            BaseActivity activity = getBaseActivity();
            if (activity != null) {
                activity.hideProgress();
            }
        }
    }

    @Override
    public void showToast(int resId) {
        if (getStatu()) {
            BaseActivity activity = getBaseActivity();
            if (activity != null) {
                activity.showToast(resId);
            }
        }
    }

    @Override
    public void showToast(String message) {
        if (getStatu()) {
            BaseActivity activity = getBaseActivity();
            if (activity != null) {
                activity.showToast(message);
            }
        }
    }

    public BaseActivity getBaseActivity() {
        if (baseActivity == null) {
            baseActivity = (BaseActivity) getActivity();
        }
        return baseActivity;
    }

    @Override
    public void close() {

    }
}
