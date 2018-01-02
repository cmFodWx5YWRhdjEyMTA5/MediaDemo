# MediaDemo
#### 一个摄像的demo,手机全屏拍照,摄像,一些参数设置
##　后期增加了很多功能，基本完成了一个视频娱乐为一体的App

[Media APK下载](https://github.com/yukunkun/MediaDemo/blob/master/image/Media.apk)
### 摄像采用的是Android 原生的Camera 类实现，包括视屏分辨率选择实现，预览变形，视频质量问题都可以在这里找到解决方法，采用MediaRecorder
### 实现的录制功能，贴上主要的代码
####　Camera的预览设置 

        mParams = mCamera.getParameters();
            //前置的话，设置焦点要报错 
            if(isBackCamera){
                mParams.setPictureFormat(ImageFormat.JPEG);
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE); 
                mCamera.cancelAutoFocus();
            }
            mCamera.setDisplayOrientation(0);
            mPreviewSizes = mParams.getSupportedPreviewSizes();
            Camera.Size mBestPictureSize = null;
            // 设置pictureSize
            List<Camera.Size> pictureSizes = mParams.getSupportedPictureSizes();
            if (mBestPictureSize == null) {
                mBestPictureSize = CameraSizeUtils.findBestPictureSize(pictureSizes, mParams.getPictureSize(), mRatio);
            }
            mParams.setPictureSize(mBestPictureSize.width, mBestPictureSize.height);
            Camera.Size optionSize = CameraSizeUtils.getOptimalPreviewSize(mPreviewSizes, mSurfaceview.getWidth(), mSurfaceview.getHeight());//获取一个最为适配的camera.size
            mParams.setPreviewSize(optionSize.width, optionSize.height);//把camera.size赋值到parameters
            //获取值
            getSupportedWhiteBalance();
            getSupportedColorEffects();
            getSupportedSceneModes();
            //设置值回去
            mCamera.setParameters(mParams);
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
####　录制的设置

        try {
                    mCamera.unlock();
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setCamera(mCamera);
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    mediaRecorder.setMaxDuration(maxDurationInMs);
                    tempFile = new File(FileUtils.PHOTO_PATH,FileUtils.getVideoFileName());
                    mediaRecorder.setOutputFile(tempFile.getPath());
                    mediaRecorder.setProfile(CamcorderProfile.get(quality));
                    mediaRecorder.setPreviewDisplay(mSurfaceview.getHolder().getSurface());
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                    return true;
                } catch (IllegalStateException e) {
        //            Log.e(TAG,e.getMessage());
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
        //            Log.e(TAG,e.getMessage());
                    e.printStackTrace();
                    return false;
                }
#### 以上主要是摄像部分，还有部分摄像头设置，分辨率设置可以在`CameeraPresent`中找到方法
#### 还有一部分是娱乐部分，主要实现了视频列表，图片列表，笑话列表，美文列表，`litepal`数据库实现收藏保存。一个App的模型几乎显现。
#### 采用了`Android 5.0`的`Design`风格，部分主要控件都有使用`Cardview`,`Recyclerview`,`CoordinatorLayout`,`AppBarLayout`,`CollapsingToolbarLayout`,`NestedScrollView`。
#### 模式切换，使用了`Android`自带的模式切换，实现了夜间模式和白天模式，具体可见如下链接

[github链接](https://github.com/yukunkun/DayAndNight)
[简书链接](http://www.jianshu.com/p/f1c09e483b11)

#### 智能聊天机器人模块：采用了图灵的第三方机器人聊天实现，智能回复
#### `litepal`数据库，一个郭霖维护第三方的数据库实现
[LitePal链接](https://github.com/LitePalFramework/LitePal)

#### 部分接口实现来自于内涵段子App,聚合数据API.
#### 下载运行可预览实现
#### 上图可预览
![](http://upload-images.jianshu.io/upload_images/3001453-3a30ffd6b470dee7.jpg?)-![](http://upload-images.jianshu.io/upload_images/3001453-33b75326d448d7f9.jpg)-![](http://upload-images.jianshu.io/upload_images/3001453-5cb4315580af23c0.jpg)-![](http://upload-images.jianshu.io/upload_images/3001453-ce60019a08194fa4.jpg)-![](http://upload-images.jianshu.io/upload_images/3001453-a16b57a2e7197204.jpg)-![](http://upload-images.jianshu.io/upload_images/3001453-f89d1ffba6334c9a.jpg)
![S71220-10102740.jpg](http://upload-images.jianshu.io/upload_images/3001453-4369d82352dd90b4.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)-![](http://upload-images.jianshu.io/upload_images/3001453-73cb4eb040476ea4.jpg)-![](http://upload-images.jianshu.io/upload_images/3001453-144428a649f48d44.jpg)-![](http://upload-images.jianshu.io/upload_images/3001453-c8d01c3518cdf204.jpg)-![](http://upload-images.jianshu.io/upload_images/3001453-5b8a7bfd73a53f7a.jpg)-![](http://upload-images.jianshu.io/upload_images/3001453-77068ddf42e34b9e.jpg)
####　用到的第三方库

        compile 'com.jakewharton:butterknife:8.4.0'
        compile 'com.github.chrisbanes.photoview:library:1.2.4'
        compile 'com.github.bumptech.glide:glide:3.6.1'
        compile 'cn.jzvd:jiaozivideoplayer:6.0.2'
        compile 'com.lyf:yflibrary:1.0.2'
        compile 'com.android.support:design:25.3.1'
        compile 'de.hdodenhof:circleimageview:2.0.0'
        compile 'org.litepal.android:core:1.6.0'
        compile 'com.zhy:okhttputils:2.6.2'
        compile 'com.google.code.gson:gson:2.8.2'
        compile 'com.android.support:cardview-v7:25.3.1'
        compile 'com.haozhang.libary:android-slanted-textview:1.2'
        compile 'com.bigkoo:convenientbanner:2.0.5'
        compile 'org.greenrobot:eventbus:3.1.1'










