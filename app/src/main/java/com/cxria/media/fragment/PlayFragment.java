package com.cxria.media.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cxria.media.BaseFragment;
import com.cxria.media.R;
import com.cxria.media.entity.EventCategrayPos;
import com.cxria.media.play.ADVActivity;
import com.cxria.media.play.AboutUsActivity;
import com.cxria.media.play.ChatActivity;
import com.cxria.media.play.HistoryTodayActivity;
import com.cxria.media.play.MViewPagerAdapter;
import com.cxria.media.play.MyCollectActivity;
import com.cxria.media.play.PlayMainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by yukun on 18-1-2.
 */

public class PlayFragment extends BaseFragment {
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
    @BindView(R.id.iv_setting)
    ImageView mIvSetting;
    @BindView(R.id.rl_me)
    RelativeLayout mRlMe;
    @BindView(R.id.tv_close)
    TextView mTvClose;
    @BindView(R.id.sc_contain)
    ScrollView mScrollview;
    @BindView(R.id.iv_chat)
    ImageView mIvChat;
    @BindView(R.id.iv_collect)
    ImageView mIvCollect;
    @BindView(R.id.rl_collect)
    RelativeLayout mRlCollect;
    private MViewPagerAdapter mMViewPagerAdapter;
    private String[] mStringArray;
    List<Fragment> mFragments = new ArrayList<>();
    private VideoFragment mInstance1;
    private ImageFragment mInstance3;
    private JokeFragment mInstance4;
    private TextFragment mInstance5;

    public static PlayFragment getInstance() {
        PlayFragment playFragment = new PlayFragment();
        return playFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_play;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mStringArray = getResources().getStringArray(R.array.title);
        for (int i = 0; i < mStringArray.length; i++) {
            mTablayout.addTab(mTablayout.newTab().setText(mStringArray[i]));
        }
        RecFragment instance = RecFragment.getInstance();
        mInstance1 = VideoFragment.getInstance();
        SpecialTxtFragment instance2 = SpecialTxtFragment.getInstance();
        mInstance3 = ImageFragment.getInstance();
        mInstance4 = JokeFragment.getInstance();
        mInstance5 = TextFragment.getInstance();
        mFragments.add(instance);
        mFragments.add(mInstance1);
        mFragments.add(instance2);
        mFragments.add(mInstance3);
        mFragments.add(mInstance4);
        mFragments.add(mInstance5);
        setAdapter();
        setListener();
        OverScrollDecoratorHelper.setUpOverScroll(mScrollview);
    }

    private void setAdapter() {
        mMViewPagerAdapter = new MViewPagerAdapter(getChildFragmentManager(), mFragments, mStringArray);
        mViewpager.setAdapter(mMViewPagerAdapter);
        mViewpager.setOffscreenPageLimit(3);
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
                if (position == 0 || position == 2 || position == 3 || position == 4) {
                    mIvSetting.setVisibility(View.GONE);
                } else {
                    mIvSetting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTablayout.setupWithViewPager(mViewpager);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventCategrayPos event) {
        /* Do something */
        if (event.pos < 1000) {
            mViewpager.setCurrentItem(event.pos);
        } else {
            switch (event.pos) {
                //横向列表
                case 1001:
                    if (mViewpager.getCurrentItem() == 1) {
                        mInstance1.getLayoutTag(true);
                    }
                    if (mViewpager.getCurrentItem() == 3) {
                        mInstance3.getLayoutTag(true);
                    }
                    if (mViewpager.getCurrentItem() == 4) {
                        mInstance4.getLayoutTag(true);
                    }
                    if (mViewpager.getCurrentItem() == 5) {
                        mInstance5.getLayoutTag(true);
                    }
                    break;
                //格子列表
                case 1002:
                    if (mViewpager.getCurrentItem() == 1) {
                        mInstance1.getLayoutTag(false);
                    }
                    if (mViewpager.getCurrentItem() == 3) {
                        mInstance3.getLayoutTag(false);
                    }
                    if (mViewpager.getCurrentItem() == 4) {
                        mInstance4.getLayoutTag(false);
                    }
                    if (mViewpager.getCurrentItem() == 5) {
                        mInstance5.getLayoutTag(false);
                    }
                    break;
            }
        }
    }

    @OnClick({R.id.iv_chat, R.id.iv_setting, R.id.iv_main, R.id.head, R.id.iv_close, R.id.rl_collect, R.id.rl_main, R.id.rl_movie, R.id.rl_change_modul, R.id.rl_me, R.id.tv_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_main:
                if (!mDrawlayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawlayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.iv_close:
                Intent intent=new Intent(getContext(), ADVActivity.class);
                startActivity(intent);
                closeDrawLayout();
                break;
            case R.id.rl_main:
                mViewpager.setCurrentItem(0);
                closeDrawLayout();
                break;
            case R.id.rl_movie:
                //推荐
                Intent intentHis = new Intent(getContext(), HistoryTodayActivity.class);
                startActivity(intentHis);
                ((Activity) getContext()).overridePendingTransition(R.anim.rotate, 0);
                closeDrawLayout();
                break;
            case R.id.rl_change_modul:
                closeDrawLayout();
                ((PlayMainActivity)getContext()).setNightMode();
                break;
            case R.id.rl_collect:
                Intent intentCol = new Intent(getContext(), MyCollectActivity.class);
                startActivity(intentCol);
                ((Activity) getContext()).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
                closeDrawLayout();
                break;
            case R.id.rl_me:
                Intent intentAbu = new Intent(getContext(), AboutUsActivity.class);
                startActivity(intentAbu);
                ((Activity) getContext()).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
                closeDrawLayout();
                break;
            case R.id.tv_close:
                closeDrawLayout();
                ((Activity) getContext()).finish();
                break;
            case R.id.head:
                Intent intentAbus = new Intent(getContext(), AboutUsActivity.class);
                startActivity(intentAbus);
                ((Activity) getContext()).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
                closeDrawLayout();
                break;
            case R.id.iv_chat:
                Intent intentChat = new Intent(getContext(), ChatActivity.class);
                startActivity(intentChat);
                ((Activity) getContext()).overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
                closeDrawLayout();
                break;
            case R.id.iv_setting:
                SettingFragment settingFragment = SettingFragment.getInstance();
                settingFragment.show(getChildFragmentManager(), "");
                break;
        }
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void closeDrawLayout() {
        mDrawlayout.closeDrawer(Gravity.LEFT);
    }

    //双击退出
    private long firstTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
