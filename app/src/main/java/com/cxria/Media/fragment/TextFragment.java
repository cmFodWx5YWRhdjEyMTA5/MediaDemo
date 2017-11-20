package com.cxria.Media.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cxria.Media.BaseFragment;
import com.cxria.Media.R;
import com.cxria.Media.adapter.JokeAdapter;
import com.cxria.Media.adapter.TextAdapter;
import com.cxria.Media.entity.JokeInfo;
import com.cxria.Media.entity.TextInfo;
import com.cxria.Media.netutils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

public class TextFragment extends BaseFragment {
    String url = "http://api.avatardata.cn/QiWenNews/Query";
    int page = 1;
    @BindView(R.id.rv_joke)
    RecyclerView mRvJoke;
    @BindView(R.id.sw)
    SwipeRefreshLayout mSw;
    List<TextInfo> jokeInfoList=new ArrayList<>();
    private TextAdapter mTextAdapter;
    private long mTime;
    private LinearLayoutManager mLayoutManager;
    private static String APPKEY="29d35d2d909845fd91dd71d12a460723";

    public static TextFragment getInstance() {
        TextFragment recFragment = new TextFragment();
        return recFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_rec;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {
        mTime = System.currentTimeMillis() / 1000;
        mLayoutManager = new LinearLayoutManager(getContext());
        mRvJoke.setLayoutManager(mLayoutManager);
        mTextAdapter = new TextAdapter(getContext(),jokeInfoList);
        mRvJoke.setAdapter(mTextAdapter);
        getInfo();
        setListener();
    }

    private void setListener() {
        mSw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jokeInfoList.clear();
                mTextAdapter.notifyDataSetChanged();
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
                .addParams("key", APPKEY)
                .addParams("rows", 20+"")
                .addParams("page", page + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i("response",response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.optJSONArray("result");
                    if(data!=null){
                        Gson gson = new Gson();
                        List<TextInfo> jokeList = gson.fromJson(data.toString(), new TypeToken<List<TextInfo>>() {
                        }.getType());
                        jokeInfoList.addAll(jokeList);
                        Log.i("----",jokeList.toString());
                        mTextAdapter.notifyDataSetChanged();
                    }
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
