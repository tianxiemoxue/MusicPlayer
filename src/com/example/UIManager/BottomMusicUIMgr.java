package com.example.UIManager;

import com.example.model.MusicInfo;
import com.example.musicplayer.MusicPlayerApplication;
import com.example.musicplayer.R;
import com.example.service.ServiceManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BottomMusicUIMgr implements OnClickListener{
	private Context mcontext;
	private View mView;
	private RelativeLayout btnMusicPlay;
	private TextView tvSongName, tvPlayerName;
	private ImageView imgvIcon, imgvPlay, imgvPause, imgvNext, imgvPlayList;
	private ServiceManager serviceManager;
	public BottomMusicUIMgr(Context context){
	   this.mcontext = context;
	   serviceManager = MusicPlayerApplication.mServiceManager;
	   mView = LayoutInflater.from(context).inflate(R.layout.bottom_play_control, null);
	   initView();
	}
	
	private void initView(){
		tvSongName = (TextView)mView.findViewById(R.id.btm_song_name);
		tvPlayerName = (TextView)mView.findViewById(R.id.btm_player_name);
		btnMusicPlay = (RelativeLayout)mView.findViewById(R.id.btnMusicPlay);
		btnMusicPlay.setOnClickListener(this);
		
		imgvIcon = (ImageView)mView.findViewById(R.id.icon_Song);
		imgvPlay = (ImageView)mView.findViewById(R.id.ivPlay);
		imgvPlay.setOnClickListener(this);
		
		imgvPause = (ImageView)mView.findViewById(R.id.ivPause);
		imgvPause.setOnClickListener(this);
		
		imgvNext = (ImageView)mView.findViewById(R.id.ivNext);
		imgvNext.setOnClickListener(this);
		imgvPlayList = (ImageView)mView.findViewById(R.id.ivPlayList);
		imgvPlayList.setOnClickListener(this);
		
	}
	
	public void refreshUI(MusicInfo musicInfo){
		tvSongName.setText(musicInfo.musicName);
		tvPlayerName.setText(musicInfo.artist);
		//图片需要缓存和二次采样处理，这里先赋一个默认的
		imgvIcon.setBackgroundResource(R.drawable.icon_def_music);
//		Bitmap bitmap = MusicUtils.getCachedArtwork(mActivity, music.albumId,
//				mDefaultAlbumIcon);
//
//		mHeadIcon.setBackgroundDrawable(new BitmapDrawable(mActivity
//				.getResources(), bitmap));
	}

	public void showPlay(boolean isShowPlay){
		if(isShowPlay){
			imgvPlay.setVisibility(View.VISIBLE);
			imgvPause.setVisibility(View.GONE);
		}else{
			imgvPlay.setVisibility(View.GONE);
			imgvPause.setVisibility(View.VISIBLE);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnMusicPlay:
	    //显示播放acticity
			break;
		case R.id.ivPlay:
			serviceManager.rePlay();
			break;
		case R.id.ivNext:
			serviceManager.next();
			break;
		case R.id.ivPlayList:
			//显示播放队列
			break;
		case R.id.ivPause:
			serviceManager.pause();
			break;
		default:
			break;
		}
		
	}
	
}
