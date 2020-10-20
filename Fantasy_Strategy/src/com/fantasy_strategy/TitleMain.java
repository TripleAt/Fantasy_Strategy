package com.fantasy_strategy;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class TitleMain implements GLSurfaceView.Renderer {


	// コンテキスト
	private Context mContext;

	private int mWidth;
	private int mHeight;
	private int mWidthOffset;
	private int mHeightOffset;

	// テクスチャ
	private int back_Texture;
	private int smpIkon_Texture;			//サンプルアイコン
	private int gamastartikon_Texture;		//ゲームスタート用アイコン
	private int title_Texture;				//タイトル

	private int load;


	private Title[] tTitle = new Title[10];
	private float rect[] = new float[4];


	float aspect;							// 画面比（アスペクト比）を計算
	float eyex;
	float eyez;
	int   angle; 							//回転角度

	int Color_flg = 0;						//点滅を制御するフラグ

	public TitleMain(Context context) {
		this.mContext = context;
		TouchManagement.list.clear();		//衝突判定の情報を格納した配列をリストを初期化する
		startTilte();
	}



	//---------------------------------------------------------------------
	//		スタート時の初期化
	//---------------------------------------------------------------------
	public void startTilte() {
		load = 0;
		//	タイトル画面の生成
		tTitle[0] = new Title();		//背景の画像
		tTitle[1] = new Title();		//タイトルの画像
		tTitle[2] = new Title(240.0f, 525.0f, (float)179 / 2);		//ゲームスタートアイコンの画像　　　衝突処理関連の情報も持たせる

		//BGMのロード
		Sound.music.BGM_Load(mContext, R.raw.title);
		Sound.music.BGM_Play();
	}


	//---------------------------------------------------------------------
	//	常時実行する処理
	//---------------------------------------------------------------------
	@Override
	public void onDrawFrame(GL10 gl) {

		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glViewport(mWidthOffset, mHeightOffset, mWidth, mHeight);
		gl.glLoadIdentity();
		gl.glOrthof(-0.9f, 0.9f, -1.6f, 1.6f, 0.5f, -0.5f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);			// とりあえず、設定
		gl.glLoadIdentity();

		switch(load){
				case 0:
					//テクスチャをロードする
					loadTextures(gl);
					loadSound();				//サウンドをロードする
					load++;
					break;

				case 1:
					render2D(gl);		//2D描画処理の設定
					break;
		}
	}


	//---------------------------------------------------------------------
	//	2D描画
	//---------------------------------------------------------------------
	private void render2D(GL10 gl){

		// 2D描画用に座標系を設定します
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glOrthof(-0.9f, 0.9f, -1.6f, 1.6f, 1.0f, -1.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();


		//テクスチャの背景の透過を有効になるようにします
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);


		//タイトル画面表示--------------------------------------------------
		//背景描画
		rect[0] = 0.0f; rect[1] = 0.0f; rect[2] = 480.0f; rect[3] = 800.0f;
		tTitle[0].draw(gl, 240, 400, back_Texture, 512.0f, 1024.0f, rect);

		//タイトル描画(影)
		rect[0] = 0.0f; rect[1] = 0.0f; rect[2] = 574.0f; rect[3] = 435.0f;
		tTitle[1].draw_Color_Custom(gl, 248, 188, title_Texture, 1024.0f, 512.0f,rect, 0.0f, 0.0f, 0.0f, 1.0f);
		//タイトル描画(影重ねがけ)
		tTitle[1].draw_Color_Custom(gl, 250, 188, title_Texture, 1024.0f, 512.0f,rect, 0.0f, 0.0f, 0.0f, 1.0f);

		//タイトル描画
		rect[0] = 0.0f; rect[1] = 0.0f; rect[2] = 574.0f; rect[3] = 435.0f;
		tTitle[1].draw(gl, 240, 180, title_Texture, 1024.0f, 512.0f, rect);

		if(Color.a_icon > 1.2f || Color.a_icon < 0.4f){ Color_flg = 1 - Color_flg;}
		if(Color_flg == 1){Color.a_icon += 0.01f;}
		if(Color_flg == 0){Color.a_icon -= 0.01f;}


		//ゲームスタートアイコン描画(点滅させてみる)
		//今は点滅させてない
		rect[0] = 0.0f; rect[1] = 0.0f; rect[2] = 179.0f; rect[3] = 179.0f;
		tTitle[2].draw_Color_Custom(gl, 240, 525, gamastartikon_Texture, 256.0f, 256.0f, rect, 1.0f, 1.0f, 1.0f, Color.a_icon/*1.0f*/);

		//ゲームスタートアイコンをクリック時
		for(int i=0;i<3;i++)
		{
			if(tTitle[i].touch_flg)
			{
				if(tTitle[i] == null){
					break;
				}

				if(Sound.music.sp != null){
					Sound.music.SE_play(0);	//決定音SEの再生
				}
				switch(i){
				case 2:
						Global.titleActivity.setActivity(Screen.GAME_SELECT);	//ゲームメイン画面に遷移する
						break;

				}
			}
		}
		if(tTitle[2].touch_flg){
			if(Sound.music.sp != null){
				Sound.music.SE_play(0);	//決定音SEの再生
			}
		}

		//テクスチャの背景の透過を無効になるようにします
		gl.glDisable(GL10.GL_BLEND);


	}





	//---------------------------------------------------------------------------------
	//			描画範囲を設定
	//---------------------------------------------------------------------------------
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		//常に9:16で描画する
		int w = 0;
		int h = 0;
		while (w < width && h < height) {
			w += 9;
			h += 16;
		}
		this.mWidth = w;
		this.mHeight = h;

		//幅の設定
		this.mWidthOffset = (width - w)/2;
		this.mHeightOffset = (height - h)/2;

		aspect = (float)width / (float)height;

		touched_2D.window_width  = this.mWidth;
		touched_2D.window_height = this.mHeight;
		touched_2D.sukima_X = width -  touched_2D.window_width;
		touched_2D.sukima_Y = height - touched_2D.window_height;
		Global.gl = gl;		//GLコンテキストを保持する

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

	}

	//--------------------------------------------------------------------------------
	//		テクスチャロード
	//--------------------------------------------------------------------------------
	private void loadTextures(GL10 gl) {
		Resources res = mContext.getResources();

		//this.mNumberTexture        = GraphicUtil.loadTexture(gl, res, R.drawable.number_texture);				//数字の書かれたFPS表示してる画像の読み込み
		//this.kinoko_Texture        = GraphicUtil.loadTexture(gl, res, R.drawable.kinoko);						//きのこの画像の読み込み
		this.back_Texture          = GraphicUtil.loadTexture(gl, res, R.drawable.title_back);					//背景の画像の読み込み
		this.title_Texture         = GraphicUtil.loadTexture(gl, res, R.drawable.title_title);					//タイトル画像の読み込み
		this.gamastartikon_Texture = GraphicUtil.loadTexture(gl, res, R.drawable.title_gamestart_icon);			//ゲームスタートアイコン画像の読み込み
		this.smpIkon_Texture       = GraphicUtil.loadTexture(gl, res, R.drawable.icon);							//アニメーション用の画像の読み込み


		//if (mNumberTexture == 0)
		//	Log.e(getClass().toString(), "load texture error! number_texture");
		//if (kinoko_Texture == 0)
		//	Log.e(getClass().toString(), "load texture error! kinoko_Texture");
		if (back_Texture == 0)
			Log.e(getClass().toString(), "load texture error! back_Texture");
		if (back_Texture == 0)
			Log.e(getClass().toString(), "load texture error! back_Texture");
		if (smpIkon_Texture == 0)
			Log.e(getClass().toString(), "load texture error! smpIkon_Texture");
		if (gamastartikon_Texture == 0)
			Log.e(getClass().toString(), "load texture error! gamastartikon_Texture");
		if (title_Texture == 0)
			Log.e(getClass().toString(), "load texture error! title_Texture");

	}

	public void loadSound() {
		Sound.music.Sound_Create(mContext);			//サウンドの生成
		//サウンドのロード
		Sound.music.SE_Sound_load(mContext, 0, R.raw.decision);

	}


}
