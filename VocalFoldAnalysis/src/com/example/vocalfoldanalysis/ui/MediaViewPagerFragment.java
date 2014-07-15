package com.example.vocalfoldanalysis.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.vocalfoldanalysis.Music;
import com.example.vocalfoldanalysis.R;
import com.example.vocalfoldanalysis.adapter.MediaContainerAdapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MediaViewPagerFragment extends Fragment {

	private ListView mListView;
	private ArrayList<Music> mMediaFileList;
	private static Context mContext;
	
	private MediaPlayer mediaPlayer;
	public int mLastPlayPos = -1;
	public String mCurrentPlayPath;
	private boolean mIsShowSetLayout = false;
	
	public static final MediaViewPagerFragment InstanceFragment(Context context, ArrayList<Music> mediaFile){
		MediaViewPagerFragment mediaViewPagerFragment = new MediaViewPagerFragment();
		mediaViewPagerFragment.mContext = context;
		mediaViewPagerFragment.mMediaFileList = mediaFile;
		
		return mediaViewPagerFragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.viewpager_fragment, container, false);
		mListView = (ListView) view.findViewById(R.id.viewpager_fragment_listview);
		final MediaContainerAdapter mediaContainerAdapter = new MediaContainerAdapter(mContext, mMediaFileList);
		mListView.setAdapter(mediaContainerAdapter);
		
		//init mediaplayer
		mediaPlayer = new MediaPlayer();
		//设置item监听
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos1,
					long id) {
				// TODO Auto-generated method stub
				Music music = mMediaFileList.get(pos1);
				mCurrentPlayPath = music.getmUrl();
				//判断点击位置
				if(pos1 != mLastPlayPos){
					showSetLayout(view, true);
					//2014.07.14
					//startPlay();
					//2014.07.14
					if(mLastPlayPos >= 0){
						hideSetLayout(adapter, mLastPlayPos);
					}
					mLastPlayPos = pos1;
					mIsShowSetLayout = true;
				}else{
					mIsShowSetLayout = !mIsShowSetLayout;
					showSetLayout(view, mIsShowSetLayout);
					//2014.07.14
					//if(!mIsShowSetLayout){
						//停止播放
					//	stopPlay();
					//}else{
					//	startPlay();
					//}
					//2014.07.14
				}
				//设置显示按钮
				mediaContainerAdapter.showSetButton(pos1, view, music.getmUrl());
			}
		});
		
		return view;
	}
	
	/**
	 * 开始播放音乐
	 */
	public void startPlay(){
		try {
			//重置多媒体,是mediaplayer出于idle状态
			mediaPlayer.reset();
			//设置播放文件路径。initialized mediaplayer
			mediaPlayer.setDataSource(mCurrentPlayPath);
			//使mediaplayer进入prepared 状态
			mediaPlayer.prepare();
			//开始播放
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
	
	public void stopPlay(){
		mediaPlayer.pause();
		mediaPlayer.stop();
	}
	
	/**
	 * 隐藏设置菜单
	 * @param adapter
	 * @param clickPos
	 */
	public void hideSetLayout(AdapterView<?> parent, int clickPos){
		int firstPos = parent.getFirstVisiblePosition();
		int endPos = parent.getLastVisiblePosition();
		boolean inListView = clickPos >= firstPos && clickPos <= endPos;
		if(inListView){
			View view1 = parent.getChildAt(clickPos-firstPos);
			RelativeLayout setLayout = (RelativeLayout) view1.findViewById(R.id.set_delete_layout);
			setLayout.setVisibility(View.GONE);
		}
	}
	/**
	 * 显示设置菜单
	 * @param view
	 */
	public void showSetLayout(View view, boolean isShow){
		RelativeLayout setLayout = (RelativeLayout)view.findViewById(R.id.set_delete_layout);
		setLayout.setVisibility(isShow?View.VISIBLE:View.GONE);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//memory 释放
		if(mediaPlayer != null){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		
	}

}
