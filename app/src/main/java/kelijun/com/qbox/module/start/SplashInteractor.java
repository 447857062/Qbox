package kelijun.com.qbox.module.start;

/**
 * Created by ${kelijun} on 2018/6/25.
 */

public interface SplashInteractor {
    interface OnEnterIntoFinishListener {
        void isFirstOpen();

        void isNoFirstOpen();

        void showContentView();

    }

    void enterInto(boolean isFirstopen, OnEnterIntoFinishListener listener);
}
