package com.cxria.Media;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

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
    private int mState = -1;    //-1:没再录制，0：录制wav，1：录制amr
    private UIHandler uiHandler;
    private UIThread uiThread;
    private CamerPresent mCamerPresent;
    private boolean isMedia;
    private boolean isShowSetting;
    private List<TextView> mTextViewListWrite=new ArrayList<>();
    private List<TextView> mTextViewListScreen=new ArrayList<>();
    private List<String> mListColor=new ArrayList<>();
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
    }

    public void initLayout(List<String> mWidthList) {
        mTextViewListWrite.clear();
        for (int i = 0; i < mWidthList.size(); i++) {
            final TextView tv = ViewUtils.getTextView(this);
            tv.setText(mWidthList.get(i));
            mTaglayout.addView(tv);
            mTextViewListWrite.add(tv);
            //监听
            final int finalI = i;
            if(i==0){
                mTextViewListWrite.get(0).setTextColor(getResources().getColor(R.color.color_ff0008));
                mTextViewListWrite.get(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_write_bg_check));
            }
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < mTextViewListWrite.size(); j++) {
                        if(j== finalI){
                            mTextViewListWrite.get(j).setTextColor(getResources().getColor(R.color.color_ff0008));
                            mTextViewListWrite.get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_write_bg_check));
                        }else {
                            mTextViewListWrite.get(j).setTextColor(getResources().getColor(R.color.color_dadada));
                            mTextViewListWrite.get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_write_bg));
                        }
                    }
                    mCamerPresent.setSupportedWhiteBalance(tv.getText().toString());
                }
            });
        }
    }

    public void initScreenLayout(List<String> mScreenList) {
        mTextViewListScreen.clear();
        for (int i = 0; i < mScreenList.size(); i++) {
            final TextView tv = ViewUtils.getTextView(this);
            tv.setText(mScreenList.get(i));
            mTaglayoutScreen.addView(tv);
            mTextViewListScreen.add(tv);
            //监听
            final int finalI = i;
            if(i==0){
                mTextViewListScreen.get(0).setTextColor(getResources().getColor(R.color.color_ffe100));
                mTextViewListScreen.get(0).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_screen_bg_check));
            }
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < mTextViewListScreen.size(); j++) {
                        if(j== finalI){
                            mTextViewListScreen.get(j).setTextColor(getResources().getColor(R.color.color_ffe100));
                            mTextViewListScreen.get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_screen_bg_check));
                        }else {
                            mTextViewListScreen.get(j).setTextColor(getResources().getColor(R.color.color_dadada));
                            mTextViewListScreen.get(j).setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_write_bg));
                        }
                    }
                    mCamerPresent.setSupportedSceneModes(tv.getText().toString());

                }
            });
        }
    }
    //获取到数据
    public void initCameraList(List<String> listColor){
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
                    instance.show(getSupportFragmentManager(),"");
                    mLlSetting.setVisibility(View.GONE);
                    mSwitchColor.setChecked(false);
                } else {
                    mRlLine.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 滤镜
     * @param value
     */
    public void setCameraColor(String value){
        mCamerPresent.setSupportedColorEffects(value);
    }

    @OnClick({R.id.iv_add_setting,R.id.iv_takphoto, R.id.iv_face, R.id.iv_bline, R.id.camera, R.id.iv_back, R.id.btn_record_wav, R.id.iv_setting, R.id.iv_back_setting, R.id.btn_record_amr, R.id.btn_stop})
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
                if(isShowSetting){
                    mIvAddSetting.setImageResource(R.mipmap.ic_add_setting);
                    mIvFace.setVisibility(View.GONE);
                    mIvBline.setVisibility(View.GONE);
                }else {
                    mIvAddSetting.setImageResource(R.mipmap.ic_remove_setting);
                    mIvFace.setVisibility(View.VISIBLE);
                    mIvBline.setVisibility(View.VISIBLE);
                }
                isShowSetting=!isShowSetting;
                break;
        }
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
