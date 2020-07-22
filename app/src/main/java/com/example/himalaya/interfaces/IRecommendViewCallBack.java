package com.example.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public interface IRecommendViewCallBack {

    //获取推荐内容结果
    void onRecommendListLoaded(List<Album> result);
    //加载更多
    void onLoadMore(List<Album> result);
    //
    void onRefreshMore(List<Album> result);
}
