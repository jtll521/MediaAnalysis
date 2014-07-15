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
		//����item����
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos1,
					long id) {
				// TODO Auto-generated method stub
				Music music = mMediaFileList.get(pos1);
				mCurrentPlayPath = music.getmUrl();
				//�жϵ��λ��
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
						//ֹͣ����
					//	stopPlay();
					//}else{
					//	startPlay();
					//}
					//2014.07.14
				}
				//������ʾ��ť
				mediaContainerAdapter.showSetButton(pos1, view, music.getmUrl());
			}
		});
		
		return view;
	}
	
	/**
	 * ��ʼ��������
	 */
	public void startPlay(){
		try {
			//���ö�ý��,��mediaplayer����idle״̬
			mediaPlayer.reset();
			//���ò����ļ�·����initialized mediaplayer
			mediaPlayer.setDataSource(mCurrentPlayPath);
			//ʹmediaplayer����prepared ״̬
			mediaPlayer.prepare();
			//��ʼ����
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
	 * �������ò˵�
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
	 * ��ʾ���ò˵�
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
		//memory �ͷ�
		if(mediaPlayer != null){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		
	}

}
