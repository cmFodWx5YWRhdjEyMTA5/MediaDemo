package com.cxria.media.play;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cxria.media.BaseActivity;
import com.cxria.media.R;
import com.qq.e.ads.cfg.MultiProcessFlag;
import com.qq.e.ads.nativ.NativeAD;
import com.qq.e.ads.nativ.NativeADDataRef;
import com.qq.e.comm.util.AdError;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ADVActivity extends BaseActivity implements NativeAD.NativeAdListener,
        View.OnClickListener {

    @BindView(R.id.img_logo)
    ImageView mImgLogo;
    @BindView(R.id.text_name)
    TextView mTextName;
    @BindView(R.id.text_desc)
    TextView mTextDesc;
    @BindView(R.id.text_status)
    TextView mTextStatus;
    @BindView(R.id.img_poster)
    ImageView mImgPoster;
    @BindView(R.id.divider)
    View mDivider;
    @BindView(R.id.btn_download)
    Button mBtnDownload;
    @BindView(R.id.nativeADContainer)
    RelativeLayout mNativeADContainer;
    @BindView(R.id.loadNative)
    Button loadNative;
    @BindView(R.id.showNative)
    Button showNative;
    private Button btn_download;// 下载按钮
    private NativeADDataRef adItem;
    private NativeAD nativeAD;

    @Override
    public int getLayout() {
        return R.layout.activity_adv;
    }

    @Override
    public void initView() {

    }


    /**
     * 加载广告，初始化
     */
    public void loadAD() {
        // 在广告初始化前调用此方法，转为多线程原生广告
        MultiProcessFlag.setMultiProcess(true);
        if (nativeAD == null) {
            // 初始化
            this.nativeAD = new NativeAD(this, "1105962710",
                    "3040126180953688", this);
        }
        int count = 1; // 一次拉取的广告条数：范围1-30
        nativeAD.loadAD(count);// 开始读取广告
    }

    /**
     * 展示广告，自定义广告布局
     */
    public void showAD() {

        ImageView img_logo = (ImageView) findViewById(R.id.nativeADContainer)
                .findViewById(R.id.img_logo);// 广告Logo
        ImageView img_poster = (ImageView) findViewById(R.id.nativeADContainer)
                .findViewById(R.id.img_poster);// 图片内容
        TextView text_name = (TextView) findViewById(R.id.nativeADContainer)
                .findViewById(R.id.text_name);// 文字标题
        TextView text_desc = (TextView) findViewById(R.id.nativeADContainer)
                .findViewById(R.id.text_desc);// 文字内容
        btn_download = (Button) findViewById(R.id.nativeADContainer)
                .findViewById(R.id.btn_download);// 下载按钮
        if(adItem==null){
            return;
        }
        getImage(img_logo, adItem.getIconUrl());
        getImage(img_poster, adItem.getImgUrl());

        text_name.setText(adItem.getTitle());//设置文字
        text_desc.setText(adItem.getDesc());

        adItem.onExposured(this.findViewById(R.id.nativeADContainer));

        //设置下载按钮
        btn_download.setText(getADButtonText());
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adItem.onClicked(v);//发送相关的状态通知
            }
        });

    }

    @OnClick({R.id.loadNative, R.id.showNative})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loadNative:
                loadAD();
                break;
            case R.id.showNative:
                showAD();

                break;
        }
    }

    /**
     * App类广告安装、下载状态的更新（普链广告没有此状态，其值为-1） 返回的AppStatus含义如下： 0：未下载 1：已安装 2：已安装旧版本
     * 4：下载中（可获取下载进度“0-100”） 8：下载完成 16：下载失败
     */
    private String getADButtonText() {
        if (adItem == null) {
            return "……";
        }
        if (!adItem.isAPP()) {
            return "查看详情";
        }
        switch (adItem.getAPPStatus()) {
            case 0:
                return "点击下载";
            case 1:
                return "点击启动";
            case 2:
                return "点击更新";
            case 4:
                return "下载中" + adItem.getProgress() + "%";
            case 8:
                return "点击安装";
            case 16:
                return "下载失败,点击重试";
            default:
                return "查看详情";
        }
    }

    /**
     * 加载成功时调用
     */

    @Override
    public void onNoAD(AdError adError) {
        Toast.makeText(this, adError.getErrorMsg(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onADLoaded(List<NativeADDataRef> list) {
        Toast.makeText(this, "list: " + list.toString(), Toast.LENGTH_LONG).show();
        Log.i("--adList",list.toString());
        if (list.size() > 0) {
            adItem = list.get(0);
            // $.id(R.id.showNative).enabled(true);//设置为可用
            showNative.setEnabled(true);
            Toast.makeText(this, "原生广告加载成功", Toast.LENGTH_LONG).show();
        } else {
            Log.i("AD_DEMO", "NOADReturn");
        }
    }

    /**
     * 广告状态发送改变,更新下载按钮文字
     */
    @Override
    public void onADStatusChanged(NativeADDataRef arg0) {
        Toast.makeText(this, "arg0: " + arg0.toString(), Toast.LENGTH_LONG).show();
        btn_download.setText(getADButtonText());
        Toast.makeText(getApplicationContext(), getADButtonText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onADError(NativeADDataRef nativeADDataRef, AdError adError) {
        Toast.makeText(this, adError.getErrorMsg(), Toast.LENGTH_LONG).show();
    }


    /**
     * @param iv
     * @param url
     */
    public void getImage(ImageView iv, String url) {
        Glide.with(this).load(url).into(iv);
//        String imgUrl = url;
//        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
//        final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(
//                20);
//        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
//            @Override
//            public void putBitmap(String key, Bitmap value) {
//                mImageCache.put(key, value);
//            }
//
//            @Override
//            public Bitmap getBitmap(String key) {
//                return mImageCache.get(key);
//            }
//        };
//        ImageLoader mImageLoader = new ImageLoader(mRequestQueue, imageCache);
//        // imageView是一个ImageView实例
//        // ImageLoader.getImageListener的第二个参数是默认的图片resource id
//        // 第三个参数是请求失败时候的资源id，可以指定为0
//        ImageLoader.ImageListener listener = ImageLoader
//                .getImageListener(iv, android.R.drawable.ic_menu_rotate,
//                        android.R.drawable.ic_delete);
//        mImageLoader.get(imgUrl, listener);
    }

}
