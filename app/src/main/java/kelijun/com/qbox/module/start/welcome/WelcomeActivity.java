package kelijun.com.qbox.module.start.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;

import java.util.List;

import kelijun.com.qbox.R;
import kelijun.com.qbox.config.Const;
import kelijun.com.qbox.database.FunctionDao;
import kelijun.com.qbox.model.entities.Function;
import kelijun.com.qbox.model.entities.FunctionBean;
import kelijun.com.qbox.utils.SPUtils;
import kelijun.com.qbox.utils.StreamUtils;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    public static void start(Context context) {
        SPUtils.put(context, Const.FIRST_OPEN, true);
        context.startActivity(new Intent(context, WelcomeActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
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
        Function function = null;

        try {
            function = new Gson().fromJson(StreamUtils.get(getApplicationContext(), R.raw.function),Function.class);
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
