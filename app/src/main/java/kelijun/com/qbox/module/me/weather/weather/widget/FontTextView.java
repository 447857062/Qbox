package kelijun.com.qbox.module.me.weather.weather.widget;

import android.content.Context;
import android.util.AttributeSet;

import kelijun.com.qbox.module.me.weather.weather.WeatherActivity;


public class FontTextView extends android.support.v7.widget.AppCompatTextView{

	public FontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(isInEditMode()){
			return ;
		}
		setTypeface(WeatherActivity.getTypeface(context));
	}

}
