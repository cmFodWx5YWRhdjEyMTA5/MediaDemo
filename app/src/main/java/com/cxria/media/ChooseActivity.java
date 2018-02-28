package com.cxria.media;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxria.media.play.PlayMainActivity;
import com.cxria.media.utils.ActivityUtils;
import com.cxria.media.video.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class ChooseActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_play)
    TextView mTvPlay;
    @BindView(R.id.tv_video)
    TextView mTvVideo;
    @BindView(R.id.tv_wanandroid)
    TextView mTvWanAndroid;
    @BindView(R.id.ll_bg)
    LinearLayout mLlBg;
    private Random mRandom;
    private List<Integer> mMImages;
    private int numCount=0;

    @Override
    public int getLayout() {
        return R.layout.activity_choose;
    }

    @Override
    public void initView() {
        mMImages = new ArrayList<>();
        mMImages.add(R.drawable.bg_1);
        mMImages.add(R.drawable.bg_2);
        mMImages.add(R.drawable.bg_3);
        mMImages.add(R.drawable.bg_4);
        mMImages.add(R.drawable.bg_5);
        mMImages.add(R.drawable.bg_6);
        mMImages.add(R.drawable.bg_7);
        mMImages.add(R.drawable.bg_8);
        mRandom = new Random();
        int ran = mRandom.nextInt(8);
        mIvBack.setBackgroundResource(mMImages.get(ran));
        startAmim();

        setNightMode();

    }

    private void setNightMode() {
        //  获取当前模式
        boolean module = getModule();
        if(module){
            getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            recreate();
        }
    }

    private boolean getModule() {
        SharedPreferences share=getSharedPreferences("module", Context.MODE_PRIVATE);
        boolean isNight=share.getBoolean("isNight",false);
        return isNight;
    }

    private void startAmim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mLlBg,"rotationX",90,0);
        animator.setDuration(1000);
        animator.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAmim();
    }

    @OnClick({R.id.tv_play, R.id.tv_video,R.id.iv_back,R.id.tv_wanandroid})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_play:
                Intent intent = new Intent(ChooseActivity.this, PlayMainActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_video:
                Intent intent2 = new Intent(ChooseActivity.this, MainActivity.class);
                startActivity(intent2);
                break;
            case R.id.iv_back:
                int ran = mRandom.nextInt(8);
                mIvBack.setBackgroundResource(mMImages.get(ran));
                //是否显示玩Android按钮
                numCount++;
                if(numCount%3==0){
                    if(mTvWanAndroid.getVisibility()==View.VISIBLE){
                        mTvWanAndroid.setVisibility(View.GONE);
                    }else {
                        mTvWanAndroid.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.tv_wanandroid:
                ActivityUtils.startWanActivity(this);
                break;
        }
    }
}
