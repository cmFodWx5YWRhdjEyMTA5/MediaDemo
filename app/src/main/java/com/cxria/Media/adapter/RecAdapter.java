package com.cxria.Media.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cxria.Media.R;
import com.cxria.Media.entity.RecInfo;
import com.cxria.Media.video.VideoPlayActivity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 17-11-20.
 */

public class RecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<RecInfo> jokeInfoList;



    public RecAdapter(Context context, List<RecInfo> jokeInfoList) {
        this.context = context;
        this.jokeInfoList = jokeInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rec_layout_item, null);
        return new MHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MHolder) {
            final RecInfo recInfo = jokeInfoList.get(position);
            ((MHolder) holder).mTvName.setText(recInfo.getUser_name());
            ((MHolder) holder).mTvTitle.setText(recInfo.getText());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String time=format.format(new Date(Long.valueOf(recInfo.getCreate_time())*1000));

            ((MHolder) holder).mTvTimes.setText(time);
            ((MHolder) holder).mTvPlayTimes.setText(recInfo.getPlay_time()+"次");
            Glide.with(context).load(recInfo.getCover()).into(((MHolder) holder).mImCover);
            Glide.with(context).load(recInfo.getHeader()).into(((MHolder) holder).mCiHead);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, VideoPlayActivity.class);
                    intent.putExtra("imagepath",recInfo.getPlay_url()+"#"+recInfo.getCover()+"#"+recInfo.getText());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            ((MHolder) holder).mImShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareSend(context,recInfo.getShare_url());
                }
            });
        }
    }

    private void shareSend(Context context,String str) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); // 纯文本
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, str);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, ""));
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
        @BindView(R.id.tv_times)
        TextView mTvTimes;
        @BindView(R.id.im_cover)
        ImageView mImCover;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_play_times)
        TextView mTvPlayTimes;
        @BindView(R.id.iv_share)
        ImageView mImShare;

        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
