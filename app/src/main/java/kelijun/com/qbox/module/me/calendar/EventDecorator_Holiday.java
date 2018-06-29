package kelijun.com.qbox.module.me.calendar;

import android.text.TextUtils;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Map;

import kelijun.com.qbox.model.HolidaysManager;

/*******************************************************************
 *    * * * *   * * * *   *     *       Created by OCN.Yang
 *    *     *   *         * *   *       Time:2017/3/23 15:46.
 *    *     *   *         *   * *       Email address:ocnyang@gmail.com
 *    * * * *   * * * *   *     *.Yang  Web site:www.ocnyang.com
 *******************************************************************/


public class EventDecorator_Holiday implements DayViewDecorator {
    private Map<String, String> mDateStringMap;

    public EventDecorator_Holiday(Map<String,String> dateStringMap) {
        this.mDateStringMap = dateStringMap;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        String formatDate = HolidaysManager.formatDate(day.getDate());

        boolean b = mDateStringMap.containsKey(formatDate);

        if (b){
            String s = mDateStringMap.get(formatDate);
            if (TextUtils.isEmpty(s)) {
                return false;
            }else {
                return true;
            }
        }else {
            return false;
        }
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new EventSpan_Holiday());
    }
}
