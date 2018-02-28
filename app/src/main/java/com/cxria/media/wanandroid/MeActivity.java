package com.cxria.media.wanandroid;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;


import com.cxria.media.BaseActivity;
import com.cxria.media.R;
import com.cxria.media.entity.EventLoginType;
import com.cxria.media.fragment.MeFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MeActivity extends BaseActivity {

    private MeFragment mMeFragment;

    @Override
    public int getLayout() {
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        return R.layout.activity_me;
    }

    @Override
    public void initView() {
        mMeFragment = MeFragment.getInstance(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fr_contain, mMeFragment);
        fragmentTransaction.commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLoginType(EventLoginType eventLoginType) {
        if (eventLoginType.logintype == 1) {
            mMeFragment.getType(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
