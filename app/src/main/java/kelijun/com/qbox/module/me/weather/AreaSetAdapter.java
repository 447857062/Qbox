package kelijun.com.qbox.module.me.weather;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kelijun.com.qbox.R;
import kelijun.com.qbox.module.me.weather.weather.api.ApiManager;


public class AreaSetAdapter extends BaseItemDraggableAdapter<ApiManager.Area, BaseViewHolder> {

    private String[] mBgImgArray;

    public AreaSetAdapter(List<ApiManager.Area> data, String[] strings) {
        super(R.layout.fragment_areaset_item, data);
        mBgImgArray = strings;
    }

    @Override
    protected void convert(BaseViewHolder helper, ApiManager.Area item) {
        ImageView imageView = (ImageView) helper.getView(R.id.img_areaset_item);
        Glide.with(mContext)
                .load(mBgImgArray[(int)(Math.random()*(mBgImgArray.length))])
                .error(R.drawable.bg2)
                .fitCenter()
                .into(imageView);
        helper.setText(R.id.name_areaset_item,item.getCity())
                .setText(R.id.province_areaset_item,item.getProvince())
                .setText(R.id.city_areaset_item,item.getName_cn());
    }
}
