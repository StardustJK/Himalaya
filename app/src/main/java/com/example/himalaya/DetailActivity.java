package com.example.himalaya;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.himalaya.adapter.DetailListAdapter;
import com.example.himalaya.base.BaseActivity;
import com.example.himalaya.interfaces.IAlbumDetailPresenter;
import com.example.himalaya.interfaces.IAlbumDetailViewCallBack;
import com.example.himalaya.presenters.AlbumDetailPresenter;
import com.example.himalaya.utils.ImageBlur;
import com.example.himalaya.utils.LogUtil;
import com.example.himalaya.views.RoundRectImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import org.w3c.dom.Text;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallBack {

    private static final String TAG = "DetailActivity";
    private ImageView largeCover;
    private RoundRectImageView smallCover;
    private TextView mAlbumTitle;
    private TextView mAlbumAuthor;
    private AlbumDetailPresenter mAlbumDetailPresenter;

    private int mCurrentPage=1;
    private RecyclerView mDetailList;
    private DetailListAdapter mDetailListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initView();
        mAlbumDetailPresenter=AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallback(this);


    }

    private void initView() {
        largeCover=findViewById(R.id.iv_large_cover);
        smallCover=findViewById(R.id.iv_small_cover);
        mAlbumTitle=findViewById(R.id.tv_album_title);
        mAlbumAuthor=findViewById(R.id.tv_album_author);
        mDetailList=findViewById(R.id.album_detail_list);
        //recyclerview使用步骤
        //1。设置布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mDetailList.setLayoutManager(linearLayoutManager);
        //2。设置适配器
        mDetailListAdapter=new DetailListAdapter();
        mDetailList.setAdapter(mDetailListAdapter);
        //设置item的上下间距
        mDetailList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top= UIUtil.dip2px(view.getContext(),2);
                outRect.bottom=UIUtil.dip2px(view.getContext(),2);
                outRect.left=UIUtil.dip2px(view.getContext(),2);
                outRect.right=UIUtil.dip2px(view.getContext(),2);

            }
        });

    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        //更新、设置UI数据
        mDetailListAdapter.setData(tracks);
    }

    @Override
    public void onAlbumLoaded(Album album) {
        //获取专辑详情内容
        mAlbumDetailPresenter.getAlumDetail((int)album.getId(),mCurrentPage);

        if (mAlbumTitle != null) {
            mAlbumTitle.setText(album.getAlbumTitle());
        }
        if (mAlbumAuthor != null) {
            mAlbumAuthor.setText(album.getAnnouncer().getNickname());
        }
        //毛玻璃效果
        if (largeCover != null) {
            //这里使用了异步网络请求，所以用callback不然可能为空
            Picasso.with(this).load(album.getCoverUrlLarge()).into(largeCover, new Callback() {
                @Override
                public void onSuccess() {
                    Drawable drawable=largeCover.getDrawable();
                    if(drawable!=null){
                        ImageBlur.makeBlur(largeCover,DetailActivity.this);
                    }
                }

                @Override
                public void onError() {

                    LogUtil.d(TAG,"on error");
                }
            });
        }
        if (smallCover != null) {
            Picasso.with(this).load(album.getCoverUrlSmall()).into(smallCover);
        }
    }
}
