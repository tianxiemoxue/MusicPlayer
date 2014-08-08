package com.example.service;

import java.util.List;

import com.example.aidl.IMediaService;
import com.example.model.MusicInfo;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;

public class MusicService extends Service {
	private MusicStub myStub;
	@Override
	public IBinder onBind(Intent intent) {
		return myStub;
	}
	
	private class MusicStub extends IMediaService.Stub{

		@Override
		public boolean play(int pos) throws RemoteException {
			return false;
		}

		@Override
		public boolean playById(int id) throws RemoteException {
			return false;
		}

		@Override
		public boolean prev() throws RemoteException {
			return false;
		}

		@Override
		public boolean next() throws RemoteException {
			return false;
		}

		@Override
		public boolean pause() throws RemoteException {
			return false;
		}

		@Override
		public int duration() throws RemoteException {
			return 0;
		}

		@Override
		public int position() throws RemoteException {
			return 0;
		}

		@Override
		public boolean seekTo(int progress) throws RemoteException {
			return false;
		}

		@Override
		public void refreshMusicList(List<MusicInfo> musicList)
				throws RemoteException {
			
		}

		@Override
		public void getMusicList(List<MusicInfo> musicList)
				throws RemoteException {
			
		}

		@Override
		public int getPlayState() throws RemoteException {
			return 0;
		}

		@Override
		public int getPlayMode() throws RemoteException {
			return 0;
		}

		@Override
		public void setPlayMode(int mode) throws RemoteException {
			
		}

		@Override
		public void sendPlayStateBrocast() throws RemoteException {
			
		}

		@Override
		public void exit() throws RemoteException {
			
		}

		@Override
		public int getCurMusicId() throws RemoteException {
			return 0;
		}

		@Override
		public void updateNotification(Bitmap bitmap, String title, String name)
				throws RemoteException {
			
		}

		@Override
		public void cancelNotification() throws RemoteException {
			
		}

		@Override
		public MusicInfo getCurMusic() throws RemoteException {
			return null;
		}

		@Override
		public boolean replay() throws RemoteException {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}
