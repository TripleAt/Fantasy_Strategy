package com.fantasy_strategy;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;


// OpenGLのViewクラス
public class Stage_SelectMain extends GLSurfaceView implements Renderer {

	// コンテキスト
	private Context mContext;

	private int mWidth;
	private int mHeight;
	private int mWidthOffset;
	private int mHeightOffset;

	float aspect;							// 画面比（アスペクト比）を計算

	// テクスチャ
	private int button_back;				//バックのボタン
	private int stage_select_back;			//背景
	private int sougen_button;				//草原のボタン
	private int danjon_button;				//ダンジョンのボタン
	private int rokujyou_button;			//六畳一間のボタン
	private int question_button;			//???のボタン
	private int spell;						//文字



	private float[] rect = new float[6];
	private float[][] spell_rect = {
			{  0,  0,  59, 32,  59 / 2, 32 / 2},
			{ 59,  0, 117, 33, 117 / 2, 33 / 2},
			{178,  0,  80, 32,  80 / 2, 32 / 2},
			{  0, 35, 119, 28, 119 / 2, 28 / 2},
	};


	FontTexture text_fTexture = new FontTexture();
	int strx, stry, numx;	//文字描画の座標
	int  numbers, number;
	int figures;	//数字の桁
	float str_param[][] = new float[4][FontTexture.text_length];	//文字のパラメーター	0:width, 1:height, 2:offset, 3:1行のheight
	float num_str_param[][] = new float[4][FontTexture.txtnum_length];



	private Stage_button[] button = new Stage_button[9];



	 public Stage_SelectMain(Context context) {
		super(context);
		this.mContext = context;
		TouchManagement.list.clear();		//衝突判定の情報を格納した配列をリストを初期化する
		startTilte();
	}



	//---------------------------------------------------------------------
	//		スタート時の初期化
	//---------------------------------------------------------------------
	public void startTilte() {
		int i = 0;

		button[i] = new Stage_button(400, 750, 64, 32);					//バックボタン	//0
		i++;

		button[i] = new Stage_button(190, 210, 64, 64);					//草原		左上//1
		i++;
		button[i] = new Stage_button(190, 350, 64, 64);					//六畳一間		左下//2
		i++;
		button[i] = new Stage_button(190, 490, 64, 64);					//？？？	//3		右上
		i++;
		button[i] = new Stage_button(190, 630, 64, 64);					//ダンジョン		右下//4

		i++;
		button[i] = new Stage_button(380, 210, 64, 64);					//草原2		右//5
		i++;
		button[i] = new Stage_button(380, 350, 64, 64);					//六畳一間2		右//6
		i++;
		button[i] = new Stage_button(380, 490, 64, 64);					//？？？		右//7
		i++;
		button[i] = new Stage_button(380, 630, 64, 64);					//ダンジョン		右下//8

		//BGMのロード
		Sound.music.BGM_Load(mContext, R.raw.stage_select);
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

		   render2D(gl);			//2D
		   button_exec();
	  }


