package com.fantasy_strategy;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;


// OpenGLのViewクラス
public class ResultMain extends GLSurfaceView implements Renderer {

	// コンテキスト
	private Context mContext;

	private int mWidth;
	public static int mHeight;
	private int mWidthOffset;
	private int mHeightOffset;

	float aspect;							// 画面比（アスペクト比）を計算

	//GameInfo用
	private GameInfo mGameInfo = new GameInfo();

	// テクスチャ
	private int back_Texture;
	private int str_Texture;			//文字のテクスチャ
	private int strnum_Texture;			//数字のテクスチャ
	private int[]enemys_Texture = new int[Enemy_types.ENEMY_TOTAL];				//敵画像


	private int rank_s_Texture;
	private int rank_a_Texture;
	private int rank_b_Texture;
	private int rank_c_Texture;
	private int rank_d_Texture;
	private int exit_Texture;
	private int next_Texture;
	private int stageclear_Texture;
	private int stagefailed_Texture;
	private int totalscore_Texture;

	//3D画像用座標
	private float x = 140.0f;
	private float y;

	//敵情報
	private Result[] tResult = new Result[10];
	private float rect[] = new float[6];
	private float rect_enemy[][][] = {
										{
											{  0.0f,  0.0f, 64.0f, 64.0f, 32.0f, 32.0f},		//ひずみ
											{ 64.0f,  0.0f, 64.0f, 64.0f, 32.0f, 32.0f},
											{  0.0f, 64.0f, 64.0f, 64.0f, 32.0f, 32.0f},
											{ 64.0f, 64.0f, 64.0f, 64.0f, 32.0f, 32.0f}
										},
										{
											{  0.0f,64.0f,32.0f,32.0f, 16.0f, 16.0f},		//左下　スライム
											{ 32.0f,64.0f,32.0f,32.0f, 16.0f, 16.0f}
										},
										{
											{  0.0f, 0.0f, 64.0f, 64.0f, 32.0f, 50.0f},		//左下　きのこ
											{ 64.0f, 0.0f, 64.0f, 64.0f, 32.0f, 50.0f},
											{128.0f, 0.0f, 64.0f, 64.0f, 32.0f, 50.0f},
											{ 64.0f, 0.0f, 64.0f, 64.0f, 32.0f, 50.0f},
										},
										{
											{  0.0f, 0.0f, 64.0f, 64.0f, 32.0f, 40.0f},		//左下　にわとり
											{ 64.0f, 0.0f, 64.0f, 64.0f, 32.0f, 40.0f},
											{128.0f, 0.0f, 64.0f, 64.0f, 32.0f, 40.0f},
											{ 64.0f, 0.0f, 64.0f, 64.0f, 32.0f, 40.0f},
										},
										{
											{  0.0f, 128.0f, 128.0f, 128.0f, 64.0f, 110.0f},	//左下　ドラゴン
											{  0.0f, 128.0f, 128.0f, 128.0f, 64.0f, 110.0f},
											{128.0f, 128.0f, 128.0f, 128.0f, 64.0f, 110.0f},
											{128.0f, 128.0f, 128.0f, 128.0f, 64.0f, 110.0f},
										},
										{
											{  0.0f, 0.0f, 64.0f, 64.0f, 32.0f, 45.0f},		//左下
											{ 64.0f, 0.0f, 64.0f, 64.0f, 32.0f, 45.0f},
											{128.0f, 0.0f, 64.0f, 64.0f, 32.0f, 45.0f},
										},
										{
											{  0.0f,64.0f,32.0f,32.0f, 16.0f, 16.0f},		//左下　スライム
											{ 32.0f,64.0f,32.0f,32.0f, 16.0f, 16.0f}
										},

									};

	private float[][]tex_sizes ={												//テクスチャサイズ x y
									{ 128.0f, 128.0f },							//ひずみ
									{ 64.0f, 128.0f },							//スライム
									{ 256.0f, 512.0f },							//きのこ
									{ 256.0f, 512.0f },							//にわとり
									{ 512.0f, 1024.0f },						//ドラゴン
									{ 256.0f, 512.0f },							//ゴブリン
									{ 64.0f, 128.0f },							//スライム2
								};

