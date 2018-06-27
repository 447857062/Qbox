package kelijun.com.qbox.module.news_category;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import kelijun.com.qbox.R;
import kelijun.com.qbox.base.BaseCommonActivity;
import kelijun.com.qbox.config.Const;
import kelijun.com.qbox.database.CategoryDao;
import kelijun.com.qbox.model.entities.AllCategoryBean;
import kelijun.com.qbox.model.entities.CategoryManager;
import kelijun.com.qbox.module.news_category.draghelper.ItemDragHelperCallback;
import kelijun.com.qbox.network.Network;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CategoryActivity extends BaseCommonActivity {
    private RecyclerView mRecy;
    private List<CategoryEntity> items;
    private List<CategoryEntity> itemsOther;
    private CategoryAdapter mCategoryAdapter;
    private Subscription mSubscription;
    private Toolbar mToolbar;
    private Observer<AllCategoryBean>mObserver=new Observer<AllCategoryBean>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Logger.e("onError:" + e.getMessage());
            setAdapter(null);
        }

        @Override
        public void onNext(AllCategoryBean allCategoryBean) {
            if (allCategoryBean != null && "200".equals(allCategoryBean.getRetCode())) {
                List<CategoryEntity> categoryEntityList = new ArrayList<>();
                List<AllCategoryBean.ResultBean> result = allCategoryBean.getResult();
                for (int i = 0; i < result.size(); i++) {
                    AllCategoryBean.ResultBean resultBean = result.get(i);
                    CategoryEntity categoryEntity = new CategoryEntity(null, resultBean.getName(), resultBean.getCid(), i);
                    categoryEntityList.add(categoryEntity);
                }
                setAdapter(categoryEntityList);
            } else {
                setAdapter(null);
            }
        }
    };
    private void setAdapter(List<CategoryEntity> categoryEntityList) {

        List<CategoryEntity> allCategory = categoryEntityList == null ? new CategoryManager(this).getAllCategory() : categoryEntityList;

        for (CategoryEntity entity : allCategory) {
            if (!items.contains(entity)) {
                itemsOther.add(entity);
            }
        }

        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecy);

        mCategoryAdapter = new CategoryAdapter(this, helper, items, itemsOther);
        mRecy.setAdapter(mCategoryAdapter);

        mCategoryAdapter.setOnMyChannelItemClickListener(new CategoryAdapter.OnMyChannelItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(CategoryActivity.this, "请点击编辑或者长按进行操作！", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void initContentView() {
        setContentView(R.layout.activity_category);
    }

    @Override
    public void initView() {
        mRecy = (RecyclerView) findViewById(R.id.recy);

        initToolbar();
        initRecycleView();
    }
    private void initToolbar() {
        mToolbar = ((Toolbar) findViewById(R.id.toolbar));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CategoryEntity categ :
                        items) {
                    String name = categ.getName();
                    Logger.e(name);
                }
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.news_category);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                saveNewDataToDB();
                Toast.makeText(CategoryActivity.this, R.string.set_success, Toast.LENGTH_SHORT).show();

                setResult(Const.NEWSFRAGMENT_CATEGORYACTIVITY_RESULTCODE, null);
                finish();
                return false;
            }


        });
    }
    /**
     * 更新数据库的存储
     */
    private void saveNewDataToDB() {
        CategoryDao categoryDao = new CategoryDao(getApplicationContext());
        categoryDao.deleteAllCategory();
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setOrder(i);
        }
        categoryDao.insertCategoryList(items);
    }

    private void requestCategory() {
        unsubscribe();
        mSubscription = Network.getAllCategoryApi()
                .getAllCategory()//key,页码,每页条数
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }
    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
    private void initRecycleView() {
        items = getCategoryFromDB();
        itemsOther = new ArrayList<>();
        if (items == null) {
            items = new ArrayList<>();
        }
        requestCategory();
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        mRecy.setLayoutManager(manager);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = mCategoryAdapter.getItemViewType(position);
                return viewType == CategoryAdapter.TYPE_MY || viewType == CategoryAdapter.TYPE_OTHER ? 1 : 4;
            }
        });
    }
    /**
     * 获取本地数据库中 序列化的新闻类别
     *
     * @return
     */
    private List<CategoryEntity> getCategoryFromDB() {
        CategoryDao categoryDao = new CategoryDao(getApplicationContext());
        return categoryDao.queryCategoryList();
    }
    @Override
    public void initPresenter() {

    }

}
