package com.cxria.Media.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cxria.Media.R;
import com.cxria.Media.entity.JokeInfo;
import com.cxria.Media.play.JokeDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 17-11-20.
 */

public class JokeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<JokeInfo> jokeInfoList;


    public JokeAdapter(Context context, List<JokeInfo> jokeInfoList) {
        this.context = context;
        this.jokeInfoList = jokeInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.joke_layout_item, null);
        return new MHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MHolder){
            ((MHolder) holder).mTvJoke.setText(jokeInfoList.get(position).getContent());
            ((MHolder) holder).mTvTime.setText(jokeInfoList.get(position).getUpdatetime());
            ((MHolder) holder).mTvName.setText("小明");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, JokeDetailActivity.class);
                    intent.putExtra("content",jokeInfoList.get(position).getContent());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return jokeInfoList.size();
    }

    class MHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ci_head)
        CircleImageView mCiHead;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_joke)
        TextView mTvJoke;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
