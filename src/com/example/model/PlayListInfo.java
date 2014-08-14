package com.example.model;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class PlayListInfo implements Parcelable{
	public static final String KEY_ID ="_id";
	public static final String KEY_NAME ="list_name";
	public static final String KEY_MUSIC_LIST ="music_list";
	
	public int _id = -1;
	public String name;
	public ArrayList<MusicInfo> music_list;
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int getCount(){
		int count = 0;
		if(music_list != null)
			count = music_list.size();
		return count;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
		bundle.putInt(KEY_ID, _id);
		bundle.putString(KEY_NAME, name);
		bundle.putParcelableArrayList(KEY_MUSIC_LIST, music_list);
		dest.writeBundle(bundle);
		
	}
	
	public static final Parcelable.Creator<PlayListInfo> creator = new 
			Parcelable.Creator<PlayListInfo>() {

				@Override
				public PlayListInfo createFromParcel(Parcel source) {
					// TODO Auto-generated method stub
					PlayListInfo info = new PlayListInfo();
					Bundle bundle = new Bundle();
					bundle = source.readBundle();
					info._id = bundle.getInt(KEY_ID);
					info.name = bundle.getString(KEY_NAME);
					info.music_list = bundle.getParcelableArrayList(KEY_MUSIC_LIST);
					return info;
				}

				@Override
				public PlayListInfo[] newArray(int size) {
					return new PlayListInfo[size];
				}
	};

}
