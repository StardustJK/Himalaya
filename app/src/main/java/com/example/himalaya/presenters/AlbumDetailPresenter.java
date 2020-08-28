package com.example.himalaya.presenters;

import com.example.himalaya.interfaces.IAlbumDetailPresenter;
import com.example.himalaya.interfaces.IAlbumDetailViewCallBack;
import com.example.himalaya.utils.Constants;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private static String TAG="AlbumDetailPresenter";
    private Album mTargetAlbum;

    private List<IAlbumDetailViewCallBack> mCallBacks=new ArrayList<>();
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
        //根据album和页码获取id
        Map<String,String> map=new HashMap<>();
        map.put(DTransferConstants.ALBUM_ID,albumId+"");
        map.put(DTransferConstants.SORT,"asc");
        map.put(DTransferConstants.PAGE,page+"");
        map.put(DTransferConstants.PAGE_SIZE, Constants.COUNT_DEFAULT+"");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                if (trackList != null) {
                    List<Track> tracks=trackList.getTracks();
                    LogUtil.d(TAG,"tracks size --> "+tracks.size());
                    handlerAlbumDetailResult(tracks);
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG,"error-->"+i);
                LogUtil.d(TAG,"errormsg-->"+s);
                handlerError(i,s);
            }
        });

    }

    /**
     * 如果网络发生错误，就通知ui
     * @param i
     * @param s
     */
    private void handlerError(int i, String s) {
        for(IAlbumDetailViewCallBack callBack:mCallBacks){
            callBack.onNetworkError(i,s);
        }
    }

    private void handlerAlbumDetailResult(List<Track> tracks) {
        for (IAlbumDetailViewCallBack mCallBack : mCallBacks) {
            mCallBack.onDetailListLoaded(tracks);
        }
    }

    @Override
    public void registerViewCallback(IAlbumDetailViewCallBack detailViewCallBack) {
        if(!mCallBacks.contains(detailViewCallBack)){
            mCallBacks.add(detailViewCallBack);
            if(mTargetAlbum!=null){
                detailViewCallBack.onAlbumLoaded(mTargetAlbum);

            }
        }
    }

    @Override
    public void unRegisterViewCallback(IAlbumDetailViewCallBack detailViewCallBack) {
        mCallBacks.remove(detailViewCallBack);
    }


    public void setTargetAlbum(Album targetAlbum){
        this.mTargetAlbum=targetAlbum;
    }
}
