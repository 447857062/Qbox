package kelijun.com.qbox.module.start;


import android.os.Handler;

/**
 * Created by ${kelijun} on 2018/6/25.
 */

public class SplashInteractorImpl implements SplashInteractor {


    @Override
    public void enterInto(boolean isFirstopen, final OnEnterIntoFinishListener listener) {
        if (isFirstopen) {
            listener.isFirstOpen();
        } else {
            listener.showContentView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    listener.isNoFirstOpen();
                }
            },200);

        }
    }
}
