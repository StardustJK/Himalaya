package com.example.himalaya.interfaces;

public interface IAlbumDetailPresenter {


    //下拉加载更多
    void pull2RefreshMore();
    //上拉加载更多
    void loadMore();

    //获取专辑详情
    void getAlumDetail(int albumId,int page);

    void registerViewCallback(IAlbumDetailViewCallBack detailViewCallBack);

    void unRegisterViewCallback(IAlbumDetailViewCallBack detailViewCallBack);


}
