package com.example.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.example.UIManager.BottomMusicUIMgr;
import com.example.adapter.MusicFileAdapter;
import com.example.aidl.IMediaService;
import com.example.common.MPConstants;
import com.example.db.AlbumInfoDao;
import com.example.db.ArtistInfoDao;
import com.example.db.FolderInfoDao;
import com.example.db.MusicInfoDao;
import com.example.db.PlayListInfoDao;
import com.example.interfaces.IOnServiceConnectComplete;
import com.example.model.MusicInfo;
import com.example.musicplayer.MusicPlayerApplication;
import com.example.musicplayer.R;
import com.example.service.ServiceManager;

public class MainFragment extends Fragment implements IOnServiceConnectComplete {
	private GridView mGridView;
	private ListView playList;
	private MusicFileAdapter mfAdapter;
	private MusicInfoDao musicInfoDao;
	private ArtistInfoDao artistInfoDao;
	private FolderInfoDao folderInfoDao;
	private AlbumInfoDao albumInfoDao;
	private PlayListInfoDao playListInfoDao;
	private ServiceManager serviceManager;
	private BottomMusicUIMgr btmMusicUIMgr;
	private MusicPlayControlBroadCast controlBroadCast;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		musicInfoDao = new MusicInfoDao(getActivity());
		albumInfoDao = new AlbumInfoDao(getActivity());
		artistInfoDao = new ArtistInfoDao(getActivity());
		folderInfoDao = new FolderInfoDao(getActivity());
		playListInfoDao = new PlayListInfoDao(getActivity());
		serviceManager = MusicPlayerApplication.mServiceManager;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_page, null);
		
		mGridView = (GridView)view.findViewById(R.id.gvMusicFile);
		mfAdapter = new MusicFileAdapter(getActivity());
		mGridView.setAdapter(mfAdapter);
		
		playList = (ListView)view.findViewById(R.id.playList);
		btmMusicUIMgr = new BottomMusicUIMgr(getActivity()); 
		serviceManager.connectService();
		serviceManager.setOnServiceConnectComplete(this);
		controlBroadCast = new MusicPlayControlBroadCast();
		IntentFilter filter = new IntentFilter(MPConstants.BROADCAST_NAME);
		filter.addAction(MPConstants.BROADCAST_NAME);
		getActivity().registerReceiver(controlBroadCast, filter);
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
		 int folder_num = folderInfoDao.getDataCount();
		 mfAdapter.setNum(song_num, album_num, artist_num, folder_num);
	}
	
	private class MusicPlayControlBroadCast extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(MPConstants.BROADCAST_NAME)){
				int curPlayIndex = intent.getIntExtra(MPConstants.PLAY_MUSIC_INDEX, -1);
				int curPlayState = intent.getIntExtra(MPConstants.PLAY_STATE_NAME, -1);
				Bundle bundle = intent.getBundleExtra(MusicInfo.KEY_MUSIC);
				MusicInfo musicInfo = new MusicInfo();
				if(bundle != null){
					musicInfo = bundle.getParcelable(MusicInfo.KEY_MUSIC);
				}
				btmMusicUIMgr.refreshUI(musicInfo);
				switch(curPlayState){
				case MPConstants.MPS_NOFILE:
					btmMusicUIMgr.showPlay(true);
					break;
				case MPConstants.MPS_INVALID:
					btmMusicUIMgr.showPlay(true);
					break;
				case MPConstants.MPS_PAUSE:
					btmMusicUIMgr.showPlay(true);
					break;
				case MPConstants.MPS_PLAYING:
					btmMusicUIMgr.showPlay(false);
					break;
				}
			}
		}
		
	}
	@Override
	public void onServiceConnectComplete(IMediaService service) {
		refreshNum();
	}
}
