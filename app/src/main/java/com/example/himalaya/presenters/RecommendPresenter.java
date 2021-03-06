package com.example.himalaya.presenters;

import com.example.himalaya.interfaces.IRecommendPresenter;
import com.example.himalaya.interfaces.IRecommendViewCallBack;
import com.example.himalaya.utils.Constants;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendPresenter implements IRecommendPresenter {
    private static String TAG="RecommendPresenter";

    private List<IRecommendViewCallBack> mCallBacks=new ArrayList<>();

    public static RecommendPresenter sInstance=null;

    public static RecommendPresenter getInstance(){
        if(sInstance==null){
            synchronized (RecommendPresenter.class){
                if (sInstance==null) {
                    sInstance=new RecommendPresenter();
                }
            }
        }
        return sInstance;
    }
    /**
     * 获取推荐内容-猜你喜欢
     */

    @Override
    public void getRecommendList() {
        updateLoading();
        Map<String, String> map = new HashMap<>();

        map.put(DTransferConstants.LIKE_COUNT, Constants.COUNT_DEFAULT+"");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                if(gussLikeAlbumList!=null){
                    List<Album> albumList=gussLikeAlbumList.getAlbumList();
                    //更新UI
                    //upRecommendUI(albumList);
                    handlerRecommendResult(albumList);
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG,"error-->"+i);
                LogUtil.d(TAG,"errormsg-->"+s);
                handleError();
            }
        });
    }

    private void handleError() {
        if (mCallBacks!=null) {
            for (IRecommendViewCallBack callBack : mCallBacks) {
                callBack.onNetworkError();
            }
        }
    }


    private void handlerRecommendResult(List<Album> albumList) {
        if (albumList != null) {
            if (albumList.size() == 0) {
                for (IRecommendViewCallBack callBack : mCallBacks) {
                    callBack.onEmpty();
                }
            } else {
                for (IRecommendViewCallBack callBack : mCallBacks) {
                    callBack.onRecommendListLoaded(albumList);
                }
            }
        }
    }

    private void updateLoading(){
        for (IRecommendViewCallBack callBack : mCallBacks) {
            callBack.onLoading();
        }
    }

    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void registerViewCallback(IRecommendViewCallBack iRecommendViewCallBack) {
        if (mCallBacks!=null&&!mCallBacks.contains(iRecommendViewCallBack)) {
            mCallBacks.add(iRecommendViewCallBack);
        }
    }

    @Override
    public void unRegisterViewCallback(IRecommendViewCallBack iRecommendViewCallBack) {
        if (mCallBacks!=null) {
            mCallBacks.remove(iRecommendViewCallBack);
        }
    }
}
