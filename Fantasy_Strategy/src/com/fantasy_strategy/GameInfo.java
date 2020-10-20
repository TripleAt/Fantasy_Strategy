package com.fantasy_strategy;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;


public class GameInfo {

	public static int ic_flg;		//アイコンのフラグ
	private int work_flg = 1;		//作業用のフラグ
	public static int back_win = -1;		//ステージセレクトウィンドウの保持フラグ
	public static int ic_tex;				//アイコンフラグ	0:スキルアイコン 1:アイテムアイコン
	public static int action_flg = -1;		//タッチイベント判断フラグ
	public static int char_fling = -1;		//キャラクターアイコンフリックの保持フラグ

	long mFpsCountStartTime = System.currentTimeMillis();
	static int t_millisecond;			//ミリ秒
	static int t_second;				//秒
	public static int t_minute;			//分
	public static int play_score;		//プレイ中のスコア
	public static int stage_number;		//ステージ名の判断
	public float str_stage[][] = new float[4][FontTexture.stage.length];	//ステージ名のパラメーター	0:width, 1:height, 2:offset, 3:1行のheight
	public static float num_str_param[][] = new float[4][FontTexture.txtnum_length];
	public static float message_str_param[][] = new float[4][FontTexture.str_message.length];

	static float hp[][] = new float[2][GameMain.ENEMY_MAX];	//キャラクター分のHP

	float g_x;
	float g_yohaku = 2.0f;			//隙間
	float colision_work[] = new float[6];	//リストの配列を一時的に格納する（作業用）

	float skill_gage[] = new float[GameMain.SKILL_NUM];
	int skill_texture;					//スキルのテクスチャー
	float skill_x;						//スキルアイコンのx座標
	float skill_gage_x;					//スキルゲージのx座標
	int skill_no;
	boolean skill_ic_flg = false;		//スキルアイコンフラグ
	float flick_x;						//フリック時のx座標
	float flick_ic_x;	//フリック時アイコン座標

	public static int fling_chk = 1;		//フリックチェック
	public static int skill_fling = 1;
	public static int item_fling = -1;
	public static int return_fling = 1;
	public static int cnt_fling = 0;	//フリック入力のタメ
	public static int select_fling_chk = 0;	//フリック操作の際のキャラアイコンのフラグ
	public static int select_cnt_fling = 0;	//フリック入力のタメ

	public static int item_number;		//アイテムの種類判別用

	float alpha = 0;	//透過
	float alpha_ver = 0.1f;
	public static float char_col[][] = {{0.3f,0.3f,0.3f},			//キャラアイコンの背景の色
						  {0.8f,0.2f,0.1f},
	  					  {0.5f,0.5f,0.1f},
	  					  {0.6f,0.5f,0.4f},
	  					  {0.1f,0.1f,1.0f}};

	float fling_color[]  = {1.0f,1.0f,1.0f,1.0f};



	public GameInfo(){

	}

