package kelijun.com.qbox.network.api;


import kelijun.com.qbox.model.entities.FindBg;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;



public interface FindBgApi {
    @GET("HPImageArchive.aspx")
    Observable<FindBg> getFindBg(@Query("format") String format, @Query("idx") int idx, @Query("n") int n);
}
