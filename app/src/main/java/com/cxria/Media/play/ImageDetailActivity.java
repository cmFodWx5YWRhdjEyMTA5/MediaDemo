package com.cxria.Media.play;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cxria.Media.BaseActivity;
import com.cxria.Media.R;
import com.cxria.Media.utils.DownLoadUtils;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

public class ImageDetailActivity extends BaseActivity {


    @BindView(R.id.photoview)
    PhotoView mPhotoview;
    @BindView(R.id.iv_load)
    ImageView mIvLoad;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    String downloadurl;
    @BindView(R.id.iv_images)
    ImageView mIvImage;
    private boolean mIsGif;

    @Override
    public int getLayout() {
        return R.layout.activity_image_detail;
    }

    @Override
    public void initView() {
        downloadurl = getIntent().getStringExtra("url");
        mIsGif = getIntent().getBooleanExtra("isGif",false);
        Log.i("do",downloadurl+mIsGif);
        if (mIsGif) {
            Glide.with(ImageDetailActivity.this).load(downloadurl).into(mIvImage);
            mPhotoview.setVisibility(View.GONE);
            Toast.makeText(this, "gif", Toast.LENGTH_SHORT).show();
        } else {
            Glide.with(this).load(downloadurl).into(mPhotoview);
        }
    }

    @OnClick({R.id.iv_load, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_load:
                DownLoadUtils.download(this, downloadurl,mIsGif);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