	public void run(GL10 gl){

		//プレーヤー情報の枠表示
		gl.glPushMatrix();
		{
			GraphicUtil.glTranslatef_pixel(gl, 0.0f, 0.0f, -0.021f);		//座標位置
			gl.glScalef(0.94f, 0.94f, 1.0f);	//画像の大きさ指定
			//gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
			GraphicUtil.drawTexture_pixel_custom(gl, 512.0f, 128.0f, GameMain.userinfo_tex, new float[]{0.0f,0.0f,512.0f,72.0f,0.0f,0.0f},
					1.0f,1.0f, 1.0f, 1.0f);

		}
		gl.glPopMatrix();

		//ステージ名の表示
		FontTexture.text_fTexture.draw_str(gl, GameMain.str_Texture, 0.7f, 280, 26,str_stage[2][stage_number],str_stage[0][stage_number],str_stage[1][stage_number]);


		//タイムの計算
		if(!GameMain.exe_stop_flg && !GameMain.win_flg && !GameMain.defeat_flg)
		{
			GameMain.nowTime = System.currentTimeMillis();		// 現在時間を取得
			long difference = GameMain.nowTime - mFpsCountStartTime;		// 現在時間との差分を計算
			t_millisecond = (int) (difference % 1000);

			if (difference >= 1000) {		// 1秒経過していたら秒数カウントする
				t_second++;
				mFpsCountStartTime = GameMain.nowTime;

				for(int i = 0; i < GameMain.SKILL_NUM;i++){
					GameMain.skill[i].skill_gage_time++;			//スキルが使えるまでの時間をカウントする
				}
			}

		}
		if(t_second >= 60){				// 60秒経過していたら分数カウントする
			t_second = 0;
			t_minute++;
		}
		if(t_minute >= 60){				// 60分経過していたら分数をとりあえず0にする
			t_minute = 0;
		}

		//タイムの表示
		//ミリ秒数表示
		FontTexture.text_fTexture.draw_num2(gl, GameMain.strnum_Texture, 0.5f,
				250, 31,
				GameInfo.t_millisecond, GameInfo.num_str_param[2],
				1.0f, 1.0f, 1.0f, 1.0f);

		//小数点"."の表示
		FontTexture.text_fTexture.draw_num(gl, GameMain.strnum_Texture, 0.7f,
				(int) (210+ (FontTexture.num_h / 6)), 26,
				num_str_param[2][11],
				1.0f, 1.0f, 1.0f, 1.0f);

		//秒数表示
		if(GameInfo.t_second < 10){
			FontTexture.text_fTexture.draw_num2(gl, GameMain.strnum_Texture, 0.7f,
					(int) (200 - (FontTexture.num_h * 0.8f * 0.7f)), 26,
					0, GameInfo.num_str_param[2],
					1.0f, 1.0f, 1.0f, 1.0f);

		}
		FontTexture.text_fTexture.draw_num2(gl, GameMain.strnum_Texture, 0.7f,
				200, 26,
				GameInfo.t_second, GameInfo.num_str_param[2],
				1.0f, 1.0f, 1.0f, 1.0f);


		//コロン":"の表示
		FontTexture.text_fTexture.draw_num(gl, GameMain.strnum_Texture, 0.7f,
				173, 26,
				num_str_param[2][10],
				1.0f, 1.0f, 1.0f, 1.0f);

		//分数表示
		if(GameInfo.t_minute < 10){
			FontTexture.text_fTexture.draw_num2(gl, GameMain.strnum_Texture, 0.7f,
					(int) (162 - (FontTexture.num_h * 0.8f * 0.7f)), 26,
					0, GameInfo.num_str_param[2],
					1.0f, 1.0f, 1.0f, 1.0f);

		}
		FontTexture.text_fTexture.draw_num2(gl, GameMain.strnum_Texture, 0.7f,
				160, 26,
				GameInfo.t_minute, GameInfo.num_str_param[2],
				1.0f, 1.0f, 1.0f, 1.0f);

		//スコアの描画
		FontTexture.text_fTexture.draw_num3(gl, GameMain.strnum_Texture, 0.7f,
				113, 25,
				 GameInfo.play_score, GameInfo.num_str_param[2],
				1.0f, 1.0f, 1.0f, 1.0f);


		//ウィンドウの描画
		if(GameMain.ic_touch_flg && action_flg != -1){
			gl.glPushMatrix();
			{
				GraphicUtil.glTranslatef_pixel(gl, 240.0f, 672.0f-16.0f, 0.0f);		//座標位置
				gl.glScalef(1.0f, 0.5f, 1.0f);	//画像の大きさ指定
				//gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
				GraphicUtil.drawTexture_pixel_custom(gl, 416.0f, 226.0f, GameMain.map_window, new float[]{0.0f,0.0f,402.0f,204.0f,201.0f,102.0f},
						1.0f,1.0f, 1.0f, 0.5f);

			}
			gl.glPopMatrix();
		}


		//残HPを調べる
		for(int i = 0; i < GameMain.PLAYER_NUM; i++){
			if(GameMain.mSta_p[i].Hp == 0){
				hp[0][i] = 0;
			}else{
				hp[0][i] = GameMain.mSta_p[i].Hp * 100 / GameMain.mSta_p[i].MaxHp;
				hp[0][i] *= 0.01f;
			}
		}
		for(int i = 0; i < GameMain.ENEMY_MAX; i++){
			if(GameMain.mSta_e[i].Hp == 0){
				hp[1][i] = 0;
			}else{
				hp[1][i] = GameMain.mSta_e[i].Hp * 100 / GameMain.mSta_e[i].MaxHp;
				hp[1][i] *= 0.01f;
			}
		}



		//キャラクターの体力ゲージ表示
		g_x = 64.0f;
		for(int i = 1; i <= GameMain.PLAYER_NUM; i++){

			//ゲージの色選択
			int bar_tex;
			if(GameInfo.hp[0][i-1] >= 0.6){
				bar_tex = GameMain.mBar_green;
			}else if(GameInfo.hp[0][i-1] >= 0.2){
				bar_tex = GameMain.mBar_yellow;
			}else{
				bar_tex = GameMain.mBar_red;
			}
			//ゲージの表示
			gl.glPushMatrix();
			{
				GraphicUtil.glTranslatef_pixel(gl, g_yohaku*i+g_x+1.0f, 784.0f+2.0f-3.0f, -0.02f);		//座標位置
				gl.glScalef(0.34f, 0.5f, 1.01f);	//画像の大きさ指定

				GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 64.0f, bar_tex, new float[]{23.0f,20.0f,191.0f * hp[0][i - 1],28.0f,0.0f,0.0f},
												1.0f,1.0f, 1.0f, 1.0f);

			}
			gl.glPopMatrix();

			//HPゲージの表示
			gl.glPushMatrix();
			{
				GraphicUtil.glTranslatef_pixel(gl, g_x+g_yohaku*i, 784.0f-2.0f, -0.02f);		//座標位置
				//gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
				gl.glScalef(0.34f, 0.5f, 1.0f);	//画像の大きさ指定

				GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 64.0f, GameMain.mGauge, new float[]{21.0f,18.0f,195.0f,32.0f,0.0f,0.0f},
												1.0f,1.0f, 1.0f, 1.0f);

			}
			gl.glPopMatrix();

