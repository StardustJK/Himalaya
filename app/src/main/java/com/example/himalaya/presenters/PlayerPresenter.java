package com.example.himalaya.presenters;


import com.example.himalaya.base.BaseApplication;
import com.example.himalaya.interfaces.IPlayerCallback;
import com.example.himalaya.interfaces.IPlayerPresenter;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

public class PlayerPresenter implements IPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {

    private List<IPlayerCallback> mIPlayerCallbacks = new ArrayList<>();

    private static final String TAG = "PlayerPresenter";
    private XmPlayerManager mPlayerManager;
    private Track mCurrentTrack;
    private int mCurrentIndex=0;

    private PlayerPresenter() {
        mPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        //广告相关的接口
        mPlayerManager.addAdsStatusListener(this);
        //注册播放器状态相关接口
        mPlayerManager.addPlayerStatusListener(this);

    }

    private static PlayerPresenter sPlayerPresenter;

    public static PlayerPresenter getPlayerPresenter() {
        if (sPlayerPresenter == null) {
            synchronized (PlayerPresenter.class) {
                if (sPlayerPresenter == null) {
                    sPlayerPresenter = new PlayerPresenter();
                }
            }
        }

        return sPlayerPresenter;
    }

    private boolean isPlayListSet = false;

    public void setPLayList(List<Track> trackList, int playIndex) {
        if (mPlayerManager != null) {
            mPlayerManager.setPlayList(trackList, playIndex);
            isPlayListSet = true;
            mCurrentTrack=trackList.get(playIndex);
            mCurrentIndex=playIndex;

        } else {
            LogUtil.d(TAG, "mPlayerManager is null");
        }
    }

    @Override
    public void play() {

        if (isPlayListSet) {
            mPlayerManager.play();
        }
    }

    @Override
    public void pause() {
        if (mPlayerManager != null) {
            mPlayerManager.pause();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void playPre() {
        if (mPlayerManager != null) {
            mPlayerManager.playPre();
        }
    }

    @Override
    public void playNext() {
        if (mPlayerManager != null) {
            mPlayerManager.playNext();
        }
    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void getPlayList() {
        if (mPlayerManager != null) {
            List<Track> trackList=mPlayerManager.getPlayList();
            for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                iPlayerCallback.onListLoad(trackList);
            }
        }
    }

    @Override
    public void playByIndex(int index) {
        //切换播放器到index的位置
        if (mPlayerManager != null) {
            mPlayerManager.play(index);
        }
    }

    @Override
    public void seekTo(int progress) {
        if (mPlayerManager != null) {
            mPlayerManager.seekTo(progress);
        }
    }

    @Override
    public boolean isPlay() {
        return mPlayerManager.isPlaying();
    }

    @Override
    public void registerViewCallback(IPlayerCallback iPlayerCallback) {
        iPlayerCallback.onTrackUpdate(mCurrentTrack,mCurrentIndex);
        if (!mIPlayerCallbacks.contains(iPlayerCallback)) {
            mIPlayerCallbacks.add(iPlayerCallback);
        }
    }

    @Override
    public void unRegisterViewCallback(IPlayerCallback iPlayerCallback) {
        mIPlayerCallbacks.remove(iPlayerCallback);
    }

    //==========广告相关的回调方法 start============//

    @Override
    public void onStartGetAdsInfo() {
        LogUtil.d(TAG, "onStartGetAdsInfo");
    }

    @Override
    public void onGetAdsInfo(AdvertisList advertisList) {
        LogUtil.d(TAG, "onGetAdsInfo");

    }

    @Override
    public void onAdsStartBuffering() {
        LogUtil.d(TAG, "onAdsStartBuffering");

    }

    @Override
    public void onAdsStopBuffering() {
        LogUtil.d(TAG, "onAdsStopBuffering");

    }

    @Override
    public void onStartPlayAds(Advertis advertis, int i) {
        LogUtil.d(TAG, "onStartPlayAds");

    }

    @Override
    public void onCompletePlayAds() {
        LogUtil.d(TAG, "onCompletePlayAds");

    }

    @Override
    public void onError(int what, int extra) {
        LogUtil.d(TAG, "onError  what=" + what + "  extra=" + extra);

    }
    //==========广告相关的回调方法 end============//


    //==========播放器相关的回调方法 start============//

    @Override
    public void onPlayStart() {
        LogUtil.d(TAG, "onPlayStart");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
            iPlayerCallback.onPlayStart();
        }
    }

    @Override
    public void onPlayPause() {
        LogUtil.d(TAG, "onPlayPause");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
            iPlayerCallback.onPlayPause();
        }
    }

    @Override
    public void onPlayStop() {
        LogUtil.d(TAG, "onPlayStop");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
            iPlayerCallback.onPlayStop();
        }

    }

    @Override
    public void onSoundPlayComplete() {
        LogUtil.d(TAG, "onSoundPlayComplete");

    }

    @Override
    public void onSoundPrepared() {
        LogUtil.d(TAG, "onSoundPrepared");
        if (mPlayerManager.getPlayerStatus()== PlayerConstants.STATE_PREPARED) {
            mPlayerManager.play();
        }

    }

    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {
        LogUtil.d(TAG, "onSoundSwitch");
        if (lastModel != null) {

        }
        mCurrentIndex=mPlayerManager.getCurrentIndex();

        if(curModel instanceof Track){
            mCurrentTrack=(Track) curModel;
            LogUtil.i(TAG,"title"+mCurrentTrack.getTrackTitle());
            for (IPlayerCallback iPlayerCallback:mIPlayerCallbacks){
                iPlayerCallback.onTrackUpdate(mCurrentTrack,mCurrentIndex);
            }
        }

    }

    @Override
    public void onBufferingStart() {
        LogUtil.d(TAG, "onBufferingStart");

    }

    @Override
    public void onBufferingStop() {
        LogUtil.d(TAG, "onBufferingStop");

    }

    @Override
    public void onBufferProgress(int i) {
        LogUtil.d(TAG, "onBufferProgress:" + i);

    }

    @Override
    public void onPlayProgress(int currentPos, int duration) {
        //单位是毫秒
        LogUtil.d(TAG, "onPlayProgress");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
            iPlayerCallback.onProgressChange(currentPos, duration);
        }

    }

    @Override
    public boolean onError(XmPlayerException e) {
        LogUtil.d(TAG, "onError e----》" + e);

        return false;
    }
    //==========播放器相关的回调方法 end============//

}
