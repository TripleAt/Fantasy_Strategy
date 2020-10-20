package com.fantasy_strategy;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class Sound {
	public SoundPool sp;			//SEの変数
	public MediaPlayer mp;			//BGMの変数
	public int DataSourceId = 0;
	int soundId01[] = new int[30];
	public static Sound music = new Sound();


	/**
	 * 	サウンド生成
	 */
	public void Sound_Create(Context mContext)
	{
		sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		if(sp == null)
			Log.d("","ぬるぽ");
	}



	/**
	 * SEサウンドロード
	 * @param mContext
	 */
	public void SE_Sound_load(Context mContext, int num, int load_num){
		//if(sp != null)
			soundId01[num] = sp.load(mContext, load_num,1);
	}



	/***
	 *	SEサウンド再生番号
	 * @param num
	 */
	public void SE_play(int num){
		sp.play(soundId01[num], 1.0F, 1.0F, 0, 0, 1.0F);
	}



	/***
	 *	SEサウンドストップ
	 * @param num
	 */
	public void SE_stop(int num){
		sp.stop(soundId01[num]);
	}



	/***
	 *	SEサウンドの開放
	 * @param num
	 */
	public void SE_Release(){
		if(sp != null){
			sp.release();
		}
	}








	/***
	 * 	BGMのロード
	 * @param mContext
	 * @param load_num
	 */
	public void BGM_Load(Context mContext, int load_num){
		mp = MediaPlayer.create(mContext, load_num);
		DataSourceId = load_num;
	}


	/**
	 *
	 */
	public int getDataId(){
		return DataSourceId;
	}

	/**
	 * ロードしたBGMの再生
	 */
	public void BGM_Play(){
		if(mp != null)
		{
			mp.setLooping(true);
			mp.start();
		}
	}

	/**
	 * ロードしたBGMの再生 (ループ無し)
	 */
	public void BGM_notLoop(){
		if(mp != null)
		{
			mp.start();
		}
	}

	/**
	 * ロードしたBGMのストップ
	 */
	public void BGM_Stop(){
		mp.stop();
	}



	/**
	 * ロードしたBGMの開放
	 */
	public void BGM_Release(){
		mp.release();
	}

	/**
	 * BGMの一時停止
	 */
	public void BGM_Pause(){
		mp.pause();
	}



	/***
	 * 作る予定はないけどフェードINとフェードOUTのプログラムの一部を載せておく
	 *
            case FADEIN:

                if (mp == null) {
                    mp = MediaPlayer.create(GLView.get().getContext(), currentPlayBgm);
                    mp.setLooping(true);
                    mp.start();
                    vol = 0;
                    mp.setVolume(vol/100.0f, vol/100.0f);
                }
                vol += 2;
                if (targetVol < vol) {
                    vol = targetVol;
                    stat = PLAY_STAT.PLAYING;
                }
                player.setVolume(vol/100.0f, vol/100.0f);
                break;
            case PLAYING:
                break;
            case FADEOUT:

                if (player != null) {
                    vol -= 2;
                    if (vol < 0) {
                        vol = 0;
                        stat = PLAY_STAT.STOP;
                    }
                    mp.setVolume(vol/100.0f, vol/100.0f);
                } else {
                    stat = PLAY_STAT.STOP;
                }
                break;
	 *
	 */
}
