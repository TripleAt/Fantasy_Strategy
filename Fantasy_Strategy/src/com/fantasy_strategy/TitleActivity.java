package com.fantasy_strategy;

//画面をスクロールできるように

//固定表示のボタンをつける　or　スクロールの下に余白を付ける

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class TitleActivity extends Activity {

	public static TitleMain tRenderer;
	public static MyGLSurfaceView glSurfaceView;
	public static Intent selectIntent;
	public static SaveData save;
	public static int total_score;

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

		total_score = 0;
		save = new SaveData(this);
		total_score = save.SaveFileInput();

		//インスタンスを保持させる
		Global.titleActivity = this;

		TitleActivity.tRenderer = new TitleMain(this);
		glSurfaceView = new MyGLSurfaceView(this);// MyGLSurfaceViewの生成
		glSurfaceView.setRenderer(tRenderer);
		setContentView(glSurfaceView);


	}

	public void onResume() {
		super.onResume();
		glSurfaceView.onResume();	//描画をスタート

		if(Sound.music.mp != null){
			Sound.music.BGM_Play();		//BGMを一時停止する
		}
	}

	@Override
	public void onPause() {
		glSurfaceView.onPause();	//描画を止める
		super.onPause();
		finish();
		System.gc();	//ガーベジコレクションを促す

		if(Sound.music.mp != null){		//BGMが生成されていたらストップして、開放する
			if(Sound.music.mp.isPlaying()){		//BGMが生成中かチェック
				Sound.music.BGM_Stop();					//BGMのストップ
	        }
			Sound.music.BGM_Release();				//ロードしたBGMの開放
			Sound.music.SE_Release();				//SEサウンドの開放
			Sound.music.mp = null;
			Sound.music.sp = null;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}



	/***
	 * 	アクティビティの生成
	 * @param state
	 */
	 public void setActivity( int state )
	 {
		 switch (state)
		{

			case Screen.GAME_TITLE:
				TouchManagement.touch_num = -1;
				TextureManager.deleteAll(Global.gl);		//テクスチャを削除する
				TouchManagement.list.clear();

				selectIntent = new Intent();
				selectIntent.setClassName("com.fantasy_strategy", "com.fantasy_strategy.TitleActivity");
				TitleActivity.glSurfaceView.mContext.startActivity(selectIntent);
				break;

			case Screen.GAME_MAIN:
				MainActivity.thread_run = true;
				GameMain.exe_stop_flg = true;
				TouchManagement.touch_num = -1;
				TouchManagement.list.clear();

				selectIntent = new Intent();
				selectIntent.setClassName("com.fantasy_strategy", "com.fantasy_strategy.MainActivity");
				TitleActivity.glSurfaceView.mContext.startActivity(selectIntent);
				break;

			case Screen.GAME_CLEAR:
				TouchManagement.touch_num = -1;
				ResultMain.gameclear_flg = true;
				GameMain.scroll_screen = true;				//リザルト画面の判定
				TouchManagement.list.clear();

				if(Sound.music.mp != null){					//BGMが生成されていたらストップして、開放する
					if(Sound.music.mp.isPlaying()){			//BGMが生成中かチェック
						Sound.music.BGM_Stop();				//BGMのストップ
			        }
					Sound.music.BGM_Release();				//ロードしたBGMの開放
					Sound.music.SE_Release();				//SEサウンドの開放
					Sound.music.mp = null;
					Sound.music.sp = null;
				}

				TextureManager.deleteAll(Global.gl);		//テクスチャを削除する
				selectIntent = new Intent();
				selectIntent.setClassName("com.fantasy_strategy", "com.fantasy_strategy.ResultActivity");
				TitleActivity.glSurfaceView.mContext.startActivity(selectIntent);
				break;

			case Screen.GAME_OVER:
				TouchManagement.touch_num = -1;
				ResultMain.gameclear_flg = false;
				GameMain.scroll_screen = true;				//リザルト画面の判定
				TextureManager.deleteAll(Global.gl);		//テクスチャを削除する
				TouchManagement.list.clear();

				if(Sound.music.mp != null){					//BGMが生成されていたらストップして、開放する
					if(Sound.music.mp.isPlaying()){			//BGMが生成中かチェック
						Sound.music.BGM_Stop();				//BGMのストップ
			        }
					Sound.music.BGM_Release();				//ロードしたBGMの開放
					Sound.music.SE_Release();				//SEサウンドの開放
					Sound.music.mp = null;
					Sound.music.sp = null;
				}

				selectIntent = new Intent();
				selectIntent.setClassName("com.fantasy_strategy", "com.fantasy_strategy.ResultActivity");
				TitleActivity.glSurfaceView.mContext.startActivity(selectIntent);
				break;

			case Screen.GAME_SELECT:
				TouchManagement.touch_num = -1;
				TextureManager.deleteAll(Global.gl);		//テクスチャを削除する
				TouchManagement.list.clear();

				selectIntent = new Intent();
				selectIntent.setClassName("com.fantasy_strategy", "com.fantasy_strategy.Stage_Select_Activity");
				TitleActivity.glSurfaceView.mContext.startActivity(selectIntent);

				break;

			case Screen.GAME_SHOP:
				//廃止
				break;

			case Screen.GAME_NEXT_STAGE:
				TouchManagement.touch_num = -1;
				TextureManager.deleteAll(Global.gl);		//テクスチャを削除する
				TouchManagement.list.clear();

				selectIntent = new Intent();
				selectIntent.setClassName("com.fantasy_strategy", "com.fantasy_strategy.Stage_Select_Activity");
				TitleActivity.glSurfaceView.mContext.startActivity(selectIntent);

				break;


		}
	 }



}
