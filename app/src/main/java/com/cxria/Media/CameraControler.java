package com.cxria.Media;

import android.content.Context;
import android.view.SurfaceView;

import java.util.List;

/**
 * Created by yukun on 17-9-29.
 */

public interface CameraControler {
    interface presenr{
        void init(SurfaceView surface);
        void openCamer(Context context);
        void closeCamer();
        void changeCamera();
        void openBline();
        void getPhoto();
    }

    interface view{
        void initLayout(List<String> mWidthList);
        void initScreenLayout(List<String> mScreenList);
        void initCameraList(List<String> listColor);
    }
}
