package com.fantasy_strategy;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;



//カメラの変数
class Camera_var{
	public static float x = 0;				//カメラ反映値
	public static float y = 0;				//カメラ反映値
	public static float x_move = 0;			//スクロール中カメラ
	public static float y_move = 0;			//スクロール中カメラ
	public static int   angle  = 315;		//カメラ角度

	public static float tx = 0;
	public static float ty = 0;
	public static float tz = 0;


}

//カーソルの変数
class Cursor_var{
	public static float x = 0;
	public static float y = 0;
	public static float x_move = 0;
	public static float y_move = 0;
}

//タッチの変数
class touched_2D{
	public static float touch_2dx = 0;
	public static float touch_2dy = 0;
	public static float touch_2dz = 0;

	public static float window_width  = 0;
	public static float window_height = 0;
	public static float sukima_X  = 0;
	public static float sukima_Y  = 0;

	public static float touch_move_2dx = 0;
	public static float touch_move_2dy = 0;
}

//敵の種類数、各敵の種類ナンバー
class Enemy_types{
	public static final int ENEMY_TOTAL = 7;
	public static final int HIZUMI = 0;
	public static final int SLIME = 1;
	public static final int KINOKO = 2;
	public static final int NIWATORI = 3;
	public static final int DRAGON = 4;
	public static final int GOBURIN = 5;
	public static final int SLIME2 = 6;
}

//敵ごとのスコア
class Enemy_score{

	public static final int HIZUMI =	1000;
	public static final int SLIME = 	50;
	public static final int KINOKO = 	200;
	public static final int NIWATORI = 	200;
	public static final int DRAGON = 	900;
	public static final int GOBURIN = 	300;

}

//アイテム毎に必要なポイント
class Item_point{
	public static final int RECOVERY = 2500;		//回復
}

public class GameMain implements GLSurfaceView.Renderer {


	public static int map_size_x;
	public static int map_size_y;
	public static final int PLAYER_NUM = 5;			//主人公の数
	public static final int ENEMY_MAX = 35;			//敵の数
	public static final int PARTICLE_NUM = 50;		//エフェクトのマックス
	public static final int DIE_EFE_NUM = 10;		//エフェクトのマックス
	public static final int DEATH_EFE_NUM = 10;
	public static final int TOUCH_MAX = 20;			//当たり判定(ボタン)の数
	public static final int ATTACK_AREA = 50;
	public static final int SKILL_NUM = 10;			//スキルの数
	public static final int ITEM_NUM = 5;			//アイテムの数
	public int enemy_num = 0;
	public static Long nowTime;


	// コンテキスト
	private Context mContext;

	//windowについての処理
	private int mHeight;
	private int mWidth;
	private int mWidthOffset;
	private int mHeightOffset;

	//↓テクスチャ----------------------------------------------------------------------------------------------
	private int csl_Texture;				//カーソル
	private int csl_Texture2;				//カーソルアイコン
	private int map_Texture;				//マップチップの生成。
	private int[] mEnemy = new int[Enemy_types.ENEMY_TOTAL];		//敵画像
	private int[] mPlayer = new int[5];		//味方画像
	private int sak;						//柵
	private int mIwa;						//岩
	private int mGrass;						//草
	private int mGrass2;					//草
	private int mWin;						//勝利
	private int mDefeat;					//敗北
	private int mParticle;					//攻撃時パーティクル
	private int item_ic;					//アイテムアイコン
	private int skill_ic;					//スキルアイコン
	private int mKage;						//キャラクター影
	private int character_ic;				//キャラクターアイコン
	private int tensiondown_tex;			//がっかり線
	private int back_ic;					//バックアイコン
	private int attack_area_tex;			//攻撃範囲設定
	private int syougai1;					//障害物
	private int syougai2;					//障害物
	private int syougai3;					//障害物
	private int syougai4;					//障害物
	private int heart_tex;					//ハート
	private int revive_tex;					//復活アイコン
	private int[][] skill_efect_tex = new int[SKILL_NUM][2];	//スキル毎のエフェクト
	private int start_spell;				//開始時の文字
	private int skill_cutin_tex;	//スキルのカットインエフェクト


	//GameInfo用
	private GameInfo mGameInfo = new GameInfo();
	public static int map_window;			//ウィンドウ画像
	public static int mGauge;				//ゲージ（箱）
	public static int mBar_green;			//緑ゲージ
	public static int mBar_red;				//赤ゲージ
	public static int mBar_yellow;			//黄色ゲージ
	public static int flicker_tex;			//明滅用の白画像
	public static int suya_tex;				//死亡の幽霊
	public static int userinfo_tex;			//画面の上に表示するプレイヤーの情報
	public static int window_tex;			//板のウィンドウ
	public static int str_Texture;			//文字のテクスチャ
	public static int strnum_Texture;		//数字のテクスチャ
	public static int mahoflame_Texture;	//魔法スキル火柱のテクスチャ
	public static int stageselectback_tex;	//ステージセレクトバックテクスチャ
	public static int strmessage_Texture;			//文字のテクスチャ
	public static int recovery_tex;			//回復アイコンテクスチャ
	public static int heroskill_ic_tex;		//主人公スキルアイコンテクスチャ
	public static int gunnerskill_ic_tex;		//銃スキルアイコンテクスチャ
	public static int armorskill_ic_tex;		//鎧スキルアイコンテクスチャ
	public static int large_sword_skill_ic_tex;	//大剣スキルアイコンテクスチャ
	public static int return_ic_tex;			//回復アイコンテクスチャ

	public static int mNumberTexture;		//fpsを数値で表示
	//↑テクスチャ----------------------------------------------------------------------------------------------

	//勝利画面時
	public static boolean win_flg = false;
	public static boolean dieflg_HIZUMI = false;
	public static boolean to_clearresult_flg = false;
	public static boolean to_result_flg_rock = false;
	public static boolean scroll_screen = false;
	private float syou_scale = 5.0f;
	private float ri_scale = 5.0f;
	private float line_scale = 0.0f;
	private float syou_alpha = 0.0f;
	private float ri_alpha = 0.0f;
	public static int die_enemy_num[][] = new int[Enemy_types.ENEMY_TOTAL][2];			//倒した敵の集計をする

	//敗北時
	public static boolean defeat_flg = false;
	public static boolean to_defeatresult_flg = false;

	public static boolean GameMain_flg = false;						//GameMain起動中のみtrue

	public static int select_char = 0;								//選択中キャラクター
	public static boolean[] select_flg = new boolean[5];
	int strx, stry;	//文字描画の座標
	float str_param[][] = new float[4][FontTexture.text_length];	//文字のパラメーター	0:width, 1:height, 2:offset, 3:1行のheight

	//画面レイアウト関係
	private float ic_x = 64.0f;
	private float ic_y = 768.0f;
	private float yohaku = 4.0f;	//余白
	public static boolean ic_touch_flg = false;
	private float start_spell_pos[][] =  new float[2][2];		//開始時の文字
	public static int start_state;	//スタートの状態制御
	public static int start_count;	//スタートの文字のストップカウント
	private int start_se_cnt = 0;	//スタートのSEのストップカウント

	//サウンド
	//static Sound music = new Sound();

	//
	private Map_tip[][] mMap_tip = new Map_tip[30][30];			//マップ生成
	private Map_tip map_csl;									//Mapのカーソル
	private Map_tip[] map_attak = new Map_tip[ATTACK_AREA];				//攻撃範囲表示
	private Map_tip[] map_attak2 = new Map_tip[ATTACK_AREA];				//攻撃範囲表示
	public static Status_player[] mSta_p = new Status_player[5];
	public static Status_enemy[]  mSta_e = new Status_enemy[ENEMY_MAX];
	public static Attack_Effect[] attack_efect = new Attack_Effect[PARTICLE_NUM];
	public static Die_efe_enemy[] Die_efe = new Die_efe_enemy[PARTICLE_NUM];
	public static Skill[] skill = new Skill[SKILL_NUM];				//主人公,魔法,銃,鎧,大剣,主人公・・・ の順にスキルを格納してます

	private float tColision[][] = new float[TOUCH_MAX][6];	//タッチのあたり判定
	private Map_Resource mMap_Resource;
	public static Damage_num[] mDamage = new Damage_num[30];
	public static boolean exe_stop_flg = true;



	//private long mFpsCountStartTime = System.currentTimeMillis();
	private int select_cnt = 0;

	float aspect;	// 画面比（アスペクト比）を計算
	float eyex;
	float eyez;
	int   angle; 	//回転角度

	public GameMain(Context context, int stage) {
		this.mContext = context;
		//MAP[][]を呼び出す
		mMap_Resource = new Map_Resource(stage);
		Cursor_var.x = 1;
		Cursor_var.y = 1;

		if(MainActivity.th == null ){
			MainActivity.th = new SThread(); // スレッドクラスのインスタンス作成
			MainActivity.th.start();
		}

		startNewGame();

	}