			g_x += 66.3f;
		}


		// FPSを表示する
/*		if (Global.isDebuggable) {
			long nowTime = System.currentTimeMillis();		// 現在時間を取得
			long difference = nowTime - mFpsCountStartTime;		// 現在時間との差分を計算
			if (difference >= 1000) {		// 1秒経過していた場合は、フレーム数のカウント終了
				mFps = mFramesInSecond;
				mFramesInSecond = 0;
				mFpsCountStartTime = nowTime;
			}
			mFramesInSecond++;// フレーム数をカウント
			GraphicUtil.drawNumbers(gl, -0.5f, -0.75f, 0.2f, 0.2f, mNumberTexture, mFps, 2, 1.0f, 1.0f, 1.0f, 1.0f);
		}
*/


	}


	public void ic_process(GL10 gl, int texture[]){

		//アイコン下板
		gl.glPushMatrix();
		{
			GraphicUtil.glTranslatef_pixel(gl, 240.0f, 800.0f - 45.0f, 0.03f);		//座標位置
			GraphicUtil.drawTexture_pixel_custom(gl, 512.0f, 128.0f,  GameMain.window_tex, new float[]{0.0f, 0.0f, 480.0f, 90.0f, 240.0f, 45.0f},
												1.0f, 1.0f, 1.0f, 1.0f);
		}
		gl.glPopMatrix();


		//トータルスコア表示
		FontTexture.text_fTexture.draw_num3(gl, GameMain.strnum_Texture, 0.7f,
				460, 710,
				TitleActivity.total_score, GameInfo.num_str_param[2],
				1.0f, 1.0f, 1.0f, 1.0f);


		if(GameMain.win_flg || GameMain.defeat_flg){		//勝利時、敗北時は破棄　
			return;
		}
		if(!GameMain.ic_touch_flg){

			for(int i = 1;i <= 6; i++){

				if(TouchManagement.list.size() == 0){		//リストに値が格納されていないとき
					return;
				}

				try{
					colision_work = TouchManagement.list.get(i);
				}
				catch(Exception e){
					Log.d("エラー回避","error Evasion");
					return;
				}

				if(colision_work == null){
					return;
				}
				GameMain.ic_touch_flg = TouchManagement.touch_chk(i);

				if(GameMain.ic_touch_flg){
					if(Sound.music.sp != null){
						Sound.music.SE_play(1);	//決定音SEの再生
					}
					Log.d("デバッグ","debag");


					if((int)colision_work[4] != 6){	//プレイヤーアイコンなら
						GameInfo.ic_flg = (int)colision_work[4];
						GameMain.select_char = (int)colision_work[4]-1;

					}else	//アイコン以外なら
					{
						GameInfo.ic_flg = (int)colision_work[4];
					}

					break;
				}
				GameInfo.ic_flg = GameInfo.ic_flg % 8;
			}
		}


		if(!GameMain.mSta_p[GameMain.select_char ].skill_timing){
			if(GameInfo.char_fling >= 1 && GameInfo.char_fling <= 5 && GameInfo.cnt_fling > 10){		//フリック入力されているとき
				skill_ic_flg = false;
				for(int i = 0; i < GameMain.SKILL_NUM; i++){
					//スキルが使えるまで暗くする（ゲージ風）
					//GameMain.skill[i].skill_gage_flg = 1;		//スキルゲージがすぐたまる（デバッグ用）
					if(GameMain.skill[i].skill_gage_time >= GameMain.skill[i].skill_time[i]){
						GameMain.skill[i].skill_gage_time = GameMain.skill[i].skill_time[i];
						GameMain.skill[i].skill_gage_flg = 1;
					}
					skill_gage[i] = GameMain.skill[i].skill_gage_time / GameMain.skill[i].skill_time[i];

					if(!skill_ic_flg){
						switch(GameMain.select_char){
							case 0:	//主人公スキル
								if(i % 5 == 0){
									if(i / 5 == 0){
										skill_texture = GameMain.heroskill_ic_tex;
									}
									else if(i / 5 == 1){
										//skill_texture = GameMain.mahoflame_Texture;;
									}

									skill_no = i;
									skill_ic_flg = true;
								}

								skill_x = 36.0f;
								skill_gage_x = 7.0f;
								break;
							case 1: //魔法使いスキル
								if(i % 5 == 1){
									if(i / 5 == 0){
										skill_texture = GameMain.mahoflame_Texture;
									}
									else if(i / 5 == 1){
										//skill_texture = GameMain.mahoflame_Texture;;
									}

									skill_no = i;
									skill_ic_flg = true;
								}

								skill_x = 104.0f;
								skill_gage_x = 75.0f;
								break;
							case 2:		//銃スキル
								if(i % 5 == 2){
									if(i / 5 == 0){
										skill_texture = GameMain.gunnerskill_ic_tex;
									}
									else if(i / 5 == 1){
										//skill_texture = GameMain.mahoflame_Texture;;
									}

									skill_no = i;
									skill_ic_flg = true;
								}

								skill_x = 172.0f;
								skill_gage_x = 143.0f;
								break;
							case 3:		//鎧スキル
								if(i % 5 == 3){
									if(i / 5 == 0){
										skill_texture = GameMain.armorskill_ic_tex;
									}
									else if(i / 5 == 1){
										//skill_texture = GameMain.mahoflame_Texture;;
									}

									skill_no = i;
									skill_ic_flg = true;
								}

								skill_x = 240.0f;
								skill_gage_x = 211.0f;
								break;
							case 4:		//大剣スキル
								if(i % 5 == 4){
									if(i / 5 == 0){
										skill_texture = GameMain.large_sword_skill_ic_tex;
									}
									else if(i / 5 == 1){
										//skill_texture = GameMain.mahoflame_Texture;;
									}

									skill_no = i;
									skill_ic_flg = true;
								}

								skill_x = 308.0f;
								skill_gage_x = 279.0f;
								break;
						}
					}

					if(skill_ic_flg) break;
				}


				//フリックの描画判断
				flick_x  = ((touched_2D.touch_move_2dx  - (touched_2D.sukima_X / 2.0f)) *  480.0f / (touched_2D.window_width  - touched_2D.sukima_X ));
				flick_ic_x  = (skill_x + 64.0f);
				if(flick_x < (flick_ic_x - 48.0f )){		//キャラを選択して押している間で判断して色を変える
					fling_chk = 0;
				}else if(flick_x > (flick_ic_x + 48.0f)){		//キャラを選択して押している間で判断して色を変える
					fling_chk = 2;
				}else{
					fling_chk = 1;
				}
/*
				Log.d("タッチX","MOVE_x"+ flick_x);
				Log.d("アイコンX_left","ICON_x"+(flick_ic_x - 48.0f ));
				Log.d("アイコンX_right","ICON_x"+(flick_ic_x + 48.0f));
				Log.d("fling_chk","fling_chk:"+ fling_chk);
*/
				//スキル背景画像			Turquoise	#40E0D0
				for(int i = 0; i < 3; i++){
					if(fling_chk == i){
						fling_color[0] = 0.3f;
						fling_color[1] = 0.6f;
						fling_color[2] = 0.9f;
						fling_color[3] = 1.0f;
					}

					gl.glPushMatrix();
					{
						GraphicUtil.glTranslatef_pixel(gl,  skill_x, 672.0f-16.0f, 0.01f);		//座標位置
						gl.glScalef(2.0f, 2.5f, 1.0f);	//画像の大きさ指定
						//gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);

						GraphicUtil.drawTexture_pixel_custom(gl, 32.0f, 32.0f, GameMain.flicker_tex, new float[]{0.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},
								fling_color[0], fling_color[1], fling_color[2], fling_color[3]);
					}
					gl.glPopMatrix();

					switch(i){
						case 0:
							//スキルアイコン
							gl.glPushMatrix();
							{
								GraphicUtil.glTranslatef_pixel(gl,  skill_x, 672.0f-25.0f, -0.01f);		//座標位置
								gl.glScalef(1.8f, 1.8f, 1.0f);	//画像の大きさ指定
								//gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);

								GraphicUtil.drawTexture_pixel_custom(gl, 32.0f, 32.0f, skill_texture, new float[]{0.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},
																1.0f,1.0f, 1.0f, 1.0f);
							}
							gl.glPopMatrix();

							//使用不可状態の画像表示
							if(GameMain.skill[skill_no].skill_gage_flg == 0 /*|| GameMain.mSta_p[GameMain.select_char].idou*/){
								gl.glPushMatrix();
								{
									GraphicUtil.glTranslatef_pixel(gl,  skill_x, 672.0f-25.0f, -0.02f);		//座標位置
									gl.glScalef(1.8f, 1.8f, 1.0f);	//画像の大きさ指定
									//gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);

									GraphicUtil.drawTexture_pixel_custom(gl, 32.0f, 32.0f, GameMain.flicker_tex, new float[]{0.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},
																	0.2f,0.2f, 0.5f, 0.5f);
								}
								gl.glPopMatrix();
							}

							//スキルゲージの表示
							gl.glPushMatrix();
							{
								GraphicUtil.glTranslatef_pixel(gl, skill_gage_x, 690.0f - 10.0f, -0.02f);		//座標位置(HPゲージの座標位置からxyともに２ピクセルずらす)
								gl.glScalef(0.30f, 0.30f, 1.01f);	//画像の大きさ指定

								GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 64.0f, GameMain.mBar_yellow, new float[]{23.0f,20.0f,191.0f * skill_gage[skill_no],28.0f,0.0f,0.0f},
																0.9f, 0.5f, 0.0f, 1.0f);

							}
							gl.glPopMatrix();

							//HPゲージの表示
							gl.glPushMatrix();
							{
								GraphicUtil.glTranslatef_pixel(gl, skill_gage_x, 690.0f - 10.0f, -0.02f);		//座標位置
								//gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
								gl.glScalef(0.30f, 0.30f, 1.0f);	//画像の大きさ指定

								GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 64.0f, GameMain.mGauge, new float[]{21.0f,18.0f,195.0f,32.0f,0.0f,0.0f},
																1.0f,1.0f, 1.0f, 1.0f);

							}
							gl.glPopMatrix();

							break;

						case 1:

							//リターンアイコンの表示
							gl.glPushMatrix();
							{
								GraphicUtil.glTranslatef_pixel(gl,  skill_x, 672.0f-25.0f, -0.01f);		//座標位置
								gl.glScalef(1.8f, 1.8f, 1.0f);	//画像の大きさ指定
								//gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);

								GraphicUtil.drawTexture_pixel_custom(gl, 32.0f, 32.0f, GameMain.return_ic_tex, new float[]{0.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},
																1.0f,1.0f, 1.0f, 1.0f);
							}
							gl.glPopMatrix();

							break;

						case 2:
							//アイテムの表示
							//回復アイテムアイコン表示
							gl.glPushMatrix();
							{
								GraphicUtil.glTranslatef_pixel(gl,  skill_x, 672.0f-25.0f, -0.01f);		//座標位置
								gl.glScalef(1.8f, 1.8f, 1.0f);	//画像の大きさ指定
								//gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);

								GraphicUtil.drawTexture_pixel_custom(gl, 32.0f, 32.0f, GameMain.recovery_tex, new float[]{0.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},
																1.0f,1.0f, 1.0f, 1.0f);
							}
							gl.glPopMatrix();

							//使用不可状態の画像表示
							if(GameInfo.ic_flg != 6){	//キャラクターアイコンをタッチしている時
								if(!GameMain.mSta_p[GameInfo.ic_flg - 1].active_flg ||
										GameMain.mSta_p[GameInfo.ic_flg - 1].Hp >= GameMain.mSta_p[GameInfo.ic_flg - 1].MaxHp ||
										Skill.item_point_flg  == 0 && TitleActivity.total_score < Item_point.RECOVERY) {

									gl.glPushMatrix();
									{
										GraphicUtil.glTranslatef_pixel(gl,  skill_x, 672.0f-25.0f, -0.02f);		//座標位置
										gl.glScalef(1.8f, 1.8f, 1.0f);	//画像の大きさ指定
										//gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);

										GraphicUtil.drawTexture_pixel_custom(gl, 32.0f, 32.0f, GameMain.flicker_tex, new float[]{0.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},
																		0.2f,0.2f, 0.5f, 0.5f);
									}
									gl.glPopMatrix();
								}

							}

							//使用するのに必要なポイント表示
							FontTexture.text_fTexture.draw_num3(gl, GameMain.strnum_Texture, 0.6f,
									(int)skill_x + 20, 684 - 8 ,
									Item_point.RECOVERY, GameInfo.num_str_param[2],
									1.0f, 1.0f, 1.0f, 1.0f);

							break;
					}
					skill_x += 64.0f;

					for(int j = 0; j < 4; j++){		//フリック入力カラー初期化
						fling_color[j] = 1.0f;
					}
				}
			}else{
				skill_ic_flg = false;
			}
		}

		if(GameMain.ic_touch_flg && action_flg != -1){	//ウィンドウ表示時のアイコン表示
			// ステージセレクトバックアイコンをタップしたとき
			if(GameInfo.back_win == 1){
				//ステージセレクトバックメッセージ表示
				FontTexture.text_fTexture.draw_str(gl, GameMain.strmessage_Texture, 0.7f, 90, 612,message_str_param[2][0],message_str_param[0][0],message_str_param[1][0]);

				// OKアイコン表示
				gl.glPushMatrix();
				{
					GraphicUtil.glTranslatef_pixel(gl,  140.0f, 670.0f, -0.01f);		//座標位置
					gl.glScalef(1.0f, 1.0f, 1.0f);	//画像の大きさ指定
					//gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);

					GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 64.0f, GameMain.stageselectback_tex, new float[]{0.0f, 0.0f, 128.0f, 64.0f, 128.0f / 2, 64.0f / 2},
													1.0f,1.0f, 1.0f, 1.0f);
				}
				gl.glPopMatrix();

				// NOアイコン表示
				gl.glPushMatrix();
				{
					GraphicUtil.glTranslatef_pixel(gl,  340.0f, 670.0f, -0.01f);		//座標位置
					gl.glScalef(1.0f, 1.0f, 1.0f);	//画像の大きさ指定
					//gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);

					GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 64.0f, GameMain.stageselectback_tex, new float[]{128.0f, 0.0f, 128.0f, 64.0f, 128.0f / 2, 64.0f / 2},
													1.0f,1.0f, 1.0f, 1.0f);
				}
				gl.glPopMatrix();
			}
		}


		//キャラが選択されているとき、アイコンを明滅させる
		if(GameInfo.ic_flg >= 1 && GameInfo.ic_flg <= 5){
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
			work_flg = GameInfo.ic_flg;
		}


		if(TouchManagement.list.size() == 0){		//リストに値が格納されていないとき
			return;
		}



		if(alpha < 0 || alpha > 0.7f){
			alpha_ver *= -1;
		}
		alpha += alpha_ver;
		for(int i = 0; i < 5; i++) 		//主人公たち全員分
		{
			if(GameMain.select_flg[i]){		//選択中か？
				gl.glPushMatrix();
				{
					GraphicUtil.glTranslatef_pixel(gl, i * 68.0f + 100, 748.0f, -0.01f);		//座標位置
					gl.glScalef(2.0f, 2.0f, 1.0f);	//画像の大きさ指定

					GraphicUtil.drawTexture_pixel_custom(gl, 32.0f, 32.0f, GameMain.flicker_tex, new float[]{0.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},
													1.0f,1.0f, 1.0f, alpha);
				}
				gl.glPopMatrix();
			}
		}
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

	}

}
