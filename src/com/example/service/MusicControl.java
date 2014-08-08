package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.RemoteException;

import com.example.Utils.MusicUtils;
import com.example.common.MPConstants;
import com.example.model.MusicInfo;

public class MusicControl implements OnCompletionListener {
	private static final String TAG = MusicControl.class.getName();
	private int curIndex;
	private int curMusicId;
	private MusicInfo curMusic;
	private List<MusicInfo> musicList = new ArrayList<MusicInfo>();
	private int curPlayState;
	private int curPlayMode;
	private Context context;
	private MediaPlayer mPlayer;
	private Random mRandom;

	public MusicControl(Context context) {
		this.context = context;
		mPlayer = new MediaPlayer();
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mPlayer.setOnCompletionListener(this);
		curIndex = -1;
		curMusicId = -1;
		mRandom = new Random();
		curPlayState = MPConstants.MPS_NOFILE;
		curPlayMode = MPConstants.MPM_LIST_LOOP_PLAY;
	}

	public boolean play(int pos) {
		if (curIndex == pos) {
			if (!mPlayer.isPlaying()) {
				mPlayer.start();
				curPlayState = MPConstants.MPS_PLAYING;
				sendBrocast();
			} else {
				pause();
			}
			return true;
		}
		if (!prepare(pos))
			return false;
		return replay();
	}

	private boolean prepare(int pos) {
		curIndex = pos;
		mPlayer.reset();
		String path = musicList.get(pos).data;
		try {
			mPlayer.setDataSource(path);
			mPlayer.prepare();
			curPlayState = MPConstants.MPS_PREPARE;
		} catch (Exception e) {
			curPlayState = MPConstants.MPS_INVALID;
			if (pos < musicList.size()) {
				pos++;
				playById(musicList.get(pos).songid);
			}
			return false;

		}
		sendBrocast();
		return true;
	}

	public boolean playById(int id) {
		int pos = MusicUtils.seekToPosById(musicList, id);
		return play(pos);
	}

	public boolean replay() {
		if (curPlayState == MPConstants.MPS_NOFILE
				|| curPlayState == MPConstants.MPS_INVALID)
			return false;
		mPlayer.start();
		curPlayState = MPConstants.MPS_PLAYING;
		sendBrocast();
		return true;
	}

	public boolean prev() {
		if (curPlayState == MPConstants.MPS_NOFILE)
			return false;
		curIndex--;
		curIndex = reviseIndex(curIndex);
		if (!prepare(curIndex))
			return false;
		return replay();

	}

	private int reviseIndex(int index) {
		if (index < 0)
			index = musicList.size() - 1;
		if (index >= musicList.size()) {
			index = 0;
		}
		return index;
	}

	public boolean next() {
		if (curPlayState == MPConstants.MPS_NOFILE)
			return false;
		curIndex++;
		curIndex = reviseIndex(curIndex);
		if (!prepare(curIndex))
			return false;
		return replay();
	}

	public boolean pause() {
		if (curPlayState != MPConstants.MPS_PLAYING)
			return false;
		mPlayer.pause();
		curPlayState = MPConstants.MPS_PAUSE;
		sendBrocast();
		return true;
	}

	public int duration() {
		if (curPlayState == MPConstants.MPS_INVALID
				|| curPlayState == MPConstants.MPS_NOFILE)
			return 0;
		return mPlayer.getDuration();
	}

	public int position() {
		if (curPlayState == MPConstants.MPS_INVALID
				|| curPlayState == MPConstants.MPS_NOFILE)
			return 0;
		return mPlayer.getCurrentPosition();
	}

	public boolean seekTo(int progress) {
		if (curPlayState == MPConstants.MPS_INVALID
				|| curPlayState == MPConstants.MPS_NOFILE)
			return false;
		int time = duration();
		int pg = reviseProgress(progress);
		int curTime = (int) (time * pg / 100);
		mPlayer.seekTo(curTime);
		return true;
	}

	private int reviseProgress(int pg) {
		int progress = pg;
		if (pg < 0) {
			progress = 0;
		}
		if (pg > 100) {
			progress = 100;
		}
		return progress;

	}

	public void refreshMusicList(List<MusicInfo> musicList)
			throws RemoteException {
		musicList.clear();
		this.musicList.addAll(musicList);
		if (musicList.size() == 0) {
			curPlayState = MPConstants.MPS_NOFILE;
			curIndex = -1;
		}

	}

	public List<MusicInfo> getMusicList() {
		return this.musicList;
	}

	public int getPlayState() {
		return curPlayState;
	}

	public int getPlayMode() {
		return curPlayMode;
	}

	public void setPlayMode(int mode) {
		this.curPlayMode = mode;

	}

	public void sendBrocast() {
		Intent intent = new Intent(MPConstants.BROADCAST_NAME);
		intent.putExtra(MPConstants.PLAY_STATE_NAME, curPlayState);
		intent.putExtra(MPConstants.PLAY_MUSIC_INDEX, curIndex);
		intent.putExtra("music_num", musicList.size());
		if(curPlayState != MPConstants.MPS_NOFILE && musicList.size() > 0) {
			Bundle bundle = new Bundle();
			curMusic = musicList.get(curIndex);
			curMusicId = curMusic.songid;
			bundle.putParcelable(MusicInfo.KEY_MUSIC, curMusic);
			intent.putExtra(MusicInfo.KEY_MUSIC, bundle);
		}
		context.sendBroadcast(intent);

	}

	public void exit() {
		mPlayer.stop();
		mPlayer.release();
		curIndex = -1;
		musicList.clear();
	}

	public int getCurMusicId() throws RemoteException {
		return curMusicId;
	}

	public void updateNotification(Bitmap bitmap, String title, String name)
			throws RemoteException {

	}

	public void cancelNotification() throws RemoteException {

	}

	public MusicInfo getCurMusic() {
		return curMusic;
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		switch (curPlayMode) {
		case MPConstants.MPM_LIST_LOOP_PLAY:
			next();
			break;
		case MPConstants.MPM_ORDER_PLAY:
			if (curIndex != musicList.size() - 1) {
				next();
			} else {
				prepare(curIndex);
			}
			break;
		case MPConstants.MPM_RANDOM_PLAY:
			int index = getRandomIndex();
			if (index == -1)
				index = 0;
			curIndex = index;
			if (prepare(curIndex))
				replay();
			break;
		case MPConstants.MPM_SINGLE_LOOP_PLAY:
			play(curIndex);
			break;
		default:
			break;
		}

	}
	
	private int getRandomIndex(){
		int size = musicList.size();
		if(size == 0) {
			return -1;
		}
		return Math.abs(mRandom.nextInt() % size);
	}
}
