package com.cxria.media.wanandroid;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cxria.media.common.Constanct;
import com.cxria.media.fragment.HotFragment;
import com.cxria.media.fragment.IndexFragment;
import com.cxria.media.fragment.KnowledgeFragment;
import com.cxria.media.utils.ToastUtils;
import com.cxria.media.views.BMoveView;
import com.google.gson.Gson;
import com.cxria.media.BaseActivity;
import com.cxria.media.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.rg)
    RadioGroup mRg;
    @BindView(R.id.bmoveview)
    BMoveView mBMoveView;
    private int mFirstPos;
    private int mLastPos;
    private List<Fragment> mFragments = new ArrayList<>();
    private int lastPos = 0;

    @Override
    public int getLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        return R.layout.activity_wan_main;
    }

    @Override
    public void initView() {
        IndexFragment indexFragment = IndexFragment.getInstance();
        KnowledgeFragment knowledgeFragment = KnowledgeFragment.getInstance();
        HotFragment hotFragment = HotFragment.getInstance();
//        MeFragment meFragment = MeFragment.getInstance(this);
        mFragments.add(indexFragment);
        mFragments.add(knowledgeFragment);
        mFragments.add(hotFragment);
//        mFragments.add(meFragment);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_layout, mFragments.get(0));
        fragmentTransaction.commit();
        mFirstPos = 0;
        mBMoveView.startAnim();
        ((RadioButton) (mRg.getChildAt(0))).setChecked(true);
        setListener();
    }

    private void setListener() {
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                for (int i = 0; i < group.getChildCount(); i++) {
                    boolean checked = ((RadioButton) (group.getChildAt(i))).isChecked();
                    if(checked){
                        mLastPos = i;
                        mBMoveView.setTwoPos(mFirstPos, mLastPos);
                        mFirstPos = mLastPos;
                        ((RadioButton) (mRg.getChildAt(i))).setChecked(true);
                        show(i);
                    }
                }
            }
        });
    }

    /**
     * fragment 的show和hide
     *
     * @param pos
     */
    public void show(int pos) {
        Fragment fragment = mFragments.get(pos);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(mFragments.get(lastPos));

        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.fl_layout, fragment);
        }
        fragmentTransaction.commit();
        lastPos = pos;
    }

    //双击退出
//    private long firstTime=0;
//
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        switch (keyCode){
//            case KeyEvent.KEYCODE_BACK:
//                long secondTime=System.currentTimeMillis();
//
//                if(secondTime-firstTime>2000){
//                    Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
//                    firstTime=secondTime;
//                    return true;
//                }else{
//                    finish();
//                }
//                break;
//        }
//        return super.onKeyUp(keyCode, event);
//    }
}
