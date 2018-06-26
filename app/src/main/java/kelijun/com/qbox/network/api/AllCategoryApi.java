package kelijun.com.qbox.network.api;


import kelijun.com.qbox.model.entities.AllCategoryBean;
import retrofit2.http.GET;
import rx.Observable;




public interface AllCategoryApi {
    @GET("wx/article/category/query?key=1cc099ede9137")
    Observable<AllCategoryBean> getAllCategory();
}
