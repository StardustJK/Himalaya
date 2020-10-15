package com.example.himalaya;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.himalaya.base.BaseActivity;
import com.example.himalaya.interfaces.IPlayerCallback;
import com.example.himalaya.presenters.PlayerPresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public class PlayerActivity extends BaseActivity implements IPlayerCallback {

    private ImageView controlBtn;
    private PlayerPresenter mPlayerPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mPlayerPresenter=PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallback(this);
        initView();
        initEvent();
        startPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unRegisterViewCallback(this);
            mPlayerPresenter=null;
        }
    }

    /**
     * 开始播放
     */
    private void startPlay() {
        if (mPlayerPresenter != null) {
            mPlayerPresenter.play();
        }
    }

    /**
     * 设置相关事件
     */
    private void initEvent() {

        controlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPlayerPresenter.isPlay()){
                    mPlayerPresenter.pause();
                }
                else{
                    mPlayerPresenter.play();
                }
            }
        });
    }

    private void initView() {
        controlBtn=findViewById(R.id.play_or_pause_btn);

    }


    @Override
    public void onPlayStart() {
        //开始播放，修改UI
        if (controlBtn != null) {
            controlBtn.setImageResource(R.mipmap.stop_normal);

        }

    }

    @Override
    public void onPlayPause() {
        if (controlBtn != null) {
            controlBtn.setImageResource(R.mipmap.play_normal);

        }
    }

    @Override
    public void onPlayStop() {
        if (controlBtn != null) {
            controlBtn.setImageResource(R.mipmap.play_normal);

        }
    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void onNextPlay(Track track) {

    }

    @Override
    public void onPrePlay(Track track) {

    }

    @Override
    public void onListLoad(List<Track> list) {

    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onProgressChange(long currentProgress, long total) {

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }
}