	//---------------------------------------------------------------------
	//		スタート時の初期化
	//---------------------------------------------------------------------
	public void startNewGame() {
		int i,j,k;
		GameMain_flg = true;

		mMap_Resource.load_stage(TeisuuTeigi.stage_area);						//stageをロードと同時にmap_sizeに値を入れ込む
		//↑(map_sizeの代入等)
		Create_Map(map_size_x, map_size_y);					//Mapの生成

		for(i = 0; i < ATTACK_AREA; i++)			//攻撃範囲を表示するやつを生成
			map_attak[i] = new Map_tip(0.0f, 0.0f, -1);
		for(i = 0; i < ATTACK_AREA; i++)			//攻撃範囲を表示するやつを生成
			map_attak2[i] = new Map_tip(0.0f, 0.0f, -1);

		for( i = 0; i < ENEMY_MAX; i++){
			mSta_e[i] = new Status_enemy();					//エネミー発生
		}



		for(j = 0; j < map_size_x;j++){
			for(k = 0;k < map_size_y; k++){
				if(j != 0 && k != 0 && k < map_size_y-1 && j < map_size_x-1){

					//	Log.d("","x:"+j+"y:"+i);
					switch(Map_Resource.stage_map[k-1][j-1]){
						case TeisuuTeigi.M_PLAYER1:
								mSta_p[0] = new Status_player(j, k, 0, 0);	//プレイヤー発生
								select_flg[0] = false;
							break;

						case TeisuuTeigi.M_PLAYER2:
								mSta_p[1] = new Status_player(j, k, 1, 0);	//プレイヤー発生
								select_flg[1] = false;
							break;

						case TeisuuTeigi.M_PLAYER3:
								mSta_p[2] = new Status_player(j, k, 2, 0);	//プレイヤー発生
								select_flg[2] = false;
							break;

						case TeisuuTeigi.M_PLAYER4:
								mSta_p[3] = new Status_player(j, k, 3, 0);	//プレイヤー発生
								select_flg[3] = false;
							break;

						case TeisuuTeigi.M_PLAYER5:
								mSta_p[4] = new Status_player(j, k, 4, 0);	//プレイヤー発生
								select_flg[4] = false;
							break;
					}
				}
			}
		}

		for(j = 0; j < map_size_x;j++){
			for(k = 0;k < map_size_y; k++){
				if(j != 0 && k != 0 && k < map_size_y-1 && j < map_size_x-1){

					switch(Map_Resource.stage_map[k-1][j-1]){
						case TeisuuTeigi.M_HIZUMI:
							for( i = 0; i < ENEMY_MAX; i++){
								if(!mSta_e[i].active_flg){
									mSta_e[i].Status_enemy_reset(j * 0.25f, k * 0.25f, Enemy_types.HIZUMI, 0);		//空きを探してヒヅミを生成
									break;
								}
							}
							break;

						case TeisuuTeigi.M_SLIME:
							for( i = 0; i < ENEMY_MAX; i++){
								if(!mSta_e[i].active_flg){
									mSta_e[i].Status_enemy_reset( j * 0.25f, k * 0.25f, Enemy_types.SLIME, 0);		//空きを探してスライムを生成
									break;
								}
							}
							break;

						case TeisuuTeigi.M_KINOKO:
							for( i = 0; i < ENEMY_MAX; i++){
								if(!mSta_e[i].active_flg){
									mSta_e[i].Status_enemy_reset(j * 0.25f, k * 0.25f, Enemy_types.KINOKO, 0);		//空きを探してきのこを生成
									break;
								}
							}
							break;

						case TeisuuTeigi.M_NIWATORI:
							for( i = 0; i < ENEMY_MAX; i++){
								if(!mSta_e[i].active_flg){
									mSta_e[i].Status_enemy_reset(j * 0.25f, k * 0.25f, Enemy_types.NIWATORI, 0);		//空きを探してニワトリを生成
									break;
								}
							}
							break;

						case TeisuuTeigi.M_DORAGON:
							for( i = 0; i < ENEMY_MAX; i++){
								if(!mSta_e[i].active_flg){
									mSta_e[i].Status_enemy_reset(j * 0.25f, k * 0.25f, Enemy_types.DRAGON, 0);		//空きを探してドラゴンを生成
									break;
								}
							}
							break;

						case TeisuuTeigi.M_GOBURIN:
							for( i = 0; i < ENEMY_MAX; i++){
								if(!mSta_e[i].active_flg){
									mSta_e[i].Status_enemy_reset(j * 0.25f, k * 0.25f, Enemy_types.GOBURIN, 0);		//空きを探してドラゴンを生成
									break;
								}
							}
							break;

						case TeisuuTeigi.M_SLIME2:
							for( i = 0; i < ENEMY_MAX; i++){
								if(!mSta_e[i].active_flg){
									mSta_e[i].Status_enemy_reset(j * 0.25f, k * 0.25f, Enemy_types.SLIME2, 0);		//空きを探してスライム2を生成
									break;
								}
							}
							break;

					}
				}
			}
		}
		for( i = 0; i < PARTICLE_NUM; i++)
			attack_efect[i] = new Attack_Effect(0, 0, 0, 0, 0);


		for( i = 0; i < DIE_EFE_NUM; i++){
			Die_efe[i] =new Die_efe_enemy();
		}
		for(i = 0; i < 30; i++){
			mDamage[i] = new Damage_num();
		}



		for(int index = 0; index < TOUCH_MAX; index++){

			if(index >= 1 && index <= 5){		//キャラアイコンの当たり判定
				tColision[index][0] = yohaku * index + ic_x + 32.0f;	//x
				tColision[index][1] = ic_y - yohaku;				//y
				tColision[index][1] = ic_y - yohaku-16.0f;			//y		キャラアイコンのみ16ずらす
				tColision[index][2] = 32.0f;						//width
				tColision[index][3] = 32.0f;						//height
				tColision[index][4] = TouchManagement.list.size();		//ボタンごとの番号
				tColision[index][5] = 1;			//タッチアクションフラグ(onLongPress)
				ic_x += 64.0f;
				//Log.d("tColision[index][0]","確認   "+tColision[index][0]);
			}

			if(index == 6){		//ステージセレクトアイコンの当たり判定
				tColision[index][0] = yohaku * index + ic_x + 32.0f;	//x
				tColision[index][1] = ic_y - yohaku;				//y
				tColision[index][2] = 32.0f;						//width
				tColision[index][3] = 32.0f;						//height
				tColision[index][4] = TouchManagement.list.size();		//ボタンごとの番号
				tColision[index][5] = 2;			//タッチアクションフラグ(ACTION_UP)
				ic_x += 64.0f;
			}


			//rect[0] = 0.0f; rect[1] = 0.0f; rect[2] = 128.0f; rect[3] = 64.0f; rect[4] = 128.0f / 2; rect[5] = 64.0f / 2;
			if(index == 9){		//ステージセレクトバックアイコンOKの当たり判定
				tColision[index][0] = 140.0f;						//x
				tColision[index][1] = 670.0f;						//y
				tColision[index][2] = 64.0f;						//width
				tColision[index][3] = 32.0f;						//height
				tColision[index][4] = TouchManagement.list.size();		//ボタンごとの番号
				tColision[index][5] = 0;			//タッチアクションフラグ(ACTION_UP)
			}

			if(index == 10){		//ステージセレクトバックNOの当たり判定
				tColision[index][0] = 340.0f;						//x
				tColision[index][1] = 670.0f;						//y
				tColision[index][2] = 64.0f;						//width
				tColision[index][3] = 32.0f;						//height
				tColision[index][4] = TouchManagement.list.size();		//ボタンごとの番号
				tColision[index][5] = 0;			//タッチアクションフラグ(ACTION_UP)
			}

			TouchManagement.list.add(tColision[index]);		//衝突判定の情報を格納した配列をリストとして格納する

		}

		GameInfo.t_millisecond = 0;		//ミリ秒の初期化
		GameInfo.t_second = 0;			//秒の初期化
		GameInfo.t_minute = 0;			//分の初期化
		GameInfo.play_score = 0;		//スコアの初期化
		GameInfo.char_fling = -1;		//フリック判定の値の初期化
		GameInfo.cnt_fling = 0;
		Skill.skill_cutin_flg = 0;		//スキルカットイン表示フラグ初期化
		Skill.itemdraw_flg = false;		//アイテム描画フラグ初期化
		GameMain.ic_touch_flg = false;	//
		GameInfo.back_win = -1;
		for(int skill_i = 0; skill_i < PLAYER_NUM; skill_i++){
			mSta_p[skill_i].item_timing = false;
			mSta_p[skill_i].skill_timing = false;
		}


		//倒した敵数初期化
		for(i = 0; i < die_enemy_num.length; i++){
			for( j = 0; j < 2; j++){			die_enemy_num[i][j] = 0;
			}
		}


		//スキルの生成
		for( i = 0; i < SKILL_NUM; i++){
			skill[i] =new Skill(2,4, i % 5);
			skill[i].skill__flg = 0;
			GameMain.skill[i].skill_gage_time = GameMain.skill[i].skill_time[i];
			//skill[i].skill_dir = 0;		//スキル使用時のキャラクターの向き初期化
		}

		map_csl = new Map_tip(mSta_p[0].mX, mSta_p[0].mY, 999);				//カーソル
		Cursor_var.x = mSta_p[0].mX;
		Cursor_var.y = mSta_p[0].mY;
		GameInfo.ic_flg = 1;	//キャラクターアイコンフラグ初期化
		start_state = 0;
		start_count = 0;
	}





	long now;
	//---------------------------------------------------------------------
	//		常時実行する処理
	//---------------------------------------------------------------------
	@Override
	public void onDrawFrame(GL10 gl) {

		now = System.currentTimeMillis();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glViewport(mWidthOffset, mHeightOffset, mWidth, mHeight);
		gl.glLoadIdentity();
		gl.glOrthof(-0.9f, 0.9f, -1.6f, 1.6f, 0.5f, -0.5f);
		gl.glLoadIdentity();

		back_ground(gl);
		render3D(gl);		//3D描画処理の設定
		render2D(gl);		//2D描画処理の設定
/*

		*/

	}

