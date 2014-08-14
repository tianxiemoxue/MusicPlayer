package com.example.adapter;

import com.example.musicplayer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MusicFileAdapter extends BaseAdapter{
	private Context mContext;
	public MusicFileAdapter(Context context){
		this.mContext = context;
	}
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.muisc_file_item, null);
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

