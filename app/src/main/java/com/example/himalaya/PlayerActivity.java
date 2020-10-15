package com.example.himalaya;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.himalaya.adapter.PlayerTrackPagerAdapter;
import com.example.himalaya.base.BaseActivity;
import com.example.himalaya.interfaces.IPlayerCallback;
import com.example.himalaya.presenters.PlayerPresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.List;

public class PlayerActivity extends BaseActivity implements IPlayerCallback, ViewPager.OnPageChangeListener {

    private static final String TAG = "PlayerActivity";
    private ImageView mControlBtn;
    private PlayerPresenter mPlayerPresenter;
    private SimpleDateFormat minFormat = new SimpleDateFormat("mm:ss");
    private SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss");
    private TextView mTotalDuration;
    private TextView mCurrentPosition;
    private SeekBar mDurationSeekBar;
    private int mCurrentProgress = 0;
    private boolean mIsUserTouchProgressBar = false;
    private ImageView mPlayNextBtn;
    private ImageView mPlayPreBtn;
    private TextView mTrackTitleTv;
    private String mTrackTitle;
    private ViewPager mTrackPageView;
    private PlayerTrackPagerAdapter mPlayerTrackPagerAdapter;
    private boolean mIsUserSelectPage = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallback(this);
        initView();
        //初始化以后再获取数据
        mPlayerPresenter.getPlayList();
        initEvent();
        startPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unRegisterViewCallback(this);
            mPlayerPresenter = null;
        }
    }

    /**
     * 开始播放
     */
    private void startPlay() {
        if (mPlayerPresenter != null) {
            mPlayerPresenter.play();
        }
    }

    /**
     * 设置相关事件
     */
    private void initEvent() {

        mControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter.isPlay()) {
                    mPlayerPresenter.pause();
                } else {
                    mPlayerPresenter.play();
                }
            }
        });

        mDurationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mCurrentProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar = false;
                mPlayerPresenter.seekTo(mCurrentProgress);
            }
        });

        mPlayNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playNext();
                }
            }
        });

        mPlayPreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playPre();
                }
            }
        });

        mTrackPageView.addOnPageChangeListener(this);
        mTrackPageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mIsUserSelectPage = true;
                        break;
                }
                return false;
            }
        });
    }

    private void initView() {
        mControlBtn = findViewById(R.id.play_or_pause_btn);
        mTotalDuration = findViewById(R.id.track_duration);
        mCurrentPosition = findViewById(R.id.current_position);
        mDurationSeekBar = findViewById(R.id.track_seek_bar);
        mPlayNextBtn = findViewById(R.id.play_next);
        mPlayPreBtn = findViewById(R.id.play_pre);
        mTrackTitleTv = findViewById(R.id.track_title);
        if (!TextUtils.isEmpty(mTrackTitle)) {
            mTrackTitleTv.setText(mTrackTitle);
        }
        mTrackPageView = findViewById(R.id.track_pager_view);
        mPlayerTrackPagerAdapter = new PlayerTrackPagerAdapter();
        mTrackPageView.setAdapter(mPlayerTrackPagerAdapter);
    }


    @Override
    public void onPlayStart() {
        //开始播放，修改UI
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.mipmap.stop_normal);

        }

    }

    @Override
    public void onPlayPause() {
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.mipmap.play_normal);

        }
    }

    @Override
    public void onPlayStop() {
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.mipmap.play_normal);

        }
    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void onNextPlay(Track track) {

    }

    @Override
    public void onPrePlay(Track track) {

    }

    @Override
    public void onListLoad(List<Track> list) {
        //把数据设置到适配器里面
        if (mPlayerTrackPagerAdapter != null) {
            mPlayerTrackPagerAdapter.setData(list);
        }
    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onProgressChange(int currentProgress, int total) {
        mDurationSeekBar.setMax(total);
        //更新进度条总长度
        String totalDuration;
        String currentPos;
        if (total > 1000 * 60 * 60) {
            totalDuration = hourFormat.format(total);
            currentPos = hourFormat.format(currentProgress);

        } else {
            totalDuration = minFormat.format(total);
            currentPos = minFormat.format(currentProgress);

        }
        if (mTotalDuration != null) {
            mTotalDuration.setText(totalDuration);
        }
        //更新当前时间
        mCurrentPosition.setText(currentPos);
        //更新进度
        if (!mIsUserTouchProgressBar) {
            mDurationSeekBar.setProgress(currentProgress);
        }

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onTrackUpdate(Track track, int playIndex) {
        //更新标题
        this.mTrackTitle = track.getTrackTitle();
        if (mTrackTitleTv != null) {
            mTrackTitleTv.setText(mTrackTitle);
        }
        //修改页面图片
        if (mTrackPageView != null) {
            mTrackPageView.setCurrentItem(playIndex, true);
        }

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //页面选中的时候切换播放的内容
        if (mPlayerPresenter != null && mIsUserSelectPage) {
            mPlayerPresenter.playByIndex(position);
        }
        mIsUserSelectPage = false;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
