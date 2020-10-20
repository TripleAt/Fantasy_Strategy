package com.fantasy_strategy;

import java.util.ArrayList;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;


public class TouchManagement  {

public long[] click_time = new long[2];
	public static ArrayList<float[]> list = new ArrayList<float[]>();
	public static int touch_num = -1;
	public static boolean skill_cut_touch = false;	//スキルカットイン判断

	private static int scrollflg_edge;	//どの画面端かを判断するフラグ
	private static float cur_x = 0;
	private static float cur_y = 0;
	private boolean icon_touch_flg = false;

	private float colision_work[] = new float[6];	//リストの配列を一時的に格納する（作業用）
	private int cnt = 0;

	public TouchManagement(){
		click_time[0] = 0;
		click_time[1] = 0;
	}


	//画面がタッチされたときに呼ばれるメソッド
	public void touched(MotionEvent e,float x, float y) {
		if(Skill.skill_cutin_flg >= 1 && Skill.skill_cutin_flg <= 5) return;	//スキルカットイン中
	//	Log.d("touched!"," x = " + x + ", y = " +  y);
		switch(e.getAction()){

		case MotionEvent.ACTION_DOWN:
			{		//ダブルクリックの検出

				click_time[1] = click_time[0];
				if(!icon_touch_flg)
					click_time[0] = System.currentTimeMillis();

				if(click_time[1] != 0 && !icon_touch_flg){
					if(click_time[0]-click_time[1] < 300 && !icon_touch_flg){	//前回アイコンがタッチされていなければ
						//Log.d("","ダブルclick");
						//Log.d("カーソル位置","ｘ："+(Cursor_var.x + Cursor_var.x_move) / 0.25f +"ｙ：" + (Cursor_var.y + Cursor_var.y_move) / 0.25f );
						cnt = 0;
						for(int i = 0; i < 5; i++) 		//主人公たち全員分
						{
							if(GameMain.select_flg[i]){
								if(GameMain.mSta_p[i].skill_active_flg == false){		//スキル発動して無かったら移動できる

									if(GameMain.mSta_p[i] != null && GameMain.GameMain_flg){
										int click_now_x = (int) ((GameMain.mSta_p[i].mX + 0.125f)/0.25f);
										int click_now_y = (int) ((GameMain.mSta_p[i].mY + 0.125f)/0.25f);
										int click_next_x = (int)((Cursor_var.x + Cursor_var.x_move) / 0.25f);
										int click_next_y = (int)((Cursor_var.y + Cursor_var.y_move) / 0.25f);
										Log.d("","x:"+Cursor_var.x + Cursor_var.x_move);
										Log.d("","y:"+Cursor_var.y + Cursor_var.y_move);
										GameMain.mSta_p[i].move_change(click_now_x, click_now_y, click_next_x, click_next_y);
										cnt++;
									}
								}
							}
						}
						if(cnt > 0)		//一人以上なら鳴らす
							if(Sound.music.sp != null){
								Sound.music.SE_play(1);	//決定音SEの再生
							}

					}
				}

				//ゲームクリア時
				if(GameMain.to_clearresult_flg == true){
					//クリア画面へ
					Global.titleActivity.setActivity(Screen.GAME_CLEAR);
					GameMain.to_clearresult_flg = false;
				}
				//ゲームオーバー時
				if(GameMain.to_defeatresult_flg == true){
					//ゲームオーバー画面へ
					Global.titleActivity.setActivity(Screen.GAME_OVER);
					GameMain.to_defeatresult_flg = false;
				}

			}

			Camera_var.tx = x;
			Camera_var.ty = y;
			Camera_var.tz = (float)Math.sqrt(x*x + y*y);

			Log.d("TouchEvent", "getAction()" + "ACTION_DOWN");

			//リストで登録するような作りを作る
			//タッチ判定衝突処理
			for( int i = 0; i < list.size(); i++){
				colision_work = list.get(i);
				touch_num = -1;

				if(colision_work[4] == 0){		//
					if(icon_Touch(e, list.get(i)) == 1){
						touch_num = i;
						break;
					}
				}

				if(colision_work[4] >= 1 && colision_work[4] <= 5){		//ゲームメイン画面のとき、キャラアイコンの選択操作
					if(icon_Touch(e, list.get(i)) == 1){
						touch_num = i;
						GameMain.ic_touch_flg = false;		//とりあえずタップしたらポップアップウィンドウを消す
						GameInfo.action_flg = -1;
						GameInfo.back_win = -1;
						GameInfo.char_fling = i;
					}
				}

				//Log.d("touch_num","i:"+i);

				//ゲームメイン画面のとき、ステージ選択アイコンの選択操作
				if(colision_work[4] == 6){
					if(icon_Touch(e, list.get(i)) == 1){
						if(GameInfo.back_win == -1){	//ウィンドウを表示していないとき
							GameInfo.back_win = -GameInfo.back_win;
							touch_num = i;
							GameInfo.action_flg = 1;	//ウィンドウを表示するフラグ（指を離した時）
						}
						else{							//ウィンドウを表示しているとき
							GameInfo.back_win = -GameInfo.back_win;
							GameMain.ic_touch_flg = false;
							GameInfo.action_flg = -1;
						}
					}
				}


				//ステージセレクトタッチ判定調整
				if(colision_work[4] == 7 ||
				 colision_work[4] == 8){
					if(icon_Touch(e, list.get(i)) == 1){
						touch_num = i;
					}
				}


				//ステージセレクトバック(YESのとき)
				if(colision_work[4] == 9){		//ゲームメイン画面のとき、ステージ選択アイコンの選択操作
					if(GameInfo.action_flg == 1){	//ウィンドウ表示のフラグ
						if(GameInfo.back_win == 1){
							if(icon_Touch(e, list.get(i)) == 1){
								//ステージセレクト画面へ
								Global.titleActivity.setActivity(Screen.GAME_SELECT);
							}
						}
					}
				}
				//ステージセレクトバック(CANCELのとき)
				if(colision_work[4] == 10){		//ゲームメイン画面のとき、ステージ選択アイコンの選択操作
					if(GameInfo.action_flg == 1){	//ウィンドウ表示のフラグ
						if(GameInfo.back_win == 1){
							if(icon_Touch(e, list.get(i)) == 1){
								GameInfo.back_win = -GameInfo.back_win;
								GameMain.ic_touch_flg = false;
								GameInfo.action_flg = -1;
							}
						}
					}
				}

				if(touch_num != -1) break;
			}
			icon_touch_flg = false;
			touched_2D.touch_2dx = e.getX();
			touched_2D.touch_2dy = e.getY();


			break;

		case MotionEvent.ACTION_UP:		//指を離した

			//キャラアイコンセレクト
			GameInfo.select_fling_chk = GameInfo.fling_chk;
			if(!GameMain.select_flg[GameMain.select_char] &&
					GameInfo.char_fling >= 1 && GameInfo.char_fling <= 5){		//選択中ではなくフリック入力アイコン非表示
				GameMain.select_flg[GameMain.select_char] = true;
				icon_touch_flg = true;
			}else if(GameMain.select_flg[GameMain.select_char] && GameInfo.char_fling >= 1 && GameInfo.char_fling <= 5 &&
					GameInfo.select_fling_chk != -1){
				GameMain.select_flg[GameMain.select_char] = true;
				icon_touch_flg = true;
			}else if(GameMain.select_flg[GameMain.select_char] && GameInfo.char_fling >= 1 && GameInfo.char_fling <= 5 &&
					GameInfo.select_fling_chk == -1){
				GameMain.select_flg[GameMain.select_char] = false;
			}

			//フリック入力でタップされたキャラの位置にいく
			GameInfo.return_fling = GameInfo.fling_chk;
			if(GameInfo.return_fling == 1){
				if(GameInfo.char_fling >= 1 && GameInfo.char_fling <= 5 && GameInfo.cnt_fling > 10){
					Cursor_var.x = GameMain.mSta_p[GameMain.select_char].mX;
					Cursor_var.y = GameMain.mSta_p[GameMain.select_char].mY;
					GameInfo.return_fling = -1;
				}
			}


			GameInfo.char_fling = -1;
			GameInfo.skill_fling = GameInfo.fling_chk;
			GameInfo.item_fling = GameInfo.fling_chk;
			GameInfo.cnt_fling = 0;
			GameInfo.fling_chk = -1;		//初期化


			/* Camera_var.x += Camera_var.x_move;
			 Camera_var.y += Camera_var.y_move;
			 Camera_var.x_move = 0.0f;
			 Camera_var.y_move = 0.0f;*/

			Cursor_var.y += Cursor_var.y_move;
			Cursor_var.x += Cursor_var.x_move;
			Cursor_var.y_move = 0;
			Cursor_var.x_move = 0;
			scrollflg_edge = 0;
			Log.d("TouchEvent", "getAction()" + "ACTION_UP");
			break;

		case MotionEvent.ACTION_MOVE:
			if(GameInfo.char_fling >= 1 && GameInfo.char_fling <= 5){
				//Log.d("ACTION_MOVE", "e.getX():" + e.getX()+"cnt_flick:"+GameInfo.cnt_fling);
				touched_2D.touch_move_2dx = e.getX();
				touched_2D.touch_move_2dy = e.getY();

				GameInfo.select_cnt_fling = GameInfo.cnt_fling;
			}
			break;

		case MotionEvent.ACTION_CANCEL:
			break;

		case KeyEvent.KEYCODE_BACK:
			Log.d("テクスチャ消去","");
			TextureManager.deleteAll(Global.gl);		//テクスチャを削除する
			break;

		}


	}

