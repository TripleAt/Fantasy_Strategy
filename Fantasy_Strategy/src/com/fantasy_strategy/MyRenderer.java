package com.fantasy_strategy;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

class Camera_var{
	public static float x = 0;
	public static float y = 0;
	public static float x_move = 0;
	public static float y_move = 0;
	public static int angle = 200;
}


public class MyRenderer implements GLSurfaceView.Renderer {


	public static final int MAP_TIP_MAX_X = 20;
	public static final int MAP_TIP_MAX_Y = 20;



	// コンテキスト
	private Context mContext;

	private int mWidth;
	private int mHeight;
	private int mWidthOffset;
	private int mHeightOffset;

	// テクスチャ
	private int map_Texture;	//マップチップの生成。
	private int mNumberTexture;


	private Map_tip[][] mMap_tip = new Map_tip[MAP_TIP_MAX_X][MAP_TIP_MAX_Y];

	float aspect;	// 画面比（アスペクト比）を計算
	float eyex;
	float eyez;
	int   angle; //回転角度
	//private Handler mHandler = new Handler();//ハンドラー

	public MyRenderer(Context context) {
		this.mContext = context;
		startNewGame();
	}

	public void startNewGame() {

		//-------------------------------------------------------------------------
		//				マップ生成
		//-------------------------------------------------------------------------
		for(int i = 0; i < MAP_TIP_MAX_X;i++)
		{
			for(int j = 0; j < MAP_TIP_MAX_Y;j++)
			{
					float x = 0.25f * (float)i -0.0f;
					float y = 0.25f * (float)j -0.0f;

					mMap_tip[i][j] = new Map_tip(x,y,2);
			}

		}
	}

	private long mFpsCountStartTime = System.currentTimeMillis();
	private int mFramesInSecond = 0;
	private int mFps = 0;


	//描画を行う部分を記述するメソッドを追加する
	public void render3D(GL10 gl) {

		//float size = 0.1f / (float) Math.tan(Math.toRadians(150.0f / 2.0));

		// 3D描画用に座標系を設定します
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl,50.0f, aspect, 0.1f, 100.0f);

		float cameraX = Camera_var.x + Camera_var.x_move;
		float cameraY = Camera_var.y + Camera_var.y_move;
		/*
		*/
		//glViewportに合わせる
		GLU.gluLookAt(gl, (float)(1.0f * Math.sin(Math.PI / 180 * Camera_var.angle)) + cameraX,
				(float)(1.0f * Math.cos(Math.PI / 180 * Camera_var.angle)) + 4.75f + cameraY, 2.0f,
				-0.0f + cameraX, 4.75f + cameraY, 0.0f,
				-0.0f, 0.0f, 1.0f);

		//TODO 標準カメラ視点
		/*GLU.gluLookAt(gl, 1.0f, 3.75f, 2.0f,
				-0.0f, 4.75f, 0.0f,
				-0.0f, 0.0f, 1.0f);*/
		//gl.glFrustumf(-0.9f, 0.9f, -1.6f, 1.6f,  0.1f,100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		// ここから深度テストが有効になるようにします
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_POLYGON_SMOOTH_HINT);



//	2/25
//https://sites.google.com/a/gclue.jp/android-docs-2009/openglno-kiso


		Map_tip[][] tip = mMap_tip;
		for(int i = 0; i < MAP_TIP_MAX_X;i++)
		{
			for(int j = 0; j < MAP_TIP_MAX_Y;j++)
			{
				tip[i][j].draw(gl, map_Texture);
				tip[i][j].move();
			}

		}

//s		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// カメラ位置を設定



		gl.glDisable(GL10.GL_BLEND);

	}


	private void render2D(GL10 gl){
		// 2D描画用に座標系を設定します
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(-0.9f, 0.9f, -1.6f, 1.6f, 1.0f, -1.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		/*gl.glTranslatef(0.0f, -0.0f, -0.0f);			// 視点を少し上にするために、逆に全体を下方向に移動します(作業用)
		gl.glRotatef(-45.0f, 0.5f, 0.0f, 2.0f);		// X軸を中心に反時計回りに全体を80°回転させます

		Map_tip[][] tip = mMap_tip;
		for(int i = 0; i < MAP_TIP_MAX_X;i++)
		{
			for(int j = 0; j < MAP_TIP_MAX_Y;j++)
			{
				tip[i][j].draw(gl, map_Texture);	//
				tip[i][j].move();
			}

		}


		gl.glOrthof(-0.9f, 0.9f, -1.6f, 1.6f, 0.5f, -0.5f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();*/
		// FPSを表示する
		if (Global.isDebuggable) {
			long nowTime = System.currentTimeMillis();// 現在時間を取得
			long difference = nowTime - mFpsCountStartTime;// 現在時間との差分を計算
			if (difference >= 1000) {// 1秒経過していた場合は、フレーム数のカウント終了
				mFps = mFramesInSecond;
				mFramesInSecond = 0;
				mFpsCountStartTime = nowTime;
			}
			mFramesInSecond++;// フレーム数をカウント
			GraphicUtil.drawNumbers(gl, -0.5f, -0.75f, 0.2f, 0.2f, mNumberTexture, mFps, 2, 1.0f, 1.0f, 1.0f, 1.0f);
		}
	}


	void resize(GL10 gl, float w, float h)
	{
		//float size = 0.01f / (float) Math.tan(45.0f / 2.0);
		double scale = 0.01f * Math.tan(Math.toRadians(45.0f* 0.5));
		float x = (float) (w * scale * aspect / mWidth);
		float y = (float) (h * scale / mHeight);
		gl.glFrustumf(-x, x, -y, y, 0.01f, 100.0f);
	}



	@Override
	public void onDrawFrame(GL10 gl) {



		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glViewport(mWidthOffset, mHeightOffset, mWidth, mHeight);
		gl.glLoadIdentity();
		gl.glOrthof(-0.9f, 0.9f, -1.6f, 1.6f, 0.5f, -0.5f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);			//TODO とりあえず、設定
		gl.glLoadIdentity();

		render3D(gl);		//3D描画処理の設定
		render2D(gl);		//2D描画処理の設定
	}

	//テクスチャを読み込むメソッド
	private void loadTextures(GL10 gl) {
		Resources res = mContext.getResources();
		this.map_Texture = GraphicUtil.loadTexture(gl, res, R.drawable.map_tip);
		if (map_Texture == 0) {
			Log.e(getClass().toString(), "load texture error! map_tip");
		}
		this.mNumberTexture = GraphicUtil.loadTexture(gl, res, R.drawable.number_texture);
		if (mNumberTexture == 0) {
			Log.e(getClass().toString(), "load texture error! number_texture");
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		//常に3:2で描画する
		int w = 0;
		int h = 0;
		while (w < width && h < height) {
			w += 9;
			h += 16;
		}
		this.mWidth = w;
		this.mHeight = h;
		this.mWidthOffset = (width - w)/2;
		this.mHeightOffset = (height - h)/2;

		aspect = (float)width / (float)height;


		Global.gl = gl;//GLコンテキストを保持する

		//テクスチャをロードする
		loadTextures(gl);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

	}


}
