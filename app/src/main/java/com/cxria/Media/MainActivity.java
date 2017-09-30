package com.cxria.Media;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cxria.Media.utils.AudioFileFunc;
import com.cxria.Media.utils.AudioRecordFunc;
import com.cxria.Media.utils.ErrorCode;
import com.cxria.Media.utils.MediaRecordFunc;
import com.cxria.Media.utils.ScreenUtils;
import com.cxria.Media.utils.TagLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements CameraControler.view {
    private final static int FLAG_WAV = 0;
    private final static int FLAG_AMR = 1;
    @BindView(R.id.surfaceview)
    SurfaceView mSurfaceview;
    @BindView(R.id.text)
    TextView mText;
    @BindView(R.id.camera)
    Button mCameraStar;
    @BindView(R.id.btn_record_wav)
    Button mBtnRecordWav;
    @BindView(R.id.btn_record_amr)
    Button mBtnRecordAmr;
    @BindView(R.id.btn_stop)
    Button mBtnStop;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_setting)
    ImageView mIvSetting;
    @BindView(R.id.ll_setting)
    LinearLayout mLlSetting;
    @BindView(R.id.iv_play)
    ImageView mIvPlay;
    @BindView(R.id.rl_line)
    RelativeLayout mRlLine;
    @BindView(R.id.iv_back_setting)
    ImageView mIvBackSetting;
    @BindView(R.id.switchBtn)
    Switch mSwitchBtn;
    @BindView(R.id.iv_face)
    ImageView mIvFace;
    @BindView(R.id.iv_bline)
    ImageView mIvBline;
    @BindView(R.id.iv_takphoto)
    ImageView mIvTakphoto;
    @BindView(R.id.switchScreen)
    Switch mSwitchScreen;
    @BindView(R.id.switchColor)
    Switch mSwitchColor;
    @BindView(R.id.iv_add_setting)
    ImageView mIvAddSetting;
    @BindView(R.id.taglayout)
    TagLayout mTaglayout;
    @BindView(R.id.switchWrite)
    Switch mSwitchWrite;
    @BindView(R.id.taglayoutScreen)
    TagLayout mTaglayoutScreen;
    @BindView(R.id.rl_camera_setting)
    RelativeLayout mRlCameraSetting;
    @BindView(R.id.rl_bottom_setting)
    RelativeLayout mRlBottomSetting;
    @BindView(R.id.tv_voice)
    TextView mTvVoice;
    @BindView(R.id.tv_no_voice)
    RadioButton mTvNoVoice;
    @BindView(R.id.tv_voice_1)
    RadioButton mTvVoice1;
    @BindView(R.id.tv_voice_2)
    RadioButton mTvVoice2;
    @BindView(R.id.rg_voice)
    RadioGroup mRgVoice;
    @BindView(R.id.tv_sp)
    TextView mTvSp;
    @BindView(R.id.tv_all)
    RadioButton mTvAll;
    @BindView(R.id.tv_43)
    RadioButton mTv43;
    @BindView(R.id.tv_169)
    RadioButton mTv169;
    @BindView(R.id.rg_screen)
    RadioGroup mRgScreen;
    @BindView(R.id.tv_light)
    TextView mTvLight;
    @BindView(R.id.sb_light)
    SeekBar mSbLight;
    @BindView(R.id.ll_screen_setting)
    LinearLayout mLlScreenSetting;
    @BindView(R.id.tv_top)
    TextView mTvTop;
    @BindView(R.id.tv_bottom)
    TextView mTvBottom;
    @BindView(R.id.tv_left)
    TextView mTvLeft;
    @BindView(R.id.tv_right)
    TextView mTvRight;

    private int mState = -1;    //-1:没再录制，0：录制wav，1：录制amr
    private UIHandler uiHandler;
    private UIThread uiThread;
    private CamerPresent mCamerPresent;
    private boolean isMedia;
    private boolean isShowSetting;
    private List<TextView> mTextViewListWrite = new ArrayList<>();
    private List<TextView> mTextViewListScreen = new ArrayList<>();
    private List<String> mListColor = new ArrayList<>();
    private WindowManager.LayoutParams mLp;

    @Override
    int getLayout() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_main;
    }

    @Override
    void initView() {
        mCamerPresent = new CamerPresent(this);
        mCamerPresent.init(mSurfaceview);
        uiHandler = new UIHandler();
        setListener();
        mLp = this.getWindow().getAttributes();
        mSbLight.setMax(255);
        //屏幕亮度
        try {
            int anInt = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            mSbLight.setProgress(anInt);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        ((RadioButton)mRgScreen.getChildAt(0)).setChecked(true);
        ((RadioButton)mRgVoice.getChildAt(0)).setChecked(true);
    }

    public void initLayout(final List<String> mWidthList) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextViewListWrite.clear();
                mTaglayout.removeAllViews();
                for (int i = 0; i < mWidthList.size(); i++) {
                    final TextView tv = ViewUtils.getTextView(MainActivity.this);
                    tv.setText(mWidthList.get(i));
                    mTaglayout.addView(tv);
                    mTextViewListWrite.add(tv);
                    //监听
                    final int finalI = i;
                    if (i == 0) {
                        mTextViewListWrite.get(0).setTextColor(getResources().getColor(R.color.color_ff0008));
                        mTextViewListWrite.get(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_write_bg_check));
                    }
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int j = 0; j < mTextViewListWrite.size(); j++) {
                                if (j == finalI) {
                                    mTextViewListWrite.get(j).setTextColor(getResources().getColor(R.color.color_ff0008));
                                    mTextViewListWrite.get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_write_bg_check));
                                } else {
                                    mTextViewListWrite.get(j).setTextColor(getResources().getColor(R.color.color_dadada));
                                    mTextViewListWrite.get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_write_bg));
                                }
                            }
                            mCamerPresent.setSupportedWhiteBalance(tv.getText().toString());
                        }
                    });
                }
            }
        });
    }

    public void initScreenLayout(final List<String> mScreenList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextViewListScreen.clear();
                mTaglayoutScreen.removeAllViews();
                for (int i = 0; i < mScreenList.size(); i++) {
                    final TextView tv = ViewUtils.getTextView(MainActivity.this);
                    tv.setText(mScreenList.get(i));
                    mTaglayoutScreen.addView(tv);
                    mTextViewListScreen.add(tv);
                    //监听
                    final int finalI = i;
                    if (i == 0) {
                        mTextViewListScreen.get(0).setTextColor(getResources().getColor(R.color.color_ffe100));
                        mTextViewListScreen.get(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_screen_bg_check));
                    }
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int j = 0; j < mTextViewListScreen.size(); j++) {
                                if (j == finalI) {
                                    mTextViewListScreen.get(j).setTextColor(getResources().getColor(R.color.color_ffe100));
                                    mTextViewListScreen.get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_screen_bg_check));
                                } else {
                                    mTextViewListScreen.get(j).setTextColor(getResources().getColor(R.color.color_dadada));
                                    mTextViewListScreen.get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_write_bg));
                                }
                            }
                            mCamerPresent.setSupportedSceneModes(tv.getText().toString());
                        }
                    });
                }
            }
        });
    }

    //获取到数据
    public void initCameraList(List<String> listColor) {
        mListColor.clear();
        mListColor.addAll(listColor);
    }

    private void setListener() {
        mSwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRlLine.setVisibility(View.VISIBLE);
                } else {
                    mRlLine.setVisibility(View.GONE);
                }
            }
        });
        mSwitchWrite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mTaglayout.setVisibility(View.VISIBLE);
                } else {
                    mTaglayout.setVisibility(View.GONE);
                }
            }
        });
        mSwitchScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mTaglayoutScreen.setVisibility(View.VISIBLE);
                } else {
                    mTaglayoutScreen.setVisibility(View.GONE);
                }
            }
        });
        mSwitchColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CenterDialog instance = CenterDialog.getInstance(mListColor, MainActivity.this);
                    instance.show(getSupportFragmentManager(), "");
                    mLlSetting.setVisibility(View.GONE);
                    mSwitchColor.setChecked(false);
                }
            }
        });
        mSbLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mLp.screenBrightness = Float.valueOf(progress) * (1f / 255f);
                    getWindow().setAttributes(mLp);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //屏幕比例
        mRgScreen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                for (int i = 0; i <mRgScreen.getChildCount(); i++) {
                    if(((RadioButton)mRgScreen.getChildAt(i)).isChecked()){
                        ((RadioButton)mRgScreen.getChildAt(i)).setChecked(true);
                        setScreen(i);
                    }
                }
            }
        });
        //音效
        mRgVoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                for (int i = 0; i <mRgVoice.getChildCount(); i++) {
                    if(((RadioButton)mRgVoice.getChildAt(i)).isChecked()){
                        ((RadioButton)mRgVoice.getChildAt(i)).setChecked(true);
                        mCamerPresent.setVoiceDev(i);
                    }
                }
            }
        });
    }

    public void setScreen(int pos){
        int width = ScreenUtils.instance().getWidth(this);
        int height = ScreenUtils.instance().getHeight(this);
        switch (pos){
            //默认
            case 0:
                mTvTop.setVisibility(View.GONE);
                mTvBottom.setVisibility(View.GONE);
                mTvLeft.setVisibility(View.GONE);
                mTvRight.setVisibility(View.GONE);
                break;
            //4：3
            case 1:
                mTvTop.setVisibility(View.GONE);
                mTvBottom.setVisibility(View.GONE);
                mTvLeft.setVisibility(View.VISIBLE);
                mTvRight.setVisibility(View.VISIBLE);
                //设置值
                ViewGroup.LayoutParams params=mTvLeft.getLayoutParams();
                params.width=(width-height*4/3)/2;
                mTvLeft.setLayoutParams(params);
                ViewGroup.LayoutParams paramR=mTvRight.getLayoutParams();
                paramR.width=(width-height*4/3)/2;
                mTvRight.setLayoutParams(paramR);
            break;
            //16：9
            case 2:
                mTvLeft.setVisibility(View.GONE);
                mTvRight.setVisibility(View.GONE);
                mTvTop.setVisibility(View.VISIBLE);
                mTvBottom.setVisibility(View.VISIBLE);
                //设置值
                ViewGroup.LayoutParams paramT=mTvTop.getLayoutParams();
                paramT.height=(height-width*1/2)/2;
                mTvTop.setLayoutParams(paramT);
                ViewGroup.LayoutParams paramB=mTvBottom.getLayoutParams();
                paramB.height=(height-width*1/2)/2;
                mTvBottom.setLayoutParams(paramB);
            break;

        }

    }

    /**
     * 滤镜
     *
     * @param value
     */
    public void setCameraColor(String value) {
        mCamerPresent.setSupportedColorEffects(value);
    }

    @OnClick({R.id.iv_add_setting, R.id.iv_takphoto, R.id.iv_face, R.id.iv_bline, R.id.camera, R.id.iv_back, R.id.btn_record_wav, R.id.iv_setting, R.id.iv_back_setting, R.id.btn_record_amr, R.id.btn_stop})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camera:
                break;
            case R.id.btn_record_wav:
                record(FLAG_WAV);
                break;
            case R.id.btn_record_amr:
                record(FLAG_AMR);
                break;
            case R.id.btn_stop:
                stop();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_setting:
                mLlSetting.setVisibility(View.VISIBLE);
                rotateyAnimSet(mLlSetting);
                break;
            case R.id.iv_back_setting:
                mLlSetting.setVisibility(View.GONE);
                break;
            case R.id.iv_face:
                //摄像头在子线程切换
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mCamerPresent.changeCamera();
                    }
                }).start();
                break;
            case R.id.iv_bline:
                mCamerPresent.openBline();
                break;
            case R.id.iv_play:
                if (!isMedia) {
                    mIvPlay.setImageResource(R.mipmap.ic_pause);
                } else {
                    mIvPlay.setImageResource(R.mipmap.ic_play);
                }
                isMedia = !isMedia;
                break;
            case R.id.iv_takphoto:
                //拍照
                mCamerPresent.getPhoto();
                break;
            case R.id.iv_add_setting:
                //setting
