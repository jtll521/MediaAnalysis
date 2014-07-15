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
import android.util.Log;
import android.view.View;

public class AnalysisViewAll extends View {

	private List<Byte> mAllWave = new ArrayList<Byte>();

	private Rect mRect = new Rect();
	private Paint mPaint = new Paint();
	private float[] mPoint;
	
	public void init(){
		mPaint.setStrokeWidth(1f);
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.WHITE);
	}
	
	public void updateWaveformDate(List<Byte> allWave){
	//	this.mAllWave.addAll(Arrays.asList(ArrayUtils.toObject(waveform)));
		mAllWave = allWave;
		Log.i("jiangtao4", "all + updateWaveformDate");
		Log.i("jiangtao4", "all + updateWaveformDate" + mAllWave.size());
		invalidate();
	}
	
	public AnalysisViewAll(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public AnalysisViewAll(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	
	public AnalysisViewAll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
	
		if (mPoint == null || mPoint.length < mAllWave.size() * 4)
		{
			mPoint = new float[mAllWave.size() * 4];
		}
		
		mRect.set(0, 0, getWidth(), getHeight());

//		for (int i = 0; i < mWaveForm.length - 1; i++)
//		{
//			mPoint[i * 4] = mRect.width() * i / (mWaveForm.length - 1);
//			mPoint[i * 4 + 1] = mRect.height() / 2
//					+ ((byte) (mWaveForm[i] + 128)) * (mRect.height() / 2)
//					/ 128;
//			mPoint[i * 4 + 2] = mRect.width() * (i + 1)
//					/ (mWaveForm.length - 1);
//			mPoint[i * 4 + 3] = mRect.height() / 2
//					+ ((byte) (mWaveForm[i + 1] + 128)) * (mRect.height() / 2)
//					/ 128;
//		}
		
		for (int i = 0; i < mAllWave.size() - 1; i++)
		{
			mPoint[i * 4] = mRect.width() * i / (mAllWave.size() - 1);
			mPoint[i * 4 + 1] = mRect.height() / 2
					+ ((byte) (mAllWave.get(i) + 128)) * (mRect.height() / 2)
					/ 128;
			mPoint[i * 4 + 2] = mRect.width() * (i + 1)
					/ (mAllWave.size() - 1);
			mPoint[i * 4 + 3] = mRect.height() / 2
					+ ((byte) (mAllWave.get(i + 1) + 128)) * (mRect.height() / 2)
					/ 128;
		}

		canvas.drawLines(mPoint, mPaint);
		if(mPoint != null){
			mPoint = null;
		}
	}
}
