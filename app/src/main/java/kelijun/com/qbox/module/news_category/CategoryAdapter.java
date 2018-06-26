package kelijun.com.qbox.module.news_category;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kelijun.com.qbox.R;
import kelijun.com.qbox.module.news_category.draghelper.OnDragVHListener;
import kelijun.com.qbox.module.news_category.draghelper.OnItemMoveListener;

/**
 * Created by ${kelijun} on 2018/6/26.
 * 拖拽排序,增删功能
 * 多item类型适配
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemMoveListener {
    // 我的频道 标题部分
    public static final int TYPE_MY_CHANNEL_HEADER = 0;
    // 我的频道
    public static final int TYPE_MY = 1;
    // 其他频道 标题部分
    public static final int TYPE_OTHER_CHANNEL_HEADER = 2;
    // 其他频道
    public static final int TYPE_OTHER = 3;

    // 我的频道之前的header数量  该demo中 即标题部分 为 1
    private static final int COUNT_PRE_MY_HEADER = 1;
    // 其他频道之前的header数量  该demo中 即标题部分 为 COUNT_PRE_MY_HEADER + 1
    private static final int COUNT_PRE_OTHER_HEADER = COUNT_PRE_MY_HEADER + 1;

    private List<CategoryEntity> mMyChannelItems, mOtherChannelItems;
    // 是否为 编辑 模式
    private boolean isEditMode;
    private ItemTouchHelper mItemTouchHelper;
    private LayoutInflater mInflater;
    // touch 点击开始时间
    private long startTime;
    // touch 间隔时间  用于分辨是否是 "点击"
    private static final long SPACE_TIME = 100;
    public CategoryAdapter(
            Context context,
            ItemTouchHelper itemTouchHelper,
            List<CategoryEntity> mMyChannelItems,
            List<CategoryEntity> mOtherChannelItems) {
        this.mMyChannelItems = mMyChannelItems;
        this.mOtherChannelItems = mOtherChannelItems;
        this.mInflater = LayoutInflater.from(context);
        this.mItemTouchHelper = itemTouchHelper;
    }
    interface OnMyChannelItemClickListener {
        void onItemClick(View v, int position);
    }
    public void setOnMyChannelItemClickListener(OnMyChannelItemClickListener listener) {
        this.mChannelItemClickListener = listener;
    }
    // 我的频道点击事件
    private OnMyChannelItemClickListener mChannelItemClickListener;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view;
        switch (viewType) {
            case TYPE_MY:
                view = mInflater.inflate(R.layout.item_my, parent, false);
                final MyViewHolder myViewHolder = new MyViewHolder(view);
                myViewHolder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = myViewHolder.getAdapterPosition();
                        if (isEditMode) {
                            RecyclerView recyclerView= (RecyclerView) parent;

                            View targetView = recyclerView.getLayoutManager().findViewByPosition(mMyChannelItems.size() + COUNT_PRE_OTHER_HEADER);
                            //如果目标view在屏幕内,或者不在屏幕内
                            //在屏幕内需要手动添加一个动画
                            if (recyclerView.indexOfChild(targetView) >= 0) {


                            }else{
                                moveMyToOther(myViewHolder);
                            }
                        }else{
                            mChannelItemClickListener.onItemClick(v,position-COUNT_PRE_MY_HEADER);
                        }

                    }
                });
                myViewHolder.textView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        if (!isEditMode) {
                            RecyclerView recyclerView= (RecyclerView) parent;
                            startEditMode(recyclerView);

                            // header 按钮文字 改成 "完成"
                            View view = recyclerView.getChildAt(0);
                            if (view == recyclerView.getLayoutManager().findViewByPosition(0)) {
                                TextView tvBtnEdit = (TextView) view.findViewById(R.id.tv_btn_edit);
                                tvBtnEdit.setText(R.string.finish);
                            }
                        }
                        mItemTouchHelper.startDrag(myViewHolder);

                        return false;
                    }
                });

                //触摸事件处理,如果触摸事件开始事件到结束事件大于一个值,并且处于编辑模式,就处理拖拽事件
                myViewHolder.textView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (isEditMode) {
                            switch (MotionEventCompat.getActionMasked(event)) {

                                case MotionEvent.ACTION_MOVE:
                                    if (System.currentTimeMillis() - startTime > SPACE_TIME) {
                                        mItemTouchHelper.startDrag(myViewHolder);
                                    }
                                    break;
                                case MotionEvent.ACTION_DOWN:
                                    startTime=System.currentTimeMillis();
                                    break;
                                case MotionEvent.ACTION_CANCEL:
                                case MotionEvent.ACTION_UP:
                                    startTime=0;
                                    break;
                            }
                        }
                        return false;
                    }
                });
                return myViewHolder;
            case TYPE_MY_CHANNEL_HEADER:
                break;
            case TYPE_OTHER:
                break;
            case TYPE_OTHER_CHANNEL_HEADER:
                break;
            default:
                break;
        }
        return null;
    }

    private void moveMyToOther(MyViewHolder myViewHolder) {
        int position = myViewHolder.getAdapterPosition();
        int startPosition = position - COUNT_PRE_MY_HEADER;

        if (startPosition > mMyChannelItems.size() - 1) {
            return;
        }
        CategoryEntity categoryEntity = mMyChannelItems.get(startPosition);

        mMyChannelItems.remove(categoryEntity);

        mOtherChannelItems.add(0, categoryEntity);
        notifyItemMoved(position, mMyChannelItems.size() + COUNT_PRE_OTHER_HEADER);
    }

    /**
     * 开启编辑模式
     * @param recyclerView
     */
    private void startEditMode(RecyclerView recyclerView) {
        isEditMode = true;
        int visiableChilrenCount = recyclerView.getChildCount();
        for (int i = 0; i < visiableChilrenCount; i++) {
            View view = recyclerView.getChildAt(i);
            ImageView imageView = (ImageView) view.findViewById(R.id.img_edit);
            if (imageView != null) {
                imageView.setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyChannelHeaderViewHolder) {
            MyChannelHeaderViewHolder myViewHolder = (MyChannelHeaderViewHolder) holder;
            if (isEditMode) {
                myViewHolder.tvBtnEdit.setText(R.string.finish);

            } else {
                myViewHolder.tvBtnEdit.setText(R.string.edit);
            }
        } else if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;

            myViewHolder.textView.setText(mMyChannelItems.get(position - COUNT_PRE_MY_HEADER).getName());

            if (isEditMode) {
                myViewHolder.imageView.setVisibility(View.VISIBLE);

            } else {
                myViewHolder.imageView.setVisibility(View.GONE);
            }
        } else if (holder instanceof OtherViewHolder) {
            ((OtherViewHolder) holder).textView.setText(
                    mOtherChannelItems.get(
                            //当前位置 - 我的频道的item-header的大小
                            position - mMyChannelItems.size() - COUNT_PRE_OTHER_HEADER
                    ).getName()
            );
        }
    }

    @Override
    public int getItemCount() {
        return mMyChannelItems.size() + mOtherChannelItems.size() + COUNT_PRE_OTHER_HEADER;
    }

    /**
     * position=0 标题item
     * position>0 position<(其他频道的列表大小) 我的频道部分
     * position=我的频道加一(其它频道的标题item)
     * position>(其它频道的标题)
     * <p>
     * 图例:
     * 标题
     * 我的频道
     * (这里可能有n个其它频道,或者只有一个其它频道,区别对待)
     * 其它频道标题
     * 其它频道
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_MY_CHANNEL_HEADER;
        } else if (position > 0 && position <= mMyChannelItems.size()) {
            return TYPE_MY;
        } else if (position == mMyChannelItems.size() + 1) {
            return TYPE_OTHER_CHANNEL_HEADER;
        } else {
            return TYPE_OTHER;
        }

    }
    //android itemtouchhelper默认拖拽事件的实现接口
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        CategoryEntity categoryEntity = mMyChannelItems.get(fromPosition - COUNT_PRE_MY_HEADER);

        mMyChannelItems.remove(fromPosition - COUNT_PRE_MY_HEADER);
        mMyChannelItems.add(toPosition - COUNT_PRE_MY_HEADER, categoryEntity);
        notifyItemMoved(fromPosition, toPosition);
    }


    /**
     * 我的频道适配
     * 拖拽,点击回调
     */
    class MyViewHolder extends RecyclerView.ViewHolder implements OnDragVHListener {
        private TextView textView;
        private ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv);
            imageView = (ImageView) itemView.findViewById(R.id.img_edit);
        }

        @Override
        public void onItemSelected() {
            textView.setBackgroundResource(R.drawable.bg_channel_p);
        }

        @Override
        public void onItemFinish() {
            textView.setBackgroundResource(R.drawable.bg_channel);
        }
    }

    /**
     * 其它频道适配
     */
    class OtherViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public OtherViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv);
        }
    }

    /**
     * 我的频道标题适配
     */
    class MyChannelHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBtnEdit;

        public MyChannelHeaderViewHolder(View itemView) {
            super(itemView);
            tvBtnEdit = (TextView) itemView.findViewById(R.id.tv_btn_edit);
        }
    }

}
