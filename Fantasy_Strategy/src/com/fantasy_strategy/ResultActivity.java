package com.fantasy_strategy;

//http://blog.fujiu.jp/2013/05/android-opengl.html

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class ResultActivity  extends Activity {

	private ResultMain tRenderer;



	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// フルスクリーン、タイトルバーの非表示
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// デバックモードであるか判定する
		try {
			PackageManager pm = getPackageManager();
			ApplicationInfo ai = pm.getApplicationInfo(getPackageName(), 0);
			Global.isDebuggable = (ApplicationInfo.FLAG_DEBUGGABLE == (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		//インスタンスを保持させる
		Global.ResultActivity = this;

		TextureManager.deleteAll(Global.gl);		//テクスチャを削除する
		TouchManagement.list.clear();
		tRenderer = new ResultMain(Global.ResultActivity);
		TitleActivity.glSurfaceView = new MyGLSurfaceView(Global.ResultActivity);// MyGLSurfaceViewの生成
		TitleActivity.glSurfaceView.setRenderer(tRenderer);
		setContentView(TitleActivity.glSurfaceView);



	}
	/*
	public boolean onPrepareOptionsMenu(Menu menu) {
	       return false;
	}*/

	public void onResume() {
		TitleActivity.glSurfaceView.onResume();
//		super.onResume();

		if(Sound.music.mp != null){
			Sound.music.BGM_Play();		//BGMを一時停止する
		}

		TitleActivity.glSurfaceView.onResume();
		super.onResume();


	}

	@Override
	public void onPause() {
		TitleActivity.glSurfaceView.onPause();

		if(Sound.music.mp != null){		//BGMが生成されていたらストップして、開放する
			if(Sound.music.mp.isPlaying()){		//BGMが生成中かチェック
				Sound.music.BGM_Stop();					//BGMのストップ
	        }
			Sound.music.BGM_Release();				//ロードしたBGMの開放
			Sound.music.SE_Release();				//SEサウンドの開放
			Sound.music.mp = null;
			Sound.music.sp = null;
		}

		GameMain.GameMain_flg = false;
		finish();
		super.onDestroy();
		super.onPause();

		finish();
		GameMain.scroll_screen = false;					//リザルト画面(スクロール)の判定リセット
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		//とりあえず注釈。今後、バックボタンを押した際、消したくない場合使用。
		if (event.getAction() == KeyEvent.ACTION_DOWN)
		{		// キーが押された
			switch (event.getKeyCode())
			{
				case KeyEvent.KEYCODE_BACK: //Backボタン
					Log.d("テクスチャ消去","aaa");
					TextureManager.deleteAll(Global.gl);		//テクスチャを削除する
					//return true;
				default:
			}
		}
		//Log.d("テクスチャ消去","");
		//TextureManager.deleteAll(Global.gl);		//テクスチャを削除する
		return super.dispatchKeyEvent(event);
	}

}
