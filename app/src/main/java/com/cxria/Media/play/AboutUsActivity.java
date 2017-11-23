package com.cxria.Media.play;

import android.animation.ObjectAnimator;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxria.Media.BaseActivity;
import com.cxria.Media.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_deal)
    TextView mTvDeal;
    @BindView(R.id.rl)
    RelativeLayout mRl;
    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.v_line)
    View mVLine;

    @Override
    public int getLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView() {
        try {
            PackageManager pm = this.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);
            String versionName = pi.versionName;
            mTvVersion.setText("V "+versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        startAmim(mIvIcon);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
    private void startAmim(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotationY",360,0);
        animator.setDuration(4000);
        animator.setRepeatCount(-1);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }
}
