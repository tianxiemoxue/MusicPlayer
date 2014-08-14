package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.model.PlayListInfo;
import com.example.musicplayer.R;

public class MusicPlayListAdapter extends ArrayAdapter<PlayListInfo> {

	public MusicPlayListAdapter(Context context, int textViewResourceId,
			List<PlayListInfo> objects) {
		super(context, textViewResourceId, objects);
		
	}
	
	@Override
	public int getCount() {
		return super.getCount();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.playlist_item, null);
			vh = new ViewHolder();
			vh.listName = (TextView)convertView.findViewById(R.id.playListName);
			vh.songNum = (TextView)convertView.findViewById(R.id.numOfPlayList);
		}else{
			vh = (ViewHolder)convertView.getTag();
		}
		vh.listName.setText(getItem(position).name);
		vh.songNum.setText(getItem(position).getCount() +"สื");
		convertView.setTag(vh);
		return convertView;
	}
	
	private class ViewHolder{
		TextView listName;
		TextView songNum;
	}

}
