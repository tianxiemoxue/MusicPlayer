package com.example.musicplayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.example.Utils.MusicUtils;
import com.example.Utils.SlashScreen;
import com.example.db.MusicInfoDao;
import com.example.fragment.MainFragment;

public class MainActivity extends FragmentActivity {
	private Handler mHandler;
	private SlashScreen slashScreen;
	private MainFragment mMainFragment;
	private MusicInfoDao musicInfoDao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		slashScreen = new SlashScreen(this);
		slashScreen.show(R.drawable.lanucher_background, SlashScreen.SLASH_FADE_OUT);
		
		mMainFragment = new MainFragment();
		getFragmentManager().beginTransaction().replace(R.id.frame_main, mMainFragment).commit();
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				slashScreen.removeSlashScreen();
			}
		};
		
	}

	private void getData(){
		new Runnable() {
			
			public void run() {
				if(musicInfoDao.hasData()){
					mHandler.sendMessageDelayed(mHandler.obtainMessage(), 3000);
				}else{
					MusicUtils.queryMusic(MainActivity.this,
							START_FROM_LOCAL);
					MusicUtils.queryAlbums(MainActivity.this);
					MusicUtils.queryArtist(MainActivity.this);
					MusicUtils.queryFolder(MainActivity.this);
					mHandler.sendEmptyMessage(1);
					
					
				}
				
			}
		};
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
