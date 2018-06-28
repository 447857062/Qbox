package kelijun.com.qbox.module.find;

import android.content.Context;
import android.content.res.Resources;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import kelijun.com.qbox.R;
import kelijun.com.qbox.model.entities.FunctionBean;

/**
 * Created by ${kelijun} on 2018/6/28.
 */

public class FindMoreAdapter extends BaseItemDraggableAdapter<FunctionBean,BaseViewHolder> {
    public final int[] mColorArray;
    public final int mColorLength;

    public FindMoreAdapter(Context context, List<FunctionBean> data) {
        super(R.layout.item_findmore, data);
        Resources resources = context.getResources();

        mColorArray = resources.getIntArray(R.array.itemcolor);
        mColorLength = mColorArray.length;
    }

    @Override
    protected void convert(BaseViewHolder helper, FunctionBean item) {
        int layoutPosition = helper.getLayoutPosition();

        helper.setText(R.id.icon_item_findmore, String.valueOf(layoutPosition))
                .setBackgroundColor(R.id.icon_item_findmore,mColorArray[layoutPosition%mColorLength])
                .setBackgroundColor(R.id.flag_item_findmore, mContext.getResources().getColor(
                        (layoutPosition <= 5) ? R.color.colorAccent : R.color.colorPrimary))
                .setText(R.id.name_item_findmore,item.getName());
    }
}