	//フレーム関係
	private int work;
	private int frame;

	//スコア関係
	int mons_kill_1, mons_kill_10;
	int chara_score[] = new int [Enemy_types.ENEMY_TOTAL];
	private int clear_bonus, time_bonus, stage_total;
	private int[] each_clear_bonus = { 2000, 3000, 5000, 5000, 2000, 3000, 5000, 5000} ;	//各クリアボーナス
	private int[] rankS = { 15000, 15000, 20000, 20000, 18000, 75000, 30000, 30000 };			//ステージごとのrankSの基準点



	int strx, stry, numx;	//文字描画の座標
	int  numbers, number;
	int figures;	//数字の桁
	float str_param[][] = new float[4][FontTexture.text_length];	//文字のパラメーター	0:width, 1:height, 2:offset, 3:1行のheight
	float num_str_param[][] = new float[4][FontTexture.txtnum_length];



	// カメラの位置
	private float cameraX = 0;
	private static float cameraY = 0;

	//カメラの移動量
	private static float cameraDist;

	private int[] textures = new int[1];
	private int textureId = 0;


	public static boolean gameclear_flg;		//GameClearか？ GameOverか？


	static void setCameraPotision(float x, float y) {

		int higth_len = 0;
		for(int i = 0; GameMain.die_enemy_num[i][0] > 0; i++){
			higth_len++;
		}
		if(!gameclear_flg){
			higth_len -= 2;
			if(higth_len < 0){
				higth_len = 0;
			}
		}

		// カメラ位置変更
//		cameraX +=  x/cameraDist;(横移動無し)
		cameraY += -y / cameraDist;


		//画像が画面の外に出ない様にブレーキ　(ここでスクロール範囲を指定する)
//		if (cameraX> 1)cameraX  = 1;
//		if (cameraX<-1)cameraX = -1;
//		if (cameraY> 0)cameraY  = 0;
//		if (cameraY< -(64.0f / cameraDist * 2.0f)*5.0f - (64.0f / cameraDist * 5.5f * higth_len)){
//			cameraY =  -(64.0f / cameraDist  * 2.0f)*5.0f - (64.0f / cameraDist  * 5.5f * higth_len);
//		}
		if (cameraY> 0)cameraY  = 0;
		if (cameraY< 0 - (higth_len * (444 / cameraDist)) ){
			cameraY = 0 - (higth_len * (444 / cameraDist));
		}
		//
		Log.d("",""+cameraY);
		Log.d("",""+cameraDist);

	}

	 public ResultMain(Context context) {
		super(context);
		this.mContext = context;
		TouchManagement.list.clear();		//衝突判定の情報を格納した配列をリストを初期化する
		startTilte();
	}



