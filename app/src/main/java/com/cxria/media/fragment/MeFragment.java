package com.cxria.media.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cxria.media.BaseFragment;
import com.cxria.media.MyApplication;
import com.cxria.media.R;
import com.cxria.media.entity.UesrInfo;
import com.cxria.media.utils.ActivityUtils;
import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by yukun on 18-1-4.
 */

public class MeFragment extends BaseFragment {
    @BindView(R.id.rl_collect)
    RelativeLayout mRlCollect;
    @BindView(R.id.rl_adoutus)
    RelativeLayout mRlAdoutus;
    @BindView(R.id.scrollview)
    ScrollView mScrollview;
    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.tv_name)
    TextView mTvName;
    private boolean isLogin;

    public static MeFragment getInstance(Context context) {
        return new MeFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.me_fragment;
    }

    public void getType(boolean isLogin) {
        this.isLogin = isLogin;
        if (!isLogin) {
            mTvLogin.setText("登录");
            mTvName.setText("我的");
        } else {
            mTvLogin.setText("退出登录");
            mTvName.setText(MyApplication.getUesrInfo().getUsername());
        }

    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        OverScrollDecoratorHelper.setUpOverScroll(mScrollview);
        //判断登录与否
        if (MyApplication.getUesrInfo() == null) {
            isLogin = false;
        } else {
            isLogin = true;
        }
        if(MyApplication.getUesrInfo()!=null){
            Log.i("－－user",MyApplication.getUesrInfo().toString());
        }

        if (!isLogin) {
            mTvLogin.setText("登录");
            mTvName.setText("我的");
        } else {
            mTvName.setText(MyApplication.getUesrInfo().getUsername());
            mTvLogin.setText("退出登录");
        }
        try {
            PackageManager pm = getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getContext().getPackageName(), 0);
            String versionName = pi.versionName;
            mTvVersion.setText("V " + versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.rl_collect, R.id.rl_adoutus, R.id.tv_login, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_collect:
                if (!isLogin) {
                    ActivityUtils.startLoginActivity(getContext());
                } else {
                    ActivityUtils.startCollectActivity(getContext());
                }
                break;
            case R.id.rl_adoutus:
                AboutUsWanFragment aboutUsFragment = new AboutUsWanFragment();
                aboutUsFragment.show(getChildFragmentManager(), "");
                break;
            case R.id.tv_login:
                if (!isLogin) {
                    ActivityUtils.startLoginActivity(getContext());
                } else {
                    DataSupport.deleteAll(UesrInfo.class);
                    MyApplication.uesrInfo = null;
                    SharedPreferences.Editor editor = getContext().getSharedPreferences("cookie",Context.MODE_PRIVATE).edit();
                    editor.clear();
                    editor.commit();
                    ((Activity) getContext()).finish();
                }
                break;
            case R.id.iv_back:
                ((Activity) getContext()).finish();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
