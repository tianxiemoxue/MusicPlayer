package com.example.Utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Files.FileColumns;
import android.text.TextUtils;

import com.example.common.MPConstants;
import com.example.db.AlbumInfoDao;
import com.example.db.ArtistInfoDao;
import com.example.db.FolderInfoDao;
import com.example.db.MusicInfoDao;
import com.example.db.PlayListInfoDao;
import com.example.model.AlbumInfo;
import com.example.model.ArtistInfo;
import com.example.model.FolderInfo;
import com.example.model.MusicInfo;
import com.example.model.PlayListInfo;

public class MusicUtils {
	private static MusicInfoDao musicInfoDao;
	private static ArtistInfoDao artistInfoDao;
	private static FolderInfoDao folderInfoDao;
	private static AlbumInfoDao albumInfoDao;
	private static PlayListInfoDao pListInfoDao;
	
	private static String[] proj_music = new String[] {
		MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
		MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
		MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ARTIST_ID,
		MediaStore.Audio.Media.DURATION };
	
	private static String[] proj_album = new String[] { Albums.ALBUM,
		Albums.NUMBER_OF_SONGS, Albums._ID, Albums.ALBUM_ART };
	
	private static String[] proj_folder = new String[] { FileColumns.DATA };
	
	private static String[] proj_artist = new String[] {
		MediaStore.Audio.Artists.ARTIST,
		MediaStore.Audio.Artists.NUMBER_OF_TRACKS };
	
	public static final int FILTER_SIZE = 1 * 1024 * 1024;// 1MB
	public static final int FILTER_DURATION = 1 * 60 * 1000;// 1分钟
	public static int seekToPosById(List<MusicInfo> list, int id){
		if(list == null || id == -1)
			return -1;
		for(int i=0; i< list.size(); i++)
		{
			if(list.get(i).songid == id)
			{
				return i;
			}
		}
		return -1;
	}
	
	/*----------------query operation -------------------------*/
	public static List<MusicInfo> queryMusic(Context context, int from) {
		return queryMusic(context, null, null, from);
	}

	public static List<MusicInfo> queryMusic(Context context,
			String selections, String selection, int from) {
		if(musicInfoDao == null) {
			musicInfoDao = new MusicInfoDao(context);
		}
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		ContentResolver cr = context.getContentResolver();

		StringBuffer select = new StringBuffer(" 1=1 ");
		// 查询语句：检索出.mp3为后缀名，时长大于1分钟，文件大小大于1MB的媒体文件

		select.append(" and " + Media.SIZE + " > " + FILTER_SIZE);
		select.append(" and " + Media.DURATION + " > " + FILTER_DURATION);

		if (!TextUtils.isEmpty(selections)) {
			select.append(selections);
		}
		
		switch(from) {
		case MPConstants.START_FROM_LOCAL:
			if (musicInfoDao.hasData()) {
				return musicInfoDao.getMusicInfo();
			} else {
				List<MusicInfo> list = getMusicList(cr.query(uri, proj_music,
						select.toString(), null,
						MediaStore.Audio.Media.ARTIST_KEY));
				musicInfoDao.saveMusicInfo(list);
				return list;
			}
		case MPConstants.START_FROM_ARTIST:
			if (musicInfoDao.hasData()) {
				return musicInfoDao.getMusicInfoByType(selection,
						MPConstants.START_FROM_ARTIST);
			} else {
//				return getMusicList(cr.query(uri, proj_music,
//						select.toString(), null,
//						MediaStore.Audio.Media.ARTIST_KEY));
			}
		case MPConstants.START_FROM_ALBUM:
			if (musicInfoDao.hasData()) {
				return musicInfoDao.getMusicInfoByType(selection,
						MPConstants.START_FROM_ALBUM);
			}
		case MPConstants.START_FROM_FOLDER:
			if(musicInfoDao.hasData()) {
				return musicInfoDao.getMusicInfoByType(selection, MPConstants.START_FROM_FOLDER);
			}
			default:
				return null;
		}

	}
	
