package kelijun.com.qbox.module.find;

import kelijun.com.qbox.R;
import kelijun.com.qbox.base.BaseCommonActivity;

public class QueryInfoActivity extends BaseCommonActivity {
    public static final String QUERY_IDCARD_KEY = "220f329adbf071c81b4b3011e0439cad";
    public static final String QUERY_QQ_KEY = "27100330526ba52940ecc6846436c398";
    public static final String QUERY_TEL_KEY = "576f995fdbc2c0e9db5fa785da34efd6";

    public static final String QUERY_STYLE="style";
    public static final int QUERY_TEL = 1;
    public static final int QUERY_QQ =2;
    public static final int QUERY_IDCARD = 3;

    public static final String[] sQueryStyleName = {"手机号查询","QQ吉凶查询","身份证号查询"};
    @Override
    public void initContentView() {
        setContentView(R.layout.activity_query_info);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initPresenter() {

    }

}
