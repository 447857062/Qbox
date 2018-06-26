package kelijun.com.qbox.network.api;


import kelijun.com.qbox.model.entities.WechatItem;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface WechatApi {
    @GET("wx/article/search?key=1cc099ede9137")
    Observable<WechatItem> getWechat(@Query("cid") String cid,
                                     @Query("page") int page,
                                     @Query("size") int size);
}
