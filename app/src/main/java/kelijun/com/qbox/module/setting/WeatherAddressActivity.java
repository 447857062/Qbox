package kelijun.com.qbox.module.setting;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import kelijun.com.qbox.R;
import kelijun.com.qbox.base.BaseCommonActivity;
import kelijun.com.qbox.model.entities.City;
import kelijun.com.qbox.module.me.AddressActivity;
import kelijun.com.qbox.module.me.weather.AreaSetAdapter;
import kelijun.com.qbox.module.me.weather.weather.api.ApiManager;
import kelijun.com.qbox.utils.SPUtils;

public class WeatherAddressActivity extends BaseCommonActivity {
    @BindView(R.id.recy_area_set)
    RecyclerView mRecyAreaSet;
    @BindView(R.id.fab_area_set)
    FloatingActionButton mFabAreaSet;

    AreaSetAdapter mAreaSetAdapter;
    ArrayList<ApiManager.Area> mSelectedAreas;

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_weather_address);
    }

    @Override
    public void initView() {
        initToolbar();
        EventBus.getDefault().register(this);

        initRecy();
    }
    private void initRecy() {
        mSelectedAreas = ApiManager.loadSelectedArea(this);
        Logger.e("ms"+mSelectedAreas.size());
        mRecyAreaSet.setLayoutManager(new LinearLayoutManager(this));
        mAreaSetAdapter = new AreaSetAdapter(mSelectedAreas, getResources().getStringArray(R.array.bgimg));

        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAreaSetAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mRecyAreaSet);
        // 开启滑动删除
        mAreaSetAdapter.enableSwipeItem();
        mAreaSetAdapter.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                ApiManager.Area area = (ApiManager.Area) mSelectedAreas.get(pos);
                Logger.e(area.getCity());
                removeAddress(area);
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

            }
        });

        mRecyAreaSet.setAdapter(mAreaSetAdapter);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_weatheraddress);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeAddress(ApiManager.Area area) {
        List<ApiManager.Area> areas = new ArrayList<>();
        String s = (String) SPUtils.get(this, ApiManager. KEY_SELECTED_AREA, "");

        if (!TextUtils.isEmpty(s)) {
            ApiManager.Area[] aa = new Gson().fromJson(s, ApiManager.Area[].class);
            if (aa != null) {
                Collections.addAll(areas, aa);
            }
        }

        for (ApiManager.Area areaitem : areas) {
            if (area.getId().equals(areaitem.getId())) {
                areas.remove(areaitem);
                break;
            }
        }
        SPUtils.put(this, ApiManager. KEY_SELECTED_AREA, new Gson().toJson(areas));
    }

    @Override
    public void initPresenter() {

    }
    @OnClick(R.id.fab_area_set)
    public void onViewClicked() {
        if (mAreaSetAdapter.getData().size() < 3) {
            startActivity(new Intent(this, AddressActivity.class));
        } else {
            Toast.makeText(this, "对不起！最多支持添加3个地点。", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onEventCome(City.HeWeather5Bean.BasicBean BasicBean) {

        List<ApiManager.Area> areas = new ArrayList<>();
        String s = (String) SPUtils.get(this, ApiManager. KEY_SELECTED_AREA, "");
        if (!TextUtils.isEmpty(s)) {
            ApiManager.Area[] aa = new Gson().fromJson(s, ApiManager.Area[].class);
            if (aa != null) {
                Collections.addAll(areas, aa);
            }
        }

        boolean flag = false;
        for (ApiManager.Area area : areas) {
            if (BasicBean.getId().equals(area.getId())) {
                flag = true;
                break;
            }
        }

        ApiManager.Area e = new ApiManager.Area();

        if (!flag) {
            e.setCity(BasicBean.getCity());
            e.setProvince(BasicBean.getProv());
            e.setId(BasicBean.getId());
            e.setName_cn(BasicBean.getCity());
            areas.add(e);
        }

        SPUtils.put(this,ApiManager. KEY_SELECTED_AREA, new Gson().toJson(areas));
        initRecy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
