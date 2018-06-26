package kelijun.com.qbox.network;

import android.os.Environment;

import java.io.File;
import java.util.concurrent.TimeUnit;

import kelijun.com.qbox.network.api.AllCategoryApi;
import kelijun.com.qbox.network.api.WechatApi;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ${kelijun} on 2018/6/26.
 */

public class Network {
    public static final String ROOT_URL = "http://v.juhe.cn/";
    public static final String MOB_ROOT_URL = "http://apicloud.mob.com/";
    private static AllCategoryApi mAllCategoryApi;
    private static WechatApi mWechatApi;


    public static final long cacheSize = 1024 * 1024 * 20;
    public static String cacheDirectory= Environment.getExternalStorageDirectory()+"okhttpcaches";
    private static Cache cache = new Cache(new File(cacheDirectory), cacheSize);

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(8, TimeUnit.SECONDS);
        builder.readTimeout(8, TimeUnit.SECONDS);
        builder.writeTimeout(8, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);

    }

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    /**
     * okhttp retrofit
     * @return
     */
    public static AllCategoryApi getAllCategoryApi() {
        if (mAllCategoryApi == null) {
            Retrofit retrofit = new Retrofit.Builder()//build模式
                    .client(okHttpClient)//client
                    .baseUrl(MOB_ROOT_URL)//url
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            mAllCategoryApi = retrofit.create(AllCategoryApi.class);
        }
        return mAllCategoryApi;
    }
    public static WechatApi getWecheatApi() {
        if (mWechatApi == null) {
            Retrofit retrofit = new Retrofit.Builder()//build模式
                    .client(okHttpClient)//client
                    .baseUrl(MOB_ROOT_URL)//url
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            mWechatApi = retrofit.create(WechatApi.class);
        }
        return mWechatApi;
    }
}
