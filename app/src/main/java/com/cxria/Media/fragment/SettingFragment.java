package com.cxria.Media.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cxria.Media.R;
import com.cxria.Media.entity.EventCategrayPos;
import com.cxria.Media.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yukun on 17-12-26.
 */

public class SettingFragment extends DialogFragment {

    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.ll_lin)
    LinearLayout mLlLin;
    @BindView(R.id.ll_gril)
    LinearLayout mLlGril;

    public static SettingFragment getInstance() {
        SettingFragment recFragment = new SettingFragment();
        return recFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景透明
        View inflate = inflater.inflate(R.layout.setting_frag_layout, null);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onStart() {
        super.onStart();
        //得到dialog对应的window
        Window window = getDialog().getWindow();
        if (window != null) {
            //得到LayoutParams
            WindowManager.LayoutParams params = window.getAttributes();
            //修改gravity
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height =WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }

    @OnClick({R.id.iv_close, R.id.ll_lin, R.id.ll_gril,R.id.ll_call})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                getDialog().dismiss();
                break;
            case R.id.ll_lin:
                EventBus.getDefault().post(new EventCategrayPos(1001));
                getDialog().dismiss();
                break;
            case R.id.ll_gril:
                EventBus.getDefault().post(new EventCategrayPos(1002));
                getDialog().dismiss();
                break;
            case R.id.ll_call:
                getDialog().dismiss();
                break;
        }
    }
}
