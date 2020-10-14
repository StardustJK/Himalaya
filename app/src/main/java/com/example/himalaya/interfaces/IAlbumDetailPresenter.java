package com.example.himalaya.interfaces;

import com.example.himalaya.base.IBasePresenter;

public interface IAlbumDetailPresenter extends IBasePresenter<IAlbumDetailViewCallBack> {


    //下拉加载更多
    void pull2RefreshMore();
    //上拉加载更多
    void loadMore();

    //获取专辑详情
    void getAlumDetail(int albumId,int page);



}
