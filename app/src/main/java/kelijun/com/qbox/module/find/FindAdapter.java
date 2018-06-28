package kelijun.com.qbox.module.find;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.List;

import kelijun.com.qbox.R;
import kelijun.com.qbox.model.entities.FunctionBean;

/**
 * Created by ${kelijun} on 2018/6/27.
 * 万能适配器的使用方法
 */

public class FindAdapter extends BaseItemDraggableAdapter<FunctionBean,BaseViewHolder>{
    public FindAdapter(List<FunctionBean> data) {
        super(R.layout.item_find,data);
    }

    public FindAdapter(int layoutResId, List<FunctionBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FunctionBean item) {
        helper.setText(R.id.name_item_find, item.getName());
        ImageView imageView = helper.getView(R.id.icon_item_find);
        try {
            int camera = (int) R.drawable.class.getField(item.getCode()).get(null);
            Logger.i("camera="+camera);
            imageView.setImageResource(camera);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