	//スクロールした距離が返ってくるメソッド
	/**
	 * 分かりづらいけど、カメラの制御に応じて、ディスプレイがワールドに対しての座標計算を変えている。
	 * 0～180度、180～360度
	 * 90～270度、270～90度
	 * それぞれで、割合の計算をしている。
	 *
	 * 例えば、angle_s1が0度のときはｘを+方向に動かしたときの割合は
	 * X：1
	 * Y：0
	 * となる
	 *
	 * angle_s1が45度の場合、ｘを+方向に動かしたときの割合は
	 * X:0.5
	 * Y:-0.5
	 * となる
	 * **/
	public void scroll(MotionEvent e, float x, float y){
		if(GameInfo.char_fling >= 1 && GameInfo.char_fling <= 5 ||
				skill_cut_touch == true) return;		//キャラアイコンを触れているときと、スキル発動中はスクロールさせない


		//画面座標でフィールドを動かしたとき実際にはどのように動くか計算する
		float ans_x = 0.0f, ans_y = 0.0f;		//一時的に保存するｘ,y
		int i;
		float angle_s2 = 0;
		float angle_s1 = (float)(( (Camera_var.angle % 360) ) * Math.PI / 180 );		//スクロール計算用angle


		angle_s2 = (float) Math.atan2(x, y);
		ans_y = (float) ( Math.cos( angle_s2 - angle_s1 ) * ((Math.abs( x ) + Math.abs( y )) / 500) );
		ans_x = (float) ( Math.sin( angle_s2 - angle_s1 ) * -((Math.abs( x ) + Math.abs( y )) / 500) );

		i = (int) (ans_y / 0.25f);
		Cursor_var.y_move  = (float) (i * 0.25f);
		i = (int) (ans_x  / 0.25f);

		Cursor_var.x_move  = (float) (i * 0.25);


		//スクロールした指が画面端にふれたときの処理
		if(icon_Touch(e,new float[]{480.0f-20.0f, 400.0f,  40.0f, 800.0f}) == 1 ||		//右端
		icon_Touch(e,new float[]{  0.0f+2.0f, 400.0f,  40.0f, 800.0f}) == 1 ||		//左端
		icon_Touch(e,new float[]{240.0f, 800.0f-20.0f, 480.0f,  40.0f}) == 1 ||		//下端
		icon_Touch(e,new float[]{240.0f,   0.0f+20.0f, 480.0f,  40.0f}) == 1 )		//上端
		{
			scrollflg_edge = 1;
		}else{
			scrollflg_edge = 0;
		}

	}


