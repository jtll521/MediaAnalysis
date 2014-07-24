package com.example.vocalfoldanalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
//2014.07.15
public class AnalysisView extends View {

	private byte[] mWaveForm;
	private float[] mPoint;
	
	private Rect mRect = new Rect();
	private Paint mPaint = new Paint();
	
	//2014.07.14
	private List<Byte> mAllWaveData = new ArrayList<Byte>();
	//2014.07.24 øÿ÷∆AnalysisViewAllœ‘ æ
	private List<Byte> mAllWaveDataOnce = new ArrayList<Byte>();
	
	public void init(){
		mWaveForm = null;
		mPaint.setStrokeWidth(1f);
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.WHITE);
	}
	
	public void updateWaveformDate(byte[] waveform, boolean isUpdate){
		if(waveform == null){
			return;
		}
		
		for(byte b:waveform){
			mAllWaveData.add(b);
		}
		this.mWaveForm = waveform;
		invalidate();
	}
	
	public List<Byte> getAllWaveData(){
		//finish play, return the data
		if(mAllWaveDataOnce != null && mAllWaveDataOnce.size() == 0){
			mAllWaveDataOnce.addAll(mAllWaveData);
			//Arrays.asList
		}
		return mAllWaveDataOnce;
	}
	public AnalysisView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public AnalysisView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	
	public AnalysisView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		if(mWaveForm == null){
			return;
		}
		if (mPoint == null || mPoint.length < mWaveForm.length * 4)
		{
			mPoint = new float[mWaveForm.length * 4];
		}
		
		mRect.set(0, 0, getWidth(), getHeight());

		for (int i = 0; i < mWaveForm.length - 1; i++)
		{
			mPoint[i * 4] = mRect.width() * i / (mWaveForm.length - 1);
			mPoint[i * 4 + 1] = mRect.height() / 2
					+ ((byte) (mWaveForm[i] + 128)) * (mRect.height() / 2)
					/ 128;
			mPoint[i * 4 + 2] = mRect.width() * (i + 1)
					/ (mWaveForm.length - 1);
			mPoint[i * 4 + 3] = mRect.height() / 2
					+ ((byte) (mWaveForm[i + 1] + 128)) * (mRect.height() / 2)
					/ 128;
		}
		
		canvas.drawLines(mPoint, mPaint);
	}
}
