package com.example.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.common.MPConstants;
import com.example.model.MusicInfo;

public class MusicInfoDao {
	private static final String TABLE_MUSIC = "music_info";
	private static final String TABLE_LISTDETAIL = "list_data";
	private Context context;
	public MusicInfoDao(Context context)
	{
		this.context = context;
		
	}
	
	public void saveMusicInfo(List<MusicInfo> list)
	{
		SQLiteDatabase db = DatabaseHelper.getInstance(context);
		for(MusicInfo music: list)
		{
			ContentValues cv = new ContentValues();
			cv.put("songid", music.songid);
			cv.put("albumid", music.albumid);
			cv.put("duration", music.duration);
			cv.put("musicname", music.musicName);
			cv.put("artist", music.artist);
			cv.put("data",music.data);
			cv.put("folder", music.folder);
			cv.put("musicnamekey", music.musicNameKey);
			cv.put("artistkey", music.artistKey);
			db.insert(TABLE_MUSIC, null, cv);
		}
	}
	
	public List<MusicInfo> getMusicInfo()
	{
		SQLiteDatabase db = DatabaseHelper.getInstance(context);
		String sql = "SELECT * FROM" + TABLE_MUSIC;
		return parseCursor(db.rawQuery(sql, null));
	}
	
	public List<MusicInfo> getMusicInfoByPlayListID(String selection, int list_Id)
	{
		SQLiteDatabase db = DatabaseHelper.getInstance(context);
		String sql = "SELECT * FROM" + TABLE_MUSIC +", " + TABLE_LISTDETAIL
				+"WHERE list_data.list_id = " + list_Id +
				"music_info._id = list_data.song_id";
		
		return parseCursor(db.rawQuery(sql, null));
	}
	private List<MusicInfo> parseCursor(Cursor cursor)
	{
		List<MusicInfo> list = new ArrayList<MusicInfo>();
		while(cursor.moveToNext())
		{
			MusicInfo music = new MusicInfo();
			music._id = cursor.getInt(cursor.getColumnIndex("_id"));
			music.songid = cursor.getInt(cursor.getColumnIndex("songid"));
			music.albumid = cursor.getInt(cursor.getColumnIndex("albumid"));
			music.duration = cursor.getInt(cursor.getColumnIndex("duration"));
			music.musicName = cursor.getString(cursor.getColumnIndex("musicname"));
			music.artist = cursor.getString(cursor.getColumnIndex("artist"));
			music.data = cursor.getString(cursor.getColumnIndex("data"));
			music.folder = cursor.getString(cursor.getColumnIndex("folder"));
			music.musicNameKey = cursor.getString(cursor.getColumnIndex("musicnamekey"));
			music.artistKey = cursor.getString(cursor.getColumnIndex("artistkey"));
			list.add(music);
		}
		cursor.close();
		return list;
	}
	
	public List<MusicInfo> getMusicInfoByType(String selection, int type){
		SQLiteDatabase db = DatabaseHelper.getInstance(context);
		String sql = "";
		if(type == MPConstants.START_FROM_ARTIST) {
			sql = "select * from " + TABLE_MUSIC + " where artist = ?";
		} else if(type == MPConstants.START_FROM_ALBUM) {
			sql = "select * from " + TABLE_MUSIC + " where albumid = ?";
		} else if(type == MPConstants.START_FROM_FOLDER) {
			sql = "select * from " + TABLE_MUSIC + " where folder = ?";
		}
		return parseCursor(db.rawQuery(sql, new String[]{ selection }));
	}
	public boolean hasData()
	{
		SQLiteDatabase db = DatabaseHelper.getInstance(context);
		String sql = "SELECT COUNT(*) FROM " + TABLE_MUSIC;
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
		String sql = "SELECT COUNT(*) FROM " + TABLE_MUSIC;
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
