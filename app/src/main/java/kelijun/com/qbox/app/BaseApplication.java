package kelijun.com.qbox.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;

import com.mob.MobSDK;

import org.greenrobot.greendao.database.Database;

import kelijun.com.qbox.greendao.db.DaoMaster;
import kelijun.com.qbox.greendao.db.DaoSession;

/**
 * Created by ${kelijun} on 2018/6/26.
 */

public class BaseApplication extends Application {
    public static final boolean DEBUG = false;
    public static final boolean USE_SAMPLE_DATA = false;
    private static BaseApplication mInstance;
    public static Context getInstance() {
        return mInstance;
    }
    public static final boolean ENCRYPTED = true;
    private DaoSession daoSession;
    /**
     * 屏幕宽度
     */
    public static int screenWidth;
    /**
     * 屏幕高度
     */
    public static int screenHeight;
    /**
     * 屏幕密度
     */
    public static float screenDensity;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initScreenSize();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper
                (this, ENCRYPTED ? "users-db-encrypted" : "users-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        MobSDK.init(this);

    }
    /**
     * 初始化当前设备屏幕宽高
     */
    private void initScreenSize() {
        DisplayMetrics curMetrics = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = curMetrics.widthPixels;
        screenHeight = curMetrics.heightPixels;
        screenDensity = curMetrics.density;
    }

    /**
     * 突破64K问题，MultiDex构建
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public DaoSession getDaoSession() {
        return daoSession;
    }
}
