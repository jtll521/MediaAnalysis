package com.example.vocalfoldanalysis.adapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.vocalfoldanalysis.MemoryCache;
import com.example.vocalfoldanalysis.Music;
import com.example.vocalfoldanalysis.R;
import com.example.vocalfoldanalysis.ui.AnalysisActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MediaContainerAdapter extends BaseAdapter {

	private ArrayList<Music> mExternalMediaFile;
	private Context mContext;
	// using memory cache to update speed
	private MemoryCache mMemoryCache;
	// 当前点击位置
	public int mCurrentPos = -1;
	// 当前播放路径
	public String mCurrentPlayPath;

	public Music mMusic;
	
	//MediaPlayer
	public MediaPlayer mediaPlayer;
	public boolean mIsCurrentPlaying = false;

	public MediaContainerAdapter(Context context,
			ArrayList<Music> externalMediaFile) {
		this.mExternalMediaFile = externalMediaFile;
		this.mContext = context;
		this.mMemoryCache = new MemoryCache();
		this.mediaPlayer = new MediaPlayer();
		mIsCurrentPlaying = false;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mExternalMediaFile.size() == 0 ? 0 : mExternalMediaFile.size();
	}

	@Override
	public Object getItem(int index) {
		// TODO Auto-generated method stub
		return mExternalMediaFile.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View contentView, ViewGroup group) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		if (contentView == null) {
			contentView = LayoutInflater.from(mContext).inflate(
					R.layout.media_listview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mSetLayout = (RelativeLayout) contentView
					.findViewById(R.id.set_delete_layout);
			viewHolder.mMusicName = (TextView) contentView
					.findViewById(R.id.name);
			viewHolder.mMusicTime = (TextView) contentView
					.findViewById(R.id.time);
			viewHolder.mMusicSize = (TextView) contentView
					.findViewById(R.id.size);
			viewHolder.mAnalysisLayout = (RelativeLayout) contentView
					.findViewById(R.id.analysis_layout);
			viewHolder.mPlayLayout = (RelativeLayout) contentView
					.findViewById(R.id.play_layout);
			viewHolder.mPlayText =  (TextView) contentView
					.findViewById(R.id.play);

			contentView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) contentView.getTag();
		}

		Music music = null;
		final String title = mExternalMediaFile.get(pos).getmTilte().toString();

		if (mMemoryCache.get(title) != null) {
			music = mMemoryCache.get(title);
		} else {
			music = mExternalMediaFile.get(pos);
			mMemoryCache.put(title, music);
		}

		// mMusic = music;
		// Log.i("jiangtao4", music.getmUrl());

		boolean isCurrentPlaying = pos == mCurrentPos;

		viewHolder.mSetLayout.setVisibility(isCurrentPlaying ? View.VISIBLE
				: View.GONE);
		viewHolder.mMusicName.setText(music.getmTilte());
		viewHolder.mMusicSize.setText(getMusicSize(music.getmSize()) + "M");
		viewHolder.mMusicTime.setText(getMusicTime(music.getmDuration()));
		viewHolder.mAnalysisLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//2014.07.14
				if(mIsCurrentPlaying){
					stopPlay();
					viewHolder.mPlayText.setText("Play");
				}
				//2014.07.14
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				// Use parcelable to pass the object
				mMusic = mMemoryCache.get(title);
				bundle.putParcelable("musicitem", mMusic);
				Log.i("jiangtao4", mMusic.getmUrl());
				intent.putExtras(bundle);
				intent.setClass(mContext, AnalysisActivity.class);
				mContext.startActivity(intent);
			}
		});

		viewHolder.mPlayLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mIsCurrentPlaying){
					stopPlay();
					viewHolder.mPlayText.setText("Play");
				}else{
					startPlay();
					//监听播放完成 2014.07.15
					mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
						
						@Override
						public void onCompletion(MediaPlayer arg0) {
							// TODO Auto-generated method stub
							viewHolder.mPlayText.setText("Play");
							mIsCurrentPlaying = false;
						}
					} );
					viewHolder.mPlayText.setText("Stop");
				}
				
				mIsCurrentPlaying = !mIsCurrentPlaying;
			}
		});

		return contentView;
	}

	/**
	 * 开始播放音乐
	 */
	public void startPlay() {
		try {
			// 重置多媒体,是mediaplayer出于idle状态
			mediaPlayer.reset();
			// 设置播放文件路径。initialized mediaplayer
			mediaPlayer.setDataSource(mCurrentPlayPath);
			// 使mediaplayer进入prepared 状态
			mediaPlayer.prepare();
			// 开始播放
			mediaPlayer.start();
		
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stopPlay() {
		mediaPlayer.pause();
		mediaPlayer.stop();
	}

	/**
	 * 
	 * @param size
	 * @return 返回music的实际大小
	 */
	public String getMusicSize(long size) {
		float l = size / 1024f / 1024f;

		return String.format("%.2f", l);
	}

	/**
	 * SIMPLEDATEFORMAT返回时间大小
	 * 
	 * @param time
	 * @return
	 */
	public String getMusicTime(int time) {
		SimpleDateFormat format = new SimpleDateFormat("mm:ss:SSS");

		return format.format(time);
	}

	/**
	 * 控制显示set layout
	 * 
	 * @param pos
	 */
	public void showSetButton(int pos, View view, String playPath) {
		mCurrentPos = pos;
		mCurrentPlayPath = playPath;

	}

	class ViewHolder {
		public RelativeLayout mSetLayout, mAnalysisLayout, mPlayLayout;
		public TextView mMusicName, mMusicTime, mMusicSize, mPlayText;
	}
}
