package kelijun.com.qbox.module.wechat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import kelijun.com.qbox.R;
import kelijun.com.qbox.base.BaseFragment;
import kelijun.com.qbox.config.Const;
import kelijun.com.qbox.model.entities.WechatItem;
import kelijun.com.qbox.network.Network;
import kelijun.com.qbox.utils.PixelUtil;
import kelijun.com.qbox.utils.SPUtils;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ${kelijun} on 2018/6/27.
 */

public class WechatFragment extends BaseFragment implements BaseSectionQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //每页请求的 item 数量
    public final int mPs = 21;
    public int mPageMark = 1;
    public boolean mRefreshMark;
    private String mParam1;
    private String mParam2;

    @BindView(R.id.recycler_wechat)
    RecyclerView mRecyclerWechat;
    @BindView(R.id.swiper_wechat)
    SwipeRefreshLayout mSwiperWechat;
    @BindView(R.id.floatingactionbutton_wechat)
    FloatingActionButton mFloatingActionButton;

    private WechatItemAdapter mWechatItemAdapter;
    private List<WechatItem.ResultBean.ListBean> mListBeanList;

    private Subscription mSubscription;
    private View errorView;
    private View notDataView;
    public WechatFragment() {
    }

    public static WechatFragment newInstance(String param1, String param2) {
        WechatFragment fragment = new WechatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRefresh() {
        mWechatItemAdapter.setEnableLoadMore(false);
        mRefreshMark = true;
        mPageMark = 1;
        requestData();
    }

    @Override
    public void onLoadMoreRequested() {
        mSwiperWechat.setEnabled(false);
        requestData();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_wechat;
    }

    @Override
    public void initView() {
        initFAB();
        initSwipeRefresh();
        initEmptyView();
        initRecyclerWechat();
        onLoading();
        requestData();
    }

    private void requestData() {
        unsubscribe();
        mSubscription = Network.getWecheatApi()
                .getWechat("8", mPageMark, mPs).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(mObserver);
    }

    Observer<WechatItem> mObserver = new Observer<WechatItem>() {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Logger.e("onError:" + e.getMessage());
            onErrorView();
            if (mSwiperWechat.isRefreshing()) {
                mSwiperWechat.setRefreshing(false);
            }
            if (mSwiperWechat.isEnabled()) {
                mSwiperWechat.setEnabled(false);
            }
        }

        @Override
        public void onNext(WechatItem wechatItem) {
            setNewDataAddList(wechatItem);
        }
    };

    private void setNewDataAddList(WechatItem wechatItem) {
        if (wechatItem != null && "200".equals(wechatItem.getRetCode())) {
            mPageMark++;
            List<WechatItem.ResultBean.ListBean> newData = wechatItem.getResult().getList();
            WechatItem.ResultBean.ListBean listBean = newData.get(0);
            listBean.setItemType(1);
            listBean.setSpanSize(2);
            if (mRefreshMark) {
                mWechatItemAdapter.setNewData(newData);
                mRefreshMark = false;
            } else {
                mWechatItemAdapter.addData(newData);
            }

            if (mSwiperWechat.isRefreshing()) {
                mSwiperWechat.setRefreshing(false);
            }
            if (!mSwiperWechat.isEnabled()) {
                mSwiperWechat.setEnabled(true);
            }
            if (mWechatItemAdapter.isLoading()) {
                mWechatItemAdapter.loadMoreComplete();
            }
            if (!mWechatItemAdapter.isLoadMoreEnable()) {
                mWechatItemAdapter.setEnableLoadMore(true);
            }
        } else {
            if (mWechatItemAdapter.isLoading()) {
                Toast.makeText(getContext(), R.string.load_more_error, Toast.LENGTH_SHORT).show();
                mWechatItemAdapter.loadMoreFail();
            } else {
                mWechatItemAdapter.setEmptyView(notDataView);
                if (mSwiperWechat.isRefreshing()) {
                    mSwiperWechat.setRefreshing(false);
                }
                if (mSwiperWechat.isEnabled()) {
                    mSwiperWechat.setEnabled(false);
                }
            }
        }
    }

    private void onErrorView() {
        mWechatItemAdapter.setEmptyView(errorView);
    }
    private void onLoading() {
        mWechatItemAdapter.setEmptyView(R.layout.loading_view, (ViewGroup) mRecyclerWechat.getParent());
    }

    private void initRecyclerWechat() {
        mRecyclerWechat.setLayoutManager(new GridLayoutManager(getContext().getApplicationContext(), 2));
        mListBeanList = new ArrayList<>();
        boolean isNotLoad = (boolean) SPUtils.get(getContext(), Const.SLLMS, false);
        int imgWidth = PixelUtil.getWindowWidth();
        int imgHeight = imgWidth * 3 / 4;
        mWechatItemAdapter = new WechatItemAdapter(mListBeanList, isNotLoad, imgWidth, imgHeight);
        mWechatItemAdapter.openLoadAnimation();
        mWechatItemAdapter.setOnLoadMoreListener(this);

        mWechatItemAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                if (position == 0) {
                    return 2;
                } else {
                    return mWechatItemAdapter.getData().get(position).getSpanSize();
                }
            }
        });
        mRecyclerWechat.setAdapter(mWechatItemAdapter);
        mRecyclerWechat.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                WechatItem.ResultBean.ListBean listBean = (WechatItem.ResultBean.ListBean) adapter.getData().get(position);

                Intent intent = new Intent(getContext(), WeChatDetailsActivity.class);
                intent.putExtra("wechat", listBean);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        view.findViewById(R.id.img_wechat_style),
                        getString(R.string.transition_wechat_img)
                );

                ActivityCompat.startActivity((Activity) getContext(), intent, optionsCompat.toBundle());
            }
        });
    }

    private void initEmptyView() {
        /**
         * 网络请求失败没有数据
         */
        notDataView = getActivity().getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) mRecyclerWechat.getParent(), false);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onRequestAgain();
            }
        });

        /**
         * 网络请求错误 | 没有网络
         */
        errorView = getActivity().getLayoutInflater().inflate(R.layout.error_view, (ViewGroup) mRecyclerWechat.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onRequestAgain();
            }
        });
    }

    private void initSwipeRefresh() {
        mSwiperWechat.setOnRefreshListener(this);
        mSwiperWechat.setColorSchemeColors(Color.rgb(47, 223, 189));
        mSwiperWechat.setEnabled(false);
    }

    private void initFAB() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWechatItemAdapter != null && mWechatItemAdapter.getData() != null && mWechatItemAdapter.getData().size() > 0) {
                    mRecyclerWechat.scrollToPosition(0);
                }
            }
        });
    }

    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    protected void managerArguments() {
        mParam1 = getArguments().getString(ARG_PARAM1);
        mParam2 = getArguments().getString(ARG_PARAM2);
    }
}