	//---------------------------------------------------------------------
	//		2D描画
	//---------------------------------------------------------------------
	public void render2D(GL10 gl) {
		int i = 0;

		// 2D描画用に座標系を設定します
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glOrthof(-0.9f, 0.9f, -1.6f, 1.6f, 1.0f, -1.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();


		//テクスチャの背景の透過を有効になるようにします
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		//ウィンドウの描画
		gl.glPushMatrix();
		{
			GraphicUtil.glTranslatef_pixel(gl, 240.0f, 400.0f, -0.0f);		//座標位置
			gl.glScalef(1.0f, 1.0f, 1.0f);	//画像の大きさ指定

			GraphicUtil.drawTexture_pixel_custom(gl, 512.0f, 1024.0f, stage_select_back, new float[]{0.0f, 0.0f, 480.0f, 800.0f, 240.0f, 400.0f},
											1.0f,1.0f, 1.0f, 1.0f);

		}
		gl.glPopMatrix();

		i++;

		//草原		右
		rect[0] = 0.0f; rect[1] = 0.0f; rect[2] = 128.0f; rect[3] = 128.0f; rect[4] = 128.0f / 2; rect[5] = 128.0f / 2;
		button[i].draw_shadow(gl, 190, 210, sougen_button, 128, 128, rect);
		button[i].draw(gl, 190, 210, sougen_button, 128, 128, rect);
		i++;

		//六畳一間
		button[i].draw_shadow(gl, 190, 350, rokujyou_button, 128, 128, rect);
		button[i].draw(gl, 190, 350, rokujyou_button, 128, 128, rect);
		i++;

		//???
		button[i].draw_shadow(gl, 190, 490, question_button, 128, 128, rect);
		button[i].draw(gl, 190, 490, question_button, 128, 128, rect);
		i++;

		//ダンジョン
		button[i].draw_shadow(gl, 190, 630, danjon_button, 128, 128, rect);
		button[i].draw(gl, 190, 630, danjon_button, 128, 128, rect);
		i++;




		//草原2		右
		button[i].draw_shadow(gl, 380, 210, sougen_button, 128, 128, rect);
		button[i].draw(gl, 380, 210, sougen_button, 128, 128, rect);
		i++;

		//六畳一間2
		button[i].draw_shadow(gl, 380, 350, rokujyou_button, 128, 128, rect);
		button[i].draw(gl, 380, 350, rokujyou_button, 128, 128, rect);
		i++;

		//???2
		button[i].draw_shadow(gl, 380, 490, question_button, 128, 128, rect);
		button[i].draw(gl, 380, 490, question_button, 128, 128, rect);
		i++;

		//ダンジョン2
		button[i].draw_shadow(gl, 380, 630, danjon_button, 128, 128, rect);
		button[i].draw(gl, 380, 630, danjon_button, 128, 128, rect);
		i++;


		//バックボタン
		rect[0] = 0.0f; rect[1] = 0.0f; rect[2] = 128.0f; rect[3] = 64.0f; rect[4] = 128.0f / 2; rect[5] = 64.0f / 2;
		button[0].draw_shadow(gl, 400, 750, sougen_button, 128, 64, rect);
		button[0].draw(gl, 400, 750, button_back, 128, 64, rect);

		for(i = 0; i < 4; i++)
		{
			gl.glPushMatrix();		//文字の表示
			{
				GraphicUtil.glTranslatef_pixel(gl, 60.0f, 210.0f+(i * 140), -0.0f);		//座標位置
				gl.glScalef(1.0f, 1.0f, 1.0f);	//画像の大きさ指定
				GraphicUtil.drawTexture_pixel_custom(gl, 512, 128, spell, spell_rect[i],
												1.0f,1.0f, 1.0f, 1.0f);
			}
			gl.glPopMatrix();
		}


		//テクスチャの背景の透過を無効になるようにします
		gl.glDisable(GL10.GL_BLEND);
	}



	//---------------------------------------------------------------------
	//		ボタンのタッチチェック
	//---------------------------------------------------------------------
	private void button_exec()
	{

		for(int i = 0; i < 9; i++)
		{
			if( TouchManagement.touch_chk(i) ){
				if(Sound.music.sp != null){
					Sound.music.SE_play(0);	//決定音SEの再生
				}
				switch(i)
				{
					case 0:			//バック
						Global.titleActivity.setActivity( Screen.GAME_TITLE );
						break;

					case 1:			//左0草原
						TeisuuTeigi.stage_area  = 0;
						Global.titleActivity.setActivity( Screen.GAME_MAIN );
						break;

					case 2:			//左1六畳一間
						TeisuuTeigi.stage_area  = 1;
						Global.titleActivity.setActivity( Screen.GAME_MAIN );
						break;

					case 3:			//左2？？？
						TeisuuTeigi.stage_area  = 3;
						Global.titleActivity.setActivity( Screen.GAME_MAIN);
						break;

					case 4:			//左3ダンジョン
						TeisuuTeigi.stage_area  = 2;
						Global.titleActivity.setActivity( Screen.GAME_MAIN );
						break;


					case 5:			//右0
						TeisuuTeigi.stage_area  = 4;
						Global.titleActivity.setActivity( Screen.GAME_MAIN );
						break;

					case 6:			//右1
						TeisuuTeigi.stage_area  = 5;
						Global.titleActivity.setActivity( Screen.GAME_MAIN );
						break;

					case 7:			//右2
						TeisuuTeigi.stage_area  = 7;
						Global.titleActivity.setActivity( Screen.GAME_MAIN );
						break;

					case 8:			//右3
						TeisuuTeigi.stage_area  = 6;
						Global.titleActivity.setActivity( Screen.GAME_MAIN );
						break;
				}
				GameInfo.stage_number = TeisuuTeigi.stage_area; 	//ステージ名の判断
				Log.d("stage",""+i);
			}
		}

	}




	  @Override
	  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		  loadTextures(gl);
		   loadSound();				//サウンドをロードする
	  }







	  @Override
	  public void onSurfaceChanged(GL10 gl, int width, int height) {

		  Log.d("ううううううううううううううう","ResultMain onSurfaceChanged  exe_stop_flg"+GameMain.exe_stop_flg);
		  	GameMain.exe_stop_flg = true;
		    GameMain.win_flg = false;
		    GameMain.defeat_flg = false;
		    GameMain.to_clearresult_flg = false;
		    GameMain.to_result_flg_rock = false;
		    GameMain.to_defeatresult_flg = false;
		    GameMain.start_state = 0;
		    GameMain.start_count = 0;


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






	//--------------------------------------------------------------------------------
	//		テクスチャロード
	//--------------------------------------------------------------------------------
	private void loadTextures(GL10 gl) {

		Resources res = mContext.getResources();

		this.stage_select_back = GraphicUtil.loadTexture(gl, res, R.drawable.stage_sele_back);
		this.sougen_button = GraphicUtil.loadTexture(gl, res, R.drawable.stage_sougen);
		this.danjon_button = GraphicUtil.loadTexture(gl, res, R.drawable.stage_danjon);
		this.rokujyou_button = GraphicUtil.loadTexture(gl, res, R.drawable.stage_rokujyou);
		this.question_button = GraphicUtil.loadTexture(gl, res, R.drawable.stage_question);
		this.spell = GraphicUtil.loadTexture(gl, res, R.drawable.stage_spell);
		this.button_back = GraphicUtil.loadTexture(gl, res, R.drawable.stage_back_button);

		if (button_back == 0) {
			Log.e(getClass().toString(), "load texture error! stage_back_button");
		}
		if (sougen_button == 0) {
			Log.e(getClass().toString(), "load texture error! stage_sougen");
		}
		if (stage_select_back == 0) {
			Log.e(getClass().toString(), "load texture error! stage_sele_back");
		}
		if (spell == 0) {
			Log.e(getClass().toString(), "load texture error! stage_spell");
		}

	}

	public void loadSound() {
		Sound.music.Sound_Create(mContext);			//サウンドの生成
		//サウンドのロード
		Sound.music.SE_Sound_load(mContext, 0, R.raw.decision);

	}
}