	//---------------------------------------------------------------------
	//		スタート時の初期化
	//---------------------------------------------------------------------
	public void startTilte() {

		for(int i=0;i < Enemy_types.ENEMY_TOTAL;i++){
			chara_score[i] = 0;
		}

		//	タイトル画面の生成
		tResult[0] = new Result(110, 770, (float)128.0f / 2);		//ボタン  左
//		tResult[1] = new Result(240, 770, (float)128.0f / 2);		//ボタン  中
		tResult[1] = new Result(370, 770, (float)128.0f / 2);		//ボタン  右
		tResult[3] = new Result(320.0f, 650.0f, (float)179 / 2);		//ショップアイコンの画像　　　衝突処理関連の情報も持たせる

		tResult[9] = new Result();		//サンプルアイコンの画像


		//BGMのロード
		if(gameclear_flg){
			Sound.music.BGM_Load(mContext, R.raw.gameclear);
			Sound.music.BGM_Play();
		}else{
			Sound.music.BGM_Load(mContext, R.raw.gameover);
			Sound.music.BGM_Play();
		}

		int cleartime;
		if(gameclear_flg){
			clear_bonus = each_clear_bonus[TeisuuTeigi.stage_area];

			cleartime = GameInfo.t_minute;
			if(cleartime < 2){				//2分以内にクリア
				time_bonus = 5000;
			}else if(cleartime < 5){		//5分以内にクリア
				time_bonus = 3000;
			}else if(cleartime < 20){		//20分以内にクリア
				time_bonus = 1000;
			}else{
				time_bonus = 100;
			}
		}else{
			clear_bonus = 0;
			cleartime = 0;
		}

		for(int killi=0; killi < Enemy_types.ENEMY_TOTAL; killi++){
			//倒した敵キャラのスコア格納
			if(GameMain.die_enemy_num[killi][0] > 0){
				chara_score[killi] = GameMain.die_enemy_num[killi][0] * GameMain.die_enemy_num[killi][1];
			}
		}

		//集計
		TitleActivity.total_score += clear_bonus + time_bonus;
		stage_total = clear_bonus + time_bonus;
		for(int i = 0; i < Enemy_types.ENEMY_TOTAL; i++){
			TitleActivity.total_score += chara_score[i];
			stage_total+= chara_score[i];
		}
		if(TitleActivity.total_score > 999990){
			TitleActivity.total_score = 999990;
		}
		TitleActivity.save.SaveFileOutput(TitleActivity.total_score);
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

			//フレーム制御
			frame++;
			//15Fごとに数値を変更する
			work = frame;
			work /= 3;
			work %= 12;

		   render3D(gl);			//3D
		   render2D(gl);			//2D
	  }

	//---------------------------------------------------------------------
	//		3D描画
	//---------------------------------------------------------------------
	public void render3D(GL10 gl) {

		// 3D描画用に座標系を設定します
		gl.glMatrixMode(GL10.GL_PROJECTION);
	   // カメラ
	   GLU.gluPerspective(gl, 50.0f, aspect, 0.1f, 100f);
	   GLU.gluLookAt(gl, cameraX, cameraY, 2.5f, cameraX, cameraY, 0, 0, 1, 0);


	   //背景を描画
	   for(int i = 0; i<5; i++){
		   gl.glPushMatrix();
			{
				GraphicUtil.glTranslatef_pixel(gl, 240.0f, (i*1100) + 400.0f, 0.0f);		//座標位置(ピクセル指定)
				gl.glRotatef(0.0f, 0.0f, 0.0f, 1.0f);	//回転
				GraphicUtil.drawTexture_pixel_custom(gl, 512.0f, 1524.0f, back_Texture, new float[]{0.0f,0.0f,512.0f,1524.0f,512.0f / 2,1024.0f / 2},
							1.0f,1.0f, 1.0f, 0.0f);
			}
			gl.glPopMatrix();
	   }

		//テクスチャの背景の透過を有効になるようにします
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		if(gameclear_flg){
			//Clear画面
			gl.glPushMatrix();
			{
				GraphicUtil.glTranslatef_pixel(gl,260.0f, 400.0f, 0.0f);		//座標位置
				gl.glScalef( 0.7f, 0.9f, 1.0f);									//画像の大きさ指定

				GraphicUtil.drawTexture_pixel_custom(gl, 512.0f, 1024.0f, stageclear_Texture, new float[]{0.0f,0.0f,512.0f,480.0f, 512.0f / 2,1024.0f / 2},
												1.0f,1.0f, 1.0f, 1.0f);
			}
			gl.glPopMatrix();
			y = 200.0f;

		}else{
			//GameOver画面
			gl.glPushMatrix();
			{
				GraphicUtil.glTranslatef_pixel(gl,260.0f, 400.0f, 0.0f);		//座標位置
				gl.glScalef( 0.7f, 0.9f, 1.0f);									//画像の大きさ指定

				GraphicUtil.drawTexture_pixel_custom(gl, 512.0f, 1024.0f, stagefailed_Texture, new float[]{0.0f,0.0f,512.0f,340.0f, 512.0f / 2,1024.0f / 2},
												1.0f,1.0f, 1.0f, 1.0f);
			}
			gl.glPopMatrix();
			y = 280.0f;
		}

		//ClearBonus
		if(gameclear_flg){
			FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,		//千
					320, (int)y,
					num_str_param[2][clear_bonus / 1000 % 10],
					1.0f, 1.0f, 1.0f, 1.0f);
			FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,		//百
					340, (int)y,
					num_str_param[2][clear_bonus / 100 % 10],
					1.0f, 1.0f, 1.0f, 1.0f);
			FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,		//十
					360, (int)y,
					num_str_param[2][clear_bonus / 10 % 10],
					1.0f, 1.0f, 1.0f, 1.0f);
			FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,		//一
					380, (int)y,
					num_str_param[2][0],
					1.0f, 1.0f, 1.0f, 1.0f);
			y += 70.0f;
		}

		//TimeBonus
		if(gameclear_flg){


			if(time_bonus >= 1000){
				FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
						320, (int)y,
						num_str_param[2][time_bonus / 1000 % 10],
						1.0f, 1.0f, 1.0f, 1.0f);
			}
			FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
					340, (int)y,
					num_str_param[2][time_bonus / 100 % 10],
					1.0f, 1.0f, 1.0f, 1.0f);
			FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
					360, (int)y,
					num_str_param[2][time_bonus / 10 % 10],
					1.0f, 1.0f, 1.0f, 1.0f);
			FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
					380, (int)y,
					num_str_param[2][0],
					1.0f, 1.0f, 1.0f, 1.0f);
			y += 160.0f;
		}

