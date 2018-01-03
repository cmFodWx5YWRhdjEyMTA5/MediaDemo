package com.cxria.media.play;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxria.media.BaseActivity;
import com.cxria.media.R;

import butterknife.BindView;
import butterknife.OnClick;

public class JokeDetailActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    private String mContent;

    @Override
    public int getLayout() {
        return R.layout.activity_joke_detail;
    }

    @Override
    public void initView() {
        mContent = getIntent().getStringExtra("content");
        mTvContent.setText(mContent);

    }


    @OnClick({R.id.iv_back, R.id.iv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                shareSend();
                break;
        }
    }

    private void shareSend() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); // 纯文本
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, mContent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, ""));
    }
}
