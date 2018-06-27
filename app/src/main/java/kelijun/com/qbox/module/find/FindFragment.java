package kelijun.com.qbox.module.find;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import kelijun.com.qbox.R;
import kelijun.com.qbox.base.BaseFragment;
import kelijun.com.qbox.config.Const;
import kelijun.com.qbox.database.FunctionDao;
import kelijun.com.qbox.model.entities.Constellation;
import kelijun.com.qbox.model.entities.DayJoke;
import kelijun.com.qbox.model.entities.FunctionBean;
import kelijun.com.qbox.model.entities.RefreshFindFragmentEvent;
import kelijun.com.qbox.network.Network;
import kelijun.com.qbox.utils.SPUtils;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ${kelijun} on 2018/6/27.
 */

public class FindFragment extends BaseFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    @BindView(R.id.bg_find_find)
    KenBurnsView mBgFind;
    @BindView(R.id.Recycler_find)
    RecyclerView mRecyclerFind;
    @BindView(R.id.xiaohua_find)
    TextView mXiaohuaFind;
    @BindView(R.id.joke_find)
    LinearLayout mJokeFind;
    @BindView(R.id.year_calendar)
    TextView mYearCalendar;
    @BindView(R.id.years_calendar)
    TextView mYearsCalendar;
    @BindView(R.id.day_clendar)
    TextView mDayClendar;
    @BindView(R.id.nongli_calendar)
    TextView mNongliCalendar;
    @BindView(R.id.jieri_calendar)
    TextView mJieriCalendar;
    @BindView(R.id.wannianli_find)
    LinearLayout mWannianliFind;
    @BindView(R.id.xz_star_find)
    TextView mXzStarFind;
    @BindView(R.id.qfriend_star_find)
    TextView mQfriendStarFind;
    @BindView(R.id.yunshi_star_find)
    TextView mYunshiStarFind;
    @BindView(R.id.star_find)
    LinearLayout mStarFind;
    @BindView(R.id.thefooter_find)
    LinearLayout mThefooterFind;
    @BindView(R.id.bg_title_find)
    TextView mBgTitleFind;
    @BindView(R.id.before_find)
    ImageButton mBeforeFind;
    @BindView(R.id.next_find)
    ImageButton mNextFind;
    @BindView(R.id.yi_calendar)
    TextView mYiCalendar;
    @BindView(R.id.ji_calendar)
    TextView mJiCalendar;
    @BindView(R.id.week_calendar)
    TextView mWeekCalendar;

    private FindAdapter mFindAdapter;
    private List<FunctionBean> mFindList;
    public ItemDragAndSwipeCallback mItemDragAndSwipeCallback;
    public ItemTouchHelper mItemTouchHelper;

    public Subscription mConstellationSubscription;
    public Subscription mDayJokeSubscribe;

    public FindFragment() {
    }

    public static FindFragment newInstance(String param1, String param2) {
        FindFragment fragment = new FindFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.joke_find:
                // startActivity(new Intent(getContext(), JokeActivity.class));
                break;
            case R.id.star_find:
                //  startActivity(new Intent(getContext(), ConstellationActivity.class));
                break;
            case R.id.wannianli_find:
                // startActivity(new Intent(getContext(), ChinaCalendarActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_find;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);

        initRecycleView();
    }

    private void initRecycleView() {

        mFindList = initData();
        mFindAdapter = new FindAdapter(mFindList);
        mRecyclerFind.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mFindAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerFind);

        mFindAdapter.enableDragItem(mItemTouchHelper);
        mFindAdapter.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                FunctionDao functionDao1 = new FunctionDao(getContext().getApplicationContext());
                List<FunctionBean> data = mFindAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    FunctionBean functionBean = data.get(i);
                    if (functionBean.getId() != i) {
                        functionBean.setId(i);
                        functionDao1.updateFunction(functionBean);

                    }
                }
            }
        });
        mRecyclerFind.setAdapter(mFindAdapter);
        mRecyclerFind.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                String name = ((FunctionBean) adapter.getData().get(position)).getName();
                itemActionEvent(name);
            }
        });
    }

    private void itemActionEvent(String name) {
        switch (name) {
            case "万年历":
                //   startActivity(new Intent(getContext(), ChinaCalendarActivity.class));
                break;
            case "快递查询":
              /*  Intent intent = new Intent(getContext(), Html5Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url","https://m.kuaidi100.com/");
                intent.putExtra("bundle",bundle);
                startActivity(intent);*/
                break;
            case "黄金数据":
                notOpen();
                break;
            case "股票数据":
                notOpen();
                break;
            case "更多":
                //   startActivity(new Intent(getContext(), FindMoreActivity.class));
                break;
            case "身份证查询":
              /*  Intent intent_idcard = new Intent(getContext(), QueryInfoActivity.class);
                intent_idcard.putExtra(QueryInfoActivity.QUERY_STYLE,QueryInfoActivity.QUERY_IDCARD);
                startActivity(intent_idcard);*/
                break;
            case "邮编查询":
                notOpen();
                break;
            case "手机归属地":
               /* Intent intent_tel = new Intent(getContext(), QueryInfoActivity.class);
                intent_tel.putExtra(QueryInfoActivity.QUERY_STYLE,QueryInfoActivity.QUERY_TEL);
                startActivity(intent_tel);*/
                break;
            case "QQ吉凶":
               /* Intent intent_qq = new Intent(getContext(), QueryInfoActivity.class);
                intent_qq.putExtra(QueryInfoActivity.QUERY_STYLE,QueryInfoActivity.QUERY_QQ);
                startActivity(intent_qq);*/
                break;
            case "星座运势":
                //  startActivity(new Intent(getContext(), ConstellationActivity.class));
                break;
            case "周公解梦":
                notOpen();
                break;
            case "汇率":
                notOpen();
                break;
            case "笑话大全":
                // startActivity(new Intent(getContext(), JokeActivity.class));
                break;
            default:
                break;
        }
    }

    public void notOpen() {
        Toast.makeText(getContext(), "该功能现在暂时未开放！", Toast.LENGTH_SHORT).show();
    }

    private List<FunctionBean> initData() {
        FunctionDao functionDao = new FunctionDao(getContext().getApplicationContext());
        return functionDao.queryFunctionBeanListSmall();
    }

    @Override
    protected void managerArguments() {
        mParam1 = getArguments().getString(ARG_PARAM1);
        mParam2 = getArguments().getString(ARG_PARAM2);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBgFind.resume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (mBgTitleFind.getFitsSystemWindows() == false) {
                mBgTitleFind.setFitsSystemWindows(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                    mBgTitleFind.requestApplyInsets();
                }
            }
        }
    }

    private void initBottomContext() {
        boolean starIsOpen = (boolean) SPUtils.get(getContext(), Const.STAR_IS_OPEN, true);
        boolean jokeIsOpen = (boolean) SPUtils.get(getContext(), Const.JOKE_IS_OPEN, true);
        boolean wannianliIsOpen = (boolean) SPUtils.get(getContext(), Const.STUFF_IS_OPEN, true);

        if (!starIsOpen && !jokeIsOpen && !wannianliIsOpen) {
            mThefooterFind.setVisibility(View.GONE);
            return;
        } else {
            if (mThefooterFind.getVisibility() == View.GONE)
                mThefooterFind.setVisibility(View.VISIBLE);
        }

        if (starIsOpen) {
            mStarFind.setVisibility(View.VISIBLE);
            String starName = (String) SPUtils.get(getContext(), Const.USER_STAR, "水瓶座");
            mXzStarFind.setText("-" + starName);
            mStarFind.setOnClickListener(this);
            requestStarData(starName);
        } else {
            mStarFind.setVisibility(View.GONE);
        }

        if (jokeIsOpen) {
            mJokeFind.setVisibility(View.VISIBLE);
            mJokeFind.setOnClickListener(this);
            requestJokeData();
        } else {
            mJokeFind.setVisibility(View.GONE);
        }

        if (wannianliIsOpen) {
            mWannianliFind.setVisibility(View.VISIBLE);
            mWannianliFind.setOnClickListener(this);
            //  requestWannianli();
        } else {
            mWannianliFind.setVisibility(View.GONE);
        }

    }

    private void requestJokeData() {
        unsubscribe("joke");
        mDayJokeSubscribe = Network.getDayJokeApi()
                .getDayJoke("39094c8b40b831b8e7b7a19a20654ed7")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(mDayJokeObserver)
        ;
    }

    private Observer<DayJoke> mDayJokeObserver = new Observer<DayJoke>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Logger.e("每日一笑：" + e.getMessage());
        }

        @Override
        public void onNext(DayJoke dayJoke) {
            if (dayJoke.getError_code() == 0 && dayJoke.getResult() != null && dayJoke.getResult().getData() != null)
                showDayJoke(dayJoke.getResult().getData().get(0));
        }
    };

    private void showDayJoke(DayJoke.ResultBean.DataBean dataBean) {
        String jokeContent = dataBean.getContent();
        if (!TextUtils.isEmpty(jokeContent))
            mXiaohuaFind.setText(jokeContent);
    }

    private Observer<Constellation> mConstellationObserver = new Observer<Constellation>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Logger.e(e.getMessage());
        }

        @Override
        public void onNext(Constellation constellation) {
            if (constellation.getError_code() == 0) {
                showConstellation(constellation);
            }
        }
    };

    private void showConstellation(Constellation constellation) {
        mQfriendStarFind.setText(constellation.getQFriend());
        mYunshiStarFind.setText(constellation.getSummary());
    }

    private void requestStarData(String starName) {
        unsubscribe("star");
        mConstellationSubscription = Network.getConstellationApi()
                .getConstellation(starName, "today", "35de7ea555a8b5d58ce2d7e4f8cb7c9f")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mConstellationObserver)
        ;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRefreshNewsFragmentEvent(RefreshFindFragmentEvent event) {
        if (event.getFlagBig() > 0) {
            initBottomContext();
        }
        if (event.getFlagSmall() > 0) {
            mFindAdapter.setNewData(initData());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mBgFind.pause();

    }

    private void unsubscribe(String string) {
        switch (string) {
            case "bg":
                break;
            case "joke":

                break;
            case "star":
                if (mConstellationSubscription != null && !mConstellationSubscription.isUnsubscribed()) {
                    mConstellationSubscription.unsubscribe();
                }
                break;
            case "chinacalendar":
                break;
            default:
                break;
        }
    }
}