//		//てすと
//		int killi = 4;
//		gl.glPushMatrix();
//		{
//			GraphicUtil.glTranslatef_pixel(gl, x, y, 0.0f);		//座標位置
//			gl.glScalef(1.2f, 1.2f, 1.0f);									//画像の大きさ指定
//
//			GraphicUtil.drawTexture_pixel_custom(gl, tex_sizes[killi][0], tex_sizes[killi][1], enemys_Texture[killi], rect_enemy[killi][ work / (12/rect_enemy[killi].length) ],
//													1.0f,1.0f, 1.0f, 1.0f);
//		}
//		gl.glPopMatrix();

		//Monster Kills
		{
			for(int killi=0; killi < Enemy_types.ENEMY_TOTAL; killi++){

				//倒した敵キャラ画像を表示
				if(GameMain.die_enemy_num[killi][0] > 0){

					gl.glPushMatrix();
					{
						GraphicUtil.glTranslatef_pixel(gl, x, y, 0.0f);		//座標位置
						if(killi == 4){
							gl.glScalef(1.2f, 1.2f, 1.0f);									//画像の大きさ指定
						}else if(killi == 5){
							gl.glScalef(1.7f, 1.7f, 1.0f);									//画像の大きさ指定
						}
						else{
							gl.glScalef(1.8f, 1.8f, 1.0f);									//画像の大きさ指定
						}
						GraphicUtil.drawTexture_pixel_custom(gl, tex_sizes[killi][0], tex_sizes[killi][1], enemys_Texture[killi], rect_enemy[killi][ work / (12/rect_enemy[killi].length) ],
																1.0f,1.0f, 1.0f, 1.0f);
					}
					gl.glPopMatrix();

					//文字描画
					for(int i = 0; i < FontTexture.text_length; i++){
							FontTexture.text_fTexture.draw_str(gl, str_Texture, 1.0f, 170, (int)y, str_param[2][i],str_param[0][i],str_param[1][i]);
					}

					int die_list[] = new int[Enemy_types.ENEMY_TOTAL];
					for(int i = 0; i < Enemy_types.ENEMY_TOTAL; i++){
						die_list[i] = GameMain.die_enemy_num[killi][0];
					}

					//Monster Killsのカウント
					if(GameMain.die_enemy_num[killi][0] == 0){
						mons_kill_1 = 0;
						mons_kill_10 = 0;
					}else if(GameMain.die_enemy_num[killi][0] < 10){
						mons_kill_1 = GameMain.die_enemy_num[killi][0] % 10;
						mons_kill_10 = 0;
					}else if(GameMain.die_enemy_num[killi][0] > 99){			//99体以上はカウントしない
						mons_kill_1 = 9;
						mons_kill_10 = 9;
					}else{
						mons_kill_1 = GameMain.die_enemy_num[killi][0] % 10;
						mons_kill_10 = GameMain.die_enemy_num[killi][0]/ 10;
					}

					FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
							200, (int)y,
							num_str_param[2][mons_kill_10],
							1.0f, 1.0f, 1.0f, 1.0f);
					FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
							220, (int)y,
							num_str_param[2][mons_kill_1],
							1.0f, 1.0f, 1.0f, 1.0f);
					chara_score[killi] = GameMain.die_enemy_num[killi][0] * GameMain.die_enemy_num[killi][1];

					//合計スコア
					if(chara_score[killi] >= 10000){
						FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
								300, (int)y,
								num_str_param[2][chara_score[killi] / 10000 % 10],
								1.0f, 1.0f, 1.0f, 1.0f);
					}
					if(chara_score[killi] >= 1000){
						FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
								320, (int)y,
								num_str_param[2][chara_score[killi] / 1000 % 10],
								1.0f, 1.0f, 1.0f, 1.0f);
					}
					if(chara_score[killi] >= 100){
						FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
								340, (int)y,
								num_str_param[2][chara_score[killi] / 100 % 10],
								1.0f, 1.0f, 1.0f, 1.0f);
					}
					FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
							360, (int)y,
							num_str_param[2][chara_score[killi] / 10 % 10],
							1.0f, 1.0f, 1.0f, 1.0f);
					FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
							380, (int)y,
							num_str_param[2][0],
							1.0f, 1.0f, 1.0f, 1.0f);
					y += 85.0f;
				}
			}
		}


		//TotalScore
		{
			y += 70.0f;
			//totalscoreの表示
			gl.glPushMatrix();
			{
				GraphicUtil.glTranslatef_pixel(gl, 166.0f, y+5.0f, 0.0f);		//座標位置
				gl.glScalef( 0.7f, 0.9f, 1.0f);									//画像の大きさ指定

				GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 128.0f, totalscore_Texture, new float[]{0.0f,0.0f,256.0f,128.0f, 256.0f / 2, 128.0f / 2},
												1.0f,1.0f, 1.0f, 1.0f);
			}
			gl.glPopMatrix();



			//スコア
			if(stage_total > 100000){
				FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
						280, (int)y,
						num_str_param[2][stage_total / 100000 % 10],
						255.0f, 255.0f, 255.0f, 1.0f);
			}
			if(stage_total > 10000){
				FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
						300, (int)y,
						num_str_param[2][stage_total / 10000 % 10],
						1.0f, 1.0f, 1.0f, 1.0f);
			}
			if(stage_total > 1000){
				FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
						320, (int)y,
						num_str_param[2][stage_total / 1000 % 10],
						1.0f, 1.0f, 1.0f, 1.0f);
			}
			if(stage_total > 100){
				FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
						340, (int)y,
						num_str_param[2][stage_total / 100 % 10],
						1.0f, 1.0f, 1.0f, 1.0f);
			}
			FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
					360, (int)y,
					num_str_param[2][stage_total / 10 % 10],
					1.0f, 1.0f, 1.0f, 1.0f);
			FontTexture.text_fTexture.draw_num(gl, strnum_Texture, 1.0f,
					380, (int)y,
					num_str_param[2][0],
					1.0f, 1.0f, 1.0f, 1.0f);
			y += 100.0f;
		}


		//Rank
		{
			if(stage_total >= rankS[TeisuuTeigi.stage_area]){//kkk
				gl.glPushMatrix();
				{
					GraphicUtil.glTranslatef_pixel(gl, 330.0f, y, 0.0f);		//座標位置
					gl.glScalef( 1.0f + (work/2 * 0.005f), 1.0f + (work/2 * 0.005f), 1.0f);									//画像の大きさ指定

					GraphicUtil.drawTexture_pixel_custom(gl, 128.0f, 128.0f, rank_s_Texture, new float[]{0.0f,0.0f,128.0f,128.0f, 128.0f / 2, 128.0f / 2},
													1.0f,1.0f, 1.0f, 1.0f);
				}
				gl.glPopMatrix();
			}else if(stage_total >= rankS[TeisuuTeigi.stage_area] * 0.9){
				gl.glPushMatrix();
				{
					GraphicUtil.glTranslatef_pixel(gl, 330.0f, y, 0.0f);		//座標位置
					gl.glScalef( 1.0f + (work/2 * 0.005f), 1.0f + (work/2 * 0.005f), 1.0f);									//画像の大きさ指定

					GraphicUtil.drawTexture_pixel_custom(gl, 128.0f, 128.0f, rank_a_Texture, new float[]{0.0f,0.0f,128.0f,128.0f, 128.0f / 2, 128.0f / 2},
													1.0f,1.0f, 1.0f, 1.0f);
				}
				gl.glPopMatrix();
			}else if(stage_total >= rankS[TeisuuTeigi.stage_area] * 0.7){
				gl.glPushMatrix();
				{
					GraphicUtil.glTranslatef_pixel(gl, 330.0f, y, 0.0f);		//座標位置
					gl.glScalef( 1.0f + (work/2 * 0.005f), 1.0f + (work/2 * 0.005f), 1.0f);									//画像の大きさ指定

					GraphicUtil.drawTexture_pixel_custom(gl, 128.0f, 128.0f, rank_b_Texture, new float[]{0.0f,0.0f,128.0f,128.0f, 128.0f / 2, 128.0f / 2},
													1.0f,1.0f, 1.0f, 1.0f);
				}
				gl.glPopMatrix();
			}else if(stage_total >= rankS[TeisuuTeigi.stage_area] * 0.5){
				gl.glPushMatrix();
				{
					GraphicUtil.glTranslatef_pixel(gl, 330.0f, y, 0.0f);		//座標位置
					gl.glScalef( 1.0f + (work/2 * 0.005f), 1.0f + (work/2 * 0.005f), 1.0f);									//画像の大きさ指定

					GraphicUtil.drawTexture_pixel_custom(gl, 128.0f, 128.0f, rank_c_Texture, new float[]{0.0f,0.0f,128.0f,128.0f, 128.0f / 2, 128.0f / 2},
													1.0f,1.0f, 1.0f, 1.0f);
				}
				gl.glPopMatrix();
			}else{
				gl.glPushMatrix();
				{
					GraphicUtil.glTranslatef_pixel(gl, 330.0f, y, 0.0f);		//座標位置
					gl.glScalef( 1.0f + (work/2 * 0.005f), 1.0f + (work/2 * 0.005f), 1.0f);									//画像の大きさ指定

					GraphicUtil.drawTexture_pixel_custom(gl, 128.0f, 128.0f, rank_d_Texture, new float[]{0.0f,0.0f,128.0f,128.0f, 128.0f / 2, 128.0f / 2},
													1.0f,1.0f, 1.0f, 1.0f);
				}
				gl.glPopMatrix();
			}
			y += 80.0f;
		}

		//テクスチャの背景の透過を無効になるようにします
		gl.glDisable(GL10.GL_BLEND);
	}



	//---------------------------------------------------------------------
	//		2D描画
	//---------------------------------------------------------------------
	public void render2D(GL10 gl) {

		// 2D描画用に座標系を設定します
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glOrthof(-0.9f, 0.9f, -1.6f, 1.6f, 1.0f, -1.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		//テクスチャの背景の透過を有効になるようにします
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);


		//Exitアイコン描画　左
		rect[0] = 0.0f; rect[1] = 0.0f; rect[2] = 128.0f; rect[3] = 64.0f; rect[4] = 128.0f/2; rect[5] = 64.0f/2;
		tResult[0].draw_Color_Custom(gl, 110.0f, 770.0f, exit_Texture, 128.0f, 64.0f, rect, 1.0f, 1.0f, 1.0f, /*Color.a_icon*/1.0f);

//		//Shopアイコン描画　真ん中
//		rect[0] = 0.0f; rect[1] = 0.0f; rect[2] = 128.0f; rect[3] = 64.0f; rect[4] = 128.0f/2; rect[5] = 64.0f/2;
//		tResult[1].draw_Color_Custom(gl, 240.0f, 770.0f, shop_Texture, 128.0f, 64.0f, rect, 1.0f, 1.0f, 1.0f, /*Color.a_icon*/1.0f);

		//Nextアイコン描画　右
//		rect[0] = 0.0f; rect[1] = 0.0f; rect[2] = 128.0f; rect[3] = 64.0f; rect[4] = 128.0f/2; rect[5] = 64.0f/2;
		tResult[1].draw_Color_Custom(gl, 370.0f, 770.0f, next_Texture, 128.0f, 64.0f, rect, 1.0f, 1.0f, 1.0f, /*Color.a_icon*/1.0f);


		for(int i = 0; i < 3; i++){
			if(tResult[i] == null){
				break;
			}

			if(tResult[i].touch_flg){
				if(Sound.music.sp != null){
					Sound.music.SE_play(0);	//決定音SEの再生
				}
				switch(i){
					case 0:
						Global.titleActivity.setActivity(Screen.GAME_TITLE);		//ゲームタイトル画面に遷移する
						break;

					case 1:
						Global.titleActivity.setActivity(Screen.GAME_NEXT_STAGE);	//次の画面に遷移する
						break;

					case 2:
						Global.titleActivity.setActivity(Screen.GAME_SHOP);			//ショップ画面に遷移する
						break;
				}
			}
		}

		//テクスチャの背景の透過を無効になるようにします
		gl.glDisable(GL10.GL_BLEND);
	}

	  @Override
	  public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		  // テクスチャ管理番号割り当て
		  gl.glDeleteTextures(1, textures, 0);
		  gl.glGenTextures(1, textures, 0);
		  textureId = textures[0];

		   // テクスチャ管理番号バインド
		   gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

		   // 画像ファイルをロードし、テクスチャにバインド
		   Bitmap back_ground = BitmapFactory.decodeResource(getResources(), R.drawable.title_back);
		   GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, back_ground, 0);
		   gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		   gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		   back_ground.recycle();


		   loadTextures(gl);		//テクスチャをロードする
		   loadSound();				//サウンドをロードする
	  }

	  @Override
	  public void onSurfaceChanged(GL10 gl, int width, int height) {
		  Log.d("いいいいいいいいいいいいい","ResultMain onSurfaceChanged  exe_stop_flg"+GameMain.exe_stop_flg);
		    GameMain.win_flg = false;
		    GameMain.defeat_flg = false;
		    GameMain.to_clearresult_flg = false;
		    GameMain.to_result_flg_rock = false;
		    GameMain.to_defeatresult_flg = false;

		    for(int i = 0; i < GameMain.ENEMY_MAX; i++){
		    	GameMain.mSta_e[i].Name 		= "";
		    	GameMain.mSta_e[i].Level		= -1;
		    	GameMain.mSta_e[i].MaxHp		= -1;
		    	GameMain.mSta_e[i].Hp			= -1;
		    	GameMain.mSta_e[i].Pow			= -1;
		    	GameMain.mSta_e[i].Def			= -1;
		    	GameMain.mSta_e[i].M_Def		= -1;
		    	GameMain.mSta_e[i].Avoid		= -1;
		    	GameMain.mSta_e[i].Speed		= -1;
		    	GameMain.mSta_e[i].move_type	= -1;
		    	GameMain.mSta_e[i].active_flg 	= false;
		    	GameMain.mSta_e[i].mX			= 1.25f;
		    	GameMain.mSta_e[i].mY			= 1.25f;
		    }

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

			aspect = (float)this.mWidth / (float)this.mHeight;
			cameraDist =(float)(this.mHeight/2);

			touched_2D.window_width  = this.mWidth;
			touched_2D.window_height = this.mHeight;
			touched_2D.sukima_X = width -  touched_2D.window_width;
			touched_2D.sukima_Y = height - touched_2D.window_height;
			Global.gl = gl;		//GLコンテキストを保持する

		   // 背面塗り潰し色の指定
		  // gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);// 黒
	  }

	//--------------------------------------------------------------------------------
	//		テクスチャロード
	//--------------------------------------------------------------------------------
	private void loadTextures(GL10 gl) {
		Resources res = mContext.getResources();

		//this.mNumberTexture        = GraphicUtil.loadTexture(gl, res, R.drawable.number_texture);				//数字の書かれたFPS表示してる画像の読み込み
		this.back_Texture          = GraphicUtil.loadTexture(gl, res, R.drawable.title_back);					//背景の画像の読み込み
		this.rank_s_Texture 	   = GraphicUtil.loadTexture(gl, res, R.drawable.result_rank_s);
		this.rank_a_Texture		   = GraphicUtil.loadTexture(gl, res, R.drawable.result_rank_a);
		this.rank_b_Texture		   = GraphicUtil.loadTexture(gl, res, R.drawable.result_rank_b);
		this.rank_c_Texture		   = GraphicUtil.loadTexture(gl, res, R.drawable.result_rank_c);
		this.rank_d_Texture		   = GraphicUtil.loadTexture(gl, res, R.drawable.result_rank_d);
		this.exit_Texture		   = GraphicUtil.loadTexture(gl, res, R.drawable.result_button_exit);
		this.next_Texture		   = GraphicUtil.loadTexture(gl, res, R.drawable.result_button_next);
		this.stageclear_Texture	   = GraphicUtil.loadTexture(gl, res, R.drawable.result_stageclear);
		this.stagefailed_Texture   = GraphicUtil.loadTexture(gl, res, R.drawable.result_stagefailed);
		this.totalscore_Texture	   = GraphicUtil.loadTexture(gl, res, R.drawable.result_totalscore);

		//敵のテクスチャ
		this.enemys_Texture[0] = GraphicUtil.loadTexture(gl, res, R.drawable.game_e_distortion);		//ヒズミ
		this.enemys_Texture[1] = GraphicUtil.loadTexture(gl, res, R.drawable.game_e_enemy);				//スライム
		this.enemys_Texture[2] = GraphicUtil.loadTexture(gl, res, R.drawable.game_e_kinoko);			//きのこ
		this.enemys_Texture[3] = GraphicUtil.loadTexture(gl, res, R.drawable.game_e_niwatori);			//にわとり
		this.enemys_Texture[4] = GraphicUtil.loadTexture(gl, res, R.drawable.game_e_dragon);			//ドラゴン
		this.enemys_Texture[5] = GraphicUtil.loadTexture(gl, res, R.drawable.game_e_goburin);			//ゴブリン
		this.enemys_Texture[6] = GraphicUtil.loadTexture(gl, res, R.drawable.game_e_enemy2);				//スライム



		//文字生成
		FontTexture.text_fTexture.createTextBuffer(gl, mContext);
		FontTexture.text_fTexture.drawStringToTexture(gl, FontTexture.result_mk, 1024, 0);
		str_param[0][0] = FontTexture.text_fTexture.getWidth(0);
		str_param[1][0] = FontTexture.text_fTexture.getHeight(0);
		str_param[2][0] = FontTexture.text_fTexture.getOffset(0);
		str_param[3][0] = FontTexture.text_fTexture.lineHeight(0);
		FontTexture.text_fTexture.nextReadPoint(0);
		str_Texture = FontTexture.text_fTexture.getTexture(0);


		//数字文字生成
		for( int i = 0; i < FontTexture.txtnum_length; i++){
			FontTexture.text_fTexture.drawStringToTexture(gl, FontTexture.chr_num[i], 1024, 1);
			num_str_param[0][i] = FontTexture.text_fTexture.getWidth(0);
			num_str_param[1][i] = FontTexture.text_fTexture.getHeight(0);
			num_str_param[2][i] = FontTexture.text_fTexture.getOffset(1);
			num_str_param[3][i] = FontTexture.text_fTexture.lineHeight(1);
			FontTexture.num_h = num_str_param[3][i];
			FontTexture.text_fTexture.nextReadPoint(1);
		}
		strnum_Texture = FontTexture.text_fTexture.getTexture(1);
	}

	public void loadSound() {
		Sound.music.Sound_Create(mContext);			//サウンドの生成
		//サウンドのロード
		Sound.music.SE_Sound_load(mContext, 0, R.raw.decision);

	}
}



