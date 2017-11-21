package com.cxria.Media.play;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cxria.Media.BaseActivity;
import com.cxria.Media.R;
import com.cxria.Media.adapter.HistoryAdapter;
import com.cxria.Media.adapter.ImageAdapter;
import com.cxria.Media.entity.HistoryInfo;
import com.cxria.Media.entity.JokeInfo;
import com.cxria.Media.netutils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class HistoryTodayActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rl)
    RelativeLayout mRl;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    private LinearLayoutManager mLayoutManager;
    private List<HistoryInfo> mList=new ArrayList<>();
    private String APPKEY="1c88cd3077c448b199b227a5eccca0ed";
    private HistoryAdapter mHistoryAdapter;

    @Override
    public int getLayout() {
        return R.layout.activity_history_today;
    }

    @Override
    public void initView() {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mHistoryAdapter = new HistoryAdapter(this,mList);
        mRecyclerview.setAdapter(mHistoryAdapter);
        getInfo();
    }

    private void getInfo() {
        String url="http://api.avatardata.cn/HistoryToday/LookUp";
        Calendar CD = Calendar.getInstance();
        int MM = CD.get(Calendar.MONTH)+1;
        int DD = CD.get(Calendar.DATE);
        NetworkUtils.networkGet(url)
                .addParams("key", APPKEY)
                .addParams("yue",  MM+ "")
                .addParams("ri",  DD+ "")
                .addParams("type", 1+ "")
                .addParams("rows", 50+"")
                .addParams("page", 1+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(HistoryTodayActivity.this, "请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    Log.i("00",response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.optJSONArray("result");
                    if(data!=null){
                        Gson gson = new Gson();
                        List<HistoryInfo> jokeList = gson.fromJson(data.toString(), new TypeToken<List<HistoryInfo>>() {
                        }.getType());
                        mList.addAll(jokeList);
                        mHistoryAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
