package com.example.vocalfoldanalysis.ui;

import java.io.IOException;

import com.example.vocalfoldanalysis.AnalysisView;
import com.example.vocalfoldanalysis.AnalysisViewAll;
import com.example.vocalfoldanalysis.Music;
import com.example.vocalfoldanalysis.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.media.audiofx.Visualizer.OnDataCaptureListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class AnalysisActivity extends Activity {
	
	private AnalysisView mAnalysisView;
	private AnalysisViewAll mAnalysisViewAll;
	private Button mAnalysisPlay;
	private Button mAnalysisStop;
    private Music mAnalysisMusic;
    
    private MediaPlayer mMediaPlayer;
    //波形显示
    private Visualizer mVisulizer;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.analysis_activity);
		initView();
		mAnalysisMusic = getIntent().getParcelableExtra("musicitem");
		
		//初始化
		mMediaPlayer = MediaPlayer.create(this, Uri.parse(mAnalysisMusic.getmUrl()));
		Log.i("jiangtao4", mAnalysisMusic.getmUrl());
		//mMediaPlayer = MediaPlayer.create(this, R.raw.test);
	//	mMediaPlayer.reset();
		try {
		//	mMediaPlayer.setDataSource(mAnalysisMusic.getmUrl());
		//	mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//初始化Visualizer
		mVisulizer = new Visualizer(mMediaPlayer.getAudioSessionId());
		//必须设置
		mVisulizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
	
		mVisulizer.setDataCaptureListener(new OnDataCaptureListener() {
			
			@Override
			public void onWaveFormDataCapture(Visualizer arg0, byte[] waveform, int arg2) {
				// TODO Auto-generated method stub
				mAnalysisView.init();
				mAnalysisView.updateWaveformDate(waveform);
			}
			
			@Override
			public void onFftDataCapture(Visualizer arg0, byte[] arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
		}, Visualizer.getMaxCaptureRate()/2, true, false);
		
		mVisulizer.setEnabled(true);
		
		mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				Log.i("jiangtao4", "onCompletion");
				mAnalysisViewAll.init();
				mAnalysisViewAll.updateWaveformDate(mAnalysisView.getAllWaveData());
				mVisulizer.setEnabled(false);
			}
		});
	}
	
	public void initView(){
		mAnalysisPlay = (Button)findViewById(R.id.analysis_play);
		mAnalysisStop = (Button)findViewById(R.id.analysis_stop);
		mAnalysisView = (AnalysisView)findViewById(R.id.visualizerView);
		mAnalysisViewAll = (AnalysisViewAll)findViewById(R.id.visualizerView_all);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mMediaPlayer != null && mVisulizer != null){
			mVisulizer.release();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
