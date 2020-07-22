package com.example.himalaya.interfaces;

import com.example.himalaya.utils.Constants;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IRecommendPresenter {

    /*
    获取推荐内容
     */
    void getRecommendList();


    //下拉加载更多
    void pull2RefreshMore();

    void loadMore();

    //注册UI的回调
    void registerViewCallBack(IRecommendViewCallBack callback);
    void unRegisterViewCallBack(IRecommendViewCallBack callBack);

}
