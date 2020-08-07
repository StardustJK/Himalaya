package com.example.himalaya.fragments;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.himalaya.R;
import com.example.himalaya.adapter.RecommendListAdapter;
import com.example.himalaya.base.BaseFragment;
import com.example.himalaya.interfaces.IRecommendViewCallBack;
import com.example.himalaya.presenters.RecommendPresenter;
import com.example.himalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecommendFragment extends BaseFragment implements IRecommendViewCallBack, UILoader.OnRetryClickListener {
    private static String TAG="RecommendFragment";
    private View mRootView;
    private RecyclerView mRecommendRv;
    private RecommendListAdapter recommendListAdapter;
    private RecommendPresenter mRecommendPresenter;
    private UILoader mUiLoader;

    @Override
    protected View onSubViewLoaded(final LayoutInflater layoutInflater, ViewGroup container) {
        mUiLoader=new UILoader(getContext()) {
           @Override
           protected View getSuccessView(ViewGroup container) {
               return createSuccessView(layoutInflater,container);
           }
       };

        //获取逻辑层的对象
        mRecommendPresenter = RecommendPresenter.getInstance();
        //获取推荐列表
        mRecommendPresenter.getRecommendList();
        mRecommendPresenter.registerViewCallBack(this);

        if (mUiLoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
        }

        mUiLoader.setOnRetryClickListener(this);
        return mUiLoader;
    }

    private View createSuccessView(LayoutInflater layoutInflater, ViewGroup container) {
        mRootView=layoutInflater.inflate(R.layout.fragment_recommend,container,false);

        mRecommendRv=mRootView.findViewById(R.id.recommendList);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecommendRv.setLayoutManager(linearLayoutManager);
        mRecommendRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top= UIUtil.dip2px(view.getContext(),5);
                outRect.bottom=UIUtil.dip2px(view.getContext(),5);
                outRect.left=UIUtil.dip2px(view.getContext(),5);
                outRect.right=UIUtil.dip2px(view.getContext(),5);

            }
        });
        //设置适配器
        recommendListAdapter=new RecommendListAdapter();
        mRecommendRv.setAdapter(recommendListAdapter);

        return mRootView;
    }



    @Override
    public void onRecommendListLoaded(List<Album> result) {
        //获取到推荐内容后，此方法会被调用（成功
        //更新ui
        recommendListAdapter.setData(result);
        mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
    }

    @Override
    public void onNetworkError() {
        mUiLoader.updateStatus(UILoader.UIStatus.NETWORK_ERROR);

    }

    @Override
    public void onEmpty() {
        mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);

    }

    @Override
    public void onLoading() {
        mUiLoader.updateStatus(UILoader.UIStatus.LOADING);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消注册
        if (mRecommendPresenter!=null) {
            mRecommendPresenter.unRegisterViewCallBack(this);
        }
    }

    @Override
    public void onRetryClick() {
        //网络不佳时，用户点击了重试
        if (mRecommendPresenter != null) {
            mRecommendPresenter.getRecommendList();
        }
    }
}
