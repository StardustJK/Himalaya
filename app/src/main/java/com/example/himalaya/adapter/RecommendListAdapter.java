package com.example.himalaya.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.himalaya.R;
import com.example.himalaya.utils.LogUtil;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.innerHolder> {

    private List<Album> mData=new ArrayList<>();
    private static final String TAG="RecommendListAdapter";
    private OnRecommendItemClickListener mItemClickListener=null;

    @NonNull
    @Override
    public innerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //载view
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend,parent,false);

        return new innerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull innerHolder holder, int position) {
        //设置数据
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick((Integer) v.getTag());
                }
                LogUtil.d(TAG,"holder.itemView click -->"+v.getTag());
            }
        });
        holder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if(mData!=null){
            return mData.size();
        }
        return 0;
    }

    public void setData(List<Album> albumList) {
        if(mData!=null){
            mData.clear();
            mData.addAll(albumList);
        }
        //更新UI
        notifyDataSetChanged();
    }

    public class innerHolder extends RecyclerView.ViewHolder {
        public innerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Album album) {
            //找到控件设置数据
            ImageView albumCoverIv=itemView.findViewById(R.id.album_cover);
            TextView albumTitle=itemView.findViewById(R.id.album_title_tv);
            TextView descrTv=itemView.findViewById(R.id.album_description_tv);
            TextView playCountTv=itemView.findViewById(R.id.album_play_count);
            TextView albumContentSizeTv=itemView.findViewById(R.id.album_content_size);

            albumTitle.setText(album.getAlbumTitle());
            descrTv.setText(album.getAlbumIntro());
            playCountTv.setText(album.getPlayCount()+"");
            albumContentSizeTv.setText(album.getIncludeTrackCount()+"");

            Picasso.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(albumCoverIv);



        }
    }
    public void setOnRecommendItemClickListener(OnRecommendItemClickListener listener){
        this.mItemClickListener=listener;
    }
    public interface OnRecommendItemClickListener{
        void onItemClick(int position);
    }
}
