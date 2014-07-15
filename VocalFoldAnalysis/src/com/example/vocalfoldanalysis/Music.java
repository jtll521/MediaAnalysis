package com.example.vocalfoldanalysis;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * music class 
 * @author tao
 *
 */
public class Music implements Parcelable{
	
	private int mId; 
	private String mTilte;
	private String mAlbum;
	private String mArtist;
	private String mUrl;
	private int mDuration;
	private Long mSize;
	
	public Music(int id, String title, String album, String artist, String url, int duration, long size){
		this.mId = id;
		this.mTilte = title;
		this.mAlbum = album;
		this.mArtist = artist;
		this.mUrl = url;
		this.mDuration = duration;
		this.mSize = size;
	}
	public int getmId() {
		return mId;
	}
	public void setmId(int mId) {
		this.mId = mId;
	}
	public String getmTilte() {
		return mTilte;
	}
	public void setmTilte(String mTilte) {
		this.mTilte = mTilte;
	}
	public String getmAlbum() {
		return mAlbum;
	}
	public void setmAlbum(String mAlbum) {
		this.mAlbum = mAlbum;
	}
	public String getmArtist() {
		return mArtist;
	}
	public void setmArtist(String mArtist) {
		this.mArtist = mArtist;
	}
	public String getmUrl() {
		return mUrl;
	}
	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}
	public int getmDuration() {
		return mDuration;
	}
	public void setmDuration(int mDuration) {
		this.mDuration = mDuration;
	}
	public Long getmSize() {
		return mSize;
	}
	public void setmSize(Long mSize) {
		this.mSize = mSize;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		// TODO Auto-generated method stub
		parcel.writeInt(mId);
		parcel.writeString(mTilte);
		parcel.writeString(mAlbum);
		parcel.writeString(mArtist);
		parcel.writeString(mUrl);
		parcel.writeInt(mDuration);
		parcel.writeLong(mSize);
	}
	
	public static final Parcelable.Creator<Music> CREATOR = new Creator<Music>(){

		@Override
		public Music createFromParcel(Parcel parcel) {
			// TODO Auto-generated method stub
			Music music = new Music(parcel.readInt(), parcel.readString(), parcel.readString(), 
					parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readLong());
			
			return music;
		}

		@Override
		public Music[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Music[size];
		}
		
	};
}
