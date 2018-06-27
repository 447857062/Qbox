package kelijun.com.qbox.widget.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * 字体是自定义IconFont字体库的 TextView
 * 自定义字体
 */

public class IconFontTextView extends android.support.v7.widget.AppCompatTextView {

    public static final String ICON_FONT = "fonts/iconfont.ttf";

    public IconFontTextView(Context context) {
        this(context, null);
    }

    public IconFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(), ICON_FONT));
    }
}
