package com.fantasy_strategy;


import java.math.BigDecimal;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;


public class Status_enemy {

	//-----------------------------------------------------------
	//				変数宣言
	//-----------------------------------------------------------
	private int mMap[][] = Map_Resource.map;

	//MapMoveを呼び出す
	MapMove mMapMove = new MapMove();

	public static Status_player[] mSta_p = GameMain.mSta_p;

	public int map_posY, map_posX;			//移動先

	//キャラクターのパラメータ
	public float mX, mY;	//位置

	public int No;			//個別番号
	public float Speed;		//移動距離(速度)
	public int Joutai;		//状態値（行動パターン）
	public String Name;		//名前
	public int Level;		//レベル
	public int MaxHp;		//MAXHP
	public int Hp;			//HP
	public int Pow;			//攻撃力
	public int Def;			//防御力
	public int M_Def;		//魔法防御
	public int Avoid;		//回避率（％）
	public boolean active_flg;	//敵キャラクターが有効か？
	public boolean die_flg = false;		//死亡フラグ
	public int score;		//敵のスコア


	private float rect_enemy[][][][];	//フロート型
	public boolean state_flg;	//停止条件	private float rect_enemy[][][];	//フロート型
	private boolean move_stop = false;	//攻撃時移動停止フラグ
	private float texture_width;	//幅
	private float texture_height;	//高さ
	private int work = 0;		//適当な変数
	private int work_move = 0;		//移動用の大まかな時間
	private boolean sraim2_syujin_flg = false;
	private double sraim2_syujin = 0;
	private double work_rnd = Math.floor( Math.random() * 4 );
	private boolean snag_flg = false;

	int attack_cnt = 0;
	public int damage_anime = 0;	//食らった時アニメーションする
	private float red_col = 1.0f;
	private float Xswing = 0;	//キャラクター揺れる
	private float Xswing_width = 0.05f;	//揺れ幅
	private int swing = 0;	//揺れ方向

	public int texture = -1;
	private int scaleflg = -1;
	/*
	 * 移動を判別する
	 *
	 * -1 移動停止
	 * 0 移動を継続（1,3,4は、最初だけ実行され、0で移動を行う）
	 * 1 追う
	 * 2 行ったり来たり
	 * 3 近づき、一定間隔を保つ
	 * 4 逃げる
	 */
	public int move_type;

	public int point;
	public boolean idou = true;

	private int rect_mode;
	public int rect_numdir;		//移動方向決定用
	public int rect_numdir2 = 0;		//移動方向決定用
	private char dir_char = 0;		//移動方向決定用チェック
	private int play_num;

	private int vec;
	//ひずみ矩形用
	public static float[][][][]distotion_rect = {
		{
			{
				{  0.0f,  0.0f, 64.0f, 64.0f, 32.0f, 32.0f},
				{ 64.0f,  0.0f, 64.0f, 64.0f, 32.0f, 32.0f},
				{  0.0f, 64.0f, 64.0f, 64.0f, 32.0f, 32.0f},
				{ 64.0f, 64.0f, 64.0f, 64.0f, 32.0f, 32.0f}
			}
		},
	};

	//スライム移動方向矩形用
	public static float[][][][]sraim_Direct = {
		//MOVE
		{
			{
				{  0.0f, 96.0f, 32.0f, 32.0f, 16.0f, 32.0f},	//左上（png上から見て）
				{ 32.0f, 96.0f, 32.0f, 32.0f, 16.0f, 32.0f},
			},
			{
				{  0.0f, 64.0f, 32.0f, 32.0f, 16.0f, 32.0f}, 	//左下
				{ 32.0f, 64.0f, 32.0f, 32.0f, 16.0f, 32.0f},
			},
			{
				{  0.0f, 32.0f, 32.0f, 32.0f, 16.0f, 32.0f},	//右上
				{ 32.0f, 32.0f, 32.0f, 32.0f, 16.0f, 32.0f},
			},
			{
				{  0.0f,  0.0f, 32.0f, 32.0f, 16.0f, 32.0f},	//右下
					{ 32.0f,  0.0f, 32.0f, 32.0f, 16.0f, 32.0f},
				},
			},
			//ATTACK
			{
				{
					{  0.0f, 96.0f, 32.0f, 32.0f, 16.0f, 32.0f},	//左上（png上から見て）
				{ 32.0f, 96.0f, 32.0f, 32.0f, 16.0f, 32.0f},
			},
			{
				{  0.0f, 64.0f, 32.0f, 32.0f, 16.0f, 32.0f}, 	//左下
				{ 32.0f, 64.0f, 32.0f, 32.0f, 16.0f, 32.0f},
			},
			{
				{  0.0f, 32.0f, 32.0f, 32.0f, 16.0f, 32.0f},	//右上
				{ 32.0f, 32.0f, 32.0f, 32.0f, 16.0f, 32.0f},
			},
			{
				{  0.0f,  0.0f, 32.0f, 32.0f, 16.0f, 32.0f},	//右下
				{ 32.0f,  0.0f, 32.0f, 32.0f, 16.0f, 32.0f},
			},
		},
	};

