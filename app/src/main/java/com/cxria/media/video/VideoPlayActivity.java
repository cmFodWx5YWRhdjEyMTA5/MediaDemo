package com.cxria.media.video;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cxria.media.BaseActivity;
import com.cxria.media.R;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayerStandard;

public class VideoPlayActivity extends BaseActivity {

    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard mVideoplayer;
    @BindView(R.id.im_cover)
    ImageView mImCover;
    @BindView(R.id.im_play)
    ImageView mImPlay;
    @BindView(R.id.rl_bg)
    RelativeLayout mRlBg;
    @Override
    public int getLayout() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_video_play;
    }

    @Override
    public void initView() {
        String imagepath = getIntent().getStringExtra("imagepath");
        String[] split = imagepath.split("#");
        mVideoplayer.setUp(split[0]
                , JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, split[split.length - 1]);
        if(split.length>2){
            //网络
            Glide.with(this).load(split[1]).into(mImCover);
            mRlBg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    mRlBg.setVisibility(View.GONE);
                    return false;
                }
            });
        }else {
            Glide.with(this).load(split[0]).into(mImCover);
            mRlBg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    mRlBg.setVisibility(View.GONE);
                    return false;
                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        if (mVideoplayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoplayer.releaseAllVideos();
    }
}