//                mCamerPresent.getSupportedWhiteBalance();
//                mCamerPresent.getSupportedColorEffects();
//                mCamerPresent.getSupportedSceneModes();
                if (isShowSetting) {
                    mIvAddSetting.setImageResource(R.mipmap.ic_add_setting);
                    mRlCameraSetting.setVisibility(View.GONE);
                    mLlScreenSetting.setVisibility(View.GONE);
                } else {
                    mIvAddSetting.setImageResource(R.mipmap.ic_remove_setting);
                    mRlCameraSetting.setVisibility(View.VISIBLE);
                    mLlScreenSetting.setVisibility(View.VISIBLE);
                    rotateyAnimRun(mLlScreenSetting);
                    rotateyAnimRun(mRlCameraSetting);

                }
                isShowSetting = !isShowSetting;
                break;
        }
    }

    /**
     * 位移动画
     * @param view
     */
    public void rotateyAnimRun(View view){
        ObjectAnimator//
                .ofFloat(view, "alpha", 0F, 1F)//
                .setDuration(400)//
                .start();
        ObjectAnimator//
                .ofFloat(view, "translationX",  -360F, 0F)//
                .setDuration(500)//
                .start();
        ObjectAnimator//
                .ofFloat(view, "translationY", -360F, 0F)//
                .setDuration(500)//
                .start();
    }
    /**
     * 位移动画
     * @param view
     */
    public void rotateyAnimSet(View view){
        ObjectAnimator//
                .ofFloat(view, "alpha", 0F, 1F)//
                .setDuration(400)//
                .start();
        ObjectAnimator//
                .ofFloat(view, "translationX",  360F, 0F)//
                .setDuration(500)//
                .start();
    }

    /**
     * 开始录音
     *
     * @param mFlag，0：录制wav格式，1：录音amr格式
     */
    private void record(int mFlag) {
        if (mState != -1) {
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd", CMD_RECORDFAIL);
            b.putInt("msg", ErrorCode.E_STATE_RECODING);
            msg.setData(b);
            uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
            return;
        }
        int mResult = -1;
        switch (mFlag) {
            case FLAG_WAV:
                AudioRecordFunc mRecord_1 = AudioRecordFunc.getInstance();
                mResult = mRecord_1.startRecordAndFile();
                break;
            case FLAG_AMR:
                MediaRecordFunc mRecord_2 = MediaRecordFunc.getInstance();
                mResult = mRecord_2.startRecordAndFile();
                break;
        }
        if (mResult == ErrorCode.SUCCESS) {
            uiThread = new UIThread();
            new Thread(uiThread).start();
            mState = mFlag;
        } else {
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd", CMD_RECORDFAIL);
            b.putInt("msg", mResult);
            msg.setData(b);

            uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
        }
    }

    /**
     * 停止录音
     */
    private void stop() {
        if (mState != -1) {
            switch (mState) {
                case FLAG_WAV:
                    AudioRecordFunc mRecord_1 = AudioRecordFunc.getInstance();
                    mRecord_1.stopRecordAndFile();
                    break;
                case FLAG_AMR:
                    MediaRecordFunc mRecord_2 = MediaRecordFunc.getInstance();
                    mRecord_2.stopRecordAndFile();
                    break;
            }
            if (uiThread != null) {
                uiThread.stopThread();
            }
            if (uiHandler != null)
                uiHandler.removeCallbacks(uiThread);
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putInt("cmd", CMD_STOP);
            b.putInt("msg", mState);
            msg.setData(b);
            uiHandler.sendMessageDelayed(msg, 1000); // 向Handler发送消息,更新UI
            mState = -1;
        }
    }

    private final static int CMD_RECORDING_TIME = 2000;
    private final static int CMD_RECORDFAIL = 2001;
    private final static int CMD_STOP = 2002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    class UIHandler extends Handler {
        public UIHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.d("MyHandler", "handleMessage......");
            super.handleMessage(msg);
            Bundle b = msg.getData();
            int vCmd = b.getInt("cmd");
            switch (vCmd) {
                case CMD_RECORDING_TIME:
                    int vTime = b.getInt("msg", 0);
                    MainActivity.this.mText.setText("正在录音中，已录制：" + vTime + " s");
                    break;
                case CMD_RECORDFAIL:
                    int vErrorCode = b.getInt("msg");
                    String vMsg = ErrorCode.getErrorInfo(MainActivity.this, vErrorCode);
                    MainActivity.this.mText.setText("录音失败：" + vMsg);
                    break;
                case CMD_STOP:
                    int vFileType = b.getInt("msg");
                    switch (vFileType) {
                        case FLAG_WAV:
                            AudioRecordFunc mRecord_1 = AudioRecordFunc.getInstance();
                            final long mSize = mRecord_1.getRecordFileSize();
                            MainActivity.this.mText.setText("录音已停止.录音文件:" + AudioFileFunc.getWavFilePath() + "\n文件大小：" + mSize);

                            break;
                        case FLAG_AMR:
                            MediaRecordFunc mRecord_2 = MediaRecordFunc.getInstance();
                            final long mSizes = mRecord_2.getRecordFileSize();
                            MainActivity.this.mText.setText("录音已停止.录音文件:" + AudioFileFunc.getAMRFilePath() + "\n文件大小：" + mSizes);

                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    class UIThread implements Runnable {
        int mTimeMill = 0;
        boolean vRun = true;

        public void stopThread() {
            vRun = false;
        }

        public void run() {
            while (vRun) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mTimeMill++;
                Log.d("thread", "mThread........" + mTimeMill);
                Message msg = new Message();
                Bundle b = new Bundle();// 存放数据
                b.putInt("cmd", CMD_RECORDING_TIME);
                b.putInt("msg", mTimeMill);
                msg.setData(b);
                MainActivity.this.uiHandler.sendMessage(msg); // 向Handler发送消息,更新UI
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCamerPresent.closeCamer();
    }
}
