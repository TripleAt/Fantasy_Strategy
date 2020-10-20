package com.fantasy_strategy;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.fantasy_strategy.GameMain.SThread;


class Screen{
	//定数（仮）
	public static final int GAME_MAIN = 20;
	public static final int GAME_TITLE = 10;
	public static final int GAME_CLEAR = 30;
	public static final int GAME_SELECT = 40;
	public static final int GAME_SHOP = 50;
	public static final int GAME_NEXT_STAGE = 60;
	public static final int GAME_OVER = 70;


}


public class MainActivity extends Activity {

private GameMain mRenderer;
static SThread th;
static boolean thread_run = true;





	@Override
	public void onCreate(Bundle savedInstanceState) {
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
        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		//インスタンスを保持させる
		Global.mainActivity = this;

		TextureManager.deleteAll(Global.gl);		//テクスチャを削除する
		TouchManagement.list.clear();
		mRenderer = new GameMain(Global.mainActivity,TeisuuTeigi.stage_area );
		TitleActivity.glSurfaceView = new MyGLSurfaceView(Global.mainActivity);// MyGLSurfaceViewの生成
		TitleActivity.glSurfaceView.setRenderer(mRenderer);
		setContentView(TitleActivity.glSurfaceView);
		Log.d("onCreate","onCreate"+TeisuuTeigi.stage_area);
	}

	public void onResume() {		//起動時呼ばれる
		if(Sound.music.mp != null){
			Sound.music.BGM_Play();		//BGMを一時停止する
		}
		MainActivity.thread_run = true;
		TitleActivity.glSurfaceView.onResume();
		super.onResume();


	}

	@Override
	public void onPause() {		//ホームボタンをおした時やバックボタンなどをおした時呼ばれる
		TitleActivity.glSurfaceView.onPause();
		MainActivity.thread_run = false;
		Log.d("","th削除");
		th = null;
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

		if(GameMain.to_clearresult_flg == true || GameMain.to_defeatresult_flg == true){
			Log.d("ゲームクリア画面に戻る","clear");
			System.gc();
			finish();
		}

		if(GameInfo.back_win == 1){		//ステージバックアイコンをおした時
			Log.d("ステージセレクト画面に戻る","back");
			System.gc();
			finish();
		}
		//finish();
		super.onPause();
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

					finishDialog();		//終了確認ダイアログ表示
					return false;

					//return true;
					//break;
				default:
			}
		}
		//Log.d("テクスチャ消去","");
		TextureManager.deleteAll(Global.gl);		//テクスチャを削除する
		return super.dispatchKeyEvent(event);
	}


	//終了ダイアログ
	public void finishDialog(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("終了確認");
		alertDialogBuilder.setMessage("ゲームを終了しますか？");
		alertDialogBuilder.setPositiveButton("はい",
		         new DialogInterface.OnClickListener() {
		             public void onClick(DialogInterface dialog, int which) {
		            	 Log.d("テクスチャ消去","aaa");
		            	 TextureManager.deleteAll(Global.gl);		//テクスチャを削除する
		                 finish();
		             }
		         });
		 alertDialogBuilder.setNegativeButton("いいえ",
		         new DialogInterface.OnClickListener() {
		             public void onClick(DialogInterface dialog, int which) {
		            	 Log.d("キャンセル","bbb");
		             }
		         });
		 alertDialogBuilder.setCancelable(true);
		 AlertDialog alertDialog = alertDialogBuilder.create();
		 alertDialog.show();
	}
}

