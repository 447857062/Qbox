package kelijun.com.qbox.database;

import android.content.Context;

import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import kelijun.com.qbox.greendao.db.DaoMaster;
import kelijun.com.qbox.greendao.db.DaoSession;
import kelijun.com.qbox.greendao.db.FunctionBeanDao;
import kelijun.com.qbox.model.entities.FunctionBean;

/**
 * Created by ${kelijun} on 2018/6/25.
 */

public class FunctionDao {
    private DBManager dbManager;

    public FunctionDao(Context context) {
        this.dbManager = DBManager.getInstance(context);

    }
    /**
     * 插入一条记录
     *
     * @param user
     */
    public void insertFunction(FunctionBean user) {
        DaoMaster daoMaster = new DaoMaster(dbManager.getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FunctionBeanDao functionBeanDao = daoSession.getFunctionBeanDao();
        functionBeanDao.insert(user);
    }

    /**
     * 插入用户集合
     *
     * @param users
     */
    public void insertFunctionList(List<FunctionBean> users) {
        Logger.i("插入用户集合");
        if (users == null || users.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(dbManager.getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FunctionBeanDao userDao = daoSession.getFunctionBeanDao();
        userDao.insertOrReplaceInTx(users);
    }

    /**
     * 删除一条记录
     *
     * @param user
     */
    public void deleteFunction(FunctionBean user) {
        DaoMaster daoMaster = new DaoMaster(dbManager.getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FunctionBeanDao userDao = daoSession.getFunctionBeanDao();
        userDao.delete(user);
    }

    /**
     * 删除所有记录
     */
    public void deleteAllFunction(){
        DaoMaster daoMaster = new DaoMaster(dbManager.getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FunctionBeanDao functionBeanDao = daoSession.getFunctionBeanDao();
        functionBeanDao.deleteAll();
    }

    /**
     * 更新一条记录
     *
     * @param user
     */
    public void updateFunction(FunctionBean user) {
        DaoMaster daoMaster = new DaoMaster(dbManager.getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FunctionBeanDao userDao = daoSession.getFunctionBeanDao();
        userDao.update(user);
    }
    /**
     * 更新多条记录
     *
     * @param user
     */
    public void updateAllFunctionBean(List<FunctionBean> user) {
        DaoMaster daoMaster = new DaoMaster(dbManager.getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FunctionBeanDao userDao = daoSession.getFunctionBeanDao();
        userDao.updateInTx(user);
    }

    /**
     * 查询用户列表
     */
    public List<FunctionBean> queryFunctionList() {
        DaoMaster daoMaster = new DaoMaster(dbManager.getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FunctionBeanDao userDao = daoSession.getFunctionBeanDao();
        QueryBuilder<FunctionBean> qb = userDao.queryBuilder();
        List<FunctionBean> list = qb.orderAsc(FunctionBeanDao.Properties.Id).list();
        return list;
    }
    /**
     * 查询用户列表
     */
    public List<FunctionBean> queryFunctionBeanListSmall() {
        DaoMaster daoMaster = new DaoMaster(dbManager.getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FunctionBeanDao userDao = daoSession.getFunctionBeanDao();
        QueryBuilder<FunctionBean> qb = userDao.queryBuilder();
        List<FunctionBean> list = qb.where(FunctionBeanDao.Properties.Mark.eq(1))
                .orderAsc(FunctionBeanDao.Properties.Id).list();
        for (int i = 0; i < list.size(); i++) {
            Logger.i("功能名称:"+list.get(i).getName());
        }
        return list;
    }
    /**
     * 查询用户列表
     */
    public List<FunctionBean> queryFunctionBeanListSmallNoMore() {
        DaoMaster daoMaster = new DaoMaster(dbManager.getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FunctionBeanDao userDao = daoSession.getFunctionBeanDao();
        QueryBuilder<FunctionBean> qb = userDao.queryBuilder();
        List<FunctionBean> list = qb.where(FunctionBeanDao.Properties.Mark.eq(1))
                .where(FunctionBeanDao.Properties.Name.notEq("更多"))
                .orderAsc(FunctionBeanDao.Properties.Id).list();
        return list;
    }

    /**
     * 查询用户列表
     */
    public List<FunctionBean> queryFunctionBeanListSmallNeed() {
        DaoMaster daoMaster = new DaoMaster(dbManager.getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FunctionBeanDao userDao = daoSession.getFunctionBeanDao();
        QueryBuilder<FunctionBean> qb = userDao.queryBuilder();
        List<FunctionBean> list = qb.where(FunctionBeanDao.Properties.Mark.eq(1))
                .orderAsc(FunctionBeanDao.Properties.Id)
                .limit(6)
                .list();
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 查询某个功能是否开启
     */
    public boolean queryFunctionBeanListBigIsOpen(String string) {
        DaoMaster daoMaster = new DaoMaster(dbManager.getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FunctionBeanDao userDao = daoSession.getFunctionBeanDao();
        QueryBuilder<FunctionBean> qb = userDao.queryBuilder();
        FunctionBean functionBean = qb.where(FunctionBeanDao.Properties.Code.eq(string)).uniqueOrThrow();
        return !(functionBean.getNotOpen());
    }

    /**
     * 查询 更多 的位置
     */
    public int queryMoreFunctionBean() {
        DaoMaster daoMaster = new DaoMaster(dbManager.getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FunctionBeanDao userDao = daoSession.getFunctionBeanDao();
        QueryBuilder<FunctionBean> qb = userDao.queryBuilder();
        FunctionBean functionBean = qb.where(FunctionBeanDao.Properties.Name.eq("更多")).uniqueOrThrow();
        return functionBean.getId();
    }


    /**
     * 更新 更多 一条记录
     *
     *
     */
    public void updateMoreFunctionBean() {
        DaoMaster daoMaster = new DaoMaster(dbManager.getWriteableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FunctionBeanDao userDao = daoSession.getFunctionBeanDao();
        QueryBuilder<FunctionBean> qb = userDao.queryBuilder();
        //查询的条件查询
        FunctionBean functionBean = qb.where(FunctionBeanDao.Properties.Name.eq("更多")).uniqueOrThrow();
        functionBean.setId(6);
        userDao.update(functionBean);
    }
}
