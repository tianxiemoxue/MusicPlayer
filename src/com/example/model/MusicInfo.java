package com.example.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class MusicInfo implements Parcelable{
	public static final String KEY_MUSIC = "musicInfo";
	private static final String  KEY_ID = "_id";
	private static final String  KEY_SONGID = "songid";
	private static final String  KEY_ALBUMID = "albumid";
	private static final String  KEY_DURATION = "duration";
	
	private static final String  KEY_MUSIC_NAME = "musicname";
	private static final String  KEY_ARTIST = "artist";
	private static final String KEY_DATA = "data";
	private static final String  KEY_FOLDER = "folder";
	private static final String  KEY_MUSIC_NAME_KEY = "musicnamekey";
	private static final String  KEY_ARTISTKEY = "artistkey";

	
	public int _id;
	public int songid;
	public int albumid;
	public int duration;
	public String musicName; 
	public String artist; 
	public String data; 
	public String folder;
	public String musicNameKey; 
	public String artistKey; 

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
		bundle.putInt(KEY_ID, _id);
		bundle.putInt(KEY_SONGID, songid);
		bundle.putInt(KEY_ALBUMID, albumid);
		bundle.putInt(KEY_DURATION, duration);
		bundle.putString(KEY_MUSIC_NAME, musicName);
		bundle.putString(KEY_ARTIST, artist);
		bundle.putString(KEY_DATA, data);
		bundle.putString(KEY_FOLDER, folder);
		bundle.putString(KEY_MUSIC_NAME_KEY, musicNameKey);
		bundle.putString(KEY_ARTISTKEY, artistKey);
		dest.writeBundle(bundle);
	}
	
	public static final Parcelable.Creator<MusicInfo> CREATOR = new 
			Parcelable.Creator<MusicInfo>() {

				@Override
				public MusicInfo createFromParcel(Parcel source) {
					MusicInfo musicInfo = new MusicInfo();
					Bundle bundle = new Bundle();
					bundle = source.readBundle();
					musicInfo._id = bundle.getInt(KEY_ID);
					musicInfo.songid = bundle.getInt(KEY_SONGID); 
					musicInfo.albumid = bundle.getInt(KEY_ALBUMID); 
					
					musicInfo.duration = bundle.getInt(KEY_DURATION);  
					musicInfo.musicName = bundle.getString(KEY_MUSIC_NAME);
					musicInfo.artist = bundle.getString(KEY_ARTIST);
					musicInfo.data = bundle.getString(KEY_DATA);
					musicInfo.folder = bundle.getString(KEY_FOLDER);
					musicInfo.musicNameKey = bundle.getString(KEY_MUSIC_NAME_KEY);
					musicInfo.artistKey = bundle.getString(KEY_ARTISTKEY);
					return musicInfo;
				}

				@Override
				public MusicInfo[] newArray(int size) {
					// TODO Auto-generated method stub
					return new MusicInfo[size];
				}
	};

}
