package kelijun.com.qbox.base;

/**
 * Created by ${kelijun} on 2018/6/26.
 */

public interface IBaseView {
    /**
     * 显示进度条
     * @param flag 是否可以取消
     * @param message  消息
     */
    void showProgress(boolean flag, String message);

    void showProgress(String message);

    void showProgress(boolean flag);

    void hideProgress();

    /**
     * 显示提示
     *
     * @param resId
     */
    void showToast(int resId);

    void showToast(String message);

    void close();

}
