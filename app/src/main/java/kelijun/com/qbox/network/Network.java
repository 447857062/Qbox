package kelijun.com.qbox.network;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

import kelijun.com.qbox.network.api.AllCategoryApi;
import kelijun.com.qbox.network.api.ChinaCalendarApi;
import kelijun.com.qbox.network.api.ConstellationApi;
import kelijun.com.qbox.network.api.DayJokeApi;
import kelijun.com.qbox.network.api.FindBgApi;
import kelijun.com.qbox.network.api.QueryInfoApi;
import kelijun.com.qbox.network.api.TextJokeApi;
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
    public static ChinaCalendarApi sChinaCalendarApi;
    private static FindBgApi sFindBgApi;
    private static TextJokeApi sTextJokeApi;
    private static TextJokeApi sRandomTextJokeApi;

    private static QueryInfoApi sQueryTelApi;
    private static QueryInfoApi sQueryQQApi;
    private static QueryInfoApi sQueryIDCardApi;

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
    public static FindBgApi getFindBgApi() {
        if (sFindBgApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://www.bing.com/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sFindBgApi = retrofit.create(FindBgApi.class);
        }
        Log.e("oooooo","getFindBgApi");
        return sFindBgApi;
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
    public static TextJokeApi getNewTextJokeApi(){
        if (sTextJokeApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://japi.juhe.cn/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sTextJokeApi = retrofit.create(TextJokeApi.class);
        }
        return sTextJokeApi;

    }
    public static TextJokeApi getRandomTextJokeApi(){
        if (sRandomTextJokeApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://v.juhe.cn/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sRandomTextJokeApi = retrofit.create(TextJokeApi.class);
        }
        return sRandomTextJokeApi;

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
    public static ChinaCalendarApi getChinaCalendarApi(){
        if (sChinaCalendarApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://v.juhe.cn/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sChinaCalendarApi = retrofit.create(ChinaCalendarApi.class);
        }
        return sChinaCalendarApi;
    }
    public static QueryInfoApi getQueryIDCardApi(){
        if (sQueryIDCardApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://apis.juhe.cn/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sQueryIDCardApi = retrofit.create(QueryInfoApi.class);
        }
        return sQueryIDCardApi;
    }
    public static QueryInfoApi getQueryQQApi(){
        if (sQueryQQApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://japi.juhe.cn/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sQueryQQApi = retrofit.create(QueryInfoApi.class);
        }
        return sQueryQQApi;
    }
    public static QueryInfoApi getQueryTelApi(){
        if (sQueryTelApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://apis.juhe.cn/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            sQueryTelApi = retrofit.create(QueryInfoApi.class);
        }
        return sQueryTelApi;
    }
}
