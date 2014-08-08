package com.example.service;

import java.util.List;
import com.example.aidl.IMediaService;
import com.example.model.MusicInfo;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;

public class MusicService extends Service {
	private static final String PAUSE_BROADCAST_NAME = "com.example.music.pause.broadcast";
	private static final String NEXT_BROADCAST_NAME ="com.example.music.next.broadcast";
	private static final String PREV_BROADCAST_NAME ="com.example.music.prev.broadcast";
	private MusicStub mMusicStub;
	private MusicControl mMusicControl;
	private NotificationManager ntfMgr;
	private int NOTIFICATION_ID = 0x1;
	private static final int STOP_FLAG = 0x1;
	private static final int NEXT_FLAG = 0x2;
	private static final int PREV_FLAG = 0X3;
	private ControlBroadCast controlBroadCast;
	
	@Override
	public void onCreate() {
		mMusicStub = new MusicStub();
		mMusicControl = new MusicControl(this);
		ntfMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		controlBroadCast = new ControlBroadCast();
		IntentFilter filter = new IntentFilter();
		filter.addAction(PAUSE_BROADCAST_NAME);
		filter.addAction(NEXT_BROADCAST_NAME);
		filter.addAction(PREV_BROADCAST_NAME);
		registerReceiver(controlBroadCast, filter);
		super.onCreate();
	}
	@Override
	public IBinder onBind(Intent intent) {
		return mMusicStub;
	}
	
	private void updateNotification(Bitmap bitmap, String title, String name){
		//自定义通知
	}
	
	private void cancelNotification(){
		stopForeground(true);
		ntfMgr.cancel(NOTIFICATION_ID);
	} 

	private class ControlBroadCast extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			int flag = intent.getIntExtra("FLAG", -1);
			switch(flag){
			case STOP_FLAG:
				mMusicControl.pause();
				break;
			case PREV_FLAG:
				mMusicControl.prev();
				break;
			case NEXT_FLAG:
				mMusicControl.next();
				break;
			default:
				break;
			}
		}
		
	}
	
	private class MusicStub extends IMediaService.Stub{

		@Override
		public boolean play(int pos) throws RemoteException {
			return mMusicControl.play(pos);
		}

		@Override
		public boolean playById(int id) throws RemoteException {
			return mMusicControl.playById(id);
		}

		@Override
		public boolean prev() throws RemoteException {
			return mMusicControl.prev();
		}

		@Override
		public boolean next() throws RemoteException {
			return mMusicControl.next();
		}

		@Override
		public boolean pause() throws RemoteException {
			return mMusicControl.pause();
		}

		@Override
		public int duration() throws RemoteException {
			return mMusicControl.duration();
		}

		@Override
		public int position() throws RemoteException {
			return mMusicControl.position();
		}

		@Override
		public boolean seekTo(int progress) throws RemoteException {
			return mMusicControl.seekTo(progress);
		}

		@Override
		public void refreshMusicList(List<MusicInfo> musicList)
				throws RemoteException {
			mMusicControl.refreshMusicList(musicList);
			
		}

		@Override
		public void getMusicList(List<MusicInfo> musicList)
				throws RemoteException {
			List<MusicInfo> mList = mMusicControl.getMusicList();
			for(MusicInfo musicInfo : mList){
				musicList.add(musicInfo);
			}
			
		}

		@Override
		public int getPlayState() throws RemoteException {
			return mMusicControl.getPlayState();
		}

		@Override
		public int getPlayMode() throws RemoteException {
			return mMusicControl.getPlayMode();
		}

		@Override
		public void setPlayMode(int mode) throws RemoteException {
			mMusicControl.setPlayMode(mode);
		}

		@Override
		public void sendPlayStateBrocast() throws RemoteException {
			mMusicControl.sendBrocast();
		}

		@Override
		public void exit() throws RemoteException {
			cancelNotification();
			stopSelf();
			mMusicControl.exit();
		}

		@Override
		public int getCurMusicId() throws RemoteException {
			return mMusicControl.getCurMusicId();
		}

		@Override
		public void updateNotification(Bitmap bitmap, String title, String name)
				throws RemoteException {
			MusicService.this.updateNotification(bitmap, title, name);
			
		}

		@Override
		public void cancelNotification() throws RemoteException {
			MusicService.this.cancelNotification();
		}

		@Override
		public MusicInfo getCurMusic() throws RemoteException {
			return mMusicControl.getCurMusic();
		}

		@Override
		public boolean replay() throws RemoteException {
			return mMusicControl.replay();
		}
		
	}

}
