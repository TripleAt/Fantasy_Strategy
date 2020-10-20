package com.fantasy_strategy;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {


	private MyRenderer mRenderer;


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

		//インスタンスを保持させる
		Global.mainActivity = this;

		this.mRenderer = new MyRenderer(this);
		MyGLSurfaceView glSurfaceView = new MyGLSurfaceView(this);// MyGLSurfaceViewの生成
		glSurfaceView.setRenderer(mRenderer);

		setContentView(glSurfaceView);


	}


	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		/*//とりあえず注釈。今後、バックボタンを押した際、消したくない場合使用。
		if (event.getAction() == KeyEvent.ACTION_DOWN) {// キーが押された
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK: //Backボタン
				return true;
			default:
			}
		}*/
		return super.dispatchKeyEvent(event);
	}

}