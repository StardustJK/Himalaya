package com.example.himalaya;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;

import com.example.himalaya.base.BaseActivity;
import com.example.himalaya.presenters.PlayerPresenter;

public class PlayerActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //播放测试
        PlayerPresenter playerPresenter=PlayerPresenter.getPlayerPresenter();
        playerPresenter.play();
    }
}
