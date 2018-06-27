package kelijun.com.qbox.module.news;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import kelijun.com.qbox.R;
import kelijun.com.qbox.base.BaseFragment;
import kelijun.com.qbox.config.Const;
import kelijun.com.qbox.model.entities.NewsItem;
import kelijun.com.qbox.model.entities.WechatItem;
import kelijun.com.qbox.module.NewsDetailsActivity.NewsDetailsActivity;
import kelijun.com.qbox.network.Network;
import kelijun.com.qbox.utils.PixelUtil;
import kelijun.com.qbox.utils.SPUtils;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ${kelijun} on 2018/6/26.
 */

public class DefaultStyleFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{
    private static final String ARG_PARAM1 = "param1";

    @BindView(R.id.news_list)
    RecyclerView mNewsList;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private View notDataView;
    private View errorView;
    private String mParam1;
    int mPage = 1;
    // 最多加载的条目个数
    private static final int TOTAL_COUNTER = 30;
    private Subscription mSubscription;
    BaseQuickAdapter baseQuickAdapter;
    private List<NewsItem.ResultBean.DataBean> mNewsItemList;
    // 当前列表的item个数
    int mCurrentCounter;

    private int delayMillis = 1000;
    public DefaultStyleFragment() {
    }

    public static DefaultStyleFragment newInstance(String param1, String param2) {
        DefaultStyleFragment fragment = new DefaultStyleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onRefresh() {
        baseQuickAdapter.setEnableLoadMore(false);
        onRequestAgain();
    }

    private void onRequestAgain() {
        mPage++;
        requestNews();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_default_style;
    }

    @Override
    public void initView() {
        initSwipeRefresh();
        initEmptyView();
        initRecyclerView();
        onLoading();

        requestNews();
    }

    private void requestNews() {
        unsubscribe();

        mSubscription= Network.getWecheatApi()
                .getWechat(mParam1,mPage,TOTAL_COUNTER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }
    private void onErrorView() {
        baseQuickAdapter.setEmptyView(errorView);
    }
    Observer<WechatItem>mObserver=new Observer<WechatItem>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Logger.e("onError" + e.getMessage());
            onErrorView();
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);

            }
            if (mSwipeRefreshLayout.isEnabled()) {
                mSwipeRefreshLayout.setEnabled(false);
            }
        }

        @Override
        public void onNext(WechatItem wechatItem) {
            setNewDataAddList(wechatItem);
        }
    };

    private void setNewDataAddList(WechatItem wechatItem) {
        if (wechatItem != null && "200".equals(wechatItem.getRetCode())) {
            baseQuickAdapter.setNewData(wechatItem.getResult().getList());
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if (!mSwipeRefreshLayout.isEnabled()) {
                mSwipeRefreshLayout.setEnabled(true);

            }

            if (!baseQuickAdapter.isLoadMoreEnable()) {
                baseQuickAdapter.setEnableLoadMore(true);
            }

        } else {
            baseQuickAdapter.setEmptyView(notDataView);
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }
    }

    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();

        }
    }

    private void onLoading() {
        baseQuickAdapter.setEmptyView(R.layout.loading_view, (ViewGroup) mNewsList.getParent());
    }

    private void initRecyclerView() {
        mNewsList.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        mNewsItemList = new ArrayList<>();
        boolean isNotLoad = (boolean) SPUtils.get(getContext(), Const.SLLMS, false);
        int imgWidth = (PixelUtil.getWindowWidth() - PixelUtil.dp2px(40)) / 3;
        int imgHeight = imgWidth / 4 * 3;

        baseQuickAdapter = new DefaultStyleItemAdapter(R.layout.news_item_default, isNotLoad, imgWidth, imgHeight);
        baseQuickAdapter.openLoadAnimation();
        baseQuickAdapter.setOnLoadMoreListener(this);


        mNewsList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<WechatItem.ResultBean.ListBean> data = adapter.getData();

                Bundle bundle = new Bundle();
                bundle.putString("url", data.get(position).getSourceUrl());
                bundle.putString("title", data.get(position).getTitle());
                Intent intent = new Intent(getContext(), NewsDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mNewsList.setAdapter(baseQuickAdapter);
        mCurrentCounter = baseQuickAdapter.getData().size();
    }

    private void initEmptyView() {

    }

    private void initSwipeRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mSwipeRefreshLayout.setEnabled(false);
    }

    @Override
    protected void managerArguments() {
        mParam1 = getArguments().getString(ARG_PARAM1);
    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeRefreshLayout.setEnabled(false);
        mNewsList.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCurrentCounter >= TOTAL_COUNTER) {
                    baseQuickAdapter.loadMoreEnd();
                } else {
                    baseQuickAdapter.addData(mNewsItemList);
                    mCurrentCounter = baseQuickAdapter.getData().size();
                    baseQuickAdapter.loadMoreComplete();//加载完成。
                }
                mSwipeRefreshLayout.setEnabled(true);
            }
        }, delayMillis);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }
}
