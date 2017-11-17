package com.cxria.Media.video;

import android.view.WindowManager;

import com.cxria.Media.R;
import com.cxria.Media.BaseActivity;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayerStandard;

public class VideoPlayActivity extends BaseActivity {

    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard mVideoplayer;

    @Override
    public int getLayout() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_video_play;
    }

    @Override
    public void initView() {
        String imagepath = getIntent().getStringExtra("imagepath");
        String[] split = imagepath.split("/");
        mVideoplayer.setUp(imagepath
                , JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, split[split.length-1]);
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
