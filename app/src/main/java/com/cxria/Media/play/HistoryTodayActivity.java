package com.cxria.Media.play;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cxria.Media.BaseActivity;
import com.cxria.Media.R;
import com.cxria.Media.adapter.HistoryAdapter;
import com.cxria.Media.adapter.LVAdapter;
import com.cxria.Media.entity.HistoryInfo;
import com.cxria.Media.entity.SortModel;
import com.cxria.Media.netutils.NetworkUtils;
import com.cxria.Media.utils.Cn2Spell;
import com.cxria.Media.utils.ISideBarSelectCallBack;
import com.cxria.Media.utils.PinyinComparator;
import com.cxria.Media.views.SideBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    ListView mListview;
    @BindView(R.id.bar)
    SideBar mBar;

    private LinearLayoutManager mLayoutManager;
    private List<HistoryInfo> mList = new ArrayList<>();
    private String APPKEY = "1c88cd3077c448b199b227a5eccca0ed";
    private HistoryAdapter mHistoryAdapter;
    private List<SortModel> mSortModule;


    @Override
    public int getLayout() {
        return R.layout.activity_history_today;
    }

    @Override
    public void initView() {
        mHistoryAdapter = new HistoryAdapter(this, mList);
        getInfo();
        mBar.setOnStrSelectCallBack(new ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < mSortModule.size() ; i++){
                    if(mSortModule.get(i).getSortLetters().equals(selectStr)){
                        mListview.setSelection(i);
                        return;
                    }
                }
            }

            @Override
            public void onSelectEnd() {

            }

            @Override
            public void onSelectStart() {

            }
        });
    }

    private void getInfo() {
        String url = "http://api.avatardata.cn/HistoryToday/LookUp";
        Calendar CD = Calendar.getInstance();
        int MM = CD.get(Calendar.MONTH) + 1;
        int DD = CD.get(Calendar.DATE);
        NetworkUtils.networkGet(url)
                .addParams("key", APPKEY)
                .addParams("yue", MM + "")
                .addParams("ri", DD + "")
                .addParams("type", 1 + "")
                .addParams("rows", 50 + "")
                .addParams("page", 1 + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(HistoryTodayActivity.this, "请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    Log.i("00", response);
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.optJSONArray("result");
                    if (data != null) {
                        Gson gson = new Gson();
                        List<HistoryInfo> jokeList = gson.fromJson(data.toString(), new TypeToken<List<HistoryInfo>>() {
                        }.getType());
                        mList.addAll(jokeList);

                        mSortModule = getSortModule();
                        LVAdapter lvAdapter = new LVAdapter(mSortModule, HistoryTodayActivity.this);
                        mListview.setAdapter(lvAdapter);
                        mHistoryAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private List<SortModel> getSortModule() {

        List<SortModel> filterDateList = new ArrayList<SortModel>();
        for (int i = 0; i < mList.size(); i++) {
            String pinYinFirstLetter = Cn2Spell.getPinYinFirstLetter(mList.get(i).getTitle());
            SortModel sortModel = new SortModel(mList.get(i).getTitle(), pinYinFirstLetter.toUpperCase().charAt(0) + "", mList.get(i).getYear(), mList.get(i).getMonth(), mList.get(i).getDay());
            filterDateList.add(sortModel);
        }
        Collections.sort(filterDateList, new PinyinComparator());
        return filterDateList;
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
        overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        overridePendingTransition(R.anim.rotate, R.anim.rotate_out);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
