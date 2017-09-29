package com.cxria.Media;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
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
import com.cxria.Media.utils.CameraSizeUtils;
import com.cxria.Media.utils.ErrorCode;
import com.cxria.Media.utils.MediaRecordFunc;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
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
    private int mState = -1;    //-1:没再录制，0：录制wav，1：录制amr
    private UIHandler uiHandler;
    private UIThread uiThread;
    private Camera mCamera;
    private Camera.Size mBestPreviewSize;


    @Override
    int getLayout() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_main;
    }

    @Override
    void initView() {
        uiHandler = new UIHandler();
        initSurface();
        setListener();
    }


    private void setListener() {
        mSwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mRlLine.setVisibility(View.VISIBLE);
                }else {
                    mRlLine.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.camera, R.id.iv_back, R.id.btn_record_wav, R.id.iv_setting, R.id.iv_back_setting, R.id.btn_record_amr, R.id.btn_stop})
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
        }
    }

    private void initSurface() {
        mSurfaceview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                openCamer();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                closeCamer();
            }
        });
        mSurfaceview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void openCamer() {
        final float ratio = (float) mSurfaceview.getWidth() / mSurfaceview.getHeight();
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        Camera.Parameters params = mCamera.getParameters();
        params.setPictureFormat(ImageFormat.JPEG);
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setDisplayOrientation(0);

        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        Camera.Size mBestPictureSize = null;

        // 设置pictureSize
        List<Camera.Size> pictureSizes = params.getSupportedPictureSizes();
        if (mBestPictureSize == null) {
            mBestPictureSize = CameraSizeUtils.findBestPictureSize(pictureSizes, params.getPictureSize(), ratio);
        }
        params.setPictureSize(mBestPictureSize.width, mBestPictureSize.height);

        // 设置previewSize
        if (mBestPreviewSize == null) {
            mBestPreviewSize = CameraSizeUtils.findBestPreviewSize(previewSizes, params.getPreviewSize(),
                    mBestPictureSize, ratio);
        }
        params.setPreviewSize(mBestPreviewSize.width, mBestPreviewSize.height);
        //设置surface的值
        ViewGroup.LayoutParams param = mSurfaceview.getLayoutParams();
        param.height = mSurfaceview.getWidth() * mBestPreviewSize.width / mBestPreviewSize.height;
        mSurfaceview.setLayoutParams(param);

        //设置值回去
//        mCamera.setParameters(params);
        //回调
        mCamera.setPreviewCallback(new Camera.PreviewCallback() { //获取相机的data

            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {  //这里一直在执行

            }
        });

        try {
            mCamera.setPreviewDisplay(mSurfaceview.getHolder());
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCamer() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
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

    ;

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

}
