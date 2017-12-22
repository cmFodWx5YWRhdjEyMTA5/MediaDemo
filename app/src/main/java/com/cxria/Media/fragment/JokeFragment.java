package com.cxria.Media.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cxria.Media.BaseFragment;
import com.cxria.Media.R;
import com.cxria.Media.adapter.JokeAdapter;
import com.cxria.Media.entity.JokeInfo;
import com.cxria.Media.netutils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by yukun on 17-11-17.
 */

public class JokeFragment extends BaseFragment {
    String url = "http://api.avatardata.cn/Joke/QueryJokeByTime";
    int page = 1;
    @BindView(R.id.rv_joke)
    RecyclerView mRvJoke;
    @BindView(R.id.sw)
    SwipeRefreshLayout mSw;
    List<JokeInfo> jokeInfoList=new ArrayList<>();
    private JokeAdapter mJokeAdapter;
    private long mTime;
    private LinearLayoutManager mLayoutManager;

    public static JokeFragment getInstance() {
        JokeFragment recFragment = new JokeFragment();
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
        mJokeAdapter = new JokeAdapter(getContext(),jokeInfoList);
        mRvJoke.setAdapter(mJokeAdapter);
        getInfo();
        setListener();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDragCallBack());
        itemTouchHelper.attachToRecyclerView(mRvJoke);
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
                .addParams("key", NetworkUtils.APPKEY)
                .addParams("time", mTime + "")
                .addParams("sort", "desc")
                .addParams("page", page + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getContext(), "请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.optJSONArray("result");
                    if(data!=null){
                        Gson gson = new Gson();
                        List<JokeInfo> jokeList = gson.fromJson(data.toString(), new TypeToken<List<JokeInfo>>() {
                        }.getType());
                        jokeInfoList.addAll(jokeList);
                        mJokeAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    class ItemDragCallBack extends ItemTouchHelper.Callback {
        //通过返回值来设置是否处理某次拖曳或者滑动事件
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            } else {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                //注意：和拖曳的区别就是在这里
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
//                int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

//            dragFlags 是拖拽标志，
//            swipeFlags 是滑动标志，
//            swipeFlags 都设置为0，暂时不考虑滑动相关操作。
        }

        //当长按并进入拖曳状态时，拖曳的过程中不断的回调此方法
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //拖动的 item 的下标
            int fromPosition = viewHolder.getAdapterPosition();
            //目标 item 的下标，目标 item 就是当拖曳过程中，不断和拖动的 item 做位置交换的条目。
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(jokeInfoList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(jokeInfoList, i, i - 1);
                }
            }
            mJokeAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        //滑动删除的回调
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int adapterPosition = viewHolder.getAdapterPosition();
            mJokeAdapter.notifyItemRemoved(adapterPosition);
            jokeInfoList.remove(adapterPosition);

        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                //给被拖曳的 item 设置一个深颜色背景
                viewHolder.itemView.setBackgroundColor(Color.GRAY);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            //给已经完成拖曳的 item 恢复开始的背景。
            //这里我们设置的颜色尽量和你 item 在 xml 中设置的颜色保持一致
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

}
