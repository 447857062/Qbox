package kelijun.com.qbox.network;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

import kelijun.com.qbox.network.api.AllCategoryApi;
import kelijun.com.qbox.network.api.ConstellationApi;
import kelijun.com.qbox.network.api.DayJokeApi;
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
    private static ConstellationApi sConstellationApi;
    private static DayJokeApi sDayJokeApi;

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
    public static ConstellationApi getConstellationApi(){
        if (sConstellationApi == null) {
            Retrofit retrofit = new Retrofit.Builder()//build模式
                    .client(okHttpClient)//client
                    .baseUrl("http://web.juhe.cn:8080/")//url
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sConstellationApi = retrofit.create(ConstellationApi.class);
        }
        return sConstellationApi;
    }
    public static DayJokeApi getDayJokeApi() {
        if (sDayJokeApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://japi.juhe.cn/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sDayJokeApi = retrofit.create(DayJokeApi.class);
        }
        Log.e("oooooo","getDayJokeApi");
        return sDayJokeApi;
    }
}
