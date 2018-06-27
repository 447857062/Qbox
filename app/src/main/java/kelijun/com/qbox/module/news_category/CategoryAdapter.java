package kelijun.com.qbox.module.news_category;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.GridLayoutManager;
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
                            RecyclerView recyclerView = (RecyclerView) parent;

                            View targetView = recyclerView.getLayoutManager().findViewByPosition(mMyChannelItems.size() + COUNT_PRE_OTHER_HEADER);
                            //如果目标view在屏幕内,或者不在屏幕内
                            //在屏幕内需要手动添加一个动画
                            if (recyclerView.indexOfChild(targetView) >= 0) {
                                int targetX, targetY;
                                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                                int spanCount = ((GridLayoutManager) layoutManager).getSpanCount();

                                if ((mMyChannelItems.size() - COUNT_PRE_MY_HEADER) % spanCount == 0) {
                                    View preTargetView = recyclerView.getLayoutManager().findViewByPosition(mMyChannelItems.size() + COUNT_PRE_OTHER_HEADER - 1);
                                    targetX = preTargetView.getLeft();
                                    targetY = preTargetView.getTop();
                                } else {
                                    targetX = targetView.getLeft();
                                    targetY = targetView.getTop();
                                }
                                moveMyToOther(myViewHolder);
                            } else {
                                moveMyToOther(myViewHolder);
                            }
                        } else {
                            mChannelItemClickListener.onItemClick(v, position - COUNT_PRE_MY_HEADER);
                        }

                    }
                });
                myViewHolder.textView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        if (!isEditMode) {
                            RecyclerView recyclerView = (RecyclerView) parent;
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
                                    startTime = System.currentTimeMillis();
                                    break;
                                case MotionEvent.ACTION_CANCEL:
                                case MotionEvent.ACTION_UP:
                                    startTime = 0;
                                    break;
                            }
                        }
                        return false;
                    }
                });
                return myViewHolder;
            case TYPE_MY_CHANNEL_HEADER:
                view = mInflater.inflate(R.layout.item_my_channel_header, parent, false);
                final MyChannelHeaderViewHolder myChannelHeaderViewHolder = new MyChannelHeaderViewHolder(view);
                myChannelHeaderViewHolder.tvBtnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isEditMode) {
                            startEditMode((RecyclerView) parent);
                            myChannelHeaderViewHolder.tvBtnEdit.setText(R.string.finish);

                        }else {
                            cancelEditMode((RecyclerView) parent);
                            myChannelHeaderViewHolder.tvBtnEdit.setText(R.string.edit);
                        }
                    }
                });
                return myChannelHeaderViewHolder;
            case TYPE_OTHER:
                view = mInflater.inflate(R.layout.item_other, parent, false);
                final OtherViewHolder otherViewHolder = new OtherViewHolder(view);
                otherViewHolder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecyclerView recyclerView = (RecyclerView) parent;
                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                        int currentPosition = otherViewHolder.getAdapterPosition();
                        //如果recycleview滑动到底部,移动的目标位置的Y轴-height

                        //当前我的频道的最后一个
                        View preTargetView = layoutManager.findViewByPosition(mMyChannelItems.size() - 1 + COUNT_PRE_MY_HEADER);

                        // 如果targetView不在屏幕内,则为-1  此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
                        // 如果在屏幕内,则添加一个位移动画
                        if (recyclerView.indexOfChild(preTargetView) >= 0) {
                            int targetX = preTargetView.getLeft();
                            int targetY = preTargetView.getTop();

                            int targetPosition = mMyChannelItems.size() - 1 + COUNT_PRE_OTHER_HEADER;

                            GridLayoutManager gridLayoutManager = ((GridLayoutManager) layoutManager);
                            int spanCount = gridLayoutManager.getSpanCount();
                            // target 在最后一行第一个
                            if ((targetPosition - COUNT_PRE_MY_HEADER) % spanCount == 0) {
                                View targetView = layoutManager.findViewByPosition(targetPosition);
                                targetX = targetView.getLeft();
                                targetY = targetView.getTop();
                            } else {
                                targetX += preTargetView.getWidth();
                                if (gridLayoutManager.findLastVisibleItemPosition() == getItemCount() - 1) {
                                    // 最后的item在最后一行第一个位置
                                    if ((getItemCount() - 1 - mMyChannelItems.size() - COUNT_PRE_OTHER_HEADER) % spanCount == 0) {
                                        // RecyclerView实际高度 > 屏幕高度 && RecyclerView实际高度 < 屏幕高度 + item.height
                                        int firstVisiblePostion = gridLayoutManager.findFirstVisibleItemPosition();
                                        if (firstVisiblePostion == 0) {
                                            // FirstCompletelyVisibleItemPosition == 0 即 内容不满一屏幕 , targetY值不需要变化
                                            // // FirstCompletelyVisibleItemPosition != 0 即 内容满一屏幕 并且 可滑动 , targetY值 + firstItem.getTop
                                            if (gridLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) {
                                                int offset = (-recyclerView.getChildAt(0).getTop()) - recyclerView.getPaddingTop();
                                                targetY += offset;
                                            }
                                        } else { // 在这种情况下 并且 RecyclerView高度变化时(即可见第一个item的 position != 0),
                                            // 移动后, targetY值  + 一个item的高度
                                            targetY += preTargetView.getHeight();
                                        }
                                    }
                                } else {
                                    System.out.println("current--No");
                                }
                            }
                            // 如果当前位置是otherChannel可见的最后一个
                            // 并且 当前位置不在grid的第一个位置
                            // 并且 目标位置不在grid的第一个位置

                            // 则 需要延迟250秒 notifyItemMove , 这是因为这种情况 , 并不触发ItemAnimator , 会直接刷新界面
                            // 导致我们的位移动画刚开始,就已经notify完毕,引起不同步问题
                            if (currentPosition == gridLayoutManager.findLastVisibleItemPosition()
                                    && (currentPosition - mMyChannelItems.size() - COUNT_PRE_OTHER_HEADER) % spanCount != 0
                                    && (targetPosition - COUNT_PRE_MY_HEADER) % spanCount != 0) {
                                moveOtherToMyWithDelay(otherViewHolder);
                            } else {
                                moveOtherToMy(otherViewHolder);
                            }
                           // startAnimation(recyclerView, currentView, targetX, targetY);
                        } else {
                            moveOtherToMy(otherViewHolder);
                        }
                    }
                });
                return otherViewHolder;
            case TYPE_OTHER_CHANNEL_HEADER:
                view = mInflater.inflate(R.layout.item_other_channel_header, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            default:
                break;
        }
        return null;
    }

    private static final long ANIM_TIME = 360L;
    /**
     * 其他频道 移动到 我的频道 伴随延迟
     *
     * @param otherHolder
     */
    private void moveOtherToMyWithDelay(OtherViewHolder otherHolder) {
        final int position = processItemRemoveAdd(otherHolder);
        if (position == -1) {
            return;
        }
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyItemMoved(position, mMyChannelItems.size() - 1 + COUNT_PRE_MY_HEADER);
            }
        }, ANIM_TIME);
    }

    private Handler delayHandler = new Handler();
    /**
     * 其他频道 移动到 我的频道
     *
     * @param otherHolder
     */
    private void moveOtherToMy(OtherViewHolder otherHolder) {
        int position = processItemRemoveAdd(otherHolder);
        if (position == -1) {
            return;
        }
        notifyItemMoved(position, mMyChannelItems.size() - 1 + COUNT_PRE_MY_HEADER);
    }
    private int processItemRemoveAdd(OtherViewHolder otherHolder) {
        int position = otherHolder.getAdapterPosition();

        int startPosition = position - mMyChannelItems.size() - COUNT_PRE_OTHER_HEADER;
        if (startPosition > mOtherChannelItems.size() - 1) {
            return -1;
        }
        CategoryEntity item = mOtherChannelItems.get(startPosition);
        mOtherChannelItems.remove(startPosition);
        mMyChannelItems.add(item);
        return position;
    }
    /**
     * 完成编辑模式
     *
     * @param parent
     */
    private void cancelEditMode(RecyclerView parent) {
        isEditMode = false;
        int visibleChildCount = parent.getChildCount();
        for (int i = 0; i < visibleChildCount; i++) {
            View view = parent.getChildAt(i);
            ImageView imgEdit = (ImageView) view.findViewById(R.id.img_edit);
            if (imgEdit != null) {
                imgEdit.setVisibility(View.INVISIBLE);
            }
        }
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
     *
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
