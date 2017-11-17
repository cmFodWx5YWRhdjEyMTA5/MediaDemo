package com.cxria.Media.play;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxria.Media.BaseActivity;
import com.cxria.Media.R;
import com.cxria.Media.fragment.RecFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PlayActivity extends BaseActivity {


    @BindView(R.id.iv_main)
    ImageView mIvMain;
    @BindView(R.id.rl_contain)
    RelativeLayout mRlContain;
    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.drawlayout)
    DrawerLayout mDrawlayout;
    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.head)
    CircleImageView mHead;
    List<Fragment> mFragments = new ArrayList<>();
    @BindView(R.id.im_snow)
    ImageView mImSnow;
    @BindView(R.id.rl_main)
    RelativeLayout mRlMain;
    @BindView(R.id.im_bird)
    ImageView mImBird;
    @BindView(R.id.rl_movie)
    RelativeLayout mRlMovie;
    @BindView(R.id.im_modu)
    ImageView mImModu;
    @BindView(R.id.rl_change_modul)
    RelativeLayout mRlChangeModul;
    @BindView(R.id.im_ball)
    ImageView mImBall;
    @BindView(R.id.rl_me)
    RelativeLayout mRlMe;
    @BindView(R.id.tv_close)
    TextView mTvClose;
    private MViewPagerAdapter mMViewPagerAdapter;
    private String[] mStringArray;

    @Override
    public int getLayout() {
        return R.layout.activity_play;
    }

    @Override
    public void initView() {
        mStringArray = getResources().getStringArray(R.array.title);
        for (int i = 0; i < mStringArray.length; i++) {
            mTablayout.addTab(mTablayout.newTab().setText(mStringArray[i]));
        }
        RecFragment instance = RecFragment.getInstance();
        RecFragment instance2 = RecFragment.getInstance();
        RecFragment instance3 = RecFragment.getInstance();
        RecFragment instance4 = RecFragment.getInstance();

        mFragments.add(instance);
        mFragments.add(instance2);
        mFragments.add(instance3);
        mFragments.add(instance4);
        setAdapter();
        setListener();
    }

    private void setAdapter() {
        mMViewPagerAdapter = new MViewPagerAdapter(getSupportFragmentManager(), mFragments, mStringArray);
        mViewpager.setAdapter(mMViewPagerAdapter);
    }

    private void setListener() {
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTablayout.setScrollPosition(position, 0, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTablayout.setupWithViewPager(mViewpager);
    }


    @OnClick({R.id.iv_main, R.id.iv_close,R.id.rl_collect,R.id.rl_main, R.id.rl_movie, R.id.rl_change_modul, R.id.rl_me, R.id.tv_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_main:
                if (!mDrawlayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawlayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.iv_close:
                closeDrawLayout();
                break;
            case R.id.rl_main:
                closeDrawLayout();
                break;
            case R.id.rl_movie:
                closeDrawLayout();
                break;
            case R.id.rl_change_modul:
                closeDrawLayout();
                break;
            case R.id.rl_collect:
                closeDrawLayout();
                break;
            case R.id.rl_me:
                closeDrawLayout();
                break;
            case R.id.tv_close:
                closeDrawLayout();
                finish();
                break;
        }
    }

    private void closeDrawLayout() {
        mDrawlayout.closeDrawer(Gravity.LEFT);
    }
}
