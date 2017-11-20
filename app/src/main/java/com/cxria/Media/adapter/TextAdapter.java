package com.cxria.Media.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cxria.Media.R;
import com.cxria.Media.entity.JokeInfo;
import com.cxria.Media.entity.TextInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yukun on 17-11-20.
 */

public class TextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<TextInfo> jokeInfoList;


    public TextAdapter(Context context, List<TextInfo> jokeInfoList) {
        this.context = context;
        this.jokeInfoList = jokeInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.text_layout_item, null);
        return new MHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MHolder){
            ((MHolder) holder).mTvName.setText(jokeInfoList.get(position).getTitle());
            Glide.with(context).load(jokeInfoList.get(position).getPicUrl()).into(((MHolder) holder).mIvCover);
        }
    }

    @Override
    public int getItemCount() {
        return jokeInfoList.size();
    }

    class MHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView mTvName;
        @BindView(R.id.iv_cover)
        ImageView mIvCover;
        public MHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
