package kelijun.com.qbox.app;

import android.app.Application;
import android.content.Context;

import org.greenrobot.greendao.database.Database;

import kelijun.com.qbox.greendao.db.DaoMaster;
import kelijun.com.qbox.greendao.db.DaoSession;

/**
 * Created by ${kelijun} on 2018/6/26.
 */

public class BaseApplication extends Application {
    private static BaseApplication mInstance;
    public static Context getInstance() {
        return mInstance;
    }
    public static final boolean ENCRYPTED = true;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper
                (this, ENCRYPTED ? "users-db-encrypted" : "users-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
