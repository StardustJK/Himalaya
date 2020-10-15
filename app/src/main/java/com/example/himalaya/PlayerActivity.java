package com.example.himalaya;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.himalaya.base.BaseActivity;
import com.example.himalaya.interfaces.IPlayerCallback;
import com.example.himalaya.presenters.PlayerPresenter;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.List;

public class PlayerActivity extends BaseActivity implements IPlayerCallback {

    private static final String TAG = "PlayerActivity";
    private ImageView controlBtn;
    private PlayerPresenter mPlayerPresenter;
    private SimpleDateFormat minFormat = new SimpleDateFormat("mm:ss");
    private SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss");
    private TextView mTotalDuration;
    private TextView mCurrentPosition;
    private SeekBar mDurationSeekBar;
    private int mCurrentProgress = 0;
    private boolean mIsUserTouchProgressBar =false;
    private ImageView playNextBtn;
    private ImageView playPreBtn;
    private TextView mTrackTitleTv;
    private String mTrackTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallback(this);
        initView();
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

        controlBtn.setOnClickListener(new View.OnClickListener() {
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
                if(fromUser){
                    mCurrentProgress=progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar =true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar =false;
                mPlayerPresenter.seekTo(mCurrentProgress);
            }
        });

        playNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playNext();
                }
            }
        });

        playPreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playPre();
                }
            }
        });
    }

    private void initView() {
        controlBtn = findViewById(R.id.play_or_pause_btn);
        mTotalDuration = findViewById(R.id.track_duration);
        mCurrentPosition = findViewById(R.id.current_position);
        mDurationSeekBar = findViewById(R.id.track_seek_bar);
        playNextBtn=findViewById(R.id.play_next);
        playPreBtn=findViewById(R.id.play_pre);
        mTrackTitleTv=findViewById(R.id.track_title);
        if(!TextUtils.isEmpty(mTrackTitle)){
            mTrackTitleTv.setText(mTrackTitle);
        }
    }


    @Override
    public void onPlayStart() {
        //开始播放，修改UI
        if (controlBtn != null) {
            controlBtn.setImageResource(R.mipmap.stop_normal);

        }

    }

    @Override
    public void onPlayPause() {
        if (controlBtn != null) {
            controlBtn.setImageResource(R.mipmap.play_normal);

        }
    }

    @Override
    public void onPlayStop() {
        if (controlBtn != null) {
            controlBtn.setImageResource(R.mipmap.play_normal);

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
        if (!mIsUserTouchProgressBar){
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
    public void onTrackTitleUpdate(String title) {
        this.mTrackTitle=title;
        if (mTrackTitleTv != null) {
            mTrackTitleTv.setText(title);
        }
    }
}
