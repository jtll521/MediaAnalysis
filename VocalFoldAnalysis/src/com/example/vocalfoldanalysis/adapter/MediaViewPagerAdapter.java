package com.example.vocalfoldanalysis.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.vocalfoldanalysis.MemoryCache;
import com.example.vocalfoldanalysis.Music;
import com.example.vocalfoldanalysis.ui.MediaViewPagerFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * fragmentadapter using by viewpager
 * @author tao
 *
 */
public class MediaViewPagerAdapter extends FragmentPagerAdapter {

	private Context mContext;
	private ArrayList<ArrayList<Music>> mMediaFileMap;
	
	private static final String[] PAGERNAME = {
		"Internal","External"
	};
	public MediaViewPagerAdapter(FragmentManager fragmentManager, Context context, ArrayList<ArrayList<Music>> mediaFileMap) {
		super(fragmentManager);
		// TODO Auto-generated constructor stub
		mContext = context;
		mMediaFileMap = mediaFileMap;
		
	}


	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return PAGERNAME[position % PAGERNAME.length];
	}


	@Override
	public Fragment getItem(int index) {
		// TODO Auto-generated method stub
		return MediaViewPagerFragment.InstanceFragment(mContext, mMediaFileMap.get(index));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mMediaFileMap.size();
	}

}
