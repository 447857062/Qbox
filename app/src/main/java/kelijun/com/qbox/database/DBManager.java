package kelijun.com.qbox.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import kelijun.com.qbox.greendao.db.DaoMaster;

/**
 * Created by ${kelijun} on 2018/6/25.
 */

public class DBManager {
    public static final String DB_NAME = "test_db";

    private static DBManager instance;

    private DaoMaster.DevOpenHelper openHelper;
    private Context mContext;
    private DBManager(Context context) {
        this.mContext = context;
        openHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
    }
    public static DBManager getInstance(Context context){
        if (instance == null) {
            synchronized (DBManager.class) {
                if (instance == null) {
                    instance = new DBManager(context);
                }
            }

        }
        return instance;
    }

    protected SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
        }
        return openHelper.getReadableDatabase();
    }
    protected SQLiteDatabase getWriteableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
        }
        return openHelper.getWritableDatabase();
    }
}
