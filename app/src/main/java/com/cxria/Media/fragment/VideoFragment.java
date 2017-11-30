package com.cxria.Media.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cxria.Media.BaseFragment;
import com.cxria.Media.R;
import com.cxria.Media.adapter.RecAdapter;
import com.cxria.Media.adapter.VideoAdapter;
import com.cxria.Media.entity.RecInfo;
import com.cxria.Media.netutils.NetworkUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yukun on 17-11-17.
 */

public class VideoFragment extends BaseFragment {
    String url = "http://lf.snssdk.com/neihan/stream/mix/v1/?content_type=-104";
    int page = 1;
    @BindView(R.id.rv_joke)
    RecyclerView mRvJoke;
    @BindView(R.id.sw)
    SwipeRefreshLayout mSw;
    List<RecInfo> jokeInfoList=new ArrayList<>();
    private VideoAdapter mJokeAdapter;
    private LinearLayoutManager mLayoutManager;

    public static VideoFragment getInstance() {
        VideoFragment recFragment = new VideoFragment();
        return recFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_rec;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRvJoke.setLayoutManager(mLayoutManager);
        mJokeAdapter = new VideoAdapter(getContext(),jokeInfoList);
        mRvJoke.setAdapter(mJokeAdapter);
        getInfo();
        setListener();
    }

    private void setListener() {
        mSw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jokeInfoList.clear();
                mJokeAdapter.notifyDataSetChanged();
                page=1;
                getInfo();
                mSw.setRefreshing(false);
            }
        });

        mRvJoke.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
                if(lastVisibleItemPosition==mLayoutManager.getItemCount()-1){
                    page++;
                    getInfo();
                }
            }
        });
    }

    private void getInfo() {
        NetworkUtils.networkGet(url)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject dataArr = jsonObject.optJSONObject("data");
                    JSONArray data = dataArr.optJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        RecInfo recInfo=new RecInfo();
                        JSONObject jsonObject1 = data.optJSONObject(i);
                        JSONObject group = jsonObject1.optJSONObject("group");
                        if(group.optString("text")!=null){
                            recInfo.setText(group.optString("text"));
                        }else {
                            recInfo.setText("不知道取个啥名！");
                        }
                        JSONObject origin_video = group.optJSONObject("origin_video");
                        JSONArray origin_video_arr = origin_video.optJSONArray("url_list");

                        recInfo.setPlay_url(origin_video_arr.optJSONObject(0).optString("url"));

                        recInfo.setShare_url(group.optString("share_url"));
                        recInfo.setCreate_time(group.optString("create_time"));
                        recInfo.setPlay_time(group.optLong("play_count"));

                        JSONObject user = group.optJSONObject("user");
                        recInfo.setUser_name(user.optString("name"));
                        recInfo.setHeader(user.optString("avatar_url"));

                        JSONObject covers = group.optJSONObject("large_cover");
                        JSONArray cover_url = covers.optJSONArray("url_list");
                        recInfo.setCover(cover_url.optJSONObject(0).optString("url"));
                        jokeInfoList.add(recInfo);
                    }
                    mJokeAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
