package kelijun.com.qbox.network.api;


import kelijun.com.qbox.model.entities.ChinaCalendar;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;




public interface ChinaCalendarApi {
    @GET("calendar/day")
    Observable<ChinaCalendar> getChinaCalendar(@Query("key") String key, @Query("date") String date);
}
