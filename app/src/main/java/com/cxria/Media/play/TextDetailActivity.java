package com.cxria.Media.play;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxria.Media.BaseActivity;
import com.cxria.Media.R;
import com.cxria.Media.views.WaterLoadView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TextDetailActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rl)
    RelativeLayout mRl;
    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.water_load)
    WaterLoadView mProgress;

    @Override
    public int getLayout() {
        return R.layout.activity_text_detail;
    }

    @Override
    public void initView() {
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        mTvTitle.setText(title);
        mWebview.getSettings().setJavaScriptEnabled(true);
        //加载需要显示的网页
        mWebview.loadUrl(url);
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
            mWebview.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return false;
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

}
