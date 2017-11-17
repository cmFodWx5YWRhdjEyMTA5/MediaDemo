package com.cxria.Media.play;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cxria.Media.BaseActivity;
import com.cxria.Media.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayActivity extends BaseActivity {


    @BindView(R.id.iv_main)
    ImageView mIvMain;
    @BindView(R.id.rl_contain)
    RelativeLayout mRlContain;
    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.sr_refresh)
    SwipeRefreshLayout mSrRefresh;
    @BindView(R.id.drawlayout)
    DrawerLayout mDrawlayout;
    @BindView(R.id.iv_close)
    ImageView mIvClose;

    @Override
    public int getLayout() {
        return R.layout.activity_play;
    }

    @Override
    public void initView() {

    }


    @OnClick({R.id.iv_main, R.id.iv_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_main:
                if (!mDrawlayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawlayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.iv_close:
                mDrawlayout.closeDrawer(Gravity.LEFT);
                break;
        }
    }

}
