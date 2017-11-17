package com.cxria.Media.fragment;

import com.cxria.Media.BaseFragment;
import com.cxria.Media.R;

/**
 * Created by yukun on 17-11-17.
 */

public class RecFragment extends BaseFragment {
    public static RecFragment getInstance(){
        RecFragment recFragment=new RecFragment();
        return recFragment;
    }
    @Override
    public int getLayout() {
        return R.layout.fragment_rec;
    }

    @Override
    public void initView() {

    }
}