	public static float[][][][]kinoko_Direct = {
		//MOVE
		{
			{
				{  0.0f, 128.0f, 64.0f, 64.0f, 32.0f, 64.0f},	//左上
				{ 64.0f, 128.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 128.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
			{
				{  0.0f, 0.0f, 64.0f, 64.0f, 32.0f, 64.0f},		//左下
				{ 64.0f, 0.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 0.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
			{
				{  0.0f, 192.0f, 64.0f, 64.0f, 32.0f, 64.0f},	//右上
				{ 64.0f, 192.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 192.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
			{
				{  0.0f, 64.0f, 64.0f, 64.0f, 32.0f, 64.0f}, 	//右下
				{ 64.0f, 64.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 64.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},


		},
		//ATTACK
		{
			{
				{  0.0f, 384.0f, 64.0f, 64.0f, 32.0f, 64.0f},	//左上
				{ 64.0f, 384.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 384.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
			{
				{  0.0f, 256.0f, 64.0f, 64.0f, 32.0f, 64.0f},	//左下
				{ 64.0f, 256.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 256.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
			{
				{  0.0f, 448.0f, 64.0f, 64.0f, 32.0f, 64.0f},	//右上
				{ 64.0f, 448.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 448.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
			{
				{  0.0f, 320.0f, 64.0f, 64.0f, 32.0f, 64.0f}, 	//右下
				{ 64.0f, 320.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 320.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
		}
	};

	public static float[][][][]niwatori_Direct = {
												//MOVE
		{
			{
				{  0.0f, 128.0f, 64.0f, 64.0f, 32.0f, 54.0f},	//左上
				{ 64.0f, 128.0f, 64.0f, 64.0f, 32.0f, 54.0f},
				{128.0f, 128.0f, 64.0f, 64.0f, 32.0f, 54.0f},
			},
			{
				{  0.0f, 0.0f, 64.0f, 64.0f, 32.0f, 54.0f},		//左下
				{ 64.0f, 0.0f, 64.0f, 64.0f, 32.0f, 54.0f},
				{128.0f, 0.0f, 64.0f, 64.0f, 32.0f, 54.0f},
			},
			{
				{  0.0f, 192.0f, 64.0f, 64.0f, 32.0f, 54.0f},	//右上
				{ 64.0f, 192.0f, 64.0f, 64.0f, 32.0f, 54.0f},
				{128.0f, 192.0f, 64.0f, 64.0f, 32.0f, 54.0f},
			},
			{
				{  0.0f, 64.0f, 64.0f, 64.0f, 32.0f, 54.0f}, 	//右下
				{ 64.0f, 64.0f, 64.0f, 64.0f, 32.0f, 54.0f},
				{128.0f, 64.0f, 64.0f, 64.0f, 32.0f, 54.0f},
			},


		},
		//ATTACK
		{
			{
				{  0.0f, 384.0f, 64.0f, 64.0f, 32.0f, 64.0f},	//左上
				{ 64.0f, 384.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 384.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
			{
				{  0.0f, 256.0f, 64.0f, 64.0f, 32.0f, 64.0f},	//左下
				{ 64.0f, 256.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 256.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
			{
				{  0.0f, 448.0f, 64.0f, 64.0f, 32.0f, 64.0f},	//右上
				{ 64.0f, 448.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 448.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
			{
				{  0.0f, 320.0f, 64.0f, 64.0f, 32.0f, 64.0f}, 	//右下
				{ 64.0f, 320.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 320.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
		}
	};

	public static float[][][][]DRAGON_Direct = {
		//MOVE
		{
			{
				{  0.0f, 384.0f, 128.0f, 128.0f, 64.0f, 128.0f},	//右下
				{128.0f, 384.0f, 128.0f, 128.0f, 64.0f, 128.0f},
				{256.0f, 384.0f, 128.0f, 128.0f, 64.0f, 128.0f},
			},
			{
				{  0.0f, 128.0f, 128.0f, 128.0f, 64.0f, 128.0f}, 	//右上
				{128.0f, 128.0f, 128.0f, 128.0f, 64.0f, 128.0f},
				{256.0f, 128.0f, 128.0f, 128.0f, 64.0f, 128.0f},
			},
			{
				{  0.0f, 256.0f, 128.0f, 128.0f, 64.0f, 128.0f},	//左下
				{128.0f, 256.0f, 128.0f, 128.0f, 64.0f, 128.0f},
				{256.0f, 256.0f, 128.0f, 128.0f, 64.0f, 128.0f},
			},
			{
				{  0.0f, 0.0f, 128.0f, 128.0f, 64.0f, 128.0f},	//左上
				{128.0f, 0.0f, 128.0f, 128.0f, 64.0f, 128.0f},
				{256.0f, 0.0f, 128.0f, 128.0f, 64.0f, 128.0f},
			},
		},
		//ATTACK
		{
			{
				{  0.0f, 896.0f, 128.0f, 128.0f, 64.0f, 128.0f},	//右下
				{128.0f, 896.0f, 128.0f, 128.0f, 64.0f, 128.0f},
				{256.0f, 896.0f, 128.0f, 128.0f, 64.0f, 128.0f},
			},
			{
				{  0.0f, 640.0f, 128.0f, 128.0f, 64.0f, 128.0f}, 	//右上
				{128.0f, 640.0f, 128.0f, 128.0f, 64.0f, 128.0f},
				{256.0f, 640.0f, 128.0f, 128.0f, 64.0f, 128.0f},
			},
			{
				{  0.0f, 768.0f, 128.0f, 128.0f, 64.0f, 128.0f},	//左下
				{128.0f, 768.0f, 128.0f, 128.0f, 64.0f, 128.0f},
				{256.0f, 768.0f, 128.0f, 128.0f, 64.0f, 128.0f},
			},
			{
				{  0.0f, 512.0f, 128.0f, 128.0f, 64.0f, 128.0f},	//左上
				{128.0f, 512.0f, 128.0f, 128.0f, 64.0f, 128.0f},
				{256.0f, 512.0f, 128.0f, 128.0f, 64.0f, 128.0f},
			},
		}
	};

	public static float[][][][]goburin_Direct = {
		//MOVE
		{
			{
				{  0.0f, 128.0f, 64.0f, 64.0f, 32.0f, 64.0f},	//左上
				{ 64.0f, 128.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 128.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
			{
				{  0.0f, 0.0f, 64.0f, 64.0f, 32.0f, 64.0f},		//左下
				{ 64.0f, 0.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 0.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
			{
				{  0.0f, 192.0f, 64.0f, 64.0f, 32.0f, 64.0f},	//右上
				{ 64.0f, 192.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 192.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
			{
				{  0.0f, 64.0f, 64.0f, 64.0f, 32.0f, 64.0f}, 	//右下
				{ 64.0f, 64.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 64.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
		},
		//ATTACK
		{
			{
				{  0.0f, 384.0f, 64.0f, 64.0f, 32.0f, 64.0f},	//左上
				{ 64.0f, 384.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 384.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
			{
				{  0.0f, 256.0f, 64.0f, 64.0f, 32.0f, 64.0f},	//左下
				{ 64.0f, 256.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 256.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
			{
				{  0.0f, 448.0f, 64.0f, 64.0f, 32.0f, 64.0f},	//右上
				{ 64.0f, 448.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 448.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
			{
				{  0.0f, 320.0f, 64.0f, 64.0f, 32.0f, 64.0f}, 	//右下
				{ 64.0f, 320.0f, 64.0f, 64.0f, 32.0f, 64.0f},
				{128.0f, 320.0f, 64.0f, 64.0f, 32.0f, 64.0f},
			},
		}
	};



	/*-----------------------------------------------------------
	 *キャラクタパラメータ{Lv, HP, 攻撃力, 防御力, 魔防力, 移動速度, 回避率(％)}
	 *---------------------------------------------------------*/
	private int[] sraim = 		{ 1, 120,  8,  4, 10, 10,  1};		//スライム
	private int[] kinoko = 		{ 3, 300, 12,  8, 20, 10,  5};		//きのこ
	private int[] distotion = 	{ 9,1000,  0,  0, 66,  0,  0};		//ヒズミ
	private int[] niwatori = 	{ 5, 250, 14,  8, 10, 10,  7};		//にわとり
	private int[] DRAGON = 		{ 5, 500, 20, 16, 10, 10,  5};		//ドラゴン
	private int[] goburin =		{ 5, 300, 16, 12, 10, 10,  7};		//ゴブリン
	private int[] sraim2 = 		{ 4, 200, 10,  4, 10, 10,  2};		//スライム


	float Angle_i = 0;	//テスト回転用

	/*************************************************************
	 * 生成、初期化処理
	 *************************************************************/
	public Status_enemy(){
		this.Name = "";
		this.Level  	= -1;
		this.MaxHp		= -1;
		this.Hp			= -1;
		this.Pow		= -1;
		this.Def		= -1;
		this.M_Def		= -1;
		this.Avoid		= -1;
		this.Speed		= -1;
		this.move_type	= -1;
		this.active_flg = false;
	}






	/*************************************************************
	 * 呼び出し元から、各キャラクターのパラメーターを受け取る
	 * @param x
	 * @param y
	 * @param no
	 * @param joutai
	 * @param texture
	 * @param scaleflg
	 *************************************************************/
	public boolean Status_enemy_reset(float x, float y, int no, int joutai)
	{
		this.texture    = no;
		this.state_flg	= true;
		this.mY = y;
		this.mX = x;
		this.No = no;
		this.rect_mode = 0;
		this.rect_numdir = 0;
		this.rect_numdir2 = 0;
		this.move_stop = false;
		this.work = 0;
		this.work_move = 0;
		this.attack_cnt = 0;
		this.damage_anime = 0;
		this.red_col = 1.0f;
		this.Xswing = 0;			//キャラクター揺れる
		this.Xswing_width = 0.05f;	//揺れ幅
		this.swing = 0;				//揺れ方向
		this.point=0;				//移動初期化

		//3D座標からマップ座標へ変換
		this.map_posY = (int) ((this.mY + 0.0f) / 0.25f);	//1.25f
		this.map_posX = (int) ((this.mX + 0.0f) / 0.25f);


		//noでキャラのパラメタを設定
		switch(this.No){
		case Enemy_types.HIZUMI:
			this.Name = "ヒズミ";
			this.Level = distotion[0];
			this.MaxHp = distotion[1];
			this.Hp = distotion[1];
			this.Pow = distotion[2];
			this.Def = distotion[3];
			this.M_Def = distotion[4];
			this.Avoid = distotion[6];
			this.Speed = 400.0f;			//ひずみにスピードは関係ないので、敵の出るタイミング制御に使う
			this.move_type = 0;
			this.rect_enemy = distotion_rect;
			this.texture_width = 128.0f;
			this.texture_height = 128.0f;
			this.score = Enemy_score.HIZUMI;
			break;
		case Enemy_types.SLIME:
			this.Name = "スライム";
			this.Level = sraim[0];
			this.MaxHp = sraim[1];
			this.Hp = sraim[1];
			this.Pow = sraim[2];
			this.Def = sraim[3];
			this.M_Def = sraim[4];
			this.Avoid = sraim[6];
			this.Speed = 0.25f / 15.0f;
			this.move_type = 0;
			this.rect_enemy = sraim_Direct;
			this.texture_width = 64.0f;
			this.texture_height =128.0f;
			this.score = Enemy_score.SLIME;
			break;
		case Enemy_types.KINOKO:
			this.Name = "きのこ";
			this.Level = kinoko[0];
			this.MaxHp = kinoko[1];
			this.Hp = kinoko[1];
			this.Pow = kinoko[2];
			this.Def = kinoko[3];
			this.M_Def = kinoko[4];
			this.Avoid = kinoko[6];
			this.Speed = 0.25f / 20.0f;
			this.move_type = 0;
			this.rect_enemy = kinoko_Direct;
			this.texture_width = 256.0f;
			this.texture_height =512.0f;
			this.score = Enemy_score.KINOKO;
			break;
		case Enemy_types.NIWATORI:
			this.Name = "にわとり";
			this.Level = niwatori[0];
			this.MaxHp = niwatori[1];
			this.Hp = niwatori[1];
			this.Pow = niwatori[2];
			this.Def = niwatori[3];
			this.M_Def = niwatori[4];
			this.Avoid = niwatori[6];
			this.Speed = 0.25f / 20.0f;
			this.move_type = 0;
			this.rect_enemy = niwatori_Direct;
			this.texture_width = 256.0f;
			this.texture_height =512.0f;
			this.score = Enemy_score.NIWATORI;

			break;
		case Enemy_types.DRAGON:
			this.Name = "ドラゴン";
			this.Level = DRAGON[0];
			this.MaxHp = DRAGON[1];
			this.Hp = DRAGON[1];
			this.Pow = DRAGON[2];
			this.Def = DRAGON[3];
			this.M_Def = DRAGON[4];
			this.Avoid = DRAGON[6];
			this.Speed = 0.25f / 20.0f;
			this.move_type = 0;
			this.rect_enemy = DRAGON_Direct;
			this.texture_width = 512.0f;
			this.texture_height =1024.0f;
			this.score = Enemy_score.DRAGON;

			break;
		case Enemy_types.GOBURIN:
			this.Name = "ゴブリン";
			this.Level = goburin[0];
			this.MaxHp = goburin[1];
			this.Hp = goburin[1];
			this.Pow = goburin[2];
			this.Def = goburin[3];
			this.M_Def = goburin[4];
			this.Avoid = goburin[6];
			this.Speed = 0.25f / 20.0f;
			this.move_type = 0;
			this.rect_enemy = goburin_Direct;
			this.texture_width = 256.0f;
			this.texture_height =512.0f;
			this.score = Enemy_score.GOBURIN;

			break;


		case Enemy_types.SLIME2:
			this.Name = "スライム2";
			this.Level = sraim2[0];
			this.MaxHp = sraim2[1];
			this.Hp = sraim2[1];
			this.Pow = sraim2[2];
			this.Def = sraim2[3];
			this.M_Def = sraim2[4];
			this.Avoid = sraim2[6];
			this.Speed = 0.25f / 20.0f;
			this.move_type = 0;
			this.rect_enemy = sraim_Direct;
			this.texture_width = 64.0f;
			this.texture_height =128.0f;
			this.score = Enemy_score.SLIME;
			this.sraim2_syujin_flg = false;
			this.sraim2_syujin = 0;
			break;
	}

		//その場に誰もいなければ、発生するよ
		for(int i = 0; i < GameMain.PLAYER_NUM; i++){
			if(  (int) ((mSta_p[i].mX + 0.125f)/0.25f) == this.map_posY &&   (int) ((mSta_p[i].mY + 0.125f)/0.25f) == this.map_posX){
				return false;
			}
		}
		for(int i = 0; i < GameMain.ENEMY_MAX; i++){
			if(GameMain.mSta_e[i].active_flg){
				if( GameMain.mSta_e[i].map_posY == this.map_posY &&   GameMain.mSta_e[i].map_posX == this.map_posX){
					this.active_flg = false;
					return false;
				}
			}
		}
		this.active_flg = true;
		return true;

	}





	/*************************************************************
	 * 		標的を描画します
	 *************************************************************/
	public void draw(GL10 gl,int texture, int get_i) {

		float Angle = (Camera_var.angle - 180);		//向いている方向

		float gauge_hight = 0;
		switch(this.No)
		{
			case Enemy_types.HIZUMI:
				gl.glPushMatrix();
				{
					gl.glTranslatef(mX+Xswing, mY, 0.002f);
					//gl.glRotatef(0.0f, 0.0f, 0.0f, 1.0f);
					//gl.glRotatef(0.0f, 1.0f, 0.0f, 0.0f);

					//gl.glScalef(0.5f, 0.5f, 1.0f);
					//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
					GraphicUtil.drawTexture_pixel_custom3(gl, this.texture_width, this.texture_height, texture,
							this.rect_enemy[0][0][rect_numdir],
							1.0f, red_col, red_col, 1.0f);
				}
				gl.glPopMatrix();

				//HPバー表示
				{
					//ゲージの色選択
					int bar_tex;
					if(GameInfo.hp[1][get_i] >= 0.6){
						bar_tex = GameMain.mBar_green;
					}else if(GameInfo.hp[1][get_i] >= 0.2){
						bar_tex = GameMain.mBar_yellow;
					}else{
						bar_tex = GameMain.mBar_red;
					}
					//ゲージの表示
					gl.glPushMatrix();
					{
						gl.glTranslatef(mX+Xswing +0.08f, mY -0.08f, 0.2f);
						gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
						gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
						gl.glScalef(0.55f, 0.4f, 1.01f);	//画像の大きさ指定

						GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 64.0f, bar_tex,
								new float[]{23.0f,20.0f,191.0f * GameInfo.hp[1][get_i],28.0f,95.5f,14.0f},
								1.0f,1.0f, 1.0f, 1.0f);

					}
					gl.glPopMatrix();

					//HPゲージの表示
					gl.glPushMatrix();
					{
						gl.glTranslatef(mX+Xswing +0.08f, mY -0.08f, 0.2f);
						gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
						gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
						gl.glScalef(0.55f, 0.3f, 1.0f);	//画像の大きさ指定

						GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 64.0f, GameMain.mGauge,
								new float[]{21.0f,18.0f,195.0f,32.0f,97.5f,16.0f},
								1.0f,1.0f, 1.0f, 1.0f);

					}
					gl.glPopMatrix();
				}
				break;

			case Enemy_types.SLIME:
			case Enemy_types.SLIME2:
				gauge_hight = 0.0f;				//HPゲージの高さを調整
				break;

			case Enemy_types.KINOKO:
				gauge_hight = 0.10f;			//HPゲージの高さを調整
				break;

			case Enemy_types.NIWATORI:
				gauge_hight = 0.15f;			//HPゲージの高さを調整
				break;

			case Enemy_types.DRAGON:
				gauge_hight = 0.28f;			//HPゲージの高さを調整
				break;

			case Enemy_types.GOBURIN:
				gauge_hight = 0.20f;			//HPゲージの高さを調整
				break;

		}

		switch(this.No){
			case Enemy_types.SLIME:
			case Enemy_types.SLIME2:
			case Enemy_types.KINOKO:
			case Enemy_types.NIWATORI:
			case Enemy_types.DRAGON:
			case Enemy_types.GOBURIN:
				gl.glPushMatrix();
				{
					gl.glTranslatef(mX+Xswing, mY, 0.01f);
					gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
					gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
					gl.glScalef(0.25f, 0.25f, 1.0f);
					GraphicUtil.drawTexture_pixel_custom2(gl, this.texture_width, this.texture_height, texture,
									this.rect_enemy[rect_mode][rect_numdir][rect_numdir2],
									1.0f, red_col, red_col, 1.0f);
				}
				gl.glPopMatrix();

				//HPバー表示
				{
					//ゲージの色選択
					int bar_tex;
					if(GameInfo.hp[1][get_i] >= 0.6){
						bar_tex = GameMain.mBar_green;
					}else if(GameInfo.hp[1][get_i] >= 0.2){
						bar_tex = GameMain.mBar_yellow;
					}else{
						bar_tex = GameMain.mBar_red;
					}
					//緑ゲージの表示
					gl.glPushMatrix();
					{
						gl.glTranslatef(mX+Xswing+gauge_hight, mY-gauge_hight, 0.26f);
						gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
						gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
						gl.glScalef(0.12f, 0.22f, 1.01f);	//画像の大きさ指定

						GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 64.0f, bar_tex,
										new float[]{23.0f,20.0f,191.0f * GameInfo.hp[1][get_i],28.0f,95.5f,14.0f},
										1.0f,1.0f, 1.0f, 1.0f);

					}
					gl.glPopMatrix();

					//HPゲージの表示
					gl.glPushMatrix();
					{
						gl.glTranslatef(mX+Xswing+gauge_hight, mY-gauge_hight, 0.26f);
						gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
						gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
						gl.glScalef(0.12f, 0.22f, 1.0f);	//画像の大きさ指定

						GraphicUtil.drawTexture_pixel_custom(gl, 256.0f, 64.0f, GameMain.mGauge,
										new float[]{21.0f,18.0f,195.0f,32.0f,97.5f,16.0f},
										1.0f,1.0f, 1.0f, 1.0f);

					}
					gl.glPopMatrix();
				}
				break;
		}

		Angle_i += 5.0f;
	}





	static final int y = 0;
	static final int x = 1;

	/*************************************************************
	 * 		標的を移動させます
	 *************************************************************/
	public void move() {
		rect_mode = 0;			//移動・攻撃アニメーションを切り替える
		int[] pos = new int[2];
		pos[x] = map_posX;
		pos[y] = map_posY;

		//フレーム制御
		switch(this.No){
			case Enemy_types.HIZUMI:
				work++;		//フレーム制御
				work %= 7;
				if(work == 0){
					rect_numdir++;
					rect_numdir %= 4;
				}
				break;

			case Enemy_types.SLIME:
			case Enemy_types.SLIME2:
				work++;
				work %= 7;
				if(work == 0){
					rect_numdir2++;
					rect_numdir2 %= 2;
				}
				break;

			case Enemy_types.KINOKO:
			case Enemy_types.NIWATORI:
			case Enemy_types.DRAGON:
			case Enemy_types.GOBURIN:
				work++;
				work %= 7;
				if(work == 0){
					rect_numdir2++;
					rect_numdir2 %= 3;
				}
				break;
		}

		//移動・特殊行動・攻撃範囲
		switch(this.No)
		{
		//歪--------------------------------------------------------------------------------------------------------------
			case Enemy_types.HIZUMI:
				int ran;
				this.Speed -= 1.0f;			//ひずみにスピードは関係ないので、敵の出るタイミング制御に使う

		        Random rnd = new Random();
		        ran = rnd.nextInt(3);
				if(this.Speed < 0){
					for(int i = 0; i < GameMain.ENEMY_MAX; i++){
						if(!GameMain.mSta_e[i].active_flg){

							if( 1 >= ran)
							{
								if(GameMain.mSta_e[i].Status_enemy_reset(this.mX -  0.25f, this.mY, Enemy_types.SLIME2, 0))
									if(Sound.music.sp != null){
										Sound.music.SE_play(15);	//敵出現時SEの再生
									}
							}else{
								if(GameMain.mSta_e[i].Status_enemy_reset(this.mX -  0.25f, this.mY, Enemy_types.GOBURIN, 0))		//空きを探してきのこを生成
									if(Sound.music.sp != null){
										Sound.music.SE_play(15);	//敵出現時SEの再生
									}
							}

						}
					}
					this.Speed = 400.0f;
				}
				break;

		//スライム--------------------------------------------------------------------------------------------------------------
			case Enemy_types.SLIME:
				Sakuteki_kougeki( pos, 3, 1 );
				break;

			case Enemy_types.KINOKO:
				if(work_move < 60){
					pos[x] =  (int) ((this.mX + 0.125f)/0.25f + 1);			//左へ
				}else if(work_move < 120){
					pos[x]  =  (int) ((this.mX + 0.125f)/0.25f - 1);			//右へ
				}
				work_move++;
				if(work_move == 120){
					work_move = 0;
				}

				Sakuteki_kougeki( pos, 3, 2 );			//遠距離攻撃
				break;

			case Enemy_types.NIWATORI:
				if(work_move < 60){
					pos[x] =  (int) ((this.mX + 0.125f)/0.25f + 1);			//左へ
				}else if(work_move < 120){
					pos[x]  =  (int) ((this.mX + 0.125f)/0.25f - 1);			//右へ
				}
				work_move++;
				if(work_move == 120){
					work_move = 0;
				}

				Sakuteki_kougeki( pos, 3, 1 );
				break;

			case Enemy_types.DRAGON:
				Sakuteki_kougeki( pos, -1, 2);
				break;

			case Enemy_types.GOBURIN:
				//ランダム移動
				if(work_move < 30){
					if(work_rnd < 1){
						pos[x]  =  (int) ((this.mX + 0.125f)/0.25f - 1);			//右へ
					}else if(work_rnd < 2){
						pos[x] =  (int) ((this.mX + 0.125f)/0.25f + 1);				//左へ
					}else if(work_rnd < 3){
						pos[y]  =  (int) ((this.mY + 0.125f)/0.25f + 1);			//上へ
					}else{
						pos[y]  =  (int) ((this.mY + 0.125f)/0.25f - 1);			//下へ
					}
				}
				work_move++;
				if(work_move >= 30){
					work_move = 0;
					work_rnd = Math.floor( Math.random() * 4 );				//ランダム値変更
				}

				Sakuteki_kougeki( pos, 3, 1 );
				break;

			case Enemy_types.SLIME2:

				//ランダム移動
				if(work_move < 30){
					if(work_rnd < 1){
						pos[x]  =  (int) ((this.mX + 0.125f)/0.25f - 1);			//右へ
					}else if(work_rnd < 2){
						pos[x] =  (int) ((this.mX + 0.125f)/0.25f + 1);				//左へ
					}else if(work_rnd < 3){
						pos[y]  =  (int) ((this.mY + 0.125f)/0.25f + 1);			//上へ
					}else if(work_rnd < 4){
						pos[y]  =  (int) ((this.mY + 0.125f)/0.25f - 1);			//下へ
					}else{

						if(!mSta_p[0].die_flg && !mSta_p[1].die_flg && !mSta_p[2].die_flg && !mSta_p[3].die_flg && !mSta_p[4].die_flg){
							do{
								if(!sraim2_syujin_flg){
									sraim2_syujin = Math.floor( Math.random() * 5 );	//0~3
								}

								if( !mSta_p[(int) sraim2_syujin].die_flg  && state_flg )
								{
									//主人公に攻め寄る
									pos[x] =  (int) ((mSta_p[(int) sraim2_syujin].mX + 0.125f)/0.25f);
									pos[y] =  (int) ((mSta_p[(int) sraim2_syujin].mY + 0.125f)/0.25f);
									sraim2_syujin_flg = true;
									break;		//近い距離に主人公の誰かがいた
								}
								sraim2_syujin_flg = false;
							}while(!mSta_p[(int) sraim2_syujin].active_flg  || !state_flg );		//条件を満たしてない場合ループ
						}
					}
				}
				work_move++;
				if(work_move >= 30){
					work_move = 0;
					work_rnd = Math.floor( Math.random() * 7 );				//ランダム値変更0^5
					sraim2_syujin_flg = false;
				}

				Sakuteki_kougeki( pos, 1, 1 );
				break;
		}


		switch(this.No){
			case Enemy_types.HIZUMI:		//歪
				break;

			case Enemy_types.SLIME:
			case Enemy_types.KINOKO:
			case Enemy_types.NIWATORI:
			case Enemy_types.DRAGON:
			case Enemy_types.GOBURIN:
			case Enemy_types.SLIME2:
				dir_char = 0;
				if(pos[y] * 0.25f > this.mY ){
					dir_char |= 0x1;		//敵キャラクター移動向き判断
					if(!move_stop)		this.mY += this.Speed;
				}else if(pos[y] * 0.25f < this.mY ){
					dir_char |= 0x2;		//敵キャラクター移動向き判断
					if(!move_stop)		this.mY -= this.Speed;
				}else{
					dir_char |= 0x0;		//敵キャラクター移動向き判断
				}

				if(pos[x]  * 0.25f > this.mX ){
					dir_char |= 0x4;		//敵キャラクター移動向き判断
					if(!move_stop)		this.mX += this.Speed;
				}else if(pos[x]  * 0.25f < this.mX ){
					dir_char |= 0x8;		//敵キャラクター移動向き判断
					if(!move_stop)		this.mX -= this.Speed;
				}else{
					dir_char |= 0x0;		//敵キャラクター移動向き判断
				}
				break;
		}
		direction_change(dir_char);		//敵キャラクターの向く方向を制御する関数を呼び出す





		/*
		 * damageアニメーション
		 * */
		if(damage_anime == 1){
			if( Xswing_width >= 0 ){
				if(swing++ % 2 == 0){
					Xswing -= Xswing_width;
				}else{
					Xswing += Xswing_width;
					Xswing_width -= 0.005f;
				}
				red_col -= 0.2f;
				if(red_col < 0.5f){
					red_col = 0.5f;
				}
			}else{
				damage_anime = 0;
			}

		}else{
			Xswing = 0;
			swing = 0;
			Xswing_width = 0.05f;
			red_col += 0.2f;
			if(red_col > 1){
				red_col = 1;
			}
		}



		//誤差の修正--------------------------------------------------------------------
		BigDecimal bi = new BigDecimal(String.valueOf(this.mY));
		this.mY =  bi.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
		bi = new BigDecimal(String.valueOf(this.mX));
		this.mX =  bi.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();

		if(this.mY % 0.25f < 0.01f){
			this.mY = (int)(this.mY / 0.25f) * 0.25f;
		}else if(this.mY % 0.25f > 0.24f){
			this.mY = ((int)(this.mY / 0.25f)+1) * 0.25f;
		}

		if(this.mX % 0.25f < 0.01f){
			this.mX = (int)(this.mX / 0.25f) * 0.25f;
		}else if(this.mX % 0.25f > 0.24f){
			this.mX = ((int)(this.mX / 0.25f)+1) * 0.25f;
		}

		//----------------------------------------------------------------------------

		int collision[] = {0,0,0,0};
		collision[0] = collision_check(-0.12f, -0.12f);	//左上
		collision[1] = collision_check( 0.12f, -0.12f);	//右上
		collision[2] = collision_check(-0.12f,  0.12f);	//左下
		collision[3] = collision_check( 0.12f,  0.12f);	//右下

		revisioncorrection(collision);
	}





	/*************************************************************
	 * 敵キャラクターの向く方向を制御します
	 * @param flg 向いてる数値
	 *************************************************************/
	public void direction_change(char flg){
		char char_direction[] ={
								1,	//上
								2,	//下
								0,	//なし
								0,	//右
								0,	//右上
								2,	//右下
								0,	//なし
								3,	//左
								1,	//左上
								3};	//左下

		int char_direction2[] ={
				1,	//上
				6,	//下
				0,	//なし
				4,	//右
				2,	//右上
				7,	//右下
				0,	//なし
				3,	//左
				0,	//左上
				5};	//左下
		if(flg > 0 && flg != 3 && flg != 7){	//念のため
			flg --;
			rect_numdir = char_direction[flg];
			vec = char_direction2[flg];
		}

	}








	/*************************************************************
	 * 障害物処理
	 * @param offset_x 中心からどの程度離れているかのｘ
	 * @param offset_y 中心からどの程度離れているかのｙ
	 *************************************************************/
	private int collision_check(float offset_x,float offset_y){
		int tes_x, tes_y;
		tes_x =(int)((this.mX+0.125f + offset_x) / 0.25f);
		tes_y =(int)((this.mY+0.125f + offset_y) / 0.25f);

		for(int i = 0; i < GameMain.PLAYER_NUM; i++){
			if(mMap[tes_y][tes_x] < TeisuuTeigi.M_SNAG || (tes_x ==(int)((GameMain.mSta_p[i].mX+0.125f) / 0.25f) && tes_y == (int)((GameMain.mSta_p[i].mY+0.125f) / 0.25f))){
				return 1;
			}
		}
		for(int i = 0; i < GameMain.ENEMY_MAX; i++)
			if(GameMain.mSta_e[i].active_flg){
				if(GameMain.mSta_e[i] != this  && (tes_x ==(int)((GameMain.mSta_e[i].mX+0.125f) / 0.25f) && tes_y == (int)((GameMain.mSta_e[i].mY+0.125f) / 0.25f)))
					return 1;
			}

		return 0;
	}










	/*************************************************************
	 * 位置の補正
	 * @param collision
	 *************************************************************/
	private void revisioncorrection(int collision[]){

		int tes_x = 0, tes_y = 0;
		float sa_x, sa_y;
			float offset[][] = {
				{-0.12f, -0.12f},
				{ 0.12f, -0.12f},
				{-0.12f,  0.12f},
				{ 0.12f,  0.12f}
			};

		if(collision[0] == 1 && collision[1] == 1){		//上側判定
			this.mY += this.Speed;
		}

		if(collision[2] == 1 && collision[3] == 1){	//下側判定
			this.mY -= this.Speed;
		}

		if(collision[1] == 1 && collision[3] == 1){		//右側
			this.mX -=  this.Speed;
		}
		if(collision[0] == 1 && collision[2] == 1){		//左側
			this.mX += this.Speed;
		}

		{
			for(int i = 0; i < 4; i++){		//斜めのすり抜けをしないようにチェック
				tes_x =(int)((this.mX+0.125f + offset[i][0]) / 0.25f);
				tes_y =(int)((this.mY+0.125f + offset[i][1]) / 0.25f);

				//障害物との当たり判定
				if(mMap[tes_y][tes_x] < 0){
					sa_y = this.mY - tes_y * 0.25f;
					sa_x = this.mX - tes_x * 0.25f;

					if(Math.abs(sa_y) > Math.abs(sa_x)){
						if( this.mY  - tes_y * 0.25f < 0){
							this.mY -= this.Speed;
						}else if(this.mY - tes_y * 0.25f > 0){
							this.mY += this.Speed;
						}
					}else if(Math.abs(sa_y) < Math.abs(sa_x)){
						if( this.mX - tes_x  * 0.25f < 0){
							this.mX -=  this.Speed;
						}else if( this.mX - tes_x * 0.25f > 0){
							this.mX += this.Speed;
						}
					}
				}

				//主人公との当たり判定
				for(int j = 0; j < GameMain.PLAYER_NUM; j++){
					if((tes_x ==(int)((GameMain.mSta_p[j].mX+0.125f) / 0.25f) && tes_y == (int)((GameMain.mSta_p[j].mY+0.125f) / 0.25f)) ){
						sa_y = this.mY - tes_y * 0.25f;
						sa_x = this.mX - tes_x * 0.25f;

						if(Math.abs(sa_y) > Math.abs(sa_x)){
							if( this.mY  - tes_y * 0.25f < 0){
								this.mY -= this.Speed;
							}else if(this.mY - tes_y * 0.25f > 0){
								this.mY += this.Speed;
							}
						}else if(Math.abs(sa_y) < Math.abs(sa_x)){
							if( this.mX - tes_x  * 0.25f < 0){
								this.mX -=  this.Speed;
							}else if( this.mX - tes_x * 0.25f > 0){
								this.mX += this.Speed;
							}
						}
					}
				}



				//敵同士の当たり判定
				for(int j = 0; j < GameMain.ENEMY_MAX; j++){
					if(GameMain.mSta_e[j].active_flg){

						sa_y = this.mY - tes_y * 0.25f;
						sa_x = this.mX - tes_x * 0.25f;

						if(GameMain.mSta_e[j] != this  && (tes_x ==(int)((GameMain.mSta_e[j].mX+0.125f) / 0.25f) && tes_y == (int)((GameMain.mSta_e[j].mY+0.125f) / 0.25f))){
							if(Math.abs(sa_y) > Math.abs(sa_x)){
								if( this.mY  - tes_y * 0.25f < 0){
									this.mY -= this.Speed;
								}else if(this.mY - tes_y * 0.25f > 0){
									this.mY += this.Speed;
								}
							}else if(Math.abs(sa_y) < Math.abs(sa_x)){
								if( this.mX - tes_x  * 0.25f < 0){
									this.mX -=  this.Speed;
								}else if( this.mX - tes_x * 0.25f > 0){
									this.mX += this.Speed;
								}
							}
						}
					}
				}
				this.map_posX = (int)((this.mX + 0.125f) / 0.25f);
				this.map_posY = (int)((this.mY + 0.125f) / 0.25f);
			}
		}
	}




	/**
	 * バトル処理
	 * @param player_No
	 */
	private void battle(int player_No ){
		rect_mode = 1;

		//20Fごとに攻撃
		if(attack_cnt % 20 != 0){
			return;
		}else{
			attack_cnt = 0;

			//ためしに作ってみたバトル処理
	        //ダメージ計算式 (攻/2 - 防/4 ±乱数(ダメージの0.5~-0.5倍) )
			int damage_p = this.Pow / 2 - mSta_p[player_No].Def / 4;
			Random rnd = new Random();

			//攻撃をミスしなければ
			if(mSta_p[player_No].Avoid == 0 || mSta_p[player_No].Avoid != 0 && Math.floor( Math.random() * 100 ) / mSta_p[player_No].Avoid >= 1){

				//敵が攻撃
		        damage_p = damage_p + (int)( (rnd.nextInt(10) * 0.1f - 0.5f) * damage_p);
		        if(damage_p < 1)	damage_p = 1;
		        mSta_p[player_No].Hp -= damage_p;
		        mSta_p[player_No].damage_anime = 1;
		        se_generator(this.No);
		        mSta_p[player_No].damage_flg = true; //ダメージを受けたら探索するのでフラグ
		        //エフェクト
		        effect_generate(mSta_p[player_No].mX, mSta_p[player_No].mY, this.mX, this.mY);
		        for(int i = 0; i < 30; i++){
					if(!GameMain.mDamage[i].disp_flg){
						GameMain.mDamage[i].reset(damage_p, mSta_p[player_No].mX, mSta_p[player_No].mY);
						break;
					}
	        	}
			}else{
				for(int i = 0; i < 30; i++){
					if(!GameMain.mDamage[i].disp_flg){
						GameMain.mDamage[i].reset(0, mSta_p[player_No].mX, mSta_p[player_No].mY);
						break;
					}
	        	}
			}

	        if(mSta_p[player_No].Hp > 0){
//	        	Log.d("攻撃！ダメージ",""+mSta_p[player_No].Hp);

	        }else{

	        	mSta_p[player_No].Hp = 0;
	        	mSta_p[player_No].active_flg = false;
	        	mSta_p[player_No].die_flg = true;

	        	//全滅したら、GameOver画面へ
        		if(mSta_p[0].die_flg && mSta_p[1].die_flg && mSta_p[2].die_flg && mSta_p[3].die_flg && mSta_p[4].die_flg){
        			GameMain.defeat_flg = true;
    				MainActivity.thread_run = false;
        		}
	        }

//	        Log.d("",""+mSta_p[player_No].Name);
		}
	}


	private void effect_generate(float play_x, float play_y, float ene_x, float ene_y){
		float vec_x = 0.0f, vec_y = 0.0f;
		float efe_pos_x = 0, efe_pos_y = 0;

		if(play_x < ene_x){
			vec_x = 0.01f;
			efe_pos_x = 0.08f;
		}
		else if(play_x > ene_x){
			vec_x = -0.01f;
			efe_pos_x = -0.08f;
		}

		if(play_y < ene_y){
			vec_y = 0.01f;
			efe_pos_y = 0.08f;
		}
		else if(play_y > ene_y){
			vec_y = -0.01f;
			efe_pos_y = -0.08f;
		}

		for(int i = 0;i < 5; i++)
			for(int j = 0; j < GameMain.PARTICLE_NUM; j++)
			if(!GameMain.attack_efect[j].Active_flg){
				GameMain.attack_efect[j].attac_efe(play_x+efe_pos_x, play_y+efe_pos_y, vec_x, vec_y, 10);
				break;
			}

	}



	private void Sakuteki_kougeki( int[] pos , int kyori, int syatei)
	{

		//-------------------------------------------------------------------
		//接近移動処理
		//-------------------------------------------------------------------
//		int enemy_x, enemy_y, play_x, play_y;
		play_num = 0;
		int play_flg[] = new int[5];
		int play = 0;
		for(play = 0; play < 5; play++ )
			play_flg[play] = 0;
		for(int j = 0; j <= kyori; j++)
		{
			for(int i = 0; i < GameMain.PLAYER_NUM; i++){
				if( !mSta_p[i].die_flg  && state_flg )
				{
					if( ( Math.abs( (int) ((mSta_p[i].mX + 0.125f)/0.25f) - this.map_posX) <= j ) &&
						( Math.abs( (int) ((mSta_p[i].mY + 0.125f)/0.25f) - this.map_posY) <= j ) ){		//敵の誰かが近くにいる

						if(this.No != 4){
							//主人公に攻め寄る
							pos[x] =  (int) ((mSta_p[i].mX + 0.125f)/0.25f);
							pos[y] =  (int) ((mSta_p[i].mY + 0.125f)/0.25f);
						}else{
							rect_mode = 1;

						}
						play_flg[i] = 1;
						play_num++;
					}
				}
			}
		}

		if(play_num > 0){
			do{
				play = (int) Math.floor( Math.random() * 5 );
			}while(play_flg[play] != 1);
			syatei_kougeki(play, syatei);
			return;
		}


		//移動しない敵用
		if(kyori == -1){
			for(int i = 0; i < GameMain.PLAYER_NUM; i++){
				if( mSta_p[i].active_flg  && state_flg ){
					if( ( Math.abs( (int) ((mSta_p[i].mX + 0.125f)/0.25f) - this.map_posX) <= syatei ) &&
							( Math.abs( (int) ((mSta_p[i].mY + 0.125f)/0.25f) - this.map_posY) <= syatei ) ){		//射程範囲に敵がいる

						play_num++;
						play_flg[i] = 1;
					}

				}
			}

			if(play_num > 0){
				do{
					play = (int) Math.floor( Math.random() * 5 );
				}while(play_flg[play] != 1);
				syatei_kougeki(play, syatei);
				return;
			}
		}else{
			move_stop = false;
		}
	}

	private void syatei_kougeki(int i, int syatei){
		int enemy_x, enemy_y, play_x, play_y;
		enemy_x = (int)((this.mX + 0.125f) / 0.25f);
		enemy_y	= (int)((this.mY + 0.125f) / 0.25f);
		play_x  = (int)((mSta_p[i].mX + 0.125f) / 0.25f);
		play_y  = (int)((mSta_p[i].mY + 0.125f) / 0.25f);

		int collision[] = {0,0,0,0};
		collision[0] = collision_check(-0.12f, -0.12f);	//左上
		collision[1] = collision_check( 0.12f, -0.12f);	//右上
		collision[2] = collision_check(-0.12f,  0.12f);	//左下
		collision[3] = collision_check( 0.12f,  0.12f);	//右下

		//マップチップの中心まで来たら止まる 遠距離攻撃の敵は2マス以上離れてれば止まって攻撃
		if(collision[0] == 0 && collision[1] == 0 && collision[2] == 0 && collision[3] == 0 &&
				 Math.abs( enemy_x - play_x ) <= syatei && Math.abs( enemy_y - play_y ) <=  syatei ){
			move_stop = true;
		}else{
			move_stop = false;
		}

		if(( Math.abs( enemy_x - play_x ) <= syatei) && ( Math.abs( enemy_y - play_y ) <= syatei )){	//射程内なら攻撃じゃ！

			battle(i);
			attack_cnt++;

			//1回でも攻撃したらその場に居座る。（攻撃できなかったら最後の攻撃した場所に戻ろうとする）
			this.map_posX = (int)((this.mX + 0.125f) / 0.25f);
			this.map_posY = (int)((this.mY + 0.125f) / 0.25f);
		}
	}

	private void se_generator(int e_no){
		switch(e_no){
		case 0:
			break;
		case 1:
			if(Sound.music.sp != null){
				Sound.music.SE_play(11);	//攻撃SEの再生
			}
			break;
		case 2:
			if(Sound.music.sp != null){
				Sound.music.SE_play(12);	//攻撃SEの再生
			}
			break;
		case 3:
			if(Sound.music.sp != null){
				Sound.music.SE_play(13);	//攻撃SEの再生
			}
			break;
		case 4:
			if(Sound.music.sp != null){
				Sound.music.SE_play(14);	//攻撃SEの再生
			}
			break;
		case 5:
			break;

		}
	}

}