	public static List<AlbumInfo> queryAlbums(Context context){
		if(albumInfoDao == null){
			albumInfoDao = new AlbumInfoDao(context);
		}
		Uri uri = Albums.EXTERNAL_CONTENT_URI;
		ContentResolver cr = context.getContentResolver();
		StringBuilder where = new StringBuilder(Albums._ID 
				+" in (select distinct " + Albums._ID 
				+ " from audio_meta where (1=1 " );
		where.append(" and " + Media.SIZE +" > " + FILTER_SIZE);
		where.append(" and " + Media.DURATION +" > " + FILTER_DURATION);
		where.append("))");
		
		if(albumInfoDao.hasData()){
			return albumInfoDao.getAlbumInfo();
		}else{
			List<AlbumInfo> list = getAlbumList(cr.query(uri, proj_album, where.toString(), null, Albums.ALBUM_KEY));
			albumInfoDao.saveAlbumInfo(list);
			return list;
		}
	}
	
	public static List<ArtistInfo> queryArtist(Context context){
		if(artistInfoDao == null) {
			artistInfoDao = new ArtistInfoDao(context);
		}
		Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
		ContentResolver cr = context.getContentResolver();
		if (artistInfoDao.hasData()) {
			return artistInfoDao.getArtistInfo();
		} else {
			List<ArtistInfo> list = getArtistList(cr.query(uri, proj_artist,
					null, null, MediaStore.Audio.Artists.NUMBER_OF_TRACKS
							+ " desc"));
			artistInfoDao.saveArtistInfo(list);
			return list;
		}
	}
	
	public static List<FolderInfo> queryFolder(Context context){
		if(folderInfoDao == null) {
			folderInfoDao = new FolderInfoDao(context);
		}
		//SPStorage sp = new SPStorage(context);
		Uri uri = MediaStore.Files.getContentUri("external");
		ContentResolver cr = context.getContentResolver();
		StringBuilder mSelection = new StringBuilder(FileColumns.MEDIA_TYPE
				+ " = " + FileColumns.MEDIA_TYPE_AUDIO + " and " + "("
				+ FileColumns.DATA + " like'%.mp3' or " + Media.DATA
				+ " like'%.wma')");
		// 查询语句：检索出.mp3为后缀名，时长大于1分钟，文件大小大于1MB的媒体文件
		mSelection.append(" and " + Media.SIZE + " > " + FILTER_SIZE);
		mSelection.append(" and " + Media.DURATION + " > " + FILTER_DURATION);
		
		mSelection.append(") group by ( " + FileColumns.PARENT);
		if (folderInfoDao.hasData()) {
			return folderInfoDao.getFolderInfo();
		} else {
			List<FolderInfo> list = getFolderList(cr.query(uri, proj_folder, mSelection.toString(), null, null));
			folderInfoDao.saveFolderInfo(list);
			return list;
		}
	}
	
	public static List<PlayListInfo> queryPlayList(Context context){
		if(pListInfoDao == null){
			pListInfoDao = new PlayListInfoDao(context);
		}
		return pListInfoDao.getPlayListInfo(null);
		
	}
	
	/*------------------------get data -----------------------------*/
	
	public static List<AlbumInfo> getAlbumList(Cursor cursor){
		List<AlbumInfo> list = new ArrayList<AlbumInfo>();
		while(cursor.moveToNext()) {
			AlbumInfo info = new AlbumInfo();
			info.album_name = cursor.getString(cursor.getColumnIndex("album_name"));
			info.album_art = cursor.getString(cursor.getColumnIndex("album_art"));
			info.album_id = cursor.getInt(cursor.getColumnIndex("album_id"));
			info.number_of_songs = cursor.getInt(cursor.getColumnIndex("number_of_songs"));
			list.add(info);
		}
		cursor.close();
		return list;
	}
	
	public static List<FolderInfo> getFolderList(Cursor cursor){
			List<FolderInfo> list = new ArrayList<FolderInfo>();
			while(cursor.moveToNext()) {
				FolderInfo info = new FolderInfo();
				info.folder_name = cursor.getString(cursor.getColumnIndex("folder_name"));
				info.folder_path = cursor.getString(cursor.getColumnIndex("folder_path"));
				list.add(info);
			}
			cursor.close();
			return list;
	}
	
	public static List<ArtistInfo> getArtistList(Cursor cursor){
		List<ArtistInfo> list = new ArrayList<ArtistInfo>();
		while(cursor.moveToNext()) {
			ArtistInfo info = new ArtistInfo();
			info.artist_name = cursor.getString(cursor.getColumnIndex("artist_name"));
			info.number_of_tracks = cursor.getInt(cursor.getColumnIndex("number_of_tracks"));
			list.add(info);
		}
		cursor.close();
		return list;
	}
	
	public static List<MusicInfo> getMusicList(Cursor cursor){
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
}