	/***
	 * スタート時カメラ制御等
	 */
	private void Start_Move_Camera(){

		switch(start_state){
			case 0:		//初期化
				start_spell_pos[0][0] = -150.0f;
				start_spell_pos[0][1] = 400.0f - 70.0f - 10.0f;

				start_spell_pos[1][0] = 480 + 125.0f;
				start_spell_pos[1][1] = 400.0f + 70.0f + 10.0f;

				Camera_var.x = 0;
				Camera_var.x_move = 0;
				Camera_var.y = 0;
				Camera_var.y_move = 0;

				Camera_var.x = (float) ((map_size_x / 2) * 0.25) ;
				Camera_var.y = (float) ((map_size_y / 2) * 0.25);
				start_state++;
				break;

			case 1:		//ロード待ち
				start_count++;
				if(start_count >= 100){
					start_state++;
				}
				break;

			case 2:		//文字を移動させてくる(ばとる)
				start_spell_pos[0][0] += (300.0f / 10.0f);
				if(start_spell_pos[0][0] >= 240.0f){
					start_spell_pos[0][0] = 240.0f;
					start_state++;
					start_count = 0;
				}
				break;

			case 3:		//文字を移動させてくる（すたーと）
				start_spell_pos[1][0] -= (250.0f / 10.0f);
				if(start_spell_pos[1][0] <= 240.0f){
					start_spell_pos[1][0] = 240.0f;
					start_state++;
				}
				break;

			case 4:			//曲のロードと再生
				start_count++;
				if(start_count >= 40)
				{
					start_state++;
					//各ステージBGMのロード
					switch(TeisuuTeigi.stage_area){
						case 0: case 4:
							//草原
							Sound.music.BGM_Load(mContext, R.raw.stage_sougen);
							//Sound.music.BGM_Play();
							break;

						case 1: case 5:
							//六畳一間
							Sound.music.BGM_Load(mContext, R.raw.stage_rokujohitoma);
							//Sound.music.BGM_Play();
							break;

						case 2: case 6:
							//ダンジョン
							Sound.music.BGM_Load(mContext, R.raw.stage_danjon);
							//Sound.music.BGM_Play();
							break;

						case 3: case 7:
							//？？？
							Sound.music.BGM_Load(mContext, R.raw.stage_hatena);
							break;
					}

					Sound.music.BGM_Play();
				}
				break;

			case 5:			//文字がはける
				start_spell_pos[0][0] += (300.0f / 6.0f);
				start_spell_pos[1][0] -= (250.0f / 6.0f);
				if(start_spell_pos[0][0] > 480.0f + 150.0f && start_spell_pos[1][0] < -125.0f){
					start_spell_pos[0][0] = 480.0f + 150.0f;
					start_spell_pos[1][0] = -125.0f;
					start_state++;
					start_count = 0;

				}
				break;

			case 6:
				start_count++;
				if(start_count >= 50){
					exe_stop_flg = false;
				}
				break;

		}
	}








	//---------------------------------------------------------------------
	//		常時実行する処理
	//---------------------------------------------------------------------
	private void Exe_control(){


		//スキルのカットイン
		for(int i = 0; i < SKILL_NUM; i++){
			if(GameMain.mSta_p[i % 5].skill_timing && GameMain.mSta_p[i % 5].skill_cutin_timing){
				skill[i].skill_cutin_exe( i % 5 );
			}
		}
		if(Skill.skill_cutin_flg >= 1 && Skill.skill_cutin_flg <= 5) return;	//スキルカットイン中

		int map_attack_cnt = 0;

			/*
			// アプリのメモリ情報を取得
			Runtime runtime = Runtime.getRuntime();
			Log.d("Runtime", "------------------------------------------------------------------------- ");
			// トータルメモリ
			Log.d("Runtime", "トータルメモリ[KB] = " + (int)(runtime.totalMemory()/1024));
			// 空きメモリ
			Log.d("Runtime", "空きメモリ[KB] = " + (int)(runtime.freeMemory()/1024));
			// 現在使用しているメモリ
			Log.d("Runtime", "使用中メモリ[KB] = " + (int)( (runtime.totalMemory() - runtime.freeMemory())/1024) );
			Log.d("Runtime", "-------------------------------------------------------------------------");
			*/
		//プレイヤーの実行
		for(int i = 0; i < PLAYER_NUM; i++){
			mSta_p[i].move();
		}
		//エネミーの実行
		for(int i= 0;i < ENEMY_MAX; i++ ){
			if(mSta_e[i].active_flg)
				mSta_e[i].move();
		}

		//カーソル領域制御--------------------------------------------------------
		if(Cursor_var.x + Cursor_var.x_move > ((map_size_x -2) * 0.25f) + 0.125f ){
			Cursor_var.x = ((map_size_x -2) * 0.25f);
			Cursor_var.x_move = 0;
		}

		if(Cursor_var.y + Cursor_var.y_move > ((map_size_y -2) * 0.25f) + 0.125f){
			Cursor_var.y = ((map_size_y -2) * 0.25f);
			Cursor_var.y_move = 0;

		}

		if(Cursor_var.x + Cursor_var.x_move < 0.25f ){
			Cursor_var.x = 0.25f;
			Cursor_var.x_move = 0;
		}

		if(Cursor_var.y + Cursor_var.y_move < 0.25f ){
			Cursor_var.y = 0.25f;
			Cursor_var.y_move = 0;

		}

		//カーソルの移動
		map_csl.move(Cursor_var.x + Cursor_var.x_move, Cursor_var.y + Cursor_var.y_move);
		for(int i= 0;i < ENEMY_MAX; i++ ){
			if(mSta_e[i].active_flg){
				int e_x,e_y, x, y;
				 e_x = (int)((GameMain.mSta_e[i].mX + 0.125f) / 0.25f);
				 e_y = (int)((GameMain.mSta_e[i].mY + 0.125f) / 0.25f);
				 x = (int)((Cursor_var.x + Cursor_var.x_move) / 0.25f);
				 y = (int)((Cursor_var.y + Cursor_var.y_move) / 0.25f);
				if(x ==e_x && y == e_y){
					map_csl.ene_flg = 1;
					break;
				}else{
					map_csl.ene_flg = 0;
				}
			}
		}

		for(int i = 0; i < 30; i++){
			if(mDamage[i].disp_flg){
				mDamage[i].move();
			}
		}

		keep_camera();		//カメラの移動の制御
		TouchManagement.scroll_move();	//画面端までスクロールした時のカーソル移動

		//全てのひずみ破壊時、ステージクリア画面にする--------------------------------------------------------------
		dieflg_HIZUMI = true;
		for(int i = 0; i < ENEMY_MAX; i++){
			if(mSta_e[i].Name == "ヒズミ" && mSta_e[i].active_flg)
					dieflg_HIZUMI = false;
		}

		if(dieflg_HIZUMI && !win_flg){
			win_flg = true;

			//戦闘停止
			for(int j = 0; j < ENEMY_MAX; j++){
				mSta_e[j].state_flg = false;
			}
			for(int j = 0; j < PLAYER_NUM; j++){
				mSta_p[j].state_flg = false;
				mSta_p[j].s = Charstatus.MOVE;
			}

			MainActivity.thread_run = false;
		}


		for(int i = 0;i < PARTICLE_NUM; i++)
			if(attack_efect[i].Active_flg)
				attack_efect[i].move();				//攻撃時パーティクル

		for(int i = 0; i < DIE_EFE_NUM; i++)		//死亡エフェクト
			if(Die_efe[i].active_flg)
				Die_efe[i].move();


		//攻撃範囲を表示するやつを生成
		for(int j = 0; j < PLAYER_NUM; j++){
			int play_x = (int)((GameMain.mSta_p[j].mX + 0.125f) / 0.25f);
			int play_y = (int)((GameMain.mSta_p[j].mY + 0.125f) / 0.25f);
			int csl_x  = (int)((map_csl.mX +  0.125f) / 0.25f);
			int csl_y  = (int)((map_csl.mY +  0.125f) / 0.25f);

			if(play_x == csl_x && play_y == csl_y){		//カーソルとプレイヤーが被った
				for(int i = 0; i < 8; i++){			//8方向
					for(int k = 0; k < mSta_p[j].attak_map[mSta_p[j].No].length; k++){	//ひとそれぞれの攻撃範囲

						int map_attak_x = play_x + mSta_p[j].attak_map[mSta_p[j].No][k][0][i];	//マップアタックの表示させるｘ
						int map_attak_y = play_y + mSta_p[j].attak_map[mSta_p[j].No][k][1][i];	//マップアタックの表示させるｙ

						//Log.d("","map_attak_x"+map_attak_x+",map_attak_y"+map_attak_y+"map_attack_cnt"+map_attack_cnt);
						map_attak[map_attack_cnt].attac_move(map_attak_x, map_attak_y);
						if(!map_attak[map_attack_cnt].attack_Active_flg || map_attak[map_attack_cnt].play_num != j){
							map_attak[map_attack_cnt].attack_tenmetu = 1.0f;
						}
						map_attak[map_attack_cnt].attack_Active_flg = true;
						map_attak[map_attack_cnt].play_num = j;


						map_attak2[map_attack_cnt].attac_move(map_attak_x, map_attak_y);
						if(!map_attak2[map_attack_cnt].attack_Active_flg || map_attak2[map_attack_cnt].play_num != j){
							map_attak2[map_attack_cnt].attack_tenmetu = 1.0f;
						}
						map_attak2[map_attack_cnt].attack_Active_flg = true;
						map_attak2[map_attack_cnt].play_num = j;

						map_attack_cnt++;
					}
				}
				break;
			}
		}
		for(; map_attack_cnt< ATTACK_AREA; map_attack_cnt++){
			map_attak[map_attack_cnt].attack_Active_flg = false;
			map_attak2[map_attack_cnt].attack_Active_flg = false;
		}

		map_attack_cnt = 0;


		//各キャラクタースキル実行
		for(int i = 0; i < SKILL_NUM; i++){
			skill[i].skill_move(i % 5, i);
		}

		//フリック入力操作のタメ
		if(GameInfo.char_fling >= 1 && GameInfo.char_fling <= 5){
			GameInfo.cnt_fling++;		//フレームをカウント
		}


		if(Skill.itemdraw_flg){
			Skill.recovery_move();
		}

		if(GameInfo.ic_flg != 6 && GameInfo.ic_flg != 7){	//キャラクターアイコンをタッチしている時
			if(GameMain.mSta_p[GameInfo.ic_flg -1].active_flg)	//死んでたら使えない
				Skill.item_char();	//アイテムの実行
		}





	}


