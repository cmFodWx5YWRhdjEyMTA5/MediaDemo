package com.cxria.Media.play;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cxria.Media.BaseActivity;
import com.cxria.Media.R;
import com.cxria.Media.fragment.AboutUsFragment;
import com.cxria.Media.fragment.MyCollectFragment;
import com.cxria.Media.fragment.PlayFragment;
import com.cxria.Media.video.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PlayMainActivity extends BaseActivity {

    @BindView(R.id.rg)
    RadioGroup mRg;
    private List<Fragment> mFragments=new ArrayList<>();
    private int lastPos=0;
    private boolean isNight;

    @Override
    public int getLayout() {
        return R.layout.activity_play_main;
    }

    @Override
    public void initView() {
        PlayFragment playFragment=PlayFragment.getInstance();
        mFragments.add(playFragment);
        MyCollectFragment myCollectFragment= MyCollectFragment.getInstance();
        mFragments.add(myCollectFragment);
        AboutUsFragment aboutUsFragment=AboutUsFragment.getInstance();
        mFragments.add(aboutUsFragment);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_layout, playFragment);
        fragmentTransaction.commit();

        ((RadioButton) (mRg.getChildAt(0))).setChecked(true);
        setListener();
    }

    private void setListener() {
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.index:
                        ((RadioButton) (mRg.getChildAt(0))).setChecked(true);
                        show(0);
                        break;
                    case R.id.collect:
                        ((RadioButton) (mRg.getChildAt(1))).setChecked(true);
                        show(1);
                        break;
                    case R.id.me:
                        ((RadioButton) (mRg.getChildAt(2))).setChecked(true);
                        show(2);
                        break;
                }
            }
        });
    }

        public void setNightMode() {
        //  获取当前模式
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        //  将是否为夜间模式保存到SharedPreferences
        if(currentNightMode==32){
            isNight=true;
        }else {
            isNight=false;
        }

        getDelegate().setDefaultNightMode(isNight ?
                AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
        //  重启Activity
        recreate();
        //延迟导致重启一个destory的Activity，会失败。
        saveModule(isNight);
    }
    private void saveModule(boolean isNight) {
        SharedPreferences sharedPreferences = getSharedPreferences("module", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putBoolean("isNight", isNight);
        editor.commit();//提交修改
    }

    /**
     * fragment 的show和hide
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

}
