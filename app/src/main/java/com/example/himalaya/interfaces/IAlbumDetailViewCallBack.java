package com.example.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public interface IAlbumDetailViewCallBack {

    //专辑详情内容加载出来了
    void onDetailListLoaded(List<Track> tracks);

    //把Album传给ui
    void onAlbumLoaded(Album album);
    //网络错误
    void onNetworkError(int errorCode,String errorMsg);
}
