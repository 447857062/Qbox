package kelijun.com.qbox.database;

import android.content.Context;

import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import kelijun.com.qbox.greendao.db.CategoryEntityDao;
import kelijun.com.qbox.greendao.db.DaoMaster;
import kelijun.com.qbox.greendao.db.DaoSession;
import kelijun.com.qbox.module.news_category.CategoryEntity;

/**
 * Created by ${kelijun} on 2018/6/25.
 */

public class CategoryDao {
    private DBManager dbManager;

    public CategoryDao(Context context) {
        this.dbManager = DBManager.getInstance(context);
    }

    //TODO
    //插入一条数据
    public void insertCategory(CategoryEntity categoryEntity) {
        //master -> session-> entityDao-> entityDaoInsert
        DaoMaster daoMaster = new DaoMaster(dbManager.getWriteableDatabase());
        DaoSession session = daoMaster.newSession();
        CategoryEntityDao categoryEntityDao = session.getCategoryEntityDao();
        categoryEntityDao.insert(categoryEntity);
    }

    //插入用户集合
    public void insertCategoryList(List<CategoryEntity> categoryEntityList) {
        //master -> session-> entityDao-> entityDaoInsert
        if (categoryEntityList == null || categoryEntityList.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(dbManager.getWriteableDatabase());
        DaoSession session = daoMaster.newSession();
        CategoryEntityDao categoryEntityDao = session.getCategoryEntityDao();
        categoryEntityDao.insertInTx(categoryEntityList);

    }
    //删除一条记录
    public void deleteCategory(CategoryEntity categoryEntity) {
        //master -> session-> entityDao-> entityDaoInsert

        DaoMaster daoMaster = new DaoMaster(dbManager.getWriteableDatabase());
        DaoSession session = daoMaster.newSession();
        CategoryEntityDao categoryEntityDao = session.getCategoryEntityDao();
        categoryEntityDao.delete(categoryEntity);

    }
    //删除所有记录
    public void deleteAllCategory() {
        //master -> session-> entityDao-> entityDaoInsert

        DaoMaster daoMaster = new DaoMaster(dbManager.getWriteableDatabase());
        DaoSession session = daoMaster.newSession();
        CategoryEntityDao categoryEntityDao = session.getCategoryEntityDao();
        categoryEntityDao.deleteAll();

    }
    //更新一条记录
    public void updateCategory(CategoryEntity categoryEntity) {
        //master -> session-> entityDao-> entityDaoInsert

        DaoMaster daoMaster = new DaoMaster(dbManager.getWriteableDatabase());
        DaoSession session = daoMaster.newSession();
        CategoryEntityDao categoryEntityDao = session.getCategoryEntityDao();
        categoryEntityDao.update(categoryEntity);

    }
    //查询用户列表
    public List<CategoryEntity> queryCategoryList() {
        //master -> session-> entityDao-> entityDaoInsert
        DaoMaster daoMaster = new DaoMaster(dbManager.getWriteableDatabase());
        DaoSession session = daoMaster.newSession();
        CategoryEntityDao categoryEntityDao = session.getCategoryEntityDao();
        QueryBuilder<CategoryEntity> queryBuilder = categoryEntityDao.queryBuilder();
        List<CategoryEntity> categoryEntityList = queryBuilder.orderAsc(CategoryEntityDao.Properties.Order).list();
        Logger.i("queryCategoryList() return size=:"+categoryEntityList.size());
        return categoryEntityList;
    }
}
