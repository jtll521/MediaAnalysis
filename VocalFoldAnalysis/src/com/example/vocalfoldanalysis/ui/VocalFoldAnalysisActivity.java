package com.example.vocalfoldanalysis.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.vocalfoldanalysis.Music;
import com.example.vocalfoldanalysis.R;
import com.example.vocalfoldanalysis.adapter.MediaContainerAdapter;
import com.example.vocalfoldanalysis.adapter.MediaViewPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
import com.example.vocalfoldanalysis.MemoryCache;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * use this activity to list all the audio file in the phone 1. the system
 * ringtone 2. the file in the sdcard
 * 
 * @author jiang tao
 * 
 */
public class VocalFoldAnalysisActivity extends FragmentActivity {

	private MediaScanHandler mMediaScanHandler;
	//the list contains media file on sdcard
	private ArrayList<Music> mExternalMediaFile;
	//this list contains media file on system;
	private ArrayList<Music> mInternalMediaFile;
	//this List contains the file category in the phone
	private ArrayList<ArrayList<Music>> mMediaFileList;
	//viewpager
	private ViewPager mViewPager = null;
	private MediaViewPagerAdapter mMediaViewPagerAdapter = null;
	
	private static final int SCANMEDIAFINISH = 0;
	
	private Context mContext;
	
	private TabPageIndicator mTabPagerIndicator;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vocal_fold_analysis);
		// show back icon in actionbar
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		mContext = this;
		// init views in this activity
		initView();
		// handler to handle the msg send for the thread
		mMediaScanHandler = new MediaScanHandler();

		// mExternalMediaFile file in the sdcard
		mExternalMediaFile = new ArrayList<Music>();
		// mInternalMediaFile file in the sdcard
		mInternalMediaFile = new ArrayList<Music>();
		
		// start to scan the media file
		Thread scanThread = new Thread(new MediaScanThread());
		scanThread.start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

	private void initView() {
	//	mVocalListView = (ListView) findViewById(R.id.vocal_fold_list);
		mViewPager = (ViewPager)findViewById(R.id.viewpager_media);
		mTabPagerIndicator = (TabPageIndicator)findViewById(R.id.viewpager_indicator);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		// click back icon to finish this activity
		switch (item.getItemId()) {
		case android.R.id.home:
			VocalFoldAnalysisActivity.this.finish();
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	class MediaScanHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case SCANMEDIAFINISH:
				mMediaFileList = new ArrayList<ArrayList<Music>>();
				if(mExternalMediaFile != null && mExternalMediaFile.size() > 0){
					mMediaFileList.add(mExternalMediaFile);
				}
				if(mInternalMediaFile != null && mInternalMediaFile.size() > 0){
					mMediaFileList.add(mInternalMediaFile);
				}
				mMediaViewPagerAdapter = new MediaViewPagerAdapter(getSupportFragmentManager(),mContext,mMediaFileList);
				mViewPager.setAdapter(mMediaViewPagerAdapter);
				
				mTabPagerIndicator.setVisibility(View.VISIBLE);
				mTabPagerIndicator.setViewPager(mViewPager);
				break;

			default:
				break;
			}
		}

	}

	// use this thread to scan media file in this phone
	class MediaScanThread implements Runnable {

		public void scanAllExternalMedia() {
			// all media file are stored in the
			// MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
			Cursor cursor = getContentResolver().query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
					null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
			cursor.moveToFirst();

			while (!cursor.isAfterLast()) {
				// 歌曲编号
				int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
				// 歌曲标题
				String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				// 歌曲的专辑名：MediaStore.Audio.Media.ALBUM
				String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
				// 歌曲的歌手名： MediaStore.Audio.Media.ARTIST
				String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
				// 歌曲文件的路径 ：MediaStore.Audio.Media.DATA
				String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
				// 歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
				int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
				// 歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
				Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                
				Music music = new Music(id, title, album, artist, url, duration, size);
				mExternalMediaFile.add(music);
				
				cursor.moveToNext(); 
			}
		}
		
		public void scanAllInternalMedia() {
			// all media file are stored in the
			// MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
			Cursor cursor = getContentResolver().query(
					MediaStore.Audio.Media.INTERNAL_CONTENT_URI, null, null,
					null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
			cursor.moveToFirst();

			while (!cursor.isAfterLast()) {
				// 歌曲编号
				int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
				// 歌曲标题
				String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				// 歌曲的专辑名：MediaStore.Audio.Media.ALBUM
				String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
				// 歌曲的歌手名： MediaStore.Audio.Media.ARTIST
				String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
				// 歌曲文件的路径 ：MediaStore.Audio.Media.DATA
				String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
				// 歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
				int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
				// 歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
				Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                
				Music music = new Music(id, title, album, artist, url, duration, size);
				mInternalMediaFile.add(music);
				
				cursor.moveToNext(); 
			}
		}
		

		@Override
		public void run() {
            //scan external 
			scanAllExternalMedia();
			//scan internal
			scanAllInternalMedia();
			
			mMediaScanHandler.sendEmptyMessage(SCANMEDIAFINISH);
		}
	}
}
