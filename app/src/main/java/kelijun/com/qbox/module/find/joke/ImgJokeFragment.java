package kelijun.com.qbox.module.find.joke;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import kelijun.com.qbox.R;
import kelijun.com.qbox.base.BaseFragment;
import kelijun.com.qbox.model.entities.RefreshJokeStyleEvent;

/**
 * Created by ${kelijun} on 2018/6/28.
 */

public class ImgJokeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private static final String JOKE_KEY ="39094c8b40b831b8e7b7a19a20654ed7";


    //每页请求的 item 数量
    public final int mPs = 20;
    public int mPageMark = 1;
    public boolean mRefreshMark;

    int jokestyle;

    private View notDataView;
    private View errorView;

    ImgJokeAdapter textJokeAdapter;
    public static ImgJokeFragment newInstance(String param1, String param2) {
        ImgJokeFragment fragment = new ImgJokeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ImgJokeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onRefresh() {

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_joke;
    }

    @Override
    public void initView() {
        initSwiper();
        initEmptyView();
        initRecy();
        onLoading();
        requestData();
    }

    private void requestData() {

    }

    private void onLoading() {

    }

    private void initRecy() {

    }

    private void initEmptyView() {

    }

    private void initSwiper() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRefreshNewsFragmentEvent(RefreshJokeStyleEvent event) {
        jokestyle = event.getJokeStyle();
        onRefresh();
    }
    @Override
    protected void managerArguments() {

    }
}
