package kelijun.com.qbox.module.wechat;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.List;

import kelijun.com.qbox.R;
import kelijun.com.qbox.model.entities.WechatItem;

/**
 * Created by ${kelijun} on 2018/6/27.
 */

public class WechatItemAdapter extends BaseMultiItemQuickAdapter<WechatItem.ResultBean.ListBean,BaseViewHolder>{

    public boolean isNotLoad;
    public int mImgWidth;
    public int mImgHeight;
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public WechatItemAdapter(List<WechatItem.ResultBean.ListBean> data) {
        super(data);
        addItemType(WechatItem.ResultBean.ListBean.STYLE_BIG, R.layout.item_wechat_style1);
        addItemType(WechatItem.ResultBean.ListBean.STYLE_SMALL, R.layout.item_wechat_style2);
    }
    public WechatItemAdapter(List<WechatItem.ResultBean.ListBean> data,boolean isNotLoad,int imageWidth,int imageHeight) {
        super(data);
        addItemType(WechatItem.ResultBean.ListBean.STYLE_BIG, R.layout.item_wechat_style1);
        addItemType(WechatItem.ResultBean.ListBean.STYLE_SMALL, R.layout.item_wechat_style2);
        this.isNotLoad = isNotLoad;
        this.mImgWidth = imageWidth;
        this.mImgHeight = imageHeight;
        Logger.e(imageWidth+"gao:"+imageHeight);
    }

    @Override
    protected void convert(BaseViewHolder helper, WechatItem.ResultBean.ListBean item) {
        switch (helper.getItemViewType()) {
            case WechatItem.ResultBean.ListBean.STYLE_BIG:
                helper.setText(R.id.title_wechat_style1, TextUtils.isEmpty(item.getTitle()) ? mContext.getString(R.string.wechat_select) : item.getTitle());
                if (!isNotLoad) {
                    Glide.with(mContext.getApplicationContext())
                            .load(item.getThumbnails())
                            .override(mImgWidth, mImgHeight)
                            .centerCrop()
                            .error(R.drawable.errorview)
                            .crossFade(1000)
                            .into((ImageView) helper.getView(R.id.img_wechat_style));
                }
                break;
            case WechatItem.ResultBean.ListBean.STYLE_SMALL:
                helper.setText(R.id.title_wechat_style2, TextUtils.isEmpty(item.getTitle()) ? mContext.getString(R.string.wechat_select) : item.getTitle());
                if (!isNotLoad) {
                    Glide.with(mContext.getApplicationContext())
                            .load(item.getThumbnails())
                            .centerCrop()
                            .error(R.drawable.errorview)
                            .override(mImgWidth / 2, mImgWidth / 2)
                            .crossFade(1000)
                            .into((ImageView) helper.getView(R.id.img_wechat_style));
                }
                break;
                default:
                    break;
        }
    }
}