	//---------------------------------------------------------------------
	//		背景
	//---------------------------------------------------------------------
	public void  back_ground(GL10 gl){
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
			gl.glTranslatef(-0.0f, -0.0f,  1.0f);		//座標位置
			gl.glScalef(1.0f, 1.0f, 0.0f);			//画像の大きさ指定
			GraphicUtil.drawRectangle(gl, 0, 0, 1.8f, 3.8f, 0.0f, 0.552f, 0.95f, 1.0f);

		}
		gl.glPopMatrix();



	}


	//---------------------------------------------------------------------
	//		3D描画
	//---------------------------------------------------------------------
	public void render3D(GL10 gl) {

		float cameraX = Camera_var.x + Camera_var.x_move;
		float cameraY = Camera_var.y + Camera_var.y_move;
		//Camera_var.angle++;
		float eyeX = (float)Math.sin(Math.PI / 180.0f * Camera_var.angle) * 1.6f + cameraX;
		float eyeY = (float)Math.cos(Math.PI / 180.0f * Camera_var.angle) * 1.6f + cameraY;

		// 3D描画用に座標系を設定します
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl,50.0f, aspect, 0.1f, 100.0f);

		//glViewportに合わせる
		GLU.gluLookAt(gl,
				eyeX,		//eye
				eyeY,
				2.5f,
				cameraX,	//look
				cameraY,
				0.0f,
				-0.0f, 0.0f, 1.0f);	//up

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		// ここから深度テストが有効になるようにします

		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);	//透過の設定
		gl.glEnable(GL10.GL_CULL_FACE);		//カリングをオフに
		gl.glAlphaFunc(GL10.GL_GREATER, 0.2f);	//透過の描画具合の設定
		gl.glEnable(GL10.GL_DEPTH_TEST);		//奥行きによる描画の制御
		gl.glEnable(GL10.GL_ALPHA_TEST);		//透過による描画の制御
		gl.glEnable(GL10.GL_BLEND);				//透過を使えるようにする



		float  mask_x = cameraX+0.375f,mask_y = cameraY-0.375f;
		float syahen;



		//map_tip描画
		for(int y = 0; y < map_size_y;y++){
			for(int x = 0; x < map_size_x; x++){

				//Log.d("斜辺",""+syahen);
				if(Math.abs(mMap_tip[y][x].mX - (mask_x)) < 1.875f &&
					Math.abs( mMap_tip[y][x].mY - (mask_y)) < 1.75f)
				{
					//syahen = (float) Math.sqrt(Math.pow(mMap_tip[y][x].mX- cameraX,2)+ Math.pow((mMap_tip[y][x].mY-cameraY) ,2));
					//syahen = (float) Math.sqrt(((mMap_tip[y][x].mX- cameraX) * (mMap_tip[y][x].mX-cameraX))+((mMap_tip[y][x].mY-cameraY) * ( mMap_tip[y][x].mY-cameraY)));
					syahen = sqrt2((float) (Math.pow((mMap_tip[y][x].mX - mask_x), 2)+ Math.pow((mMap_tip[y][x].mY-(mask_y+0.25)), 2)));
					//syahen = sqrt2(((mMap_tip[y][x].mX- cameraX) * (mMap_tip[y][x].mX-cameraX))+((mMap_tip[y][x].mY-cameraY) * ( mMap_tip[y][x].mY-cameraY)));
					if( syahen < 2f)		//描画範囲縮小カメラに写ってないところは写さない
					{
						mMap_tip[y][x].draw(gl, map_Texture);

						if(mMap_tip[y][x].snag_num != 0){		//障害物描画
							switch(mMap_tip[y][x].snag_num)
							{
								//マイナスは柵の表示
								case -4:
									mMap_tip[y][x].draw_sak(gl, sak, 3);
									mMap_tip[y][x].draw_snag(gl, mGrass2,0);
									break;

								case -3:
									mMap_tip[y][x].draw_sak(gl, sak, 2);
									mMap_tip[y][x].draw_snag(gl, mGrass2,0);
									break;

								case -2:
									mMap_tip[y][x].draw_sak(gl, sak, 1);
									mMap_tip[y][x].draw_snag(gl, mGrass2,0);
									break;

								case -1:
									mMap_tip[y][x].draw_sak(gl, sak, 0);
									mMap_tip[y][x].draw_snag(gl, mGrass2,0);
									break;



								case 1:
									mMap_tip[y][x].draw_snag(gl, mIwa,0);
									break;

								case 2:
									mMap_tip[y][x].draw_snag(gl, mGrass,0);
									break;

								case 3:
									mMap_tip[y][x].draw_snag(gl, mGrass2,0);
									break;

								case 4:
									mMap_tip[y][x].draw_snag(gl, syougai1,1);
									break;

								case 5:
									mMap_tip[y][x].draw_snag(gl, syougai2,1);
									break;

								case 6:
									mMap_tip[y][x].draw_snag(gl, syougai3,1);
									break;

								case 7:
									mMap_tip[y][x].draw_snag(gl, syougai4,1);
									break;
							}


							//柵の表示(四隅)
							if(y == 0 && x == map_size_x-1){
								mMap_tip[y][x].draw_sak(gl, sak, 1);		//左上
							}
							if(y == map_size_y-1 && x == map_size_x-1){		//左下
								mMap_tip[y][x].draw_sak(gl, sak, 3);
							}
							if(y == 0 && x == 0){							//右下
								mMap_tip[y][x].draw_sak(gl, sak, 0);
							}
							if(y == map_size_y-1 && x == 0){				//右上
								mMap_tip[y][x].draw_sak(gl, sak, 0);
							}
						}
					}
				}
			}
		}




		for(int i = 0; i < PLAYER_NUM; i++){

			if(Math.abs( mSta_p[i].mX - mask_x) < 1.875f &&
					Math.abs(mSta_p[i].mY - mask_y) < 1.75f)		//描画範囲縮小カメラに写ってないところは写さない
			{
				syahen = sqrt2((float) (Math.pow((mSta_p[i].mX  - mask_x), 2)+ Math.pow((mSta_p[i].mY-mask_y+0.25), 2)));
				if(syahen < 2)
				{
					mSta_p[i].draw(gl, mPlayer[mSta_p[i].No]);				//主人公の描画
					mSta_p[i].shadow_draw(gl,mKage);
				}
			}
		}

		for(int i = 0; i < ENEMY_MAX; i++){
			if(mSta_e[i].active_flg){
				if(Math.abs( mSta_e[i].mX - mask_x) < 1.875f &&
					Math.abs(mSta_e[i].mY - mask_y) < 1.75f)		//描画範囲縮小カメラに写ってないところは写さない
				{
					syahen = sqrt2((float) (Math.pow((mSta_e[i].mX  - mask_x), 2)+ Math.pow((mSta_e[i].mY-mask_y+0.25), 2)));
					if(syahen < 2)
					{
						mSta_e[i].draw(gl,mEnemy[mSta_e[i].texture], i);

					}
				}
			}
		}

		for(int i = 0; i < 30; i++){
			if(mDamage[i].disp_flg){
				mDamage[i].draw(gl);
			}
		}

		map_csl.draw(gl, csl_Texture);								//カーソルの描画。

		for(int i = 0; i < 5; i++) 		//主人公たち全員分
		{
			if(GameMain.select_flg[i] && mSta_p[i].active_flg){
				select_cnt = 1;
				break;
			}
			if(i == 4 )
				select_cnt = 0;
		}

		if(Map_Resource.map[(int)((map_csl.mY + 0.125f) / 0.25f)][(int)((map_csl.mX + 0.125f) / 0.25f)] == TeisuuTeigi.M_SNAG )
		{
			select_cnt = 0;
		}

		if(select_cnt == 1 )
			map_csl.draw_csl(gl, csl_Texture2);

		for(int i = 0; i < PARTICLE_NUM; i++)
		{
				if(Math.abs( attack_efect[i].mX - mask_x) < 1.875f &&
					Math.abs(attack_efect[i].mY - mask_y) < 1.75f)		//描画範囲縮小カメラに写ってないところは写さない
				{
					syahen = sqrt2((float) (Math.pow((attack_efect[i].mX  - mask_x), 2)+ Math.pow((attack_efect[i].mY-mask_y+0.25), 2)));
					if(syahen < 2){
						if(attack_efect[i].Active_flg)
							attack_efect[i].draw(gl, mParticle);				//攻撃時パーティクル
				}
			}

		}


		for(int i = 0; i < DIE_EFE_NUM; i++)						//死亡エフェクト
			if(Die_efe[i].active_flg)
				Die_efe[i].draw(gl, suya_tex);

		for(int i = 0; i < ATTACK_AREA; i++)									//攻撃範囲表示
			if(map_attak2[i].attack_Active_flg)
				map_attak2[i].attack_Area_drow2(gl, attack_area_tex);

		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		for(int i = 0; i < ATTACK_AREA; i++)									//攻撃範囲表示
			if(map_attak[i].attack_Active_flg)
				map_attak[i].attack_Area_drow(gl, attack_area_tex);

		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);


		//スキル描画
		for(int i = 0; i < SKILL_NUM; i++){
			if(GameMain.mSta_p[i % 5].skill_timing && !GameMain.mSta_p[i % 5].skill_cutin_timing){
				skill[i].skill_draw(gl, skill_efect_tex[i], i % 5, i );
			}
		}


		if(GameInfo.ic_flg != 6 &&GameInfo.ic_flg != 7){	//キャラクターアイコンをタッチしている時
			if(GameMain.mSta_p[GameInfo.ic_flg -1].active_flg){		//死んでたら使えない
				if(Skill.itemdraw_flg)
					Skill.recovery_draw(gl, heart_tex);	//回復アイテム描画
			}

		}
	}


	//---------------------------------------------------------------------
	//		2D描画
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


		//画面のインフォメーションを表示
		mGameInfo.run(gl);



		if(exe_stop_flg){

			gl.glPushMatrix();
			{
				GraphicUtil.glTranslatef_pixel(gl, start_spell_pos[0][0], start_spell_pos[0][1], -0.0f);		//座標位置
				GraphicUtil.drawTexture_pixel_custom(gl, 512.0f, 256.0f,  start_spell, new float[]{0.0f, 0.0f, 300.0f, 70.0f, 150.0f, 35.0f},
													1.0f, 1.0f, 1.0f, 1.0f);
			}
			gl.glPopMatrix();

			gl.glPushMatrix();
			{
				GraphicUtil.glTranslatef_pixel(gl, start_spell_pos[1][0], start_spell_pos[1][1], -0.0f);		//座標位置
				GraphicUtil.drawTexture_pixel_custom(gl, 512.0f, 256.0f,  start_spell, new float[]{0.0f, 70.0f, 250.0f, 70.0f, 125.0f, 35.0f},
													1.0f, 1.0f, 1.0f, 1.0f);
			}
			gl.glPopMatrix();
		}



		//スキルのカットイン描画
		for(int i = 0; i < SKILL_NUM; i++){
			if(GameMain.mSta_p[i % 5].skill_timing && GameMain.mSta_p[i % 5].skill_cutin_timing){
				skill[i].skill_cutin_draw(gl, skill_cutin_tex, i % 5 );
			}
		}
		//Log.d("Skill.skill_cutin_flg","Skill.skill_cutin_flg:"+Skill.skill_cutin_flg);


		//文字の描画
		/*
*/

		//ステータス文字とステージ名の描画
		//stry = 660;
		/*for(int i = 0; i < FontTexture.text_length; i++){
			strx = 50;
			for(int j = 0; j < 5; j++){
				FontTexture.text_fTexture.draw_str(gl, str_Texture, 0.5f, strx, stry,str_param[2][i],str_param[0][i],str_param[1][i]);
				strx += 80;
			}
			stry +=str_param[3][i] * 0.5f;
		}*/
		//ステータス項目の表示
		//FontTexture.text_fTexture.draw_str(gl, str_Texture, 0.5f, 0, 0,str_param[2][0],str_param[0][0],str_param[1][0]);

		//アイコンの表示
		//余白4ピクセル
		ic_x = 64.0f;
		for(int i = 1; i <= 6; i++){
			//キャラアイコンの表示
			if( i >= 1 && i <= 5){

				gl.glPushMatrix();
				{
					GraphicUtil.glTranslatef_pixel(gl, yohaku * i + ic_x + 32.0f, ic_y - yohaku-16.0f, -0.0f);		//座標位置
					gl.glScalef(2.0f, 2.0f, 1.0f);	//画像の大きさ指定

					GraphicUtil.drawTexture_pixel_custom(gl, 512.0f, 64.0f,  character_ic, new float[]{64.0f * (i - 1) + 16.0f, 6.0f, 32.0f, 32.0f, 16.0f, 16.0f},
														1.0f, 1.0f, 1.0f, 1.0f);
				}
				gl.glPopMatrix();


				//キャラアイコン背景
				gl.glPushMatrix();
				{
					GraphicUtil.glTranslatef_pixel(gl, yohaku * i + ic_x + 32.0f, ic_y - yohaku-16.0f, 0.01f);		//座標位置
					gl.glScalef(2.0f, 2.0f, 1.0f);	//画像の大きさ指定

					GraphicUtil.drawTexture_pixel_custom(gl, 32.0f, 32.0f, flicker_tex, new float[]{0.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},
							GameInfo.char_col[i-1][0],GameInfo.char_col[i-1][1], GameInfo.char_col[i-1][2], 1.0f);
				}
				gl.glPopMatrix();

			}

			//バックアイコンの表示
			if( i == 6){
				gl.glPushMatrix();
				{
					GraphicUtil.glTranslatef_pixel(gl, yohaku * i + ic_x + 32.0f, ic_y - yohaku, -0.0f);		//座標位置
					gl.glScalef(2.0f, 2.0f, 1.0f);	//画像の大きさ指定

					GraphicUtil.drawTexture_pixel_custom(gl, 32.0f, 32.0f, back_ic, new float[]{0.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},
													1.0f,1.0f, 1.0f, 1.0f);
				}
				gl.glPopMatrix();
			}

			ic_x += 64.0f;
		}


		//どのアイコンに触れているか判定して、
		//アイコンごとに処理を行う
		mGameInfo.ic_process(gl, new int[]{skill_ic, item_ic});


		//勝利時アニメーション
		if(win_flg){
			if(Sound.music.getDataId() != R.raw.victory){
				//BGMのストップ
				Log.d("BGM","null");
				if(Sound.music.mp != null){
					if(Sound.music.mp.isPlaying()){		//BGMが生成中かチェック
						Sound.music.BGM_Stop();
			        }
				}

				//勝利BGMのロード
				if(Sound.music.sp != null){
					Sound.music.BGM_Load(mContext, R.raw.victory);
					Sound.music.BGM_notLoop();
				}

			}

			syou_scale -= 0.08f;
			if(syou_scale < 1.5f){
				syou_scale = 1.5f;
			}
			if(syou_scale == 1.5f){
				ri_scale -= 0.08f;
			}
			if(ri_scale < 1.5f){
				ri_scale = 1.5f;
				if(!to_result_flg_rock)
					to_clearresult_flg = true;
				to_result_flg_rock = true;
			}

			syou_alpha = TouchManagement.atai_control((int)(syou_scale*100), 500, 150, 0.0f, 1.0f );
			ri_alpha = TouchManagement.atai_control((int)(ri_scale*100), 500, 150, 0.0f, 1.0f );

			//勝
			gl.glPushMatrix();
			{
				GraphicUtil.glTranslatef_pixel(gl, 240.0f, 130.0f, 0.0f);		//座標位置
				gl.glScalef( syou_scale, syou_scale, 1.0f);									//画像の大きさ指定
				GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 256.0f, mWin, new float[]{0.0f,0.0f,250.0f, 250.0f/2,250.0f / 2, 250.0f /2.0f / 2 / 2},
												1.0f,1.0f, 1.0f, syou_alpha);
			}
			gl.glPopMatrix();
			//利
			gl.glPushMatrix();
			{
				GraphicUtil.glTranslatef_pixel(gl, 240.0f, 363.0f, 0.0f);		//座標位置
				gl.glScalef(ri_scale, ri_scale, 1.0f);									//画像の大きさ指定
				GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 256.0f, mWin, new float[]{0.0f,250.0f /2,250.0f, 250.0f / 2, 250.0f / 2, 250.0f / 2 / 2},
												1.0f,1.0f, 1.0f, ri_alpha);
			}
			gl.glPopMatrix();
		}

		//敗北時アニメーション
		if(defeat_flg){

			MainActivity.thread_run = false;
			if(Sound.music.getDataId() != R.raw.lose){
				//BGMのストップ
				if(Sound.music.mp.isPlaying()){		//BGMが生成中かチェック
					Sound.music.BGM_Stop();
		        }

				//敗北BGMのロード
				Sound.music.BGM_Load(mContext, R.raw.lose);
				Sound.music.BGM_notLoop();

			}


			syou_scale -= 0.08f;
			if(syou_scale < 1.5f){
				syou_scale = 1.5f;
			}
			if(syou_scale == 1.5f){
				ri_scale -= 0.08f;
			}
			if(ri_scale < 1.5f){
				ri_scale = 1.5f;
			}
			if(ri_scale == 1.5f){
				line_scale += 0.05f;
			}
			if(line_scale >= 1.0f){
				line_scale = 1.0f;
				if(!to_result_flg_rock)
					to_defeatresult_flg = true;
				to_result_flg_rock = true;
			}
			syou_alpha = TouchManagement.atai_control((int)(syou_scale*100), 500, 150, 0.0f, 1.0f );
			ri_alpha = TouchManagement.atai_control((int)(ri_scale*100), 500, 150, 0.0f, 1.0f );

			//敗
			gl.glPushMatrix();
			{
				GraphicUtil.glTranslatef_pixel(gl, 240.0f, 130.0f, 0.0f);		//座標位置
				gl.glScalef( syou_scale, syou_scale, 1.0f);									//画像の大きさ指定
				GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 256.0f, mDefeat, new float[]{0.0f,0.0f,250.0f, 250.0f/2,250.0f / 2, 250.0f /2.0f / 2 / 2},
												1.0f,1.0f, 1.0f, syou_alpha);
			}
			gl.glPopMatrix();
			//北tensiondown_tex
			gl.glPushMatrix();
			{
				GraphicUtil.glTranslatef_pixel(gl, 240.0f, 363.0f, 0.0f);		//座標位置
				gl.glScalef(ri_scale, ri_scale, 1.0f);									//画像の大きさ指定
				GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 256.0f, mDefeat, new float[]{0.0f,250.0f /2,250.0f, 250.0f / 2, 250.0f / 2, 250.0f / 2 / 2},
												1.0f,1.0f, 1.0f, ri_alpha);
			}
			gl.glPopMatrix();
			gl.glPushMatrix();
			{
				GraphicUtil.glTranslatef_pixel(gl, 240.0f, 130.0f, 0.0f);		//座標位置
				gl.glScalef( 1.0f, line_scale*10, 1.0f);									//画像の大きさ指定
				GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 256.0f, tensiondown_tex, new float[]{0.0f,0.0f,250.0f,256.0f, 250.0f / 2,250.0f / 2},
												1.0f,1.0f, 1.0f, syou_alpha);
			}
			gl.glPopMatrix();
		}





		// FPSを表示する
		/*
		if (Global.isDebuggable) {
			//nowTime = System.currentTimeMillis();		// 現在時間を取得
			long difference = nowTime - mFpsCountStartTime;		// 現在時間との差分を計算
			if (difference >= 1000) {		// 1秒経過していた場合は、フレーム数のカウント終了
				mFps = mFramesInSecond;
				mFramesInSecond = 0;
				mFpsCountStartTime = nowTime;
			}
			mFramesInSecond++;// フレーム数をカウント

			FontTexture.text_fTexture.draw_num2(gl, GameMain.strnum_Texture, 1.0f,
					400, 32,
					mFps, GameInfo.num_str_param[2],
					1.0f, 1.0f, 1.0f, 1.0f);
		}
		*/
	}


	//-------------------------------------------------------------------------
	//				マップ生成
	//-------------------------------------------------------------------------
	private void Create_Map(int map_size_x,int map_size_y){
		int i,j;
		float x,y;

		for(i = 0; i < map_size_y ;i++){
			for(j = 0; j < map_size_x; j++){
				x = 0.25f * j -0.0f;
				y = 0.25f * i -0.0f;

				switch(TeisuuTeigi.stage_area){

					case 1:	case 5:	//六畳一間
						if(i != 0 && j != 0 && i < map_size_y-1 && j < map_size_x-1){

							//	Log.d("","x:"+j+"y:"+i);

								switch(Map_Resource.stage_map[i-1][j-1]){
									case -1:		//石の障害物
										mMap_tip[i][j] = new Map_tip(x,y,0);
										mMap_tip[i][j].snag_num = 1;
										Map_Resource.map[i][j] = TeisuuTeigi.M_SNAG;
										break;

									case -2:		//机
										mMap_tip[i][j] = new Map_tip(x,y,14);
										mMap_tip[i][j].snag_num = 4;
										Map_Resource.map[i][j] = TeisuuTeigi.M_SNAG;
										break;


									case 0:	 case 1:  case 2:  case 3:  case 4:
									case 5:	 case 6:  case 7:  case 8:  case 9:
									case 10: case 11: case 12: case 13: case 14:
									case 15: case 16: case 17:
									case 19: //19は座布団
										//畳
										mMap_tip[i][j] = new Map_tip(x,y,Map_Resource.stage_map[i-1][j-1]+1);
										break;

									case 100: case 101:		//主人公たち
									case 102: case 103:
										mMap_tip[i][j] = new Map_tip(x,y,5);
										break;

									case 104:				//主人公たち
										mMap_tip[i][j] = new Map_tip(x,y,6);
										break;

									default:		//
										mMap_tip[i][j] = new Map_tip(x,y,19);
										break;
								}
							}else
							{		//柵の表示
								if(i == 0){		//y
									mMap_tip[i][j] = new Map_tip(x,y, 0);
									mMap_tip[i][j].snag_num = -2;
								}
								else if(j == 0){	//x
									mMap_tip[i][j] = new Map_tip(x,y, 0);
									mMap_tip[i][j].snag_num = -1;
								}

								if(i == map_size_y-1){	//y
									mMap_tip[i][j] = new Map_tip(x,y, 0);
									mMap_tip[i][j].snag_num = -3;
								}
								else if(j == map_size_x-1){	//x
									mMap_tip[i][j] = new Map_tip(x,y, 0);
									mMap_tip[i][j].snag_num = -4;
								}

							}
					break;

					case 2:	case 6:	//ダンジョン
						if(i != 0 && j != 0 && i < map_size_y-1 && j < map_size_x-1){

						//	Log.d("","x:"+j+"y:"+i);

							switch(Map_Resource.stage_map[i-1][j-1]){
								case -1:		//石の障害物
									mMap_tip[i][j] = new Map_tip(x,y,5);
									mMap_tip[i][j].snag_num = 1;
									Map_Resource.map[i][j] = TeisuuTeigi.M_SNAG;
									break;

								case -2:		//壊れた柱の障害物
									mMap_tip[i][j] = new Map_tip(x,y,5);
									mMap_tip[i][j].snag_num = 4;
									Map_Resource.map[i][j] = TeisuuTeigi.M_SNAG;
									break;

								case -3:		//球体の障害物
									mMap_tip[i][j] = new Map_tip(x,y,5);
									mMap_tip[i][j].snag_num = 5;
									Map_Resource.map[i][j] = TeisuuTeigi.M_SNAG;
									break;

								case -4:		//像の障害物
									mMap_tip[i][j] = new Map_tip(x,y,5);
									mMap_tip[i][j].snag_num = 6;
									Map_Resource.map[i][j] = TeisuuTeigi.M_SNAG;
									break;

								case -5:
									mMap_tip[i][j] = new Map_tip(x,y,5);
									mMap_tip[i][j].snag_num = 7;
									Map_Resource.map[i][j] = TeisuuTeigi.M_SNAG;
									break;

								case 0:	//草原
									mMap_tip[i][j] = new Map_tip(x,y,0);
									break;

								case 1:	//花
									mMap_tip[i][j] = new Map_tip(x,y,1);
									break;

								case 2:	//草1
									mMap_tip[i][j] = new Map_tip(x,y,0);
									mMap_tip[i][j].snag_num = 2;
									break;

								case 3:	//草2
									mMap_tip[i][j] = new Map_tip(x,y,0);
									mMap_tip[i][j].snag_num = 3;
									break;

							  case TeisuuTeigi.M_HIZUMI:	//ひずみ
									mMap_tip[i][j] = new Map_tip(x,y,6);
									break;

								default:		//草原
									mMap_tip[i][j] = new Map_tip(x,y,0);
									break;
							}
						}else
						{		//柵の表示
							if(i == 0){		//y
								mMap_tip[i][j] = new Map_tip(x,y, 0);
								mMap_tip[i][j].snag_num = -2;
							}
							else if(j == 0){	//x
								mMap_tip[i][j] = new Map_tip(x,y, 0);
								mMap_tip[i][j].snag_num = -1;
							}

							if(i == map_size_y-1){	//y
								mMap_tip[i][j] = new Map_tip(x,y, 0);
								mMap_tip[i][j].snag_num = -3;
							}
							else if(j == map_size_x-1){	//x
								mMap_tip[i][j] = new Map_tip(x,y, 0);
								mMap_tip[i][j].snag_num = -4;
							}

						}
						break;

					case 3: case 7: //??
						if(i != 0 && j != 0 && i < map_size_y-1 && j < map_size_x-1){

								switch(Map_Resource.stage_map[i-1][j-1]){
								//	Log.d("","x:"+j+"y:"+i);
								case -2:		//石の障害物
									mMap_tip[i][j] = new Map_tip(x,y,1);
									mMap_tip[i][j].snag_num = 4;
									Map_Resource.map[i][j] = TeisuuTeigi.M_SNAG;
									break;

								case -3:		//石の障害物
									mMap_tip[i][j] = new Map_tip(x,y,0);
									mMap_tip[i][j].snag_num = 5;
									Map_Resource.map[i][j] = TeisuuTeigi.M_SNAG;
									break;

								case 0:	//草原
									mMap_tip[i][j] = new Map_tip(x,y,0);
									break;

								case 1:	//草原
									mMap_tip[i][j] = new Map_tip(x,y,1);
									break;

								default:		//
									mMap_tip[i][j] = new Map_tip(x,y,0);
									break;
								}
							}else
							{		//柵の表示
								if(i == 0){		//y
									mMap_tip[i][j] = new Map_tip(x,y, 0);
									mMap_tip[i][j].snag_num = -2;
								}
								else if(j == 0){	//x
									mMap_tip[i][j] = new Map_tip(x,y, 0);
									mMap_tip[i][j].snag_num = -1;
								}

								if(i == map_size_y-1){	//y
									mMap_tip[i][j] = new Map_tip(x,y, 0);
									mMap_tip[i][j].snag_num = -3;
								}
								else if(j == map_size_x-1){	//x
									mMap_tip[i][j] = new Map_tip(x,y, 0);
									mMap_tip[i][j].snag_num = -4;
								}

							}
						break;

					default:
						if(i != 0 && j != 0 && i < map_size_y-1 && j < map_size_x-1){

						//	Log.d("","x:"+j+"y:"+i);

							switch(Map_Resource.stage_map[i-1][j-1]){
								case -1:		//石の障害物
									mMap_tip[i][j] = new Map_tip(x,y,5);
									mMap_tip[i][j].snag_num = 1;
									Map_Resource.map[i][j] = TeisuuTeigi.M_SNAG;
									break;

								case 0:	//草原
									mMap_tip[i][j] = new Map_tip(x,y,0);
									break;

								case 1:	//花
									mMap_tip[i][j] = new Map_tip(x,y,1);
									break;

								case 2:	//草1
									mMap_tip[i][j] = new Map_tip(x,y,0);
									mMap_tip[i][j].snag_num = 2;
									break;

								case 3:	//草2
									mMap_tip[i][j] = new Map_tip(x,y,0);
									mMap_tip[i][j].snag_num = 3;
									break;

							  case TeisuuTeigi.M_HIZUMI:	//ひずみ
									mMap_tip[i][j] = new Map_tip(x,y,6);
									break;

								default:		//草原
									mMap_tip[i][j] = new Map_tip(x,y,0);
									break;
							}
						}else
						{		//柵の表示
							if(i == 0){		//y
								mMap_tip[i][j] = new Map_tip(x,y, 0);
								mMap_tip[i][j].snag_num = -2;
							}
							else if(j == 0){	//x
								mMap_tip[i][j] = new Map_tip(x,y, 0);
								mMap_tip[i][j].snag_num = -1;
							}

							if(i == map_size_y-1){	//y
								mMap_tip[i][j] = new Map_tip(x,y, 0);
								mMap_tip[i][j].snag_num = -3;
							}
							else if(j == map_size_x-1){	//x
								mMap_tip[i][j] = new Map_tip(x,y, 0);
								mMap_tip[i][j].snag_num = -4;
							}

						}
					break;
				}
			}
		}

	}


	//---------------------------------------------------------------------------------
	//			カメラの減速移動のプログラム
	//---------------------------------------------------------------------------------
	private void keep_camera(){
		float sx,sy;
		int spd = 2;

		if(Math.abs(Camera_var.x  - (Cursor_var.x + Cursor_var.x_move)) > 0.0625f){
			sx = Camera_var.x - (Cursor_var.x + Cursor_var.x_move);	//まず、差を算出

			if ( Math.abs(sx) < 0.05f){
				Camera_var.x = (Cursor_var.x + Cursor_var.x_move);
			}

			sx = sx / (1 + spd);
			Camera_var.x -= sx;
		}
		if(Math.abs(Camera_var.y -(Cursor_var.y + Cursor_var.y_move)) > 0.0625f){
			sy = Camera_var.y  -(Cursor_var.y + Cursor_var.y_move);

			if (  Math.abs(sy) < 0.05f){
				Camera_var.y = (Cursor_var.y + Cursor_var.y_move);
			}
			sy = sy / (1 + spd);
			Camera_var.y -= sy;

		}
	}







	@Override
	/***
	 * 		描画範囲を設定
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		int w = 0;
		int h = 0;

		//常に16:9で描画する
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

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		loadTextures(gl);		//テクスチャをロードする
		loadSound();			//サウンドをロードする

	}


	/***
	 * テクスチャロード
	 * @param gl
	 */@SuppressWarnings("static-access")

	public void loadTextures(GL10 gl) {

		Resources res = mContext.getResources();
		switch(TeisuuTeigi.stage_area){
			case 0: case 4:		//草原
				this.map_Texture     = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_map_tip0);			//マップ画像-
				this.sak 			 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_sak0);				//柵の画像
				this.mGrass 		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_s_grass);				//草1
				this.mGrass2 		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_s_grass2);			//草2
				this.mIwa			 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_s_iwa_tes);			//岩の画像
				break;

			case 1:	case 5:	//六畳一間
				this.map_Texture     = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_map_tip1);			//マップ画像-
				this.sak 			 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_sak1);				//ふすまの画像
				this.mGrass2 		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_s_kumo);				//蜘蛛の巣
				this.syougai1		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_s_rokujyo_syougai1);	//机
				break;

			case 2:	case 6:	//ダンジョン
				this.map_Texture     = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_map_tip2);			//マップ画像-
				this.sak 			 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_sak2);				//柵の画像
				this.mGrass 		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_s_grass);				//草1
				this.mGrass2 		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_s_object);			//草2
				this.syougai1		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_s_syougai1);			//柱
				this.syougai2		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_s_syougai2);			//壊れた柱
				this.syougai3		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_s_syougai3);			//柱の上になにか載ってる奴
				this.syougai4		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_s_syougai4);			//像
				break;

			case 3:	case 7:	//？？？
				this.map_Texture     = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_map_tip3);			//マップ画像-
				this.sak 			 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_sak3);				//柵の画像
				this.syougai1		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_s_red);				//aka
				this.syougai2		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_s_blue);				//ao
				this.mGrass2 		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_s_red);				//草2
				break;

			default:
				this.map_Texture     = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_map_tip0);			//マップ画像-
				this.sak 			 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_sak0);				//柵の画像
				this.mIwa			 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_s_iwa_tes);			//岩の画像
				this.mGrass 		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_m_s_grass);				//草1

				break;


		}

		this.mEnemy[0] 		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_e_distortion);			//ひずみ
		this.mEnemy[1] 		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_e_enemy);				//スライム
		this.mEnemy[2] 		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_e_kinoko);				//きのこ
		this.mEnemy[3] 		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_e_niwatori);			//にわとり
		this.mEnemy[4] 		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_e_dragon);				//ドラゴン
		this.mEnemy[5] 		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_e_goburin);				//ゴブリン
		this.mEnemy[6]		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_e_enemy2);				//スライム2
		this.csl_Texture 	 = GraphicUtil.loadTexture(gl, res, R.drawable.game_o_csl);					//カーソル
		this.csl_Texture2	 = GraphicUtil.loadTexture(gl, res, R.drawable.game_o_csl_2);					//カーソルアイコン

		//主人公たち-------------------------------------------------------------
		this.mPlayer[0] 	 = GraphicUtil.loadTexture(gl, res, R.drawable.game_c_hero);				//主人公
		this.mPlayer[1] 	 = GraphicUtil.loadTexture(gl, res, R.drawable.game_c_magi);				//魔法使い
		this.mPlayer[2] 	 = GraphicUtil.loadTexture(gl, res, R.drawable.game_c_gunner);				//銃使い
		this.mPlayer[3] 	 = GraphicUtil.loadTexture(gl, res, R.drawable.game_c_armor);				//やり使い（よろい）
		this.mPlayer[4] 	 = GraphicUtil.loadTexture(gl, res, R.drawable.game_c_large_sword);			//大剣使い
		//----------------------------------------------------------------------
		this.character_ic 	 = GraphicUtil.loadTexture(gl, res, R.drawable.game_c_char_icon);			//キャラクターアイコン
		this.heart_tex 		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_efe_heart);				//回復アイテムのエフェクト
		this.suya_tex 		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_efe_suya);				//死亡エフェクト
		this.mParticle		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_efe_particle);			//攻撃パーティクル


		this.flicker_tex 	     = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_flicker);		//点滅
		this.userinfo_tex 	     = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_userinfo);		//上のUI
		this.window_tex		     = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_board);			//板
		this.back_ic 		     = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_back_icon);		//バックアイコ
		this.mahoflame_Texture   = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_maho_flame);		//魔法火柱
		this.item_ic 		     = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_item_icon);		//アイテムアイコン
		this.skill_ic 		     = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_skill_icon);		//スキルアイコン
		this.mGauge 		     = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_gauge);			//ゲージ
		this.mBar_green 	     = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_bar_green);		//緑ゲージ
		this.mBar_red 		     = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_bar_red);		//赤ゲージ
		this.mBar_yellow	     = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_bar_yellow);		//黄ゲージ
		this.map_window 	     = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_textwindow2);	//ウィンドウ
		this.stageselectback_tex = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_yesno_ic);		//YESとNOのアイコン
		this.recovery_tex        = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_life_ic);		//回復アイテムのアイコン
		this.heroskill_ic_tex    = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_hero_sonic);		//主人公スキルのアイコン
		this.gunnerskill_ic_tex  = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_gunner_laser);	//銃スキルのアイコン
		this.armorskill_ic_tex   = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_armor_spear);	//鎧スキルのアイコン
		this.large_sword_skill_ic_tex  = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_large_sword_slash);		//大剣スキルのアイコン
		this.return_ic_tex        = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_return_ic);		//リターンアイコン
		this.start_spell	     = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_spell);			//開始時の文字

		this.mWin		 	 = GraphicUtil.loadTexture(gl, res, R.drawable.game_o_win);					//勝利
		this.mDefeat 		 = GraphicUtil.loadTexture(gl, res, R.drawable.game_o_defeat);				//敗北
		this.tensiondown_tex = GraphicUtil.loadTexture(gl, res, R.drawable.game_o_tension_down);		//敗北時線ン
		this.attack_area_tex = GraphicUtil.loadTexture(gl, res, R.drawable.game_o_attack_area);			//攻撃範囲
		this.mKage 			 = GraphicUtil.loadTexture(gl, res, R.drawable.game_o_kage);				//主人公たちとかの影
		this.revive_tex 	 = GraphicUtil.loadTexture(gl, res, R.drawable.game_ui_revive_ic);			//復活アイテムエフェクト

		//スキルエフェクト
		this.skill_efect_tex[0][0] = GraphicUtil.loadTexture(gl, res, R.drawable.game_efe_hero_skill);			//主人公スキル
		this.skill_efect_tex[1][0] = GraphicUtil.loadTexture(gl, res, R.drawable.game_efe_maho);				//魔法陣
		this.skill_efect_tex[1][1] = GraphicUtil.loadTexture(gl, res, R.drawable.game_efe_hono);				//炎エフェクト
		this.skill_efect_tex[2][0] = GraphicUtil.loadTexture(gl, res, R.drawable.game_efe_gunner_skill);		//銃スキル
		this.skill_efect_tex[2][1] = GraphicUtil.loadTexture(gl, res, R.drawable.game_efe_gunner_skill_flash);	//銃光スキル
		this.skill_efect_tex[3][0] = GraphicUtil.loadTexture(gl, res, R.drawable.game_efe_armor_skill);			//鎧スキル
		this.skill_efect_tex[4][0] = GraphicUtil.loadTexture(gl, res, R.drawable.game_efe_large_sword_skill);	//大剣スキル

		this.skill_cutin_tex = GraphicUtil.loadTexture(gl, res, R.drawable.game_efe_skill_cut_in);			//スキル発動時のカットイン

		//ステータス文字生成
		FontTexture.text_fTexture.createTextBuffer(gl, mContext);
		for( int i = 0; i < FontTexture.text_length; i++){
			FontTexture.text_fTexture.drawStringToTexture(gl, FontTexture.chr_par[i], 1024, 0);
			str_param[0][i] = FontTexture.text_fTexture.getWidth(0);
			str_param[1][i] = FontTexture.text_fTexture.getHeight(0);
			str_param[2][i] = FontTexture.text_fTexture.getOffset(0);
			str_param[3][i] = FontTexture.text_fTexture.lineHeight(0);
			FontTexture.text_fTexture.nextReadPoint(0);
		}
		//フィールド文字生成
		for( int i = 0; i < FontTexture.stage.length; i++){
			FontTexture.text_fTexture.drawStringToTexture(gl, FontTexture.stage[i], 1024, 0);
			mGameInfo.str_stage[0][i] = FontTexture.text_fTexture.getWidth(0);
			mGameInfo.str_stage[1][i] = FontTexture.text_fTexture.getHeight(0);
			mGameInfo.str_stage[2][i] = FontTexture.text_fTexture.getOffset(0);
			mGameInfo.str_stage[3][i] = FontTexture.text_fTexture.lineHeight(0);
			FontTexture.text_fTexture.nextReadPoint(0);
		}
		//数字文字生成
		for( int i = 0; i < FontTexture.txtnum_length; i++){
			FontTexture.text_fTexture.drawStringToTexture(gl, FontTexture.chr_num[i], 1024, 1);
			mGameInfo.num_str_param[0][i] = FontTexture.text_fTexture.getWidth(1);
			mGameInfo.num_str_param[1][i] = FontTexture.text_fTexture.getHeight(1);
			mGameInfo.num_str_param[2][i] = FontTexture.text_fTexture.getOffset(1);
			mGameInfo.num_str_param[3][i] = FontTexture.text_fTexture.lineHeight(1);
			FontTexture.num_h = mGameInfo.num_str_param[3][i];
			FontTexture.text_fTexture.nextReadPoint(1);
		}
		//メッセージ文字生成
		for( int i = 0; i < FontTexture.str_message.length; i++){
			FontTexture.text_fTexture.drawStringToTexture(gl, FontTexture.str_message[i], 1024, 2);
			GameInfo.message_str_param[0][i] = FontTexture.text_fTexture.getWidth(2);
			GameInfo.message_str_param[1][i] = FontTexture.text_fTexture.getHeight(2);
			GameInfo.message_str_param[2][i] = FontTexture.text_fTexture.getOffset(2);
			GameInfo.message_str_param[3][i] = FontTexture.text_fTexture.lineHeight(2);
			FontTexture.text_fTexture.nextReadPoint(2);
		}
		str_Texture = FontTexture.text_fTexture.getTexture(0);
		strnum_Texture = FontTexture.text_fTexture.getTexture(1);
		strmessage_Texture = FontTexture.text_fTexture.getTexture(2);

	}


	//--------------------------------------------------------------------------------
	//		サウンドの生成・ロード
	//--------------------------------------------------------------------------------
	public void loadSound() {
		Sound.music.Sound_Create(mContext);			//サウンドの生成
		//サウンドのロード
		Sound.music.SE_Sound_load(mContext,  0, R.raw.attack);
		Sound.music.SE_Sound_load(mContext,  1, R.raw.decision);
		//Sound.music.SE_Sound_load(mContext,  2, R.raw.skill);
		//Sound.music.SE_Sound_load(mContext,  3, R.raw.victory);
		Sound.music.SE_Sound_load(mContext,  4, R.raw.explosion);
		Sound.music.SE_Sound_load(mContext,  5, R.raw.flame);
		Sound.music.SE_Sound_load(mContext,  6, R.raw.maho_circle);
		Sound.music.SE_Sound_load(mContext,  7, R.raw.heart);
		Sound.music.SE_Sound_load(mContext,  8, R.raw.attack1);
		Sound.music.SE_Sound_load(mContext,  9, R.raw.attack2);
		//Sound.music.SE_Sound_load(mContext, 10, R.raw.attack_p_gunner);
		Sound.music.SE_Sound_load(mContext, 11, R.raw.attack_e_sraim);
		Sound.music.SE_Sound_load(mContext, 12, R.raw.attack_e_kinoko);
		Sound.music.SE_Sound_load(mContext, 13, R.raw.attack_e_niwatori);
		Sound.music.SE_Sound_load(mContext, 14, R.raw.attack_e_dragon);
		Sound.music.SE_Sound_load(mContext, 15, R.raw.hizumi_output);
		Sound.music.SE_Sound_load(mContext, 16, R.raw.attack_p_taiken);
		Sound.music.SE_Sound_load(mContext, 17, R.raw.attack_p_gunner);
		Sound.music.SE_Sound_load(mContext, 18, R.raw.attack_p_yoroi);
		Sound.music.SE_Sound_load(mContext, 19, R.raw.die);
		Sound.music.SE_Sound_load(mContext, 20, R.raw.skill_yuusya);
		Sound.music.SE_Sound_load(mContext, 21, R.raw.skill_gun_1);
		Sound.music.SE_Sound_load(mContext, 22, R.raw.skill_gun_2);
		Sound.music.SE_Sound_load(mContext, 23, R.raw.skill_yoroi_1);
		Sound.music.SE_Sound_load(mContext, 24, R.raw.skill_yoroi_2);
		Sound.music.SE_Sound_load(mContext, 25, R.raw.skill_taiken_1);
		Sound.music.SE_Sound_load(mContext, 26, R.raw.skill_taiken_2);
		Sound.music.SE_Sound_load(mContext, 27, R.raw.skill_taiken_3);


		//		Sound.music.SE_Sound_load(mContext, 7, R.raw.attack_e_dragon);

	}




	final public static float rsqrt2(float x) {
		float half = 0.5f * x;
		x = Float.intBitsToFloat((0x5f375a86 - (Float.floatToIntBits(x) >>> 1)));
		x *= 1.5f - half * x * x;
		return(x);
	}


	final static float sqrt2( float x ){
		float xHalf = 0.5f * x;
		float tmp   = Float.intBitsToFloat(0x5F3759DF - (Float.floatToIntBits(x) >>> 1));

		tmp *= ( 1.5f - ( xHalf * tmp * tmp ) );
		//xRes *= ( 1.5f - ( xHalf * xRes * xRes ) );//コメントアウトを外すと精度が上がる
		return tmp * x;
	}



	class SThread extends Thread { // スレッドクラス

		long now;
	    SThread() { // コンストラクタ


	    }

	    public void run () {

	    	while(MainActivity.thread_run){
		 		long next_time =now + 50;
	     		now = System.currentTimeMillis();
	if(!exe_stop_flg)
	     			Exe_control();		//実行制御
	     		else	     			Start_Move_Camera();


	    		if(!MainActivity.thread_run)
	    		{
	    			return;
	    		}
	    		if(now < next_time){ //描画しても時間が余ってたら・・・
	    			try {
	    				Thread.sleep(next_time-now);
	    				//long tes = next_time-now;
	    				//Log.d("sleep","sleep"+tes);
	    			} catch (InterruptedException e) {
	    				//e.printStackTrace();
	    			}//ミリ秒精度で処理を中断する関数
	    		}
	    	}
	    }

	}


}


