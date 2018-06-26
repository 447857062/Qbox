package kelijun.com.qbox.module.news;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kelijun.com.qbox.R;
import kelijun.com.qbox.model.entities.WechatItem;

/**
 * Created by ${kelijun} on 2018/6/26.
 */

public class DefaultStyleItemAdapter extends BaseQuickAdapter<WechatItem.ResultBean.ListBean,BaseViewHolder> {

    boolean isNotLoad;
    public int mImgWidth;
    public int mImgHeight;

    public DefaultStyleItemAdapter(int layoutResId, List<WechatItem.ResultBean.ListBean> data) {
        super(layoutResId, data);
    }

    public DefaultStyleItemAdapter(int layoutResId) {
        super(layoutResId);
    }
    public DefaultStyleItemAdapter(int layoutResId, boolean isNotLoadImg, int imgWidth, int imgHeight){
        super(layoutResId);
        isNotLoad = isNotLoadImg;
        mImgWidth = imgWidth;
        mImgHeight = imgHeight;
    }
    @Override
    protected void convert(BaseViewHolder helper, WechatItem.ResultBean.ListBean item) {
            helper.setText(R.id.title_news_item,item.getTitle())
                    .setText(R.id.from_news_item,"柯礼军")
                    .setText(R.id.time_news_item,onFromatTime(item.getPubTime()));
        if (!isNotLoad) {
            Glide.with(mContext)
                    .load(item.getThumbnails())
                    .override(mImgWidth, mImgHeight)
                    .centerCrop()
                    .error(R.drawable.errorview)
                    .crossFade(1000)
                    .into((ImageView) helper.getView(R.id.img_news_item));

        }
    }

    private String onFromatTime(String pubTime) {
        Date formatDate=null;
        if (TextUtils.isEmpty(pubTime)) {
            return getNowTime("MM-dd");
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                formatDate = simpleDateFormat.parse(pubTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        if (formatDate == null) {
            return getNowTime("MM-dd");
        }
        return simpleDateFormat.format(formatDate);
    }

    private String getNowTime(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(s);
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return simpleDateFormat.format(date);
    }
}
