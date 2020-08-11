package com.example.himalaya;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.himalaya.base.BaseActivity;
import com.example.himalaya.interfaces.IAlbumDetailPresenter;
import com.example.himalaya.interfaces.IAlbumDetailViewCallBack;
import com.example.himalaya.presenters.AlbumDetailPresenter;
import com.example.himalaya.views.RoundRectImageView;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import org.w3c.dom.Text;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallBack {

    private ImageView largeCover;
    private RoundRectImageView smallCover;
    private TextView mAlbumTitle;
    private TextView mAlbumAuthor;
    private AlbumDetailPresenter mAlbumDetailPresenter;

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
    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {

    }

    @Override
    public void onAlbumLoaded(Album album) {
        if (mAlbumTitle != null) {
            mAlbumTitle.setText(album.getAlbumTitle());
        }
        if (mAlbumAuthor != null) {
            mAlbumAuthor.setText(album.getAnnouncer().getNickname());
        }
        if (largeCover != null) {
            Picasso.with(this).load(album.getCoverUrlLarge()).into(largeCover);
        }
        if (smallCover != null) {
            Picasso.with(this).load(album.getCoverUrlSmall()).into(smallCover);
        }
    }
}
