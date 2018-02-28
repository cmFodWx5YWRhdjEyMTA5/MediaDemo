package com.cxria.media.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.cxria.media.adapter.KnowledgeAdapter;
import com.cxria.media.entity.KnowledgeInfo;
import com.cxria.media.views.SwipeItemLayout;
import com.google.gson.Gson;
import com.cxria.media.BaseFragment;
import com.cxria.media.R;
import com.cxria.media.common.Constanct;
import com.cxria.media.utils.ActivityUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import okhttp3.Call;

/**
 * Created by yukun on 18-1-4.
 */

public class KnowledgeFragment extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.swipeItemLayout)
    SwipeRefreshLayout mSwipeItemLayout;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.iv_me)
    ImageView mIvMe;
    private KnowledgeAdapter mKnowledgeAdapter;
    private List<KnowledgeInfo> mKnowledgeInfos = new ArrayList<>();

    public static KnowledgeFragment getInstance() {
        return new KnowledgeFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.knowledge_fragment;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerview.setLayoutManager(layoutManager);

        setListener();
        mRecyclerview.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getContext()));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        getInfo();
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerview, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

    }

    private void getInfo() {
        OkHttpUtils.get().url(Constanct.TREEURL).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                KnowledgeInfo jokeList = gson.fromJson(response, KnowledgeInfo.class);
                mKnowledgeAdapter = new KnowledgeAdapter(jokeList, getContext());
                mRecyclerview.setAdapter(mKnowledgeAdapter);
            }
        });
    }

    private void setListener() {
        mSwipeItemLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeItemLayout.setRefreshing(false);
            }
        });
    }


    @OnClick({R.id.iv_search, R.id.iv_me})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                ActivityUtils.startSearchkActivity(getContext(),"");
                break;
            case R.id.iv_me:
                ActivityUtils.startMeActivity(getContext());
                break;
        }
    }
}
