package kelijun.com.qbox.module.find.joke;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.List;

import kelijun.com.qbox.R;
import kelijun.com.qbox.model.entities.textjoke.TextJokeBean;

/**
 * Created by ${kelijun} on 2018/6/28.
 */

public class TextJokeAdapter extends BaseMultiItemQuickAdapter<TextJokeBean,BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public TextJokeAdapter(List<TextJokeBean> data) {
        super(data);
        Logger.i("TextJokeAdapter");
        addItemType(TextJokeBean.JOKE, R.layout.item_textjoke_joke);
        addItemType(TextJokeBean.MORE,R.layout.item_textjoke_more);
    }

    @Override
    protected void convert(BaseViewHolder helper, TextJokeBean item) {
        if (helper.getItemViewType() == TextJokeBean.JOKE) {
            helper.setText(R.id.tv_item_textjoke,item.getContent());
        }else if (TextJokeBean.MORE==helper.getItemViewType()){
            Glide.with(mContext)
                    .load(R.drawable.loadingjoke)
                    .into(((ImageView) helper.getView(R.id.img_item_morejoke)));
        }
    }
}
