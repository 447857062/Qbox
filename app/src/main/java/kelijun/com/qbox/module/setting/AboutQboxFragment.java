package kelijun.com.qbox.module.setting;


import kelijun.com.qbox.R;
import kelijun.com.qbox.base.BaseFragment;




public class AboutQboxFragment extends BaseFragment{


    public AboutQboxFragment() {
    }

    public static AboutQboxFragment newInstance() {
        AboutQboxFragment fragment = new AboutQboxFragment();
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_about_qbox;
    }

    @Override
    public void initView() {

    }

    @Override
    protected void managerArguments() {

    }



}
