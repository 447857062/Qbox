package kelijun.com.qbox.module.start.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import kelijun.com.qbox.R;
import kelijun.com.qbox.config.Const;
import kelijun.com.qbox.database.CategoryDao;
import kelijun.com.qbox.database.FunctionDao;
import kelijun.com.qbox.model.entities.AllCategoryBean;
import kelijun.com.qbox.model.entities.CategoryManager;
import kelijun.com.qbox.model.entities.Function;
import kelijun.com.qbox.model.entities.FunctionBean;
import kelijun.com.qbox.module.news_category.CategoryEntity;
import kelijun.com.qbox.network.Network;
import kelijun.com.qbox.utils.SPUtils;
import kelijun.com.qbox.utils.StateBarTranslucentUtils;
import kelijun.com.qbox.utils.StreamUtils;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Subscription mSubscription;

    public static void start(Context context) {
        SPUtils.put(context, Const.FIRST_OPEN, false);
        context.startActivity(new Intent(context, WelcomeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //设置状态栏透明
        StateBarTranslucentUtils.setStateBarTranslucent(this);
        findViewById(R.id.bRetry).setOnClickListener(this);
        if (savedInstanceState == null) {
            replaceTutorialFragment();
        }
        requestCategory();
        saveFunctionToDB();
    }

    /**
     * 第一次打开app保存所有类别到本地数据库
     */
    private void requestCategory() {
        unsubscribe();
        mSubscription = Network.getAllCategoryApi()
                .getAllCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    private Observer<AllCategoryBean> mObserver = new Observer<AllCategoryBean>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Logger.e("onError:" + e.getMessage());
            saveCategoryToDB(null);
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

                saveCategoryToDB(categoryEntityList);
            } else {
                saveCategoryToDB(null);
            }
        }
    };

    private void saveCategoryToDB(List<CategoryEntity> categoryEntityList) {
        CategoryDao categoryDao = new CategoryDao(getApplicationContext());
        categoryDao.deleteAllCategory();

        categoryDao.insertCategoryList(categoryEntityList == null ?
                (new CategoryManager(getApplicationContext()).getAllCategory())
                : categoryEntityList);
    }

    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    /**
     * 当第一次打开App时，保存3个大类别 (历史上的今天、老黄历、笑话大全) 到SharedPreferences
     */
    private void saveFunctionBigToSP() {
        SPUtils.put(this, Const.STAR_IS_OPEN, true);
        SPUtils.put(this, Const.STUFF_IS_OPEN, true);
        SPUtils.put(this, Const.JOKE_IS_OPEN, true);
    }

    private void saveFunctionToDB() {
        Logger.i("saveFunctionToDB");
        Function function = null;
        try {
            function = new Gson().fromJson(StreamUtils.get(getApplicationContext(), R.raw.function), Function.class);

        } catch (JsonSyntaxException e) {
            Logger.e("读取raw中的function.json文件异常：" + e.getMessage());
            e.printStackTrace();
        }

        if (function != null && function.getFunction() != null && function.getFunction().size() != 0) {
            List<FunctionBean> functionBeanList = function.getFunction();
            FunctionDao functionDao = new FunctionDao(getApplicationContext());
            functionDao.insertFunctionList(functionBeanList);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRetry:
                replaceTutorialFragment();
                break;
        }
    }

    public void replaceTutorialFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_welcome, new CustomTutorialSupportFragment())
                .commit();
    }
}
