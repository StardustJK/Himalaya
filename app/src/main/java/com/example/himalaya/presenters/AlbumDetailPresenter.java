package com.example.himalaya.presenters;

import com.example.himalaya.interfaces.IAlbumDetailPresenter;
import com.example.himalaya.interfaces.IAlbumDetailViewCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private Album mTargetAlbum;

    private List<IAlbumDetailViewCallBack> callBacks=new ArrayList<>();
    //单例，构造方法要私有化别人才不能构造
    private AlbumDetailPresenter(){

    }

    private static AlbumDetailPresenter sInstance=null;
    public static AlbumDetailPresenter getInstance(){
        if(sInstance==null){
            //加锁防止多线程问题
            synchronized (AlbumDetailPresenter.class){
                if(sInstance==null){
                    sInstance=new AlbumDetailPresenter();
                }
            }
        }
        return sInstance;
    }
    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getAlumDetail(int albumId, int page) {

    }

    @Override
    public void registerViewCallback(IAlbumDetailViewCallBack detailViewCallBack) {
        if(!callBacks.contains(detailViewCallBack)){
            callBacks.add(detailViewCallBack);
            if(mTargetAlbum!=null){
                detailViewCallBack.onAlbumLoaded(mTargetAlbum);

            }
        }
    }

    @Override
    public void unRegisterViewCallback(IAlbumDetailViewCallBack detailViewCallBack) {
        callBacks.remove(detailViewCallBack);
    }


    public void setTargetAlbum(Album targetAlbum){
        this.mTargetAlbum=targetAlbum;
    }
}
