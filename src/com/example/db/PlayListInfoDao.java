package com.example.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.model.MusicInfo;
import com.example.model.PlayListInfo;

public class PlayListInfoDao {
	private static final String TABLE_PLAYERLIST ="playerlist_info";
	private static final String TABLE_LISTDETAIL = "list_data";
	
	private Context context;
	public PlayListInfoDao(Context context)
	{
		this.context = context;
	}
	public void savePlayListInfo(PlayListInfo playList)
	{
		SQLiteDatabase db = DatabaseHelper.getInstance(context);
		ContentValues cv = new ContentValues();
		cv.put("list_name", playList.name);
		db.insert(TABLE_PLAYERLIST, null, cv);
		String sql = "SELECT _id FROM " + TABLE_PLAYERLIST +
				"WHERE list_name = '" +playList.name +"'";
		Cursor cursor = db.rawQuery(sql, null);
		int list_id = -1;
		while(cursor.moveToNext())
		{
			list_id = cursor.getInt(0);
		}
		cursor.close();
		for(MusicInfo music: playList.music_list)
		{
			ContentValues temp = new ContentValues();
			temp.put("list_id", list_id);
			temp.put("song_id", music._id);
			db.insert(TABLE_LISTDETAIL, null, temp);
		}
	}
	
	public List<PlayListInfo> getPlayListInfo(String selection)
	{
		SQLiteDatabase db = DatabaseHelper.getInstance(context);
		List<PlayListInfo> infoList = new ArrayList<PlayListInfo>();
		String sql = "SELECT * FROM " + TABLE_PLAYERLIST ;
		MusicInfoDao musicInfoDao = new MusicInfoDao(context);
		Cursor cursor = db.rawQuery(sql, null);
		while(cursor.moveToNext())
		{
			PlayListInfo playList = new PlayListInfo();
			playList._id = cursor.getInt(cursor.getColumnIndex("_id"));
			playList.name = cursor.getString(cursor.getColumnIndex("list_name"));
			playList.music_list = (ArrayList)musicInfoDao.getMusicInfoByPlayListID(selection, playList._id);
			infoList.add(playList);
		}
		cursor.close();
		return infoList;
	}
	
	public boolean hasData()
	{
		SQLiteDatabase db = DatabaseHelper.getInstance(context);
		String sql = "SELECT COUNT(*) FROM " + TABLE_PLAYERLIST;
		Cursor cursor = db.rawQuery(sql, null);
		boolean has = false;
		while(cursor.moveToNext())
		{
			
			has = cursor.getInt(0) > 0;
		}
		cursor.close();
		return has;
	}
	
	public int getCount()
	{
		SQLiteDatabase db = DatabaseHelper.getInstance(context);
		String sql = "SELECT COUNT(*) FROM " + TABLE_PLAYERLIST;
		Cursor cursor = db.rawQuery(sql, null);
		int count = 0;
		while(cursor.moveToNext())
		{
			count = cursor.getInt(0);
		}
		cursor.close();
		return count;
	}
}
