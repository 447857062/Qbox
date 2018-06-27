package kelijun.com.qbox.widget;

public class ADFilterTool {
    public static boolean hasAd(String url) {
        //这里找到最初的病因，直接屏蔽掉这个就行了。
        if (url.contains("eastday.com/toutiaoh5")) {
            return true;
        }
        return false;
    }

}
