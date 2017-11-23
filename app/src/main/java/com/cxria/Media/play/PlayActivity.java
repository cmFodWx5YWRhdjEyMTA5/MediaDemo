package com.cxria.Media.play;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cxria.Media.BaseActivity;
import com.cxria.Media.R;
import com.cxria.Media.entity.EventCategrayPos;
import com.cxria.Media.fragment.ImageFragment;
import com.cxria.Media.fragment.JokeFragment;
import com.cxria.Media.fragment.RecFragment;
import com.cxria.Media.fragment.TextFragment;
import com.cxria.Media.fragment.VideoFragment;
import com.cxria.Media.video.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
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
    List<Fragment> mFragments = new ArrayList<>();

    private boolean isNight;

    @Override
    public int getLayout() {
        return R.layout.activity_play;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mStringArray = getResources().getStringArray(R.array.title);
        for (int i = 0; i < mStringArray.length; i++) {
            mTablayout.addTab(mTablayout.newTab().setText(mStringArray[i]));
        }

        RecFragment instance = RecFragment.getInstance();
        VideoFragment instance1 = VideoFragment.getInstance();
        ImageFragment instance2 = ImageFragment.getInstance();
        JokeFragment instance3 = JokeFragment.getInstance();
        TextFragment instance4 = TextFragment.getInstance();

        mFragments.add(instance);
        mFragments.add(instance1);
        mFragments.add(instance2);
        mFragments.add(instance3);
        mFragments.add(instance4);
        setAdapter();
        setListener();
    }

    private void setAdapter() {
        mMViewPagerAdapter = new MViewPagerAdapter(getSupportFragmentManager(), mFragments, mStringArray);
        mViewpager.setAdapter(mMViewPagerAdapter);
        mViewpager.setOffscreenPageLimit(5);
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventCategrayPos event) {
        /* Do something */
        mViewpager.setCurrentItem(event.pos);
    };


    @OnClick({R.id.iv_main,R.id.head, R.id.iv_close,R.id.rl_collect,R.id.rl_main, R.id.rl_movie, R.id.rl_change_modul, R.id.rl_me, R.id.tv_close})
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
                mViewpager.setCurrentItem(0);
                closeDrawLayout();
                break;
            case R.id.rl_movie:
                //推荐
                Intent intentHis=new Intent(this,HistoryTodayActivity.class);
                startActivity(intentHis);
                closeDrawLayout();
                break;
            case R.id.rl_change_modul:
                setNightMode();
                closeDrawLayout();
                break;
            case R.id.rl_collect:
                Intent intentCol=new Intent(this,MyCollectActivity.class);
                startActivity(intentCol);
                closeDrawLayout();
                break;
            case R.id.rl_me:
                Intent intentAbu=new Intent(this,AboutUsActivity.class);
                startActivity(intentAbu);
                closeDrawLayout();
                break;
            case R.id.tv_close:
                closeDrawLayout();
                finish();
                break;
            case R.id.head:
                Intent intentAbus=new Intent(this,AboutUsActivity.class);
                startActivity(intentAbus);
                closeDrawLayout();
                break;
        }
    }

    private void setNightMode() {
        //  获取当前模式
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        //  将是否为夜间模式保存到SharedPreferences
        if(currentNightMode==32){
            isNight=true;
        }else {
            isNight=false;
        }

        getDelegate().setDefaultNightMode(isNight ?
                AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
        //  重启Activity
        recreate();
        saveModule(isNight);
    }

    private void saveModule(boolean isNight) {
        SharedPreferences sharedPreferences = getSharedPreferences("module", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putBoolean("isNight", isNight);
        editor.commit();//提交修改
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void closeDrawLayout() {
        mDrawlayout.closeDrawer(Gravity.LEFT);
    }
    //双击退出
    private long firstTime=0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                long secondTime=System.currentTimeMillis();

                if(secondTime-firstTime>2000){
                    Toast.makeText(PlayActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                    firstTime=secondTime;
                    return true;
                }else{
                   finish();
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
