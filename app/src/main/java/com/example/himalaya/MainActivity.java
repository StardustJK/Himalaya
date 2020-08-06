package com.example.himalaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


import com.example.himalaya.adapter.IndicatorAdapter;
import com.example.himalaya.adapter.MainContentAdpater;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    private static final String TAG="MainActivity";
    private MagicIndicator magicIndicator;

    private IndicatorAdapter mIndicatorAdapter;
    private ViewPager contentPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();

    }

    private void initEvent() {
        mIndicatorAdapter.setOnIndicatorTapClickListener(new IndicatorAdapter.OnIndicatorTapClickListener() {
            @Override
            public void onTabClick(int index) {
                LogUtil.d(TAG,"click index is -->"+index);
                if(contentPager!=null){
                    contentPager.setCurrentItem(index,false);
                }
            }
        });
    }

    private void initView(){
        magicIndicator=this.findViewById(R.id.main_indicator);
        magicIndicator.setBackgroundColor(this.getResources().getColor(R.color.main_color));
        //创建indicator适配器
        mIndicatorAdapter=new IndicatorAdapter(this);
        CommonNavigator commonNavigator=new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(mIndicatorAdapter);

        contentPager=this.findViewById(R.id.content_pager);
        contentPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //创建内容适配器
        FragmentManager fragmentManager=getSupportFragmentManager();
        MainContentAdpater mainContentAdpater=new MainContentAdpater(fragmentManager);


        contentPager.setAdapter(mainContentAdpater);
        //绑定viewpager和indicator
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator,contentPager);
    }
}
