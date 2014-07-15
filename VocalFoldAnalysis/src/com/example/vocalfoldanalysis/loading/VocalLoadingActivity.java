package com.example.vocalfoldanalysis.loading;

import com.example.vocalfoldanalysis.R;
import com.example.vocalfoldanalysis.ui.VocalFoldAnalysisActivity;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
/**
 * create by jiangtao
 * welcaom page, 
 * can set fullscreen feature in androidmanifest.xml
 * @author tao
 *
 */
public class VocalLoadingActivity extends Activity {

	private static final int SPLASHTIME = 2000;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vocal_fold_analysis_loading);
		mContext = this;
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, VocalFoldAnalysisActivity.class);
				
				startActivity(intent);
				
				VocalLoadingActivity.this.finish();
			}
		}, SPLASHTIME);
	}

}
