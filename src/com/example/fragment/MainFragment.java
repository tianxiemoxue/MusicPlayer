package com.example.fragment;

import android.R.integer;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.db.AlbumInfoDao;
import com.example.db.ArtistInfoDao;
import com.example.db.FolderInfoDao;
import com.example.db.MusicInfoDao;
import com.example.model.AlbumInfo;
import com.example.musicplayer.R;

public class MainFragment extends Fragment {
	private GridView mGridView;
	private MusicFileAdapter mfAdapter;
	private MusicInfoDao musicInfoDao;
	private ArtistInfoDao artistInfoDao;
	private FolderInfoDao folderInfoDao;
	private AlbumInfoDao albumInfoDao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		musicInfoDao = new MusicInfoDao(getActivity());
		albumInfoDao = new AlbumInfoDao(getActivity());
		artistInfoDao = new ArtistInfoDao(getActivity());
		folderInfoDao = new FolderInfoDao(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_page, null);
		mGridView = (GridView)view.findViewById(R.id.gvMusicFile);
		mfAdapter = new MusicFileAdapter();
		mGridView.setAdapter(mfAdapter);
		return view;
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refreshNum();
	}
	
	public void refreshNum(){
		 int song_num = musicInfoDao.getCount();
		 int album_num = albumInfoDao.getDataCount();
		 int artist_num = artistInfoDao.getDataCount();
		 int folder_num = artistInfoDao.getDataCount();
		 mfAdapter.setNum(song_num, album_num, artist_num, folder_num);
	}
	
	private class MusicFileAdapter extends BaseAdapter{
		private final String[] typeName = new String[]{"本地音乐", "音乐专辑", "音乐人", "文件夹"};
		private int[] drawable = new int[]{R.drawable.icon_local_music, R.drawable.icon_music_album,
				R.drawable.icon_music_player, R.drawable.icon_local_folder};
		private int song_num;
		private int album_num;
		private int artist_num;
		private int folder_num;
		
		public void setNum(int songNum, int albumNum, int artistNum, int folderNum){
			this.song_num = songNum;
			this.album_num = albumNum;
			this.artist_num = artistNum;
			this.folder_num = folderNum;
			notifyDataSetChanged();
			
		}
		@Override
		public int getCount() {
			return typeName.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if(convertView == null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.muisc_file_item, null);
				vh = new ViewHolder();
				vh.tvSong_Num = (TextView)convertView.findViewById(R.id.item_num);
				vh.imgItem_Icon = (ImageView)convertView.findViewById(R.id.item_icon);
				vh.tvItem_Name = (TextView)convertView.findViewById(R.id.item_name);
			}else {
				vh = (ViewHolder)convertView.getTag();
			}
			switch (position) {
			case 0:// 我的音乐
				vh.tvSong_Num.setText(song_num + "首");
				break;
			case 1:// 音乐专辑
				vh.tvSong_Num.setText(album_num + "张");
				break;
			case 2:// 艺术家
				vh.tvSong_Num.setText(artist_num + "位");
				break;
			case 3:// 文件夹
				vh.tvSong_Num.setText(folder_num + "");
				break;
				}
			vh.imgItem_Icon.setImageResource(drawable[position]);
			vh.tvItem_Name.setText(typeName[position]);
			
			convertView.setTag(vh);
			return convertView; 
		}
		
		private class ViewHolder{
			TextView tvSong_Num;
			ImageView imgItem_Icon;
			TextView tvItem_Name;
		}
		
	}
}