	//-----------------------------------------------------------------------------
	//  与えられる値によって比例した値を返す(値の渡し方により反比例にもなる)
	//-----------------------------------------------------------------------------
	public static float atai_control(int moto_atai, int moto_min, int moto_max, float kahen_min, float kahen_max )
	{
		float moto_sa, moto_ookisa;
		float kahen_ookisa, ans;

		kahen_ookisa = kahen_max - kahen_min;		//何の値からどの値まで変化させるか
		moto_ookisa = (float)(moto_max - moto_min);	//もう片っ方の値もどの値からどの値まで変化するか
		moto_sa = (float)(moto_atai - moto_min);	//現在の大きさ

		//%を計算し、値を返す！
		ans = (float)((moto_sa / moto_ookisa) * kahen_ookisa + kahen_min);	//どの大きさにするかの比例％* 可変する大きさ + 最小値

		return ans;
	}



	/**
	 * アイコンのタッチ衝突判定処理
	 * touch_colision→
	 * 			0 x
	 * 			1 y
	 * 			2 width(半分)
	 *			3 height(半分)
	 * @param e	タッチイベント
	 * @param touch_colision 当たり判定その他
	 * @return 衝突したら1してないなら-1
	 */
	public int icon_Touch(MotionEvent e, float touch_colision[])
	{
		if(GameMain.win_flg || GameMain.defeat_flg){		//勝利と敗北が出ているときはボタンを反応させない
			return -1;
		}

		float ex, ey;

		ex = Math.abs((touch_colision[0] * touched_2D.window_width  / (float) 480 ) - e.getX() + (touched_2D.sukima_X / 2.0f));		//タッチの中心値から離れている距離
		ey = Math.abs((touch_colision[1] * touched_2D.window_height / (float) 800 ) - e.getY() + (touched_2D.sukima_Y / 2.0f));		//タッチの中心値から離れている距離

		if(ex  <= touch_colision[2] * touched_2D.window_width  / (float) 480  &&		//幅
		   ey  <= touch_colision[3] * touched_2D.window_height / (float) 800 ){			//高さ
			return 1;		//衝突した時の処理
		}

		return -1;
	}




	//そのボタンがtouchされたかチェックしにいく。
	public static boolean touch_chk(float num){

		if(touch_num == (int)num){
			touch_num = -1;
			return true;
		}
		return false;

	}


	//----------------------------------------------------------------------------------
	//スクロールした際、画面端にまでタッチしたら、カーソルを動かし続ける
	//----------------------------------------------------------------------------------
	public static void scroll_move(){
		int i;
		if(scrollflg_edge == 1)
		{
			float ang_radian;
			ang_radian = (float) Math.atan2(Cursor_var.x_move, Cursor_var.y_move);		//角度出す

			cur_y = (float) (Math.cos(ang_radian) * 0.2f);
			cur_x = (float) (Math.sin(ang_radian) * 0.2f);

			if( 0 != Cursor_var.x_move ||
			    0 != Cursor_var.y_move){
				Cursor_var.x += cur_x;
				Cursor_var.y += cur_y;
			}
		}else{
			//手が離れている時は位置補正
			i = (int) (Cursor_var.y / 0.25f);
			Cursor_var.y  = (float) (i * 0.25f);
			i = (int) (Cursor_var.x / 0.25f);
			Cursor_var.x  = (float) (i * 0.25f);
		}
	}


}