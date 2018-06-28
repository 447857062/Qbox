package kelijun.com.qbox.network.api;


import kelijun.com.qbox.model.entities.textjoke.NewTextJokeBean;
import kelijun.com.qbox.model.entities.textjoke.RandomTextJoke;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;



public interface TextJokeApi {
    @GET("joke/content/text.from")
    Observable<NewTextJokeBean> getNewTextJokeJoke(@Query("key") String appkey,
                                                   @Query("page") int pno,
                                                   @Query("pagesize") int ps);

    @GET("joke/randJoke.php")
    Observable<RandomTextJoke> getRandomTextJoke(@Query("key") String key);
}
