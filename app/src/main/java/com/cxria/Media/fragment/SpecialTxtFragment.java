package com.cxria.Media.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cxria.Media.BaseFragment;
import com.cxria.Media.R;
import com.cxria.Media.entity.TextInfo;
import com.cxria.Media.netutils.NetworkUtils;
import com.cxria.Media.play.TextDetailActivity;
import com.cxria.Media.views.AMzItemLayout;
import com.cxria.Media.views.DataImportUtils;
import com.cxria.Media.views.DataInfo;
import com.cxria.Media.views.RetailMeNotLayout;
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
 * Created by yukun on 17-12-5.
 */

public class SpecialTxtFragment extends BaseFragment {
    @BindView(R.id.l_retail_me_not)
    RetailMeNotLayout mLRetailMeNot;
    String url = "http://v3.wufazhuce.com:8000/api/onelist/idlist/?channel=wdj&version=4.0.2&uuid=ffffffff-a90e-706a-63f7-ccf973aae5ee&platform=android 或 http://v3.wufazhuce.com:8000/api/onelist/idlist/?channel=wdj&version=4.0.2&uuid=ffffffff-a90e-706a-63f7-ccf973aae5ee&platform=android";
    private int total;
    private JSONArray mData;
    int page = 0;
    List<TextInfo> jokeInfoList=new ArrayList<>();
    private RetailMeNotAdapter mAdapter;
    private String mUrl;

    public static SpecialTxtFragment getInstance() {
        SpecialTxtFragment recFragment = new SpecialTxtFragment();
        return recFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.special_list_fragment;
    }

    @Override
    public void initView(View inflate, Bundle savedInstanceState) {

        TextView tMoreInfo = new TextView(getContext());
        tMoreInfo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        tMoreInfo.setGravity(Gravity.CENTER);
        tMoreInfo.setTextSize(20);
        tMoreInfo.setTextColor(getContext().getResources().getColor(R.color.color_black));
        tMoreInfo.setBackgroundResource((R.color.color_write));
        tMoreInfo.setText("到底了，亲　-_-");
        mLRetailMeNot.addBottomContent(tMoreInfo);
        mAdapter = new RetailMeNotAdapter(getContext());
        mLRetailMeNot.setAdapter(mAdapter);
        getInfo();
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
                    mData = jsonObject.optJSONArray("data");
                    total= mData.length();
                    if(mData !=null){
                        getOneDayInfo(mData);
                        Log.i("data", mData.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getOneDayInfo(JSONArray data) {
        mUrl = "http://v3.wufazhuce.com:8000/api/onelist/ "+ data.optInt(page) + "/0?cchannel=wdj&version=4.0.2&uuid=ffffffff-a90e-706a-63f7-ccf973aae5ee&platform=android";
        getAllMsg(mUrl);
    }

    private void getAllMsg(final String url) {
        NetworkUtils.networkGet(url)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT).show();
                Log.i("err",e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.optJSONObject("data");
                    JSONArray content_list = data.optJSONArray("content_list");
                    Gson gson = new Gson();
                    List<TextInfo> jokeList = gson.fromJson(content_list.toString(), new TypeToken<List<TextInfo>>() {
                    }.getType());
                    jokeInfoList.addAll(jokeList);
                    page++;

                    if(page>=total){
                        mAdapter.notifyDataSetChanged();
                        return;
                    }else {
                        getAllMsg(mUrl);
                        mUrl = "http://v3.wufazhuce.com:8000/api/onelist/ "+ mData.optInt(page) + "/0?cchannel=wdj&version=4.0.2&uuid=ffffffff-a90e-706a-63f7-ccf973aae5ee&platform=android";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private class RetailMeNotAdapter extends RetailMeNotLayout.Adapter {

        Context context;
        public RetailMeNotAdapter(Context context){
            this.context=context;
        }
        @Override
        public View getView(final int position, ViewGroup parent, int expandedHeight, int normalHeight) {
            AMzItemLayout item = new AMzItemLayout(parent.getContext());
            item.setData(position, jokeInfoList.get(position), expandedHeight, normalHeight);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, TextDetailActivity.class);
                    intent.putExtra("url",jokeInfoList.get(position).getShare_url());
                    intent.putExtra("title",jokeInfoList.get(position).getTitle());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    ((Activity)context).overridePendingTransition(R.anim.rotate,R.anim.rotate_out);
                }
            });
            return item;
        }

        @Override
        public int getCount() {
            return jokeInfoList.size();
        }
    }
}
