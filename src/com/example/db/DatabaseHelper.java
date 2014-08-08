package com.example.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	private static DatabaseHelper mHelper;
	private static SQLiteDatabase mDB;
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "musicstore_new";
	private static final String TABLE_ALBUM = "album_info";
	private static final String TABLE_ARTIST = "artist_info";
	private static final String TABLE_MUSIC = "music_info";
	private static final String TABLE_FOLDER = "folder_info";
	private static final String TABLE_PLAYERLIST = "playerlist_info";
	private static final String TABLE_LISTDETAIL = "list_data";

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	public DatabaseHelper(Context context){
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	public static SQLiteDatabase getInstance(Context context)
	{
		if(mDB == null){
			mDB = getHelper(context).getWritableDatabase();
		}
		return mDB;
	}
	public static DatabaseHelper getHelper(Context context){
		if(mHelper == null){
			mHelper = new DatabaseHelper(context);
			
		}
		return mHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE"
				+ TABLE_MUSIC
				+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+"songid integer," +"albumid integer,"
				+"duration integer,"+"musicname varchar(20),"
				+"artist char,"+"data char," +"folder char," 
				+"musicnamekey char, "+"artistkey char)" 
				);
		db.execSQL("CREATE TABLE"
				+TABLE_ALBUM
				+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+"album_name char, album_id integer, number_of_songs integer,"
				+"album_art char)"
				);
		db.execSQL("CREATE TABLE"
				+TABLE_ARTIST
				+"(_id INTEGER PRIMARY KEY AUTOINCREMENT, artist_name char," +
				"number_of_tracks integer)"
				);
		db.execSQL("CREATE TABLE"
				+TABLE_FOLDER
				+"(_id INTEGER PRIMARY KEY AUTOINCREMENT, folder_name varchar(20),"
				+"folder_path char)"
				);
		db.execSQL("CREATE TABLE"
				+TABLE_PLAYERLIST
				+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+"list_name varchar(20))"
				);
		db.execSQL("CREATE TABLE"
				+TABLE_LISTDETAIL
				+"(list_id Integer not null,"
				+"song_id Integer not null,"
				+"PRIMARY KEY (list_id, song_id))");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS"+TABLE_MUSIC);
		db.execSQL("DROP TABLE IF EXISTS"+TABLE_ALBUM);
		db.execSQL("DROP TABLE IF EXISTS"+TABLE_ARTIST);
		db.execSQL("DROP TABLE IF EXISTS"+TABLE_FOLDER);
		db.execSQL("DROP TABLE IF EXISTS"+TABLE_PLAYERLIST);
		db.execSQL("DROP TABLE IF EXISTS"+TABLE_LISTDETAIL);
		onCreate(db);
	}

}
