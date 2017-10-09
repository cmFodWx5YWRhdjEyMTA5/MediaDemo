package com.cxria.Media;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

public class PhotoViewActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.photoview)
    PhotoView mPhotoview;

    @Override
    int getLayout() {
        return R.layout.activity_photo_view;
    }

    @Override
    void initView() {
        String imagepath = getIntent().getStringExtra("imagepath");
        Glide.with(this).load(imagepath).into(mPhotoview);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
