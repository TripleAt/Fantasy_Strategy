package com.fantasy_strategy;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class Skill {
	public int skill__flg ;
	public float mX, mY;	//位置
	public int frame_count = 0;
	public int frame_efe_count = 0;
	public int rect_number;
	public int rect_efe_number;	//スキル矩形番号
	int kyara_no;			//スキルで使うキャラクターの番号
	int hono_cnt = 0;		//火柱を途中でループさせるカウント
	public int frm_cnt_flg = 0;		//スキルの表示をコントロールするフラグ
	float maho_skill_scale;
	public int skill_gage_flg;
	static boolean itemscore_flg = false;
	static boolean itemdraw_flg = false;
	static int recovery_hp;
	static float recovery_x = 0.125f;
	static float recovery_y = 0.125f;
	static float recovery_fade = 1.0f;
	static int recovery_cnt = 5;
	public static int item_point_flg = 0;
	public float skill_gage_time;	//各スキル毎の使えるまでの時間
	public float skill_x, skill_y;
	private float gunner_flash_s_x, gunner_flash_s_y;	//銃スキル銃光x,y座標
	private float[] gunner_laser_s_x = new float[4];	//銃スキルレーザーx座標
	private float[] gunner_laser_s_y = new float[4];	//銃スキルレーザーy座標
	int skill_cutin_cont;	//スキルカットイン矩形制御
	public static float skill_cutin_alpha = 0;
	public static float skill_cutin_pos_x = +480.0f;
	public static int skill_cutin_flg;
	private float hero_efe_alpha = 1.0f;
	private float gunner_efe_alpha = 1.0f;
	private float gunner_efe_flash_alpha = 1.0f;

	float skill_acc;	//スキル加速

	public int skill_dir;	//スキル使用時のキャラクターの向き


	//スキルが使えるまでの時間　　　　主人公、魔法使い、銃、鎧、大剣
	public float[] skill_time = {20.0f, 10.0f, 20.0f, 15.0f, 25.0f,
								  5.0f,  5.0f, 5.0f, 5.0f, 5.0f};


	private float[][]rect_maho =
			{
				{  0.0f, 0.0f, 64.0f, 64.0f, 32.0f, 32.0f},
				{ 64.0f, 0.0f, 64.0f, 64.0f, 32.0f, 32.0f},
				{128.0f, 0.0f, 64.0f, 64.0f, 32.0f, 32.0f},
				{ 64.0f, 0.0f, 64.0f, 64.0f, 32.0f, 32.0f},
				{  0.0f, 0.0f, 64.0f, 64.0f, 32.0f, 32.0f},
				{  0.0f, 0.0f, 64.0f, 64.0f, 32.0f, 32.0f}
			};
	private float[][]rect_hono =
		{
			{  0.0f,  0.0f, 48.0f, 96.0f, 48.0f / 2, 96.0f},
			{ 96.0f,  0.0f, 48.0f, 96.0f, 48.0f / 2, 96.0f},
			{144.0f,  0.0f, 48.0f, 96.0f, 48.0f / 2, 96.0f},
			{192.0f,  0.0f, 48.0f, 96.0f, 48.0f / 2, 96.0f},
			{240.0f,  0.0f, 48.0f, 96.0f, 48.0f / 2, 96.0f},
			{288.0f,  0.0f, 48.0f, 96.0f, 48.0f / 2, 96.0f},
			{336.0f,  0.0f, 48.0f, 96.0f, 48.0f / 2, 96.0f},
			{  0.0f, 97.0f, 48.0f, 96.0f, 48.0f / 2, 96.0f},
			{ 48.0f, 97.0f, 48.0f, 96.0f, 48.0f / 2, 96.0f},
			{ 96.0f, 97.0f, 48.0f, 96.0f, 48.0f / 2, 96.0f},
			{ 96.0f, 97.0f, 48.0f, 96.0f, 48.0f / 2, 96.0f}		//念のため
		};

	//スキル時の主人公の動き
	private float[][][]rect_efe_hero =
		{
			//左前向き
			{
				{ 64.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f, 256.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f, 320.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
			},
			//右前向き
			{
				{ 64.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f,  64.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f, 128.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
			},
			//左後向き
			{
				{ 256.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 256.0f, 256.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 256.0f, 320.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 256.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 256.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
			},
			//右後向き
			{
				{ 256.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 256.0f,  64.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 256.0f, 128.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 256.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 256.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
			}
		};

	private float[][][]rect_efe_gunner =
		{
			//左前向き
			{
				{ 64.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},	//何もなし
				{ 72.0f, 255.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},	//ちょっと光る{   72,   255,    64,    64,     0,    64},
				{ 73.0f, 320.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},	//レーザー中間{   73,   320,    64,    64,     0,    64},
				{  9.0f, 320.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},	//レーザー先{    9,   320,    64,    64,     0,    64},
				{ 64.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
			},
			//右前向き
			{
				{ 64.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 80.0f,  64.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},	//{   80,    18,    64,    64,     0,    64},
				{249.0f, 128.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{313.0f, 129.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
			},
			//左後向き
			{
				{ 256.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 245.0f, 221.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},	//{  245,   266,    64,    64,     0,    64}
				{ 73.0f, 320.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{  9.0f, 320.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 256.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 256.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
			},
			//右後向き
			{
				{ 256.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 251.0f,  69.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},	//{  251,    69,    64,    64,     0,    64}
				{ 249.0f, 128.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},	//{  249,   128,    64,    64,     0,    64},
				{ 313.0f, 129.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},	//{  313,   129,    64,    64,     0,    64},
				{ 256.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 256.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
			},
		};

	private float[][][]rect_efe_flash_gunner =
		{
			//左前向き
			{
				{  0.0f, 0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f, 0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f, 0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				//{128.0f, 0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{  0.0f, 0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{  0.0f, 0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
			},
			//右前向き
			{
				{  0.0f, 64.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f, 64.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f, 64.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				//{128.0f, 64.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{  0.0f, 64.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{  0.0f, 64.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
			},
			//左後向き
			{
				{  0.0f, 128.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f, 128.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f, 128.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				//{128.0f, 128.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{  0.0f, 128.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{  0.0f, 128.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
			},
			//右後向き
			{
				{  0.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{ 64.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				//{128.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{  0.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
				{  0.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},
			},
		};

	private float[][][]rect_efe_armor =
		{
			//左前向き
			{
				{ 64.0f, 192.0f,  64.0f, 64.0f,  64.0f / 2, 64.0f},		//何もなし
				{ 16.0f, 320.0f, 112.0f, 64.0f, 112.0f / 2, 64.0f},		//槍{   16,   320,   112,    64,     0,    64},
				{ 64.0f, 192.0f,  64.0f, 64.0f,  64.0f / 2, 64.0f},		//何もなし
				{ 64.0f, 192.0f,  64.0f, 64.0f,  64.0f / 2, 64.0f},		//何もなし　念のため
			},
			//右前向き
			{
				{ 64.0f,   0.0f, 64.0f,  64.0f,  64.0f / 2, 64.0f},		//何もなし
				{128.0f,   0.0f, 64.0f, 112.0f,  64.0f / 2, 64.0f},		//槍{  128,     0,    64,   112,     0,   112},縦
				{ 64.0f,   0.0f, 64.0f,  64.0f,  64.0f / 2, 64.0f},		//何もなし
				{ 64.0f,   0.0f, 64.0f,  64.0f,  64.0f / 2, 64.0f},		//何もなし　念のため
			},
			//左後向き
			{
				{ 256.0f, 192.0f,  64.0f,  64.0f,  64.0f / 2, 64.0f},		//何もなし
				{ 208.0f, 208.0f,  64.0f, 112.0f,  64.0f / 2, 64.0f},		//槍{  208,   208,    64,   112,     0,   112}
				{ 256.0f, 192.0f,  64.0f,  64.0f,  64.0f / 2, 64.0f},		//何もなし
				{ 256.0f, 192.0f,  64.0f,  64.0f,  64.0f / 2, 64.0f},		//何もなし　念のため
			},
			//右後向き
			{
				{ 256.0f,   0.0f, 64.0f,  64.0f,  64.0f / 2, 64.0f},		//何もなし
				{ 256.0f, 128.0f, 112.0f, 64.0f, 112.0f / 2, 64.0f},		//槍{  256,   128,   112,    64,     0,    64},
				{ 256.0f,   0.0f, 64.0f,  64.0f,  64.0f / 2, 64.0f},		//何もなし
				{ 256.0f,   0.0f, 64.0f,  64.0f,  64.0f / 2, 64.0f},		//何もなし　念のため
			},
		};

	private float[][][]rect_efe_large_sword =
		{
			//左前向き
			{
				{ 64.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//何もなし　構え
				{ 64.0f, 256.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//何もなし　切り下げ
				{ 64.0f, 320.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//衝撃波
				{ 64.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//何もなし　構え
				{ 64.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//何もなし　念のため
			},
			//右前向き
			{
				{ 64.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//何もなし　構え
				{ 64.0f,  64.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//何もなし　切り下げ
				{ 64.0f, 128.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//衝撃波
				{ 64.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//何もなし　構え
				{ 64.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//何もなし　念のため
			},
			//左後向き
			{
				{ 256.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//何もなし　構え
				{ 256.0f, 256.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//何もなし　切り下げ
				{ 256.0f, 320.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//衝撃波
				{ 256.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//何もなし　構え
				{ 256.0f, 192.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//何もなし　念のため
			},
			//右後向き
			{
				{ 256.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//何もなし　構え
				{ 256.0f,  64.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//何もなし　切り下げ
				{ 256.0f, 128.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//衝撃波
				{ 256.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//何もなし　構え
				{ 256.0f,   0.0f, 64.0f, 64.0f, 64.0f / 2, 64.0f},		//何もなし　念のため
			}
		};

	//各キャラクタースキルの表示のフレーム数
	private int[][] skill_frame = {
			{ 8, 5,  0,  8, 0},		//主人公
			{ 7, 5,  7,  5,},	//魔法使い(魔法陣)
			{ 7, 7,  7,  7, 7},		//銃
			{ 7, 7,  8, 20, 7},		//鎧
			{ 7, 7,  7,  7, 3},		//大剣
	};
	//各キャラクタースキルのエフェクト表示のフレーム数
	private int[][] skill_efect_frame = {
			{ 6, 6, 7, 11, 11},	//主人公
			{ 3, 2, 2, 3, 3, 3, 3, 4, 3},	//魔法使い(火柱)
			{ 12, 5, 100, 100, 15},	//銃
			{ 15, 15, 15, 5, 1},	//鎧
			{ 15, 15, 15, 15, 15},	//大剣
	};


	private float rect_skill_cutin[][][] =
		{
			{	//主人公
				{  0.0f, 0.0f, 128.0f, 64.0f, 64.0f, 32.0f},
				{128.0f, 0.0f, 128.0f, 64.0f, 64.0f, 32.0f},
			},
			{	//魔法使い
				{  0.0f, 256.0f, 128.0f, 64.0f, 64.0f, 32.0f},
				{128.0f, 256.0f, 128.0f, 64.0f, 64.0f, 32.0f},
			},
			{	//銃
				{  0.0f, 192.0f, 128.0f, 64.0f, 64.0f, 32.0f},
				{128.0f, 192.0f, 128.0f, 64.0f, 64.0f, 32.0f},
			},
			{	//鎧
				{  0.0f, 128.0f, 128.0f, 64.0f, 64.0f, 32.0f},
				{128.0f, 128.0f, 128.0f, 64.0f, 64.0f, 32.0f},
			},
			{	//大剣
				{  0.0f, 64.0f, 128.0f, 64.0f, 64.0f, 32.0f},
				{128.0f, 64.0f, 128.0f, 64.0f, 64.0f, 32.0f},
			}
		};

	public Skill(int x, int y, int no){
		this.mY =  GameMain.mSta_p[no].mY;
		this.mX =  GameMain.mSta_p[no].mX;
	}



	//各キャラクタースキル処理
	public void skill_move(int no, int skill_no){
		//skill_y =  GameMain.mSta_p[no].mY;
		//skill_x =  GameMain.mSta_p[no].mX;

		if(GameMain.mSta_p[no].skill_cutin_timing) return;

		//Log.d("GameMain.select_char","GameMain.select_char："+GameMain.select_char);
		if(this.skill_gage_flg == 1){		//スキルのゲージがたまった時
			if( GameMain.mSta_p[no].skill_active_flg == false){		//スキル発動中はボタンを反応させない
				if(GameMain.mSta_p[no].active_flg){		//生きているときに発動する
					if(GameInfo.skill_fling == 0  &&  GameMain.select_char == no /*&& GameMain.mSta_p[no].idou == false*/){
						this.skill__flg = 1;
						this.frm_cnt_flg = 0;
						GameInfo.skill_fling = -1;
						this.skill_gage_time = 0.0f;
						this.skill_gage_flg = 0;
						if(no == 1){
							if(Sound.music.sp != null){
								Sound.music.SE_play(6);		//魔法陣の効果音
								Log.d("skill__flg","mahoスキル"+no);
							}
						}
					}
				}
			}
		}

		if(this.skill__flg == 1){
			if(GameMain.mSta_p[no].skill_timing){		//スキル発動
				switch(no){
					case 0:	//主人公
							if((skill_no / 5) == 0){
								hero_move(no);
							}
						break;
					case 1:	//魔法使い
							if((skill_no / 5) == 0){
								maho_move(no);
							}
						break;
					case 2:	//銃
							if((skill_no / 5) == 0){
								gunner_move(no);
							}
						break;
					case 3:	//鎧
							if((skill_no / 5) == 0){
								armor_move(no);
							}
						break;
					case 4:	//大剣
							if((skill_no / 5) == 0){
								large_sword_move(no);
							}
						break;
				}

			}
		}

	}


	//各キャラクタースキル描画
	public void skill_draw(GL10 gl, int texture[], int no, int skill_no){

		switch(no){
			case 0:	//主人公
					if((skill_no / 5) == 0){
						hero_draw(gl, texture[0], skill_no / 5);
					}
				break;
			case 1:	//魔法使い
					if((skill_no / 5) == 0){
						maho_draw(gl, texture[0], texture[1],skill_no / 5);
					}
				break;
			case 2:	//銃
					if((skill_no / 5) == 0){
						gunner_draw(gl, texture[0], texture[1],skill_no / 5);
					}
				break;
			case 3:	//鎧
					if((skill_no / 5) == 0){
						armor_draw(gl, texture[0], skill_no / 5);
					}
				break;
			case 4:	//大剣
					if((skill_no / 5) == 0){
						large_sword_draw(gl, texture[0], skill_no / 5);
					}
				break;
		}
	}



	/**
	 * 	魔法使い　火柱スキル制御
	 * @param no
	 */
	public void maho_move(int no){
		//スキル表示の流れの制御
		switch(this.frm_cnt_flg){
			case 0:
				rect_efe_number = 9;		//火柱何も表示しないように初期化
				hono_cnt = 0;
				this.rect_number = 0;
				maho_skill_scale = 0.125f;

				this.frm_cnt_flg++;
				//Sound.music.SE_play(6);		//魔法陣の効果音
				break;

			case 1:			//魔法陣出現
				if(maho_skill_scale < 0.25f){
					maho_skill_scale += 0.01;	//魔法陣を大きく
				}

				//draw()用変数
					if(frame_count <= 0){
						frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
						this.rect_number++;
					}
					frame_count--;

					if(this.rect_number == 2){
						this.frm_cnt_flg++;
					}


				break;

			case 2:			//魔法陣が光っている時に火柱
				if(rect_efe_number == 9){
					frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					rect_efe_number = 0;

					if(Sound.music.sp != null){
						Sound.music.SE_play(5);		//火柱の効果音
					}
				}

				if(frame_efe_count <= 0){
					frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					rect_efe_number++;
				}
				frame_efe_count--;

				if(rect_efe_number >= 5){	//火柱の矩形情報を少しるループさせる
					rect_efe_number = 3;
				}
				hono_cnt++;

				if(hono_cnt > 40)	//大体のフレーム数で終わる
				{
					this.frm_cnt_flg++;
				}
				break;

			case 3:
				if(frame_efe_count <= 0){
					frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					rect_efe_number++;
				}
				frame_efe_count--;

				if(rect_efe_number > 8){		//火柱の表示が終わった時
					rect_efe_number = 9;
					this.frm_cnt_flg++;
				}

				break;

			case 4:		//火柱の魔法が終わり魔法陣を消す
				if(frame_count <= 0){
					frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					this.rect_number++;
				}
				frame_count--;

				if(maho_skill_scale > 0.125f){
					maho_skill_scale -= 0.01;	//魔法陣を小さく
				}

				if(this.rect_number > 4){
					rect_efe_number = 9;
					maho_skill_scale = 0.0f;
					this.frm_cnt_flg++;
					//GameMain.mSta_p[no].idou = true;
				}

				break;

			case 5:
				this.frm_cnt_flg = 0;
				this.skill__flg = 0;		//スキル表示フラグ
				GameMain.mSta_p[no].skill_timing = false;
				GameMain.mSta_p[no].skill_active_flg = false;
				GameMain.mSta_p[no].s = Charstatus.MOVE;

				break;
		}
	}





	/**
	 * 魔法使いスキル描画
	 * @param gl
	 * @param texture1
	 * @param texture2
	 * @param skill_no
	 */
	public void maho_draw(GL10 gl, int texture1, int texture2, int skill_no) {
		float Angle = (Camera_var.angle - 180);		//向いている方向

		switch(skill_no){
			case 0:		//火柱がでるやつ
				float s_x, s_y;
				s_x = GameMain.mSta_p[1].mX;
				s_y = GameMain.mSta_p[1].mY;

				//魔法陣
				gl.glPushMatrix();
				{
					gl.glTranslatef(s_x, s_y, 0.02f);
					gl.glRotatef(0.0f, 0.0f, 0.0f, 1.0f);	//回転
					//gl.glScalef(size, size, 1.0f);
					gl.glScalef(maho_skill_scale, maho_skill_scale, 1.0f);

					//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
					GraphicUtil.drawTexture_pixel_custom2(gl, 512.0f, 512.0f, texture1,
							rect_maho[this.rect_number],
							1.0f, 1.0f, 1.0f, 1.0f);
				}
				gl.glPopMatrix();

				//火柱
				for(int i = 1; i <= 5; i++){

					//Log.d("maho","skill_dir:"+skill_dir);
					switch(this.skill_dir){
						case 0: //前
							s_y += 0.25f;
							break;
						case 1:	//右
							s_x -= 0.25f;
							break;
						case 2:	//左
							s_x += 0.25f;
							break;
						case 3:	//後
							s_y -= 0.25f;
							break;
					}

					//火柱領域制御--------------------------------------------------------
					if(s_x > ((GameMain.map_size_x  - 1) * 0.25f) + 0.125f ){
						s_x = ((GameMain.map_size_x - 1) * 0.25f);
					}

					if(s_y > ((GameMain.map_size_y  - 1) * 0.25f) + 0.125f){
						s_y = ((GameMain.map_size_y - 1) * 0.25f);
					}

					if(s_x < 0.0f ){
						s_x = 0.0f;
					}

					if(s_y < 0.0f ){
						s_y = 0.0f;
					}

					gl.glPushMatrix();
					{
						gl.glTranslatef(s_x , s_y, 0.011f);
						gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
						gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
						//gl.glScalef(size, size, 1.0f);
						gl.glScalef(0.125f, 0.1875f, 1.0f);

						//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
						GraphicUtil.drawTexture_pixel_custom2(gl, 512.0f, 512.0f, texture2,
								rect_hono[rect_efe_number],
								1.0f, 1.0f, 1.0f, 1.0f);
					}
					gl.glPopMatrix();
				}
				break;
		}

	}


	/**
	 * 主人公スキル　衝撃スキル制御
	 * @param no
	 */
	public void hero_move(int no){
		//スキル表示の流れの制御
		switch(this.frm_cnt_flg){
			case 0:
				rect_efe_number = 4;		//初期化
				this.rect_number = 4;
				skill_x = GameMain.mSta_p[no].mX;
				skill_y = GameMain.mSta_p[no].mY;
				hero_efe_alpha = 1.0f;
				Log.d("hero","skill_dir:"+skill_dir);
				switch(skill_dir){
					case 0: //前
						skill_y += 0.25f;
						break;
					case 1:	//右
						skill_x -= 0.25f;
						break;
					case 2:	//左
						skill_x += 0.25f;
						break;
					case 3:	//後
						skill_y -= 0.25f;
						break;
				}

				this.frm_cnt_flg++;
				break;

			case 1:			//主人公剣構える
				//draw()用変数
				if(this.rect_number == 4){
					frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					this.rect_number = 0;
				}
				frame_count--;

				if(frame_count <= 0){
					this.frm_cnt_flg++;
				}
				break;

			case 2:			//	剣を振りかぶる
				if(Sound.music.sp != null){
					Sound.music.SE_play(20);		//聖魔法の効果音
				}
				if(frame_count <= 0){
					frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					this.rect_number++;
				}

				if(this.rect_number == 1)
				{
					this.frm_cnt_flg++;
				}
				break;

			case 3:		//最初の衝撃が出る
				if(rect_efe_number == 4){
					frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					rect_efe_number = 0;
				}
				if(frame_efe_count <= 0){
					frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					rect_efe_number++;
				}
				frame_efe_count--;

				if(rect_efe_number == 1){		//最初の衝撃出した
					this.frm_cnt_flg++;
				}
				break;

			case 4:		//もう一回剣を振りかぶる
				if(rect_efe_number <= 0){
					frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					rect_efe_number++;
				}
				frame_efe_count--;

				hero_efe_alpha -= 0.2f;
				if(hero_efe_alpha < 0.0f)
					hero_efe_alpha = 0.0f;
				if(frame_count <= 0){
					frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					this.rect_number++;
				}
				frame_count--;

				if(this.rect_number == 2){
					this.frm_cnt_flg++;
					hero_efe_alpha = 1.0f;
				}
				break;

			case 5:		//二回目の衝撃を出す
				if(frame_efe_count <= 0){
					frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					rect_efe_number++;
				}
				frame_efe_count--;

				frame_count--;
				hero_efe_alpha -= 0.2f;
				if(hero_efe_alpha < 0.0f)
					hero_efe_alpha = 0.0f;

				if(rect_efe_number >= 3){
					this.frm_cnt_flg++;
				}
				break;

			case 6:		//剣を収める
				if(frame_efe_count <= 0){
					frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					rect_efe_number++;
				}
				frame_efe_count--;

				if(frame_count <= 0){
					frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					this.rect_number++;
				}
				frame_count--;

				if(rect_efe_number >= 4){
					rect_efe_number = 4;
				}

				if(this.rect_number >= 4){
					this.rect_number = 4;
					this.frm_cnt_flg++;
				}
				break;

			case 7:		//おわり
				this.frm_cnt_flg = 0;
				this.skill__flg = 0;		//スキル表示フラグ
				GameMain.mSta_p[no].skill_timing = false;
				//GameMain.mSta_p[no].idou = true;
				GameMain.mSta_p[no].skill_active_flg = false;
				GameMain.mSta_p[no].s = Charstatus.MOVE;
				break;
		}

	}

	//主人公スキル描画
	public void hero_draw(GL10 gl, int texture, int skill_no) {
		float Angle = (Camera_var.angle - 180);		//向いている方向

		switch(skill_no){
			case 0:		//衝撃
				//衝撃領域制御--------------------------------------------------------
				if(skill_x > ((GameMain.map_size_x  - 1) * 0.25f) + 0.125f ){
					skill_x = ((GameMain.map_size_x - 1) * 0.25f);
				}

				if(skill_y > ((GameMain.map_size_y  - 1) * 0.25f) + 0.125f){
					skill_y = ((GameMain.map_size_y - 1) * 0.25f);
				}

				if(skill_x < 0.0f ){
					skill_x = 0.0f;
				}

				if(skill_y < 0.0f ){
					skill_y = 0.0f;
				}


				//衝撃
				gl.glPushMatrix();
				{
					gl.glTranslatef(skill_x, skill_y, 0.02f);
					gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
					gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
					gl.glScalef(0.3125f, 0.3125f, 1.0f);

					//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
					GraphicUtil.drawTexture_pixel_custom2(gl, 512.0f, 512.0f, texture,
							rect_efe_hero[skill_dir][rect_efe_number],
							1.0f, 1.0f, 1.0f, hero_efe_alpha);
				}
				gl.glPopMatrix();
				break;
		}

	}


	/**
	 * 銃　レーザースキル制御
	 * @param no
	 */
	public void gunner_move(int no){
		//スキル表示の流れの制御
		switch(this.frm_cnt_flg){
			case 0:		//初期化
				this.rect_efe_number = 4;
				this.rect_number = 4;

				this.gunner_flash_s_x = GameMain.mSta_p[no].mX;
				this.gunner_flash_s_y = GameMain.mSta_p[no].mY;

				this.frm_cnt_flg++;
				break;

			case 1:			//銃撃つ構える
				if(this.rect_number == 4){
					frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					this.rect_number = 0;
				}
				frame_count--;


				//draw()用変数
				if(frame_count <= 0){
					frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					this.rect_number++;
				}
				frame_count--;

				switch(this.skill_dir){
					case 0: //前
						if(gunner_flash_s_y <= (GameMain.mSta_p[2].mY + 0.25f * 0.75)){
							gunner_flash_s_y += 0.25f;
						}
						break;
					case 1:	//右
						if(gunner_flash_s_x >= (GameMain.mSta_p[2].mX - 0.25f * 0.75)){
							gunner_flash_s_x -= 0.25;
						}
						break;
					case 2:	//左
						if(gunner_flash_s_x <= (GameMain.mSta_p[2].mX + 0.25f * 0.75)){
							gunner_flash_s_x += 0.25f;
						}
						break;
					case 3:	//後
						if(gunner_flash_s_y >= (GameMain.mSta_p[2].mY - 0.25f * 0.75)){
							gunner_flash_s_y -= 0.25f;
						}
						break;
				}

				//レーザー銃光領域制御--------------------------------------------------------
				if(gunner_flash_s_x > ((GameMain.map_size_x  - 1) * 0.25f) + 0.125f ){
					gunner_flash_s_x = ((GameMain.map_size_x - 1) * 0.25f);
				}

				if(gunner_flash_s_y > ((GameMain.map_size_y  - 1) * 0.25f) + 0.125f){
					gunner_flash_s_y = ((GameMain.map_size_y - 1) * 0.25f);
				}

				if(gunner_flash_s_x < 0.0f ){
					gunner_flash_s_x = 0.0f;
				}

				if(gunner_flash_s_y < 0.0f ){
					gunner_flash_s_y = 0.0f;
				}

				if(this.rect_number == 1){
					this.frm_cnt_flg++;
				}
				break;

			case 2:			//ためる
				if(Sound.music.sp != null){
					Sound.music.SE_play(21);		//チャージの効果音
				}
				if(rect_efe_number == 4){
					frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					rect_efe_number = 0;
				}

				if(frame_efe_count <= 0){
					frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					rect_efe_number++;
				}
				frame_efe_count--;

				if(rect_efe_number == 1){
					rect_number++;
					this.frm_cnt_flg++;
				}
				break;

			case 3:			//発射前に発射口が光る
				if(Sound.music.sp != null){
					Sound.music.SE_play(22);		//レーザーの効果音
				}
				if(frame_efe_count <= 0){
					frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					rect_efe_number++;
				}
				frame_efe_count--;

				if(rect_efe_number == 2){
					this.frm_cnt_flg++;
				}

				break;

			case 4:		//レーザーの位置
				gunner_laser_s_x[0] = GameMain.mSta_p[2].mX;
				gunner_laser_s_y[0] = GameMain.mSta_p[2].mY;
				switch(this.skill_dir){
					case 0: //前
						if(gunner_laser_s_y[0] <= (GameMain.mSta_p[2].mY + 0.25f * 5.75)){
							gunner_laser_s_y[0] += 0.5f;
						}
						break;
					case 1:	//右
						if(gunner_laser_s_x[0] >= (GameMain.mSta_p[2].mX - 0.25f * 5.75)){
							gunner_laser_s_x[0] -= 0.5;
						}
						break;
					case 2:	//左
						if(gunner_laser_s_x[0] <= (GameMain.mSta_p[2].mX + 0.25f * 5.75)){
							gunner_laser_s_x[0] += 0.5f;
						}
						break;
					case 3:	//後
						if(gunner_laser_s_y[0] >= (GameMain.mSta_p[2].mY - 0.25f * 5.75)){
							gunner_laser_s_y[0] -= 0.5f;
						}
						break;
				}

				//レーザー領域制御--------------------------------------------------------
				if(gunner_laser_s_x[0] > ((GameMain.map_size_x  - 1) * 0.25f) + 0.125f ){
					gunner_laser_s_x[0] = ((GameMain.map_size_x - 1) * 0.25f);
				}

				if(gunner_laser_s_y[0] > ((GameMain.map_size_y  - 1) * 0.25f) + 0.125f){
					gunner_laser_s_y[0] = ((GameMain.map_size_y - 1) * 0.25f);
				}

				if(gunner_laser_s_x[0] < 0.0f ){
					gunner_laser_s_x[0] = 0.0f;
				}

				if(gunner_laser_s_y[0] < 0.0f ){
					gunner_laser_s_y[0] = 0.0f;
				}

				for(int i = 1; i < 4; i++){
					gunner_laser_s_x[i] = gunner_laser_s_x[0];
					gunner_laser_s_y[i] = gunner_laser_s_y[0];
					switch(this.skill_dir){
						case 0: //前
							if(gunner_laser_s_y[i] <= (GameMain.mSta_p[2].mY + 0.25f * 7.75)){
								gunner_laser_s_y[i] = gunner_laser_s_y[i - 1] + 0.5f;
							}
							break;
						case 1:	//右
							if(gunner_laser_s_x[i] >= (GameMain.mSta_p[2].mX - 0.25f * 7.75)){
								gunner_laser_s_x[i] = gunner_laser_s_x[i - 1] - 0.5f;
							}
							break;
						case 2:	//左
							if(gunner_laser_s_x[i] <= (GameMain.mSta_p[2].mX + 0.25f * 7.75)){
								gunner_laser_s_x[i] = gunner_laser_s_x[i - 1] + 0.5f;
							}
							break;
						case 3:	//後
							if(gunner_laser_s_y[i] >= (GameMain.mSta_p[2].mY - 0.25f * 7.75)){
								gunner_laser_s_y[i] = gunner_laser_s_y[i - 1] - 0.5f;
							}
							break;
					}

					//レーザー領域制御--------------------------------------------------------
					if(gunner_laser_s_x[i] > ((GameMain.map_size_x  - 1) * 0.25f) + 0.125f ){
						gunner_laser_s_x[i] = ((GameMain.map_size_x - 1) * 0.25f);
					}

					if(gunner_laser_s_y[i] > ((GameMain.map_size_y  - 1) * 0.25f) + 0.125f){
						gunner_laser_s_y[i] = ((GameMain.map_size_y - 1) * 0.25f);
					}

					if(gunner_laser_s_x[i] < 0.0f ){
						gunner_laser_s_x[i] = 0.0f;
					}

					if(gunner_laser_s_y[i] < 0.0f ){
						gunner_laser_s_y[i] = 0.0f;
					}
				}
				this.frm_cnt_flg++;

				break;

			case 5:		//レーザー発射
				if(frame_efe_count <= 0){
					frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					rect_efe_number = 4;
				}
				frame_efe_count--;

				if(frame_efe_count % 3 == 0){
					gunner_efe_alpha = 0.3f;
				}else{
					gunner_efe_alpha = 1.0f;
				}

				if(frame_efe_count % 4 == 0){
					gunner_efe_flash_alpha = 0.6f;
				}else{
					gunner_efe_flash_alpha = 1.0f;
				}

				if(rect_efe_number == 4){
					//rect_number++;
					this.frm_cnt_flg++;
				}
				break;
			case 6:		//打ち終わる
				if(frame_count <= 0){
					frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					this.rect_number++;
				}
				frame_count--;

				if(rect_efe_number >= 4){
					rect_efe_number = 4;
				}

				if(this.rect_number >= 2){
					this.rect_number = 0;
				}

				if(this.rect_number == 0){
					this.frm_cnt_flg++;
				}

				break;
			case 7:		//終わり
				this.frm_cnt_flg = 0;
				this.skill__flg = 0;		//スキル表示フラグ
				GameMain.mSta_p[no].skill_timing = false;
				//GameMain.mSta_p[no].idou = true;
				GameMain.mSta_p[no].skill_active_flg = false;
				GameMain.mSta_p[no].s = Charstatus.MOVE;
				break;
		}
	}


	/**
	 * 銃スキル描画
	 * @param gl
	 * @param texture
	 * @param skill_no
	 */
	public void gunner_draw(GL10 gl, int texture1, int texture2, int skill_no) {
		float Angle = (Camera_var.angle - 180);		//向いている方向

		switch(skill_no){
			case 0:		//レーザー
				//銃光
				if(this.frm_cnt_flg >= 2 && this.frm_cnt_flg <= 5){
					gl.glPushMatrix();
					{
						gl.glTranslatef(gunner_flash_s_x, gunner_flash_s_y, 0.02f);
						gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
						gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
						//gl.glRotatef(90.0f/2.0f, 0.0f, 1.0f, 0.0f);
						//gl.glScalef(size, size, 1.0f);
						if(skill_dir == 0 || skill_dir == 3){	//レーザーの向きを調整
							gl.glRotatef(90.0f/2.0f, 0.0f, 1.0f, 0.0f);
						}else{
							//gl.glRotatef(90.0f/1.1f, 0.0f, 1.0f, 0.0f);
							//gl.glRotatef(-45.0f, 1.0f, 0.0f, 0.0f);
							gl.glRotatef(-45.0f, 0.0f, 1.0f, 0.0f);
							gl.glRotatef(-40.0f, 1.0f, 0.0f, 0.0f);
						}
						gl.glScalef(0.375f, 0.3375f, 1.0f);

						//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
						GraphicUtil.drawTexture_pixel_custom2(gl, 256.0f, 256.0f, texture2,
								rect_efe_flash_gunner[skill_dir][rect_efe_number],
								1.0f, 1.0f, 1.0f, gunner_efe_flash_alpha);
					}
					gl.glPopMatrix();
				}


				//敵の位置までレーザ照射
				if(this.frm_cnt_flg == 5){
					for(int i = 0; i < 3; i++){
						//レーザー中間
						//レーザーは連結させて表示しています
						gl.glPushMatrix();
						{
							gl.glTranslatef(gunner_laser_s_x[i], gunner_laser_s_y[i], 0.102f);
							gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
							gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
							if(skill_dir == 0 || skill_dir == 3){	//レーザーの向きを調整
								gl.glRotatef(90.0f/2.0f, 0.0f, 1.0f, 0.0f);
							}else{
								//gl.glRotatef(90.0f/1.1f, 0.0f, 1.0f, 0.0f);
								//gl.glRotatef(-45.0f, 1.0f, 0.0f, 0.0f);
								gl.glRotatef(-45.0f, 0.0f, 1.0f, 0.0f);
								gl.glRotatef(-40.0f, 1.0f, 0.0f, 0.0f);
							}
							gl.glScalef(0.25f, 0.25f, 1.0f);

							//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
							GraphicUtil.drawTexture_pixel_custom2(gl, 512.0f, 512.0f, texture1,
									rect_efe_gunner[skill_dir][2],
									1.0f, 1.0f, 1.0f, gunner_efe_alpha);
						}
						gl.glPopMatrix();
					}

					//レーザー先端
					gl.glPushMatrix();
					{
						gl.glTranslatef(gunner_laser_s_x[3], gunner_laser_s_y[3], 0.1012f);
						gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
						gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
						if(skill_dir == 0 || skill_dir == 3){	//レーザーの向きを調整
							gl.glRotatef(90.0f/2.0f, 0.0f, 1.0f, 0.0f);
						}else{
							//gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
							//gl.glRotatef(-45.0f, 1.0f, 0.0f, 0.0f);
							gl.glRotatef(-45.0f, 0.0f, 1.0f, 0.0f);
							gl.glRotatef(-40.0f, 1.0f, 0.0f, 0.0f);
						}
						gl.glScalef(0.25f, 0.25f, 1.0f);

						//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
						GraphicUtil.drawTexture_pixel_custom2(gl, 512.0f, 512.0f, texture1,
								rect_efe_gunner[skill_dir][3],
								1.0f, 1.0f, 1.0f, gunner_efe_alpha);
					}
					gl.glPopMatrix();

				}
				break;

		}

	}

	/**
	 * 鎧スキル制御　スピア
	 * @param no
	 */
	public void armor_move(int no){
		//スキル表示の流れの制御
			switch(this.frm_cnt_flg){
				case 0:
					if(Sound.music.sp != null){
						Sound.music.SE_play(23);		//鎧1の効果音
					}
					rect_efe_number = 3;		//初期化
					this.rect_number = 3;
					this.skill_x = GameMain.mSta_p[no].mX;
					this.skill_y = GameMain.mSta_p[no].mY;
					switch(this.skill_dir){
					case 0: //前
							if(skill_y <= (GameMain.mSta_p[3].mY + 0.25f * 3.75)){
								skill_y += 0.25f;
							}
							break;
						case 1:	//右
							if(skill_x >= (GameMain.mSta_p[3].mX - 0.25f * 3.75)){
								skill_x -= 0.25;
							}
							break;
						case 2:	//左
							if(skill_x <= (GameMain.mSta_p[3].mX + 0.25f * 3.75)){
								skill_x += 0.25f;
							}
							break;
						case 3:	//後
							if(skill_y >= (GameMain.mSta_p[3].mY - 0.25f * 3.75)){
								skill_y -= 0.25f;
							}
							break;
					}

					this.frm_cnt_flg++;
					break;

				case 1:			//槍を持って構える
					//draw()用変数
					if(rect_number == 3){
						frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
						rect_number = 0;
					}

					if(frame_count <= 0){
						frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
						this.frm_cnt_flg++;
					}
					frame_count--;

					break;

				case 2:			//槍のリリースポイントが光る
					if(rect_efe_number == 3){
						frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
						rect_efe_number = 0;
					}

					if(frame_efe_count <= 0){
						frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
						rect_efe_number++;
					}
					frame_efe_count--;

					if(rect_efe_number == 1)
					{
						this.rect_number++;
						this.frm_cnt_flg++;
					}
					break;

				case 3:		//槍発射
					if(Sound.music.sp != null){
						Sound.music.SE_play(24);		//鎧2の効果音
					}
					if(frame_count <= 0){
						frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
						this.rect_number++;
					}
					frame_count--;

					//やりの位置を動かす
					switch(this.skill_dir){
						case 0: //前
							if(skill_y <= (GameMain.mSta_p[3].mY + 0.25f * 3.75)){
								skill_y += 0.25f;
							}
							break;
						case 1:	//右
							if(skill_x >= (GameMain.mSta_p[3].mX - 0.25f * 3.75)){
								skill_x -= 0.25;
							}
							break;
						case 2:	//左
							if(skill_x <= (GameMain.mSta_p[3].mX + 0.25f * 3.75)){
								skill_x += 0.25f;
							}
							break;
						case 3:	//後
							if(skill_y >= (GameMain.mSta_p[3].mY - 0.25f * 3.75)){
								skill_y -= 0.25f;
							}
							break;
					}

					if(rect_number == 3){
						rect_efe_number++;
						this.frm_cnt_flg++;
					}
					break;

				case 4:		//槍が当たる
					if(frame_efe_count <= 0){
						frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
						rect_efe_number++;
					}
					frame_efe_count--;

					if(rect_efe_number >= 3){
						rect_efe_number = 3;
						this.frm_cnt_flg++;
					}
					break;

				case 5:	//槍を投げ終える
					if(frame_count <= 0){
						frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
						this.rect_number = 3;
					}
					frame_count--;

					if(rect_number >= 3){
						this.frm_cnt_flg++;
					}
					break;

				case 6:		//終わり
					rect_efe_number = 3;		//初期化
					this.rect_number = 3;
					this.frm_cnt_flg = 0;
					this.skill__flg = 0;		//スキル表示フラグ
					GameMain.mSta_p[no].skill_timing = false;
					//GameMain.mSta_p[no].idou = true;
					GameMain.mSta_p[no].skill_active_flg = false;
					GameMain.mSta_p[no].s = Charstatus.MOVE;
					break;
			}
	}


	/**
	 * 鎧スキル描画　スピア
	 * @param gl
	 * @param texture
	 * @param skill_no
	 */
	public void armor_draw(GL10 gl, int texture, int skill_no) {
		float Angle = (Camera_var.angle - 180);		//向いている方向
		//s_enemy_x
		switch(skill_no){
			case 0:		//衝撃
				//やりを飛ばす
				//槍領域制御--------------------------------------------------------
				if(skill_x > ((GameMain.map_size_x  - 1) * 0.25f) + 0.125f ){
					skill_x = ((GameMain.map_size_x - 1) * 0.25f);
				}

				if(skill_y > ((GameMain.map_size_y  - 1) * 0.25f) + 0.125f){
					skill_y = ((GameMain.map_size_y - 1) * 0.25f);
				}

				if(skill_x < 0.0f ){
					skill_x = 0.0f;
				}

				if(skill_y < 0.0f ){
					skill_y = 0.0f;
				}


				//槍
				gl.glPushMatrix();
				{
					gl.glTranslatef(skill_x, skill_y, 0.102f);
					gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
					gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
					if(skill_dir == 0 || skill_dir == 3){	//やりの向きを調整
						gl.glRotatef(90.0f/2.5f, 0.0f, 1.0f, 0.0f);
					}else{
						gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
						gl.glRotatef(-45.0f, 1.0f, 0.0f, 0.0f);
					}
					gl.glScalef(0.1875f, 0.1875f, 1.0f);

					//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
					GraphicUtil.drawTexture_pixel_custom2(gl, 512.0f, 512.0f, texture,
							rect_efe_armor[skill_dir][rect_efe_number],
							1.0f, 1.0f, 1.0f, 1.0f);
				}
				gl.glPopMatrix();
				break;
		}
	}


	/**
	 *	//大剣スキル　衝撃波
	 * @param no
	 */
	public void large_sword_move(int no){
		//スキル表示の流れの制御
		switch(this.frm_cnt_flg){
			case 0:
				rect_efe_number = 3;
				this.rect_number = 3;
				skill_acc = 0.0f;
				this.skill_x = GameMain.mSta_p[no].mX;
				this.skill_y = GameMain.mSta_p[no].mY;
				switch(skill_dir){
					case 0: //前
						skill_y += 0.25f;
						break;
					case 1:	//右
						skill_x -= 0.25f;
						break;
					case 2:	//左
						skill_x += 0.25f;
						break;
					case 3:	//後
						skill_y -= 0.25f;
						break;
				}

				this.frm_cnt_flg++;
				break;

			case 1:			//剣を構える
				if(Sound.music.sp != null){
					Sound.music.SE_play(25);		//大剣1の効果音
				}
				if(rect_number == 3){
					frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					rect_number = 0;
				}

				if(frame_count <= 0){
					frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					this.rect_number++;
				}
				frame_count--;

				if(this.rect_number == 1){
					this.frm_cnt_flg++;
				}

				break;

			case 2:			//剣を切り下げる
				if(rect_efe_number == 3){
					frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					rect_efe_number = 0;
				}

				if(frame_count <= 0){
					frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					this.rect_number++;
				}
				frame_count--;

				if(this.rect_number == 2){
					rect_efe_number = 2;
					this.frm_cnt_flg++;
				}
				break;

			case 3:		//衝撃波発射
				if(Sound.music.sp != null){
					Sound.music.SE_play(26);		//大剣2の効果音
				}
				if(frame_count <= 0){
					frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					this.rect_number++;
				}
				frame_count--;


				skill_acc += 0.025f;
				switch(this.skill_dir){
					case 0: //前
						if(skill_y <= (GameMain.mSta_p[4].mY + 0.25f * 4.75f)){
							skill_y += skill_acc;
						}
						break;
					case 1:	//右
						if(skill_x >= (GameMain.mSta_p[4].mX - 0.25f * 4.75f)){
							skill_x -= skill_acc;
						}
						break;
					case 2:	//左
						if(skill_x <= (GameMain.mSta_p[4].mX + 0.25f * 4.75f)){
							skill_x += skill_acc;
						}
						break;
					case 3:	//後
						if(skill_y >= (GameMain.mSta_p[4].mY - 0.25f * 4.75f)){
							skill_y -= skill_acc;
						}
						break;
				}

				if(rect_number > 2){
					this.frm_cnt_flg++;
				}

				break;

			case 4:		//衝撃波を出し終わった
				if(Sound.music.sp != null){
					Sound.music.SE_play(27);		//大剣3の効果音
				}
				if(frame_efe_count <= 0){
					frame_efe_count = skill_efect_frame[no][(rect_efe_number+1) % skill_efect_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					rect_efe_number++;
				}
				frame_efe_count--;

				if(rect_efe_number == 3){
					this.frm_cnt_flg++;
				}
				break;

			case 5:		//剣を構えなおす
				if(frame_count <= 0){
					frame_count = skill_frame[no][(this.rect_number+1) % skill_frame[no].length];		//各キャラクターのアタックframeの要素数で割った値
					this.rect_number++;
				}
				frame_count--;

				if(this.rect_number >= 3){
					this.frm_cnt_flg++;
				}
				break;

			case 6:		//終わり
				this.frm_cnt_flg = 0;
				this.skill__flg = 0;		//スキル表示フラグ
				GameMain.mSta_p[no].skill_timing = false;
				//GameMain.mSta_p[no].idou = true;
				GameMain.mSta_p[no].skill_active_flg = false;
				GameMain.mSta_p[no].s = Charstatus.MOVE;
				break;
		}
	}


	/**
	 * 大剣スキル描画　衝撃波
	 * @param gl
	 * @param texture
	 * @param skill_no
	 */
	public void large_sword_draw(GL10 gl, int texture, int skill_no) {
		float Angle = (Camera_var.angle - 180);		//向いている方向

		switch(skill_no){
			case 0:		//衝撃波
				//衝撃領域制御--------------------------------------------------------
				if(skill_x > ((GameMain.map_size_x  - 1) * 0.25f) + 0.125f ){
					skill_x = ((GameMain.map_size_x - 1) * 0.25f);
				}

				if(skill_y > ((GameMain.map_size_y  - 1) * 0.25f) + 0.125f){
					skill_y = ((GameMain.map_size_y - 1) * 0.25f);
				}

				if(skill_x < 0.0f ){
					skill_x = 0.0f;
				}

				if(skill_y < 0.0f ){
					skill_y = 0.0f;
				}




				//衝撃波
				gl.glPushMatrix();
				{
					gl.glTranslatef(skill_x, skill_y, 0.02f);
					gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
					gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
					gl.glScalef(0.375f, 0.375f, 1.0f);

					//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
					GraphicUtil.drawTexture_pixel_custom2(gl, 512.0f, 512.0f, texture,
							rect_efe_large_sword[skill_dir][rect_efe_number],
							1.0f, 1.0f, 1.0f, 1.0f);
				}
				gl.glPopMatrix();
				break;
		}
	}






	/**
	 *キャラクターのアイテム使用時の処理
	 */
	public static void item_char(){
		if(item_point_flg == 0 && TitleActivity.total_score < Item_point.RECOVERY) return;

		if(!GameMain.mSta_p[GameMain.select_char].active_flg ||
				GameMain.mSta_p[GameMain.select_char].Hp >= GameMain.mSta_p[GameMain.select_char].MaxHp) {		//死んでたら使えない
			return;
		}

		if(!GameMain.mSta_p[GameMain.select_char].item_timing){
			if(GameInfo.item_fling == 2){
				GameMain.mSta_p[GameMain.select_char].item_timing = true;	//回復アイテムボタンのタッチ判定（11番目：ウィンドウの右端）
				itemscore_flg = true;
				itemdraw_flg= true;
				GameInfo.fling_chk = -1;		//何も指定しない
			}
		}

		if(itemscore_flg){
			//recovery_hp =  GameMain.mSta_p[GameMain.select_char].MaxHp - GameMain.mSta_p[GameMain.select_char].Hp;	//回復分のHPを計算する
			recovery_hp = 200;
			TitleActivity.total_score-= Item_point.RECOVERY ;		//アイテムを使ったのでスコアから引く
			item_point_flg = 1;
			itemscore_flg = false;
			if(Sound.music.sp != null){
				Sound.music.SE_play(7);		//回復SE
			}
		}

		if(GameMain.mSta_p[GameMain.select_char].item_timing){
			switch(GameInfo.item_number){
				case 0:	//回復アイテム
					GameMain.mSta_p[GameMain.select_char].Hp += 10;	//回復中
					recovery_hp -= 10;
					if(recovery_hp <= 0 || GameMain.mSta_p[GameMain.select_char].Hp >= GameMain.mSta_p[GameMain.select_char].MaxHp){	//回復完了
						GameMain.mSta_p[GameMain.select_char].item_timing = false;
						item_point_flg = 0;
						GameInfo.item_fling = -1;

						if(GameMain.mSta_p[GameMain.select_char].Hp >= GameMain.mSta_p[GameMain.select_char].MaxHp){
							GameMain.mSta_p[GameMain.select_char].Hp = GameMain.mSta_p[GameMain.select_char].MaxHp;
						}
					}

					break;
			}
		}
	}




	public static void  recovery_move(){
		recovery_cnt--;

		if(recovery_cnt <= 0){
			recovery_x -= 0.01f;
			recovery_y -= 0.01f;
			recovery_fade -= 0.04f;
			recovery_cnt = 5;
		}

		if(recovery_x <= -0.25f || recovery_y < -0.25f){
			itemdraw_flg = false;
			recovery_fade = 1.0f;
			recovery_x = 0.125f;
			recovery_y = 0.125f;
			recovery_cnt = 5;
		}


	}


	//回復アイテム使用時の描画
	public static void recovery_draw(GL10 gl, int texture){
		float Angle = (Camera_var.angle - 180);		//向いている方向

		gl.glPushMatrix();
		{
			gl.glTranslatef(GameMain.mSta_p[GameMain.select_char].mX + recovery_x , GameMain.mSta_p[GameMain.select_char].mY - recovery_y, 0.411f);
			gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
			gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
			//gl.glScalef(size, size, 1.0f);
			gl.glScalef(0.25f, 0.25f, 1.0f);

			//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
			GraphicUtil.drawTexture_pixel_custom2(gl, 32.0f, 32.0f, texture,
					new float[]{0.0f,0.0f,32.0f, 32.0f,32.0f/2, 32.0f},
					1.0f, 1.0f, 1.0f, recovery_fade);
		}
		gl.glPopMatrix();
	}



	/**
	 * 	スキルカットイン描画
	 * @param gl
	 * @param texture
	 * @param no
	 */
	public void skill_cutin_draw(GL10 gl, int texture, int no){
		if(GameMain.mSta_p[no].skill_cutin_frame < 0 ) {
			Cursor_var.x = GameMain.mSta_p[no].mX;
			Cursor_var.y = GameMain.mSta_p[no].mY;
			Skill.skill_cutin_flg = -1;
			GameMain.mSta_p[no].skill_cutin_timing = false;
			TouchManagement.skill_cut_touch = false;
			return;
		}

		if( GameMain.mSta_p[no].skill_cutin_frame >= 20 &&
			skill_cutin_pos_x < 240.0f)
			skill_cutin_pos_x = 240.0f;
		gl.glPushMatrix();
		{
			GraphicUtil.glTranslatef_pixel(gl, skill_cutin_pos_x, 400.0f, -0.0f);		//座標位置
			gl.glScalef(4.0f, 4.0f, 1.0f);	//画像の大きさ指定

			GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 512.0f,  texture, rect_skill_cutin[GameMain.select_char][skill_cutin_cont],
												1.0f, 1.0f, 1.0f, skill_cutin_alpha);
		}
		gl.glPopMatrix();

}

	/**
	 *  スキルカットイン制御
	 * @param no
	 */
	public void skill_cutin_exe(int no){

		if(GameMain.mSta_p[no].skill_cutin_frame < 0 ) {
			return;
		}

		if(GameMain.mSta_p[no].skill_cutin_frame >= 20 )
		{
			skill_cutin_alpha += 0.09f;
			if(skill_cutin_alpha > 1.0f){
				skill_cutin_alpha = 1.0f;
			}
		}else{
			skill_cutin_alpha -= 0.07f;
			if(skill_cutin_alpha < 0.0f){
				skill_cutin_alpha = 0.0f;
			}
		}
		skill_cutin_pos_x -= 20.0f;

		if((GameMain.mSta_p[no].skill_cutin_frame % 2) == 0)
			skill_cutin_cont = 1 - skill_cutin_cont;

		GameMain.mSta_p[no].skill_cutin_frame--;

	}



}
