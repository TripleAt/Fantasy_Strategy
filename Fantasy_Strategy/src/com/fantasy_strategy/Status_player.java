package com.fantasy_strategy;


import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

enum Charstatus
{
	MOVE,
	ATTACK,
	SKILL,
}
public class Status_player {

	//MapMoveを呼び出す
	MapMove mMapMove = new MapMove();

	public static Status_enemy[] mSta_e = GameMain.mSta_e;

	//public int map_posY, map_posX;			//移動先
	public int chara_move[][] = new int[ TeisuuTeigi.MAP_TIP_STAGE_SIZE[TeisuuTeigi.stage_area][0] * TeisuuTeigi.MAP_TIP_STAGE_SIZE[TeisuuTeigi.stage_area][1] ]
									   [ TeisuuTeigi.MAP_TIP_STAGE_SIZE[TeisuuTeigi.stage_area][0] * TeisuuTeigi.MAP_TIP_STAGE_SIZE[TeisuuTeigi.stage_area][1] ];		//キャラの移動ルート
	int next_x,next_y;	public boolean battel_flg = false;
	public int battel_target = -1;	//キャラクターのパラメータ
	public float mX, mY;		//位置

	public int No;				//個別番号
	public int Joutai;			//状態値（行動パターン）
	public float Speed;			//移動距離(速度)

	public String Name;			//名前
	public int Level;			//レベル
	public int MaxHp;			//MAXHP
	public int Hp;				//HP
	public int Pow;				//攻撃力
	public int Def;				//防御力
	public int M_Def;			//魔法防御
	public int Avoid;		//回避率（％）
	public boolean state_flg;	//停止条件
	public boolean die_flg = false;		//死亡フラグ
	public boolean damage_flg = false;	//攻撃を受けた！
	private boolean enemy_search = false;		//敵検索フラグ
	public int  attack_anime;	//キャラクター毎の攻撃するアニメーションナンバー
	public boolean skill_timing = false;		//スキルを発動させるタイミングフラグ
	public boolean item_timing = false;			//	アイテムを発動させるタイミングフラグ
	public int  skill_anime;	//キャラクター毎のスキルアニメーションナンバー
	public boolean skill_cutin_timing = false;	//各キャラのスキルカットインフラグ
	public int skill_cutin_frame = 0;	//各キャラのスキルカットインフラグ
//	private int attack_range = 0;

	public Charstatus s;
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
	public int dir_char = 0;		//移動方向決定用チェック
	/***
	 * 現在のキャラムーブのポインタ
	 */
	public int point;				//
	public int rect_numdir;
	public int damage_anime = 0;	//食らった時アニメーションする
	/***
	 * 歩いている時に発生するフラグ。勝手に変えたら大変
	 */
	public boolean idou = false;
	public boolean active_flg = true;		//生きてるか判定

	private int f_count = 0;
	private int rect_num;
	private int rect_mode;
	private int vec;
	private int attack_cnt;				//何Fで攻撃するかのカウント
	private int swing = 0;				//揺れ方向
	private float red_col = 1.0f;
	private float Xswing = 0;			//キャラクター揺れる
	private float Xswing_width = 0.05f;	//揺れ幅
	private float die_pos = 0;
	private float die_vec = 1;
	private int skill_f_count = 0;		//スキルのフレームカウント
	private int skill_speed = 0;

	public boolean skill_active_flg = false;
	int skill_char_no = 0;
	private boolean enemy_found_flg = false;
	private boolean skill_chk_flg = false;		//スキルが発動したか
	private int enemy_notfound_cnt = 0;
	private int skill_attck_found = -1;

	private float[][][][] rect_kyara;	//キャラクター毎の矩形情報を一時格納
	private Boolean target_flg = false;
	private float[][][][] magi_rect =
		{
			{		//Move
				{	//歩き左へ
					{ 0.0f,   0.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{32.0f,   0.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{64.0f,   0.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{32.0f,   0.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
				},
				{	//歩き右へ
					{ 0.0f,  64.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{32.0f,  64.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{64.0f,  64.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{32.0f,  64.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
				},
				{	//歩き左後へ
					{ 0.0f, 128.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{32.0f, 128.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{64.0f, 128.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{32.0f, 128.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
				},
				{	//歩きmigi後へ
					{ 0.0f, 192.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{32.0f, 192.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{64.0f, 192.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{32.0f, 192.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
				},
			},
			{		//ATTAK
				{		//左へ
					{ 0.0f, 256.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{32.0f,   0.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
				},
				{
					//右へ
					{32.0f, 256.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{32.0f,  64.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
				},
				{
					//左後ろへ
					{64.0f, 256.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{32.0f, 128.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
				},
				{
					//右後ろへ
					{96.0f, 256.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{32.0f, 192.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
				}
			},
			{		//SKILL
				{		//左へ
					{ 0.0f, 256.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{32.0f,   0.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
				},
				{
					//右へ
					{32.0f, 256.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{32.0f,  64.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
				},
				{
					//左後ろへ
					{64.0f, 256.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{32.0f, 128.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
				},
				{
					//右後ろへ
					{96.0f, 256.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
					{32.0f, 192.0f, 32.0f, 64.0f, 32.0f / 2, 64.0f },
				}
			}
		};

	private float[][][][]hero_rect =
		{
			{		//Move
				{	//歩き左へ
					{  0.0f,   0.0f,  64.0f, 64.0f,  48.0f, 64.0f },
					{ 64.0f,   0.0f,  64.0f, 64.0f,  48.0f, 64.0f },
					{128.0f,   0.0f,  64.0f, 64.0f,  48.0f, 64.0f },
					{ 64.0f,   0.0f,  64.0f, 64.0f,  48.0f, 64.0f },
				},
				{	//歩き右へ
					{  0.0f,   64.0f,  64.0f, 64.0f, 16.0f, 64.0f },
					{ 64.0f,   64.0f,  64.0f, 64.0f, 16.0f, 64.0f },
					{128.0f,   64.0f,  64.0f, 64.0f, 16.0f, 64.0f },
					{ 64.0f,   64.0f,  64.0f, 64.0f, 16.0f, 64.0f },
				},
				{	//歩き左後へ
					{  0.0f,   128.0f, 64.0f, 64.0f, 48.0f, 64.0f },
					{ 64.0f,   128.0f, 64.0f, 64.0f, 48.0f, 64.0f },
					{128.0f,   128.0f, 64.0f, 64.0f, 48.0f, 64.0f },
					{ 64.0f,   128.0f, 64.0f, 64.0f, 48.0f, 64.0f },
				},
				{	//歩きmigi後へ
					{  0.0f,   192.0f, 64.0f, 64.0f, 16.0f, 64.0f },
					{ 64.0f,   192.0f, 64.0f, 64.0f, 16.0f, 64.0f },
					{128.0f,   192.0f, 64.0f, 64.0f, 16.0f, 64.0f },
					{ 64.0f,   192.0f, 64.0f, 64.0f, 16.0f, 64.0f },
				},
			},
			{		//ATTAK
				{		//左へ
					{  0.0f, 256.0f, 64.0f, 64.0f, 36.0f, 64.0f },
					{ 64.0f, 256.0f, 64.0f, 64.0f, 36.0f, 64.0f },
					{128.0f, 256.0f, 64.0f, 64.0f, 36.0f, 64.0f },
				},
				{
					//右へ
					{  0.0f, 320.0f, 64.0f, 64.0f, 28.0f, 64.0f },
					{ 64.0f, 320.0f, 64.0f, 64.0f, 28.0f, 64.0f },
					{128.0f, 320.0f, 64.0f, 64.0f, 28.0f, 64.0f },
				},
				{
					//左後ろへ
					{  0.0f, 384.0f, 64.0f, 64.0f, 36.0f, 64.0f },
					{ 64.0f, 384.0f, 64.0f, 64.0f, 36.0f, 64.0f },
					{128.0f, 384.0f, 64.0f, 64.0f, 36.0f, 64.0f },
				},
				{
					//右後ろへ
					{  0.0f, 448.0f, 64.0f, 64.0f, 28.0f, 64.0f },
					{ 64.0f, 448.0f, 64.0f, 64.0f, 28.0f, 64.0f },
					{128.0f, 448.0f, 64.0f, 64.0f, 28.0f, 64.0f },
				}
			},
			{		//SKILL
				{		//左へ
					{192.0f, 0.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{256.0f, 0.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{320.0f, 0.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{192.0f, 0.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{192.0f, 0.0f, 64.0f, 64.0f, 32.0f, 64.0f },
				},
				{
					//右へ
					{192.0f, 64.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{256.0f, 64.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{320.0f, 64.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{192.0f, 64.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{192.0f, 64.0f, 64.0f, 64.0f, 32.0f, 64.0f },
				},
				{
					//左後ろへ
					{192.0f, 128.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{256.0f, 128.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{320.0f, 128.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{192.0f, 128.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{192.0f, 128.0f, 64.0f, 64.0f, 32.0f, 64.0f },
				},
				{
					//右後ろへ
					{192.0f, 192.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{256.0f, 192.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{320.0f, 192.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{192.0f, 192.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{192.0f, 192.0f, 64.0f, 64.0f, 32.0f, 64.0f },
				}
			}
		};

	private float[][][][]gunner_rect =
		{
			{		//Move
				{	//歩き左へ
					{  0.0f,   0.0f,  64.0f, 64.0f,  36.0f, 64.0f },
					{ 64.0f,   0.0f,  64.0f, 64.0f,  36.0f, 64.0f },
					{128.0f,   0.0f,  64.0f, 64.0f,  36.0f, 64.0f },
					{ 64.0f,   0.0f,  64.0f, 64.0f,  36.0f, 64.0f },
				},
				{	//歩き右へ
					{  0.0f,   64.0f,  64.0f, 64.0f, 26.0f, 64.0f },
					{ 64.0f,   64.0f,  64.0f, 64.0f, 26.0f, 64.0f },
					{128.0f,   64.0f,  64.0f, 64.0f, 26.0f, 64.0f },
					{ 64.0f,   64.0f,  64.0f, 64.0f, 26.0f, 64.0f },
				},
				{	//歩き左後へ
					{  0.0f,   128.0f, 64.0f, 64.0f, 36.0f, 64.0f },
					{ 64.0f,   128.0f, 64.0f, 64.0f, 36.0f, 64.0f },
					{128.0f,   128.0f, 64.0f, 64.0f, 36.0f, 64.0f },
					{ 64.0f,   128.0f, 64.0f, 64.0f, 36.0f, 64.0f },
				},
				{	//歩きmigi後へ
					{  0.0f,   192.0f, 64.0f, 64.0f, 26.0f, 64.0f },
					{ 64.0f,   192.0f, 64.0f, 64.0f, 26.0f, 64.0f },
					{128.0f,   192.0f, 64.0f, 64.0f, 26.0f, 64.0f },
					{ 64.0f,   192.0f, 64.0f, 64.0f, 26.0f, 64.0f },
				},
			},
			{		//ATTAK
				{		//左へ
					{  0.0f, 256.0f, 64.0f, 64.0f, 48.0f, 64.0f },
					{ 64.0f, 256.0f, 64.0f, 64.0f, 48.0f, 64.0f },
					{128.0f, 256.0f, 64.0f, 64.0f, 48.0f, 64.0f },
				},
				{
					//右へ
					{  0.0f, 320.0f, 64.0f, 64.0f, 16.0f, 64.0f },
					{ 64.0f, 320.0f, 64.0f, 64.0f, 16.0f, 64.0f },
					{128.0f, 320.0f, 64.0f, 64.0f, 16.0f, 64.0f },
				},
				{
					//左後ろへ
					{  0.0f, 384.0f, 64.0f, 64.0f, 46.0f, 64.0f },
					{ 64.0f, 384.0f, 64.0f, 64.0f, 46.0f, 64.0f },
					{128.0f, 384.0f, 64.0f, 64.0f, 46.0f, 64.0f },
				},
				{
					//右後ろへ
					{  0.0f, 448.0f, 64.0f, 64.0f, 17.0f, 64.0f },
					{ 64.0f, 448.0f, 64.0f, 64.0f, 17.0f, 64.0f },
					{128.0f, 448.0f, 64.0f, 64.0f, 17.0f, 64.0f },
				}
			},
			{		//SKILL
				{		//左へ
					{192.0f, 0.0f, 64.0f, 64.0f, 46.0f, 64.0f },
					{256.0f, 0.0f, 64.0f, 64.0f, 46.0f, 64.0f },
					{320.0f, 0.0f, 64.0f, 64.0f, 46.0f, 64.0f },
					{192.0f, 0.0f, 64.0f, 64.0f, 46.0f, 64.0f },
					{192.0f, 0.0f, 64.0f, 64.0f, 46.0f, 64.0f },
				},
				{
					//右へ
					{192.0f, 64.0f, 64.0f, 64.0f, 14.0f, 64.0f },
					{256.0f, 64.0f, 64.0f, 64.0f, 14.0f, 64.0f },
					{320.0f, 64.0f, 64.0f, 64.0f, 14.0f, 64.0f },
					{192.0f, 64.0f, 64.0f, 64.0f, 14.0f, 64.0f },
					{192.0f, 64.0f, 64.0f, 64.0f, 14.0f, 64.0f },
				},
				{
					//左後ろへ
					{192.0f, 128.0f, 64.0f, 64.0f, 45.0f, 64.0f },
					{256.0f, 128.0f, 64.0f, 64.0f, 45.0f, 64.0f },
					{320.0f, 128.0f, 64.0f, 64.0f, 45.0f, 64.0f },
					{192.0f, 128.0f, 64.0f, 64.0f, 45.0f, 64.0f },
					{192.0f, 128.0f, 64.0f, 64.0f, 45.0f, 64.0f },
				},
				{
					//右後ろへ
					{192.0f, 192.0f, 64.0f, 64.0f, 22.0f, 64.0f },
					{256.0f, 192.0f, 64.0f, 64.0f, 22.0f, 64.0f },
					{320.0f, 192.0f, 64.0f, 64.0f, 22.0f, 64.0f },
					{192.0f, 192.0f, 64.0f, 64.0f, 22.0f, 64.0f },
					{192.0f, 192.0f, 64.0f, 64.0f, 22.0f, 64.0f },
				}
			}
		};

	private float[][][][]large_sword_rect =
		{
			{		//Move
				{	//歩き左へ
					{  0.0f,   0.0f,  64.0f, 64.0f,  32.0f, 64.0f },
					{ 64.0f,   0.0f,  64.0f, 64.0f,  32.0f, 64.0f },
					{128.0f,   0.0f,  64.0f, 64.0f,  32.0f, 64.0f },
					{ 64.0f,   0.0f,  64.0f, 64.0f,  32.0f, 64.0f },
				},
				{	//歩き右へ
					{  0.0f,   64.0f,  64.0f, 64.0f, 32.0f, 64.0f },
					{ 64.0f,   64.0f,  64.0f, 64.0f, 32.0f, 64.0f },
					{128.0f,   64.0f,  64.0f, 64.0f, 32.0f, 64.0f },
					{ 64.0f,   64.0f,  64.0f, 64.0f, 32.0f, 64.0f },
				},
				{	//歩き左後へ
					{  0.0f,   128.0f, 64.0f, 64.0f, 36.0f, 64.0f },
					{ 64.0f,   128.0f, 64.0f, 64.0f, 36.0f, 64.0f },
					{128.0f,   128.0f, 64.0f, 64.0f, 36.0f, 64.0f },
					{ 64.0f,   128.0f, 64.0f, 64.0f, 36.0f, 64.0f },
				},
				{	//歩きmigi後へ
					{  0.0f,   192.0f, 64.0f, 64.0f, 26.0f, 64.0f },
					{ 64.0f,   192.0f, 64.0f, 64.0f, 26.0f, 64.0f },
					{128.0f,   192.0f, 64.0f, 64.0f, 26.0f, 64.0f },
					{ 64.0f,   192.0f, 64.0f, 64.0f, 26.0f, 64.0f },
				},
			},
			{		//ATTAK
				{		//左へ
					{  0.0f, 256.0f, 64.0f, 64.0f, 38.0f, 64.0f },
					{ 64.0f, 256.0f, 64.0f, 64.0f, 38.0f, 64.0f },
					{128.0f, 256.0f, 64.0f, 64.0f, 38.0f, 64.0f },
				},
				{
					//右へ
					{  0.0f, 320.0f, 64.0f, 64.0f, 25.0f, 64.0f },
					{ 64.0f, 320.0f, 64.0f, 64.0f, 25.0f, 64.0f },
					{128.0f, 320.0f, 64.0f, 64.0f, 25.0f, 64.0f },
				},
				{
					//左後ろへ
					{  0.0f, 384.0f, 64.0f, 64.0f, 39.0f, 64.0f },
					{ 64.0f, 384.0f, 64.0f, 64.0f, 39.0f, 64.0f },
					{128.0f, 384.0f, 64.0f, 64.0f, 39.0f, 64.0f },
				},
				{
					//右後ろへ
					{  0.0f, 448.0f, 64.0f, 64.0f, 24.0f, 64.0f },
					{ 64.0f, 448.0f, 64.0f, 64.0f, 24.0f, 64.0f },
					{128.0f, 448.0f, 64.0f, 64.0f, 24.0f, 64.0f },
				}
			},
			{		//SKILL
				{		//左へ
					{192.0f, 0.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{256.0f, 0.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{320.0f, 0.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{192.0f, 0.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{192.0f, 0.0f, 64.0f, 64.0f, 32.0f, 64.0f },
				},
				{
					//右へ
					{192.0f, 64.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{256.0f, 64.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{320.0f, 64.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{192.0f, 64.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{192.0f, 64.0f, 64.0f, 64.0f, 32.0f, 64.0f },
				},
				{
					//左後ろへ
					{192.0f, 128.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{256.0f, 128.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{320.0f, 128.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{192.0f, 128.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{192.0f, 128.0f, 64.0f, 64.0f, 32.0f, 64.0f },
				},
				{
					//右後ろへ
					{192.0f, 192.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{256.0f, 192.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{320.0f, 192.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{192.0f, 192.0f, 64.0f, 64.0f, 32.0f, 64.0f },
					{192.0f, 192.0f, 64.0f, 64.0f, 32.0f, 64.0f },
				}
			}
		};


	private float[][][][]armor_rect =
		{
			{		//Move
				{	//歩き左へ
					{  0.0f,   0.0f,  64.0f, 64.0f,  48.0f-3.0f, 64.0f },
					{ 64.0f,   0.0f,  64.0f, 64.0f,  48.0f-3.0f, 64.0f },
					{128.0f,   0.0f,  64.0f, 64.0f,  48.0f-3.0f, 64.0f },
					{ 64.0f,   0.0f,  64.0f, 64.0f,  48.0f-3.0f, 64.0f },
				},
				{	//歩き右へ
					{  0.0f,   64.0f,  64.0f, 64.0f, 16.0f+3.0f, 64.0f },
					{ 64.0f,   64.0f,  64.0f, 64.0f, 16.0f+3.0f, 64.0f },
					{128.0f,   64.0f,  64.0f, 64.0f, 16.0f+3.0f, 64.0f },
					{ 64.0f,   64.0f,  64.0f, 64.0f, 16.0f+3.0f, 64.0f },
				},
				{	//歩き左後へ
					{  0.0f,   128.0f, 64.0f, 64.0f, 48.0f-3.0f, 64.0f },
					{ 64.0f,   128.0f, 64.0f, 64.0f, 48.0f-3.0f, 64.0f },
					{128.0f,   128.0f, 64.0f, 64.0f, 48.0f-3.0f, 64.0f },
					{ 64.0f,   128.0f, 64.0f, 64.0f, 48.0f-3.0f, 64.0f },
				},
				{	//歩きmigi後へ
					{  0.0f,   192.0f, 64.0f, 64.0f, 16.0f+3.0f, 64.0f },
					{ 64.0f,   192.0f, 64.0f, 64.0f, 16.0f+3.0f, 64.0f },
					{128.0f,   192.0f, 64.0f, 64.0f, 16.0f+3.0f, 64.0f },
					{ 64.0f,   192.0f, 64.0f, 64.0f, 16.0f+3.0f, 64.0f },
				},
			},
			{		//ATTAK
				{		//左へ
					{  0.0f, 256.0f, 64.0f, 64.0f, 48.0f-3.0f, 64.0f },
					{ 64.0f, 256.0f, 64.0f, 64.0f, 48.0f-3.0f, 64.0f },
					{128.0f, 256.0f, 64.0f, 64.0f, 48.0f-3.0f, 64.0f },
				},
				{
					//右へ
					{  0.0f, 320.0f, 64.0f, 64.0f, 16.0f+3.0f, 64.0f },
					{ 64.0f, 320.0f, 64.0f, 64.0f, 16.0f+3.0f, 64.0f },
					{128.0f, 320.0f, 64.0f, 64.0f, 16.0f+3.0f, 64.0f },
				},
				{
					//左後ろへ
					{  0.0f, 384.0f, 64.0f, 64.0f, 48.0f-3.0f, 64.0f },
					{ 64.0f, 384.0f, 64.0f, 64.0f, 48.0f-3.0f, 64.0f },
					{128.0f, 384.0f, 64.0f, 64.0f, 48.0f-3.0f, 64.0f },
				},
				{
					//右後ろへ
					{  0.0f, 448.0f, 64.0f, 64.0f, 16.0f+3.0f, 64.0f },
					{ 64.0f, 448.0f, 64.0f, 64.0f, 16.0f+3.0f, 64.0f },
					{128.0f, 448.0f, 64.0f, 64.0f, 16.0f+3.0f, 64.0f },
				}
			},
			{		//SKILL
				{		//左へ
					{192.0f, 0.0f, 64.0f, 64.0f, 52.0f, 64.0f },	//やり構える
					{256.0f, 0.0f, 64.0f, 64.0f, 52.0f, 64.0f },	//やり投げ終わり
					{256.0f, 0.0f, 64.0f, 64.0f, 52.0f, 64.0f },	//やり投げ終わり　念のため
					{256.0f, 0.0f, 64.0f, 64.0f, 52.0f, 64.0f },	//やり投げ終わり　念のため
				},
				{
					//右へ
					{192.0f, 64.0f, 64.0f, 64.0f, 10.0f, 64.0f },
					{256.0f, 64.0f, 64.0f, 64.0f, 10.0f, 64.0f },
					{256.0f, 64.0f, 64.0f, 64.0f, 10.0f, 64.0f },
					{256.0f, 64.0f, 64.0f, 64.0f, 10.0f, 64.0f },
				},
				{
					//左後ろへ
					{192.0f, 128.0f, 64.0f, 64.0f, 46.0f, 64.0f },
					{256.0f, 128.0f, 64.0f, 64.0f, 46.0f, 64.0f },
					{256.0f, 128.0f, 64.0f, 64.0f, 46.0f, 64.0f },
					{256.0f, 128.0f, 64.0f, 64.0f, 46.0f, 64.0f },
				},
				{
					//右後ろへ
					{192.0f, 192.0f, 64.0f, 64.0f, 16.0f, 64.0f },
					{256.0f, 192.0f, 64.0f, 64.0f, 16.0f, 64.0f },
					{256.0f, 192.0f, 64.0f, 64.0f, 16.0f, 64.0f },
					{256.0f, 192.0f, 64.0f, 64.0f, 16.0f, 64.0f },
				}
			}
		};

	/*-----------------------------------------------------------
	 *キャラクタパラメータ
	 *---------------------------------------------------------*/
								//{Lv,   HP,攻撃力,防御力,魔防力,移動速度,攻撃アニメナンバー, 回避率(％)}
private int[][] statuses = {		{1, 540, 60, 	 13,	 10,	  6,	  1, 10, 15},		//プレイヤー1主人公
									{1, 300, 20,	  9,	 15,	 40,	  1,  5, 15},		//プレイヤー2魔法使い
									{1, 270, 90,	  7,	  6,	 25,	  2,  5,  5},		//プレイヤー3銃
									{1, 465, 50,	 21,	  9,	 20,	  1,  2, 15},		//プレイヤー4鎧
									{1, 420, 70,	  8,	  5,	 35,	  1,  8, 15} };		//プレイヤー5大剣
	private String[] char_name={"主人公","魔法使い","銃","鎧","大剣"};
	private int[][] attack_frame = {
			{10,5,3},		//主人公
			{5,5},			//魔法使い
			{20,20,10},		//銃
			{15,3,5},		//鎧
			{25,5,15},		//大剣
	};
	private int[][] skill_frame = {
			{ 3, 2, 2, 5, 7},		//主人公
			{5,5},			//魔法使い
			{15, 15, 15, 15,15},		//銃
			{15, 18, 15, 20, 15},		//鎧
			{15, 15, 15, 15, 15},		//大剣
	};


	/*-----------------------------------------------------------
	 *	キャラクターの攻撃範囲
	 *---------------------------------------------------------*/
	public int attak_map[][][][] ={
		{		//主人公
			{
				{ -1, 0, 1, -1, 1, -1,  0,  1 },	//ｘ
				{  1, 1, 1,  0, 0, -1, -1, -1,},	//ｙ
			},
		},
		{		//魔法使い
			{
				{-1, 0, 1, -1, 1, -1,  0,  1 },		//ｘ
				{ 1, 1, 1,  0, 0, -1, -1, -1,},		//ｙ
			},
		},
		{		//銃
			{		//0
				{-1, 0, 1, -1, 1, -1,  0,  1 },		//ｘ
				{ 1, 1, 1,  0, 0, -1, -1, -1,},		//ｙ
			},
			{		//1
				{-2, 0, 2, -2, 2, -2,  0,  2 },		//ｘ
				{ 2, 2, 2,  0, 0, -2, -2, -2,},		//ｙ
			},
			{		//2
				{-3, 0, 3, -3, 3, -3,  0,  3 },		//ｘ
				{ 3, 3, 3,  0, 0, -3, -3, -3,},		//ｙ
			},
			{		//3
				{-4, 0, 4, -4, 4, -4,  0,  4 },		//ｘ
				{ 4, 4, 4,  0, 0, -4, -4, -4,},		//ｙ
			},
			{		//4
				{-5, 0, 5, -5, 5, -5,  0,  5 },		//ｘ
				{ 5, 5, 5,  0, 0, -5, -5, -5,},		//ｙ
			},
		},
		{		//鎧
			{		//0
				{-1, 0, 1, -1, 1, -1,  0,  1 },		//ｘ
				{ 1, 1, 1,  0, 0, -1, -1, -1,},		//ｙ
			},
			{		//1
				{-2, 0, 2, -2, 2, -2,  0,  2 },		//ｘ
				{ 2, 2, 2,  0, 0, -2, -2, -2,},		//ｙ
			},
		},
		{		//大剣(仮)
			{		//0
				{-1, 0, 1, -1, 1, -1,  0,  1 },		//ｘ
				{ 1, 1, 1,  0, 0, -1, -1, -1,},		//ｙ
			},
			{		//1
				{-2, 0, 2, -2, 2, -2,  0,  2 },		//ｘ
				{ 2, 2, 2,  0, 0, -2, -2, -2,},		//ｙ
			},
			{		//2
				{-2, 0, 2, -2, 2, -2,  0,  2 },		//ｘ
				{ 2, 2, 2,  0, 0, -2, -2, -2,},		//ｙ
			},
		}
	};


	/*-----------------------------------------------------------
	 *	キャラクターのスキル攻撃範囲
	 *---------------------------------------------------------*/
	//上 下 なし 右 右上 右下 なし 左 左上 左下
	//1, 6, 0, 4, 2, 7, 0, 3, 0, 5
	public int skill_attak_map[][][][] ={
			{		//主人公
				{
					{ -1, 0, 1, -1, 1, -1,  0,  1 },	//ｘ
					{  1, 1, 1,  0, 0, -1, -1, -1,},	//ｙ
				},
			},
			{		//魔法使い
				{		//0
					{ 0, 0, 0, -1, 1, 0,  0, 0 },		//ｘ
					{ 0, 1, 0,  0, 0, 0, -1, 0,},		//ｙ
				},
				{		//1
					{ 0, 0, 0, -2, 2, 0,  0, 0 },		//ｘ
					{ 0, 2, 0,  0, 0, 0, -2, 0,},		//ｙ
				},
				{		//2
					{ 0, 0, 0, -3, 3, 0,  0, 0 },		//ｘ
					{ 0, 3, 0,  0, 0, 0, -3, 0,},		//ｙ
				},
				{		//3
					{ 0, 0, 0, -4, 4, 0,  0, 0 },		//ｘ
					{ 0, 4, 0,  0, 0, 0, -4, 0,},		//ｙ
				},
				{		//4
					{ 0, 0, 0, -5, 5, 0,  0, 0 },		//ｘ
					{ 0, 5, 0,  0, 0, 0, -5, 0,},		//ｙ
				},
			},
			{		//銃
				{		//0
					{ 0, 0, 0, -1, 1, 0,  0, 0 },		//ｘ
					{ 0, 1, 0,  0, 0, 0, -1, 0,},		//ｙ
				},
				{		//1
					{ 0, 0, 0, -2, 2, 0,  0, 0 },		//ｘ
					{ 0, 2, 0,  0, 0, 0, -2, 0,},		//ｙ
				},
				{		//2
					{ 0, 0, 0, -3, 3, 0,  0, 0 },		//ｘ
					{ 0, 3, 0,  0, 0, 0, -3, 0,},		//ｙ
				},
				{		//3
					{ 0, 0, 0, -4, 4, 0,  0, 0 },		//ｘ
					{ 0, 4, 0,  0, 0, 0, -4, 0,},		//ｙ
				},
				{		//4
					{ 0, 0, 0, -5, 5, 0,  0, 0 },		//ｘ
					{ 0, 5, 0,  0, 0, 0, -5, 0,},		//ｙ
				},
				{		//5
					{ 0, 0, 0, -6, 6, 0,  0, 0 },		//ｘ
					{ 0, 6, 0,  0, 0, 0, -6, 0,},		//ｙ
				},
				{		//6
					{ 0, 0, 0, -7, 7, 0,  0, 0 },		//ｘ
					{ 0, 7, 0,  0, 0, 0, -7, 0,},		//ｙ
				},
			},
			{		//鎧
				{		//0
					{ 0, 0, 0, -1, 1,  0,  0,  0 },		//ｘ
					{ 0, 1, 0,  0, 0,  0, -1,  0,},		//ｙ
				},
				{		//1
					{ 0, 0, 0, -2, 2,  0,  0,  0 },		//ｘ
					{ 0, 2, 0,  0, 0,  0, -2,  0,},		//ｙ
				},
				{		//2
					{ 0, 0, 0, -3, 3,  0,  0,  0 },		//ｘ
					{ 0, 3, 0,  0, 0,  0, -3,  0,},		//ｙ
				},
				{		//3
					{ 0, 0, 0, -4, 4,  0,  0,  0 },		//ｘ
					{ 0, 4, 0,  0, 0,  0, -4,  0,},		//ｙ
				},
			},
			{		//大剣
				{		//0 真ん中
					{ 0, 0, 0, -1, 1,  0,  0,  0 },		//ｘ
					{ 0, 1, 0,  0, 0,  0, -1,  0,},		//ｙ
				},
				{		//0 横1
					{ 0, 1, 0, -1, 1,  0,  1,  0 },		//ｘ
					{ 0, 1, 0,  1, 1,  0, -1,  0,},		//ｙ
				},
				{		//0 横2
					{ 0, -1, 0, -1,  1,  0, -1,  0 },	//ｘ
					{ 0, 1, 0,  -1, -1,  0, -1,  0,},	//ｙ
				},
				{		//1 真ん中
					{ 0, 0, 0, -2, 2,  0,  0,  0 },		//ｘ
					{ 0, 2, 0,  0, 0,  0, -2,  0,},		//ｙ
				},
				{		//1 横1
					{ 0, 1, 0, -2, 2,  0,  1,  0 },		//ｘ
					{ 0, 2, 0,  1, 1,  0, -2,  0,},		//ｙ
				},
				{		//1 横2
					{ 0, -1, 0, -2,  2,  0, -1,  0 },		//ｘ
					{ 0,  2, 0, -1, -1,  0, -2,  0,},		//ｙ
				},
				{		//2 真ん中
					{ 0, 0, 0, -3, 3,  0,  0,  0 },		//ｘ
					{ 0, 3, 0,  0, 0,  0, -3,  0,},		//ｙ
				},
				{		//2 横1
					{ 0, 1, 0, -3, 3,  0,  1,  0 },		//ｘ
					{ 0, 3, 0,  1, 1,  0, -3,  0,},		//ｙ
				},
				{		//2 横2
					{ 0, -1, 0, -3,  3,  0, -1,  0 },		//ｘ
					{ 0,  3, 0, -1, -1,  0, -3,  0,},		//ｙ
				},
				{		//3 真ん中
					{ 0, 0, 0, -4, 4,  0,  0,  0 },		//ｘ
					{ 0, 4, 0,  0, 0,  0, -4,  0,},		//ｙ
				},
				{		//3 横1
					{ 0, 1, 0, -4, 4,  0,  1,  0 },		//ｘ
					{ 0, 4, 0,  1, 1,  0, -4,  0,},		//ｙ
				},
				{		//3 横2
					{ 0, -1, 0, -4,  4,  0, -1,  0 },		//ｘ
					{ 0,  4, 0, -1, -1,  0, -4,  0,},		//ｙ
				},
				{		//4 真ん中
					{ 0, 0, 0, -5, 5,  0,  0,  0 },		//ｘ
					{ 0, 5, 0,  0, 0,  0, -5,  0,},		//ｙ
				},
				{		//4 横1
					{ 0, 1, 0, -5, 5,  0,  1,  0 },		//ｘ
					{ 0, 5, 0,  1, 1,  0, -5,  0,},		//ｙ
				},
				{		//4 横2
					{ 0, -1, 0, -5,  5,  0, -1,  0 },		//ｘ
					{ 0,  5, 0, -1, -1,  0, -5,  0,},		//ｙ
				},
			}
		};


	//キャラクター別クリティカルヒット率
	private float[] critical_hit = {10, 5, 5, 7.5f, 7.5f};		//"主人公","魔法使い","銃","鎧","大剣"


	//呼び出し元から、各キャラクターのパラメーターを受け取る
	public Status_player(int x, int y, int no, int joutai)
	{
		this.s = Charstatus.MOVE;
		this.mY = (float)y * 0.25f;
		this.mX = (float)x * 0.25f;
		this.No = no;

		this.Name = char_name[no];
		this.Level = statuses[ this.No ][0];
		this.MaxHp = statuses[ this.No ][1];
		this.Hp = statuses[ this.No ][1];
		this.Pow = statuses[ this.No ][2];
		this.Def = statuses[ this.No ][3];
		this.M_Def = statuses[ this.No ][4];
		this.Speed = (float)(this.statuses[ this.No ][5]);
		this.Avoid = statuses[ this.No ][6];
		this.move_type = 0;
		this.state_flg = true;
		this.Joutai = joutai;
		f_count = attack_frame[no][0];
		this.attack_anime = this.statuses[ this.No ][6];
		skill_f_count = skill_frame[no][0];
		skill_anime = this.statuses[ this.No ][6];
		this.skill_speed = this.statuses[this.No][8];

		//移動初期化
		point=0;

		//3D座標からマップ座標へ変換


		//ゲーム開始時に移動先座標を設定
		if(this.move_type == 0){
			//this.mMapMove.ArrayGenerater(this, map_posX, map_posY, map_posX, map_posY);

			//配列のコピー
			for(int i = 0; i < 2; i++){
				System.arraycopy( mMapMove.chara_move_buf[i], 0, chara_move[i], 0, chara_move[i].length);
			}


			//現在位置を格納
			this.chara_move[0][0] =  (int) ((this.mX + 0.125f)/0.25f);
			this.chara_move[1][0] =  (int) ((this.mY + 0.125f)/0.25f);

			this.chara_move[0][1] = -1;
			this.chara_move[1][1] = -1;

		}

	}









	/**-------------------------------------------------------------------
	 *標的を描画します
	 *
	 *
	 * @param gl
	 * @param texture
	 *--------------------------------------------------------------------*/
	public void draw(GL10 gl, int texture) {

		if(!this.active_flg)		//し、死んでる！
		{
			die_efect(gl, GameMain.suya_tex);
			return;
		}
//		0.5f * (float)(map_rect_num % 2), 0.5f * (float)(map_rect_num / 2),

		float Angle = (Camera_var.angle - 180);		//向いている方向
		gl.glPushMatrix();
		{
			gl.glTranslatef(mX + Xswing, mY, 0.01f);
			gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
			gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
			/*if(this.No == 2 && this.skill_timing && GameMain.skill[2].skill__flg == 1){
				gl.glRotatef(90.0f/3, 0.0f, 1.0f, 0.0f);
			}*/
			gl.glScalef(0.25f, 0.25f, 1.0f);


			switch(No){
				case 0:		//主人公
					rect_kyara = hero_rect;
					break;
				case 1:		//魔法使い
					rect_kyara = magi_rect;
					break;
				case 2:		//銃
					rect_kyara = gunner_rect;
					break;
				case 3:		//鎧
					rect_kyara = armor_rect;
					break;
				case 4:		//大剣
					rect_kyara = large_sword_rect;
					break;
			}

			try{
				/*if(No == 0 || No == 2 || No == 4 || No == 3){
					//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
					GraphicUtil.drawTexture_pixel_custom2(gl, 192.0f, 512.0f, texture,
							rect_kyara[rect_mode][rect_numdir][rect_num],
							1.0f, red_col, red_col, 1.0f);

				}*/
				//if(No == 0 || No == 1 || No == 2 || No == 3 || No == 4){
					//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
					GraphicUtil.drawTexture_pixel_custom2(gl, 512.0f, 512.0f, texture,
							rect_kyara[rect_mode][rect_numdir][rect_num],
							1.0f, red_col, red_col, 1.0f);

				//}
			}catch(Exception e){
				Log.e("キャラクター描画エラー","rect_kyaraのArrayIndexOutOfBoundsExceptiongが起きた");
			}

		}
		gl.glPopMatrix();

	}










	//------------------------------------------------------------------------
	//標的を移動させます
	//------------------------------------------------------------------------
	public void move() {


		if(!this.active_flg)		//し、死んでる！
		{
			return;
		}					//フレームをカウント

		switch(this.s)
		{
			case MOVE:
				move_char();
				rect_mode = 0;

				//draw()用変数
				if(f_count <= 0){
				 	f_count = 5;		//各キャラクターのアタックframeの要素数で割った値
				 	rect_num++;
				}
				f_count--;

				//damage_flgは攻撃を受けたので近くを探索し
				if((this.idou == false && this.battel_flg == false) || enemy_search ){		//プレイヤーが勝手に考えて攻撃
					int enemy_x, enemy_y, play_x, play_y;
					for(int i = 0; i < GameMain.ENEMY_MAX; i++){
						if(GameMain.mSta_e[i].active_flg){		//どの敵がいるか
							enemy_x = (int)((GameMain.mSta_e[i].mX + 0.125f) / 0.25f);
							enemy_y	= (int)((GameMain.mSta_e[i].mY + 0.125f) / 0.25f);
							play_x  = (int)((this.mX + 0.125f) / 0.25f);
							play_y  = (int)((this.mY + 0.125f) / 0.25f);

							//周りに敵がいれば
							if( Math.abs( enemy_x - play_x ) <= 1 && Math.abs( enemy_y - play_y ) <= 1 ){
								this.battel_target = i;
								this.battel_flg = true;
								this.idou = false;
								enemy_search = false;
								damage_flg = false;
								break;
							}
						}
					}
					enemy_search = false;
					damage_flg = false;
				}


				break;

			case ATTACK:

				rect_mode = 1;
				//attack_cnt++;

				//draw()用変数
				if(f_count <= 0){
				 	f_count = attack_frame[this.No][(rect_num+1) % attack_frame[this.No].length];		//各キャラクターのアタックframeの要素数で割った値
				 	rect_num++;
				}
				f_count--;
				break;

			case SKILL:
				//Log.d("skill","skill_state");
				rect_mode = 2;
				//attack_cnt++;
				attack_cnt++;

				//draw()用変数
				if(skill_f_count <= 0){
					skill_f_count = skill_frame[this.No][(rect_num+1) % skill_frame[this.No].length];		//各キャラクターのアタックframeの要素数で割った値
				 	rect_num++;
				}
				skill_f_count--;
				//draw()用変数
				/*if(f_count <= 0){
				 	f_count = 5;		//各キャラクターのアタックframeの要素数で割った値
				 	rect_num++;
				}
				f_count--;*/
				break;

		}



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






		switch(rect_mode)
		{
			case 0:
				if( this.idou ){		//移動フラグが立っている間アニメーション
					rect_num = rect_num % 4;
				}else{
					rect_num = 1;
				}
				break;

			case 1:
				rect_num = rect_num % attack_frame[this.No].length;
				break;

			case 2:
				//rect_num = rect_num % skill_frame[this.No].length;
				rect_num = GameMain.skill[skill_char_no].rect_number % skill_frame[skill_char_no % 5].length;
				break;
		}



		attack_mode();		//通常攻撃


		//------------------------------------------------------------------------------------------------------------
		//スキルの戦闘処理
		if(this.skill_active_flg == false){
			if(active_flg){
				for(int i = 0; i < GameMain.SKILL_NUM; i++){
					if(GameMain.skill[i].skill__flg == 1 && this.No == (i % 5)){
						Log.d("skill_flg","name:"+this.Name+" "+GameMain.skill[i].skill__flg);
						Log.d("iの値", "i:"+i);
						this.s = Charstatus.MOVE;
						this.skill_active_flg = true;
						skill_char_no = i;
						attack_cnt = 0;
						this.idou = false;
						enemy_found_flg = false;
						skill_chk_flg = false;
						skill_attck_found = -1;


						/*if((skill_char_no % 5) == 2){
							GameMain.skill[skill_char_no].skill_dir = this.vec;
						}else{*/
							GameMain.skill[skill_char_no].skill_dir = this.rect_numdir;
						//}
						Log.d("debug","スキル");
					}
					else{
						//this.skill_active_flg = false;
						//this.s = Charstatus.MOVE;		//移動アニメーション
					}
					if(this.skill_active_flg) break;
				}
			}

		}


		if(this.skill_active_flg){
			if(this.skill_timing){
				//各キャラクターごとのスキルの処理
				skill(skill_char_no % 5, skill_char_no);		//スキルの処理
			}
		}
	}

	/**
	 * 攻撃モード。通常とは違う動きをする。
	 */
	private void attack_mode(){


		if( GameMain.mSta_p[this.No].skill_active_flg == false && this.s != Charstatus.SKILL && GameMain.skill[skill_char_no].skill__flg != 1){		//スキル発動中は通常攻撃をしない

			if(this.battel_flg && battel_target != -1 ){		//敵キャラを選択し戦闘モード(敵に近づく)
				try{
					int target_x, target_y;
					int my_x = (int)((this.mX + 0.125f ) / 0.25f), my_y = (int)((this.mY + 0.125f) / 0.25f);
					target_flg = false;

					target_x = (int)((GameMain.mSta_e[battel_target].mX + 0.125f) / 0.25f);			//敵の場所をもらってくる
					target_y = (int)((GameMain.mSta_e[battel_target].mY + 0.125f) / 0.25f);

					for(int k = 0; k < attak_map[this.No].length; k++){
						for(int vec2 = 0; vec2 < attak_map[this.No][k][0].length; vec2++){		//全方向チェック
							if( ((my_x + attak_map[this.No][k][0][vec2]) == target_x &&
								 (my_y + attak_map[this.No][k][1][vec2]) == target_y && state_flg) ||
								( my_x  == target_x && my_y == target_y && state_flg)){		//ターゲットが攻撃範囲にいるか？
								target_flg = true;
								//Log.d("ふらぐおん","flagTEST！！！！！"+my_x+","+my_y);
								break;					//ターゲットがいたのでブレイク
							}else{
								target_flg = false;		//ターゲット見つからないのでひとまずfalseにして次のループへ
							}
						}
						if(target_flg) 	break;
					}

					if( target_flg ){		//ターゲットが攻撃範囲にいたか？
						int i = 0, j = 0;
						int enemy_x, enemy_y, play_x, play_y;

						this.s = Charstatus.ATTACK;

						play_x  = (int)((this.mX + 0.125f) / 0.25f);
						play_y  = (int)((this.mY + 0.125f) / 0.25f);
						if(GameMain.mSta_e[battel_target].active_flg){		//どの敵がいるか 狙いたい敵か？
							enemy_x = (int)((GameMain.mSta_e[battel_target].mX + 0.125f) / 0.25f);
							enemy_y	= (int)((GameMain.mSta_e[battel_target].mY + 0.125f) / 0.25f);

							//敵キャラクター移動向き判断
							dir_char = 0;
							if(enemy_y >  play_y){
								dir_char |= 0x1;
							}else if(enemy_y < play_y ){
								dir_char |= 0x2;
							}else{
								dir_char |= 0x0;
							}

							if(enemy_x >  play_x){
								dir_char |= 0x4;
							}else if(enemy_x <  play_x){
								dir_char |= 0x8;
							}else{
								dir_char |= 0x0;
							}
							direction_change(dir_char);		//向き指定
						}
						for( i = 0; i < GameMain.ENEMY_MAX; i++){
							if(GameMain.mSta_e[i].active_flg){		//どの敵がいるか
								enemy_x = (int)((GameMain.mSta_e[i].mX + 0.125f) / 0.25f);
								enemy_y	= (int)((GameMain.mSta_e[i].mY + 0.125f) / 0.25f);
								for(j = 0; j < attak_map[this.No].length; j++){
									if( play_x + attak_map[this.No][j][0][vec] == enemy_x &&		//プレイヤーの攻撃範囲のマスに敵キャラがいれば攻撃が当たる
										play_y + attak_map[this.No][j][1][vec] == enemy_y && state_flg){
										//バトル処理
										battle(i, this.Pow);		//攻撃範囲内にいる敵に攻撃！！
									}
								}
								if(play_x == enemy_x && play_y == enemy_y && state_flg){	//自分と同じ座標も攻撃
									battle(i, this.Pow);		//攻撃範囲内にいる敵に攻撃！！
								}
							}
						}
					}else{
						move_change( my_x,  my_y, target_x, target_y);		//近くにターゲットがいないのでターゲットを追尾
						this.s = Charstatus.MOVE;		//移動アニメーション
					}
				}catch(Exception e){
					Log.e("エラー","実行途中でbattel_targetが-1になったかも");
				}
			}

		}
	}

	/**
	 * スキル処理
	 * @param char_no
	 * @param skill_no
	 */
	private void skill(int char_no, int skill_no){
		int skill_enemy_x, skill_enemy_y, skill_play_x, skill_play_y;

		skill_play_x  = (int)((GameMain.mSta_p[char_no].mX + 0.125f) / 0.25f);
		skill_play_y  = (int)((GameMain.mSta_p[char_no].mY + 0.125f) / 0.25f);
		enemy_notfound_cnt = 0;

		if(!enemy_found_flg && skill_attck_found == -1){
			for( int j = 0; j < GameMain.ENEMY_MAX; j++){
				if(GameMain.mSta_e[j].active_flg){		//どの敵がいるか
					skill_enemy_x = (int)((GameMain.mSta_e[j].mX + 0.125f) / 0.25f);
					skill_enemy_y	= (int)((GameMain.mSta_e[j].mY + 0.125f) / 0.25f);

					for(int k = 0; k < skill_attak_map[skill_no].length; k++){
						if( skill_play_x + skill_attak_map[skill_no][k][0][vec] == skill_enemy_x &&		//プレイヤーの攻撃範囲のマスに敵キャラがいれば攻撃が当たる
								skill_play_y + skill_attak_map[skill_no][k][1][vec] == skill_enemy_y && state_flg){
							//GameMain.mSta_p[char_no].idou = false;
							//GameMain.mSta_p[char_no].move_change( play_x,  play_y, play_x, play_y);

							//バトル処理
							Log.d("通常スキル","normal_skillbattle");
							char_skill_battle( char_no, skill_no, j);
							skill_chk_flg = true;
						}else{
							enemy_notfound_cnt++;
						}

						if(skill_play_x == skill_enemy_x && skill_play_y == skill_enemy_y && state_flg){	//自分と同じ座標も攻撃
							Log.d("同じ位置スキル","position_skillbattle");
							char_skill_battle( char_no, skill_no, j);		//攻撃範囲内にいる敵に攻撃！！
						}
					}
				}
			}

			if(enemy_notfound_cnt == (GameMain.ENEMY_MAX * skill_attak_map[skill_no].length)){	//スキル攻撃する方に敵がいなかった
				enemy_found_flg = true;
			}
		}

		Log.d("skill_chk_flg","skill_chk_flg"+skill_chk_flg);
		//キャラクターの周り一マス分の敵の探索をする
		if(/*enemy_found_flg && */!skill_chk_flg && skill_attck_found == -1){
			for( int j = 0; j < GameMain.ENEMY_MAX; j++){
				skill_enemy_x = (int)((GameMain.mSta_e[j].mX + 0.125f) / 0.25f);
				skill_enemy_y	= (int)((GameMain.mSta_e[j].mY + 0.125f) / 0.25f);

				if(GameMain.mSta_e[j].active_flg){		//どの敵がいるか
					for(int vec2 = 0; vec2 < attak_map[this.No][0][0].length; vec2++){		//全方向チェック
						if((skill_play_x + attak_map[this.No][0][0][vec2]) == skill_enemy_x &&
							 (skill_play_y + attak_map[this.No][0][1][vec2]) == skill_enemy_y && state_flg){
							skill_attck_found = j;
						}

						if(skill_attck_found != -1) break;
					}
				}
				if(skill_attck_found != -1) break;
			}
		}

		if(skill_attck_found != -1){
			if(GameMain.mSta_e[skill_attck_found].active_flg){		//どの敵がいるか
				skill_enemy_x = (int)((GameMain.mSta_e[skill_attck_found].mX + 0.125f) / 0.25f);
				skill_enemy_y	= (int)((GameMain.mSta_e[skill_attck_found].mY + 0.125f) / 0.25f);

				//敵キャラクター移動向き判断
				dir_char = 0;
				if(skill_enemy_y >  skill_play_y){
					dir_char |= 0x1;
				}else if(skill_enemy_y < skill_play_y ){
					dir_char |= 0x2;
				}else{
					dir_char |= 0x0;
				}

				if(skill_enemy_x >  skill_play_x){
					dir_char |= 0x4;
				}else if(skill_enemy_x <  skill_play_x){
					dir_char |= 0x8;
				}else{
					dir_char |= 0x0;
				}
				direction_change(dir_char);		//向き指定
				GameMain.skill[skill_char_no].skill_dir = this.rect_numdir;

				//バトル処理
				char_skill_battle( char_no, skill_no, skill_attck_found);
				Log.d("１マススキル","skillbattle");
			}
		}

		if(attack_cnt >= 20)
			attack_cnt = 0;
	}


	/**
	 * キャラごとのバスキルバトル計算
	 * @param char_no
	 * @param skill_no
	 * @param enemy_no
	 */
	private void char_skill_battle(int char_no, int skill_no, int enemy_no){
		//バトル処理
		switch(char_no){
				case 0:	//主人公
					if((skill_no / 5) == 0){	//衝撃
						if(GameMain.skill[skill_no].frm_cnt_flg >= 2 && GameMain.skill[skill_no].frm_cnt_flg <= 5){
							skill_battle(enemy_no, (int)(GameMain.mSta_p[char_no].Pow * 7.02f));		//攻撃範囲内にいる敵に攻撃！！
							//Log.d("tes","enemy_No:"+j+", Pow:"+GameMain.mSta_p[char_no].Pow * 3.0f+", attack_cnt:"+attack_cnt);
						}
					}
					break;
				case 1:	//魔法使い
					if((skill_no / 5) == 0){	//火柱
						if(GameMain.skill[skill_no].frm_cnt_flg >= 2 && GameMain.skill[skill_no].frm_cnt_flg <= 3){
							skill_battle(enemy_no, (int)(GameMain.mSta_p[char_no].Pow * 6.25f));		//攻撃範囲内にいる敵に攻撃！！
							//Log.d("tes","enemy_No:"+j+", Pow:"+GameMain.mSta_p[char_no].Pow * 3.0f+", attack_cnt:"+attack_cnt);
						}
					}
					break;
				case 2:	//銃
					if((skill_no / 5) == 0){	//レーザー
						if(GameMain.skill[skill_no].frm_cnt_flg >= 4 && GameMain.skill[skill_no].frm_cnt_flg <= 6){
							skill_battle(enemy_no, (int)(GameMain.mSta_p[char_no].Pow * 0.33f));		//攻撃範囲内にいる敵に攻撃！！
							//Log.d("tes","enemy_No:"+j+", Pow:"+GameMain.mSta_p[char_no].Pow * 3.0f+", attack_cnt:"+attack_cnt);
						}
					}
					break;
				case 3:	//鎧
					if((skill_no / 5) == 0){	//スピア
						if(GameMain.skill[skill_no].frm_cnt_flg >= 3 && GameMain.skill[skill_no].frm_cnt_flg <= 4){
							skill_battle(enemy_no, (int)(GameMain.mSta_p[char_no].Pow * 2.85f));		//攻撃範囲内にいる敵に攻撃！！
							//Log.d("tes","enemy_No:"+j+", Pow:"+GameMain.mSta_p[char_no].Pow * 3.0f+", attack_cnt:"+attack_cnt);
						}
					}
					break;
				case 4:	//大剣
					if((skill_no / 5) == 0){	//衝撃波
						if(GameMain.skill[skill_no].frm_cnt_flg >= 3 && GameMain.skill[skill_no].frm_cnt_flg <= 4){
							skill_battle(enemy_no, (int)(GameMain.mSta_p[char_no].Pow * 3.91f));		//攻撃範囲内にいる敵に攻撃！！
							//Log.d("tes","enemy_No:"+j+", Pow:"+GameMain.mSta_p[char_no].Pow * 3.0f+", attack_cnt:"+attack_cnt);
						}
					}
					break;
		}
	}




	/**
	 *キャラクター移動の向きなどを設定実行
	 */
	private void move_char()
	{
		if(state_flg){
			boolean moveflg = false;
			int x = this.chara_move[0][point];
			int y = this.chara_move[1][point];
			if(x > 0 && y > 0){
				this.next_x = x;
				this.next_y = y;
			}else{
					//Log.e("error","キャラ"+this.Name);
			}
			//-------------------------------------------------------------------
			//移動処理　chara_move[][]を元に一歩ずつ移動
			//-------------------------------------------------------------------
			if(this.idou)
			{
				dir_char = 0;
				if(next_y * 0.25f > this.mY ){
					dir_char |= 0x1;		//敵キャラクター移動向き判断
					this.mY += 0.25f / this.Speed;
					moveflg = true;
				}else if(next_y * 0.25f < this.mY ){
					dir_char |= 0x2;		//敵キャラクター移動向き判断
					this.mY -= 0.25f / this.Speed;
					moveflg = true;
				}else{
					dir_char |= 0x0;		//敵キャラクター移動向き判断
				}

				if(next_x * 0.25f > this.mX ){
					dir_char |= 0x4;		//敵キャラクター移動向き判断
					this.mX += 0.25f / this.Speed;
					moveflg = true;
				}else if(next_x * 0.25f < this.mX ){
					dir_char |= 0x8;		//敵キャラクター移動向き判断
					this.mX -= 0.25f / this.Speed;
					moveflg = true;
				}else{
					dir_char |= 0x0;		//敵キャラクター移動向き判断
				}

				direction_change(dir_char);		//敵キャラクターの向く方向を制御する関数を呼び出す
		//誤差の修正--------------------------------------------------------------------
				if(this.mY % 0.25f < 0.001){
					this.mY = (int)(this.mY / 0.25f) * 0.25f;
					Log.d("Y","");
				}else if(this.mY % 0.25f > 0.249){
					Log.d("Y","");
					this.mY = ((int)(this.mY / 0.25f)+1) * 0.25f;
				}

				if(this.mX % 0.25f < 0.001){
					Log.d("X","");
					this.mX = (int)(this.mX / 0.25f) * 0.25f;
				}else if(this.mX % 0.25f > 0.249){
					Log.d("X","");
					this.mX = ((int)(this.mX / 0.25f)+1) * 0.25f;
				}
		//----------------------------------------------------------------------------

			}

			if(this.idou)
			{
				//指定した位置まで進んだか、移動先がない場合
				if(!moveflg)
				{
					if(damage_flg)		//ダメージを敵から受けている!
						enemy_search = true;

					point++;
					if(TeisuuTeigi.MAP_TIP_STAGE_SIZE[TeisuuTeigi.stage_area][0] * TeisuuTeigi.MAP_TIP_STAGE_SIZE[TeisuuTeigi.stage_area][1] <= point || point < 0){
						point = 0;
						this.chara_move[0][0] = -1;
						this.chara_move[1][0] = -1;
						mMapMove.chara_move_buf[0][0] = -1;
						mMapMove.chara_move_buf[1][0] = -1;
						this.idou = false;
					}
					Log.d("",""+this.Name+","+this.chara_move[0][point]);

					//移動先がなければ移動終了
					if(this.chara_move[0][point] == -1 ){
						point = 0;
						this.chara_move[0][0] = -1;
						this.chara_move[1][0] = -1;
						mMapMove.chara_move_buf[0][0] = -1;
						mMapMove.chara_move_buf[1][0] = -1;
						this.idou = false;
					}
				}

				if(enemey_collision_chk() && !moveflg){		//敵と衝突していたら立ち止まる
					move_change( (int)((this.mX + 0.125f)/0.25f),  (int)((this.mY + 0.125f)/0.25f), (int)((this.mX + 0.125f)/0.25f), (int)((this.mY + 0.125f)/0.25f));		//敵と接触したので立ち止まる
				}

			}else{
				if(this.chara_move[0][point] != -1){
					this.idou = true;
				}
			}


			//キャラが動き終わった後にスキルを発動させる
			for(int i = 0; i < GameMain.SKILL_NUM; i++){
				if(GameMain.skill[i % 5].skill__flg == 1 && this.skill_active_flg && !this.skill_timing){
					this.skill_timing = true;				//スキルを発動させるフラグ
					Log.d("skill_timing","スキル発動");
					this.skill_cutin_timing = true;			//スキルのカットインのタイミング
					Skill.skill_cutin_flg = (i % 5) + 1;
					this.skill_cutin_frame = 60;			//スキルカットインフレーム数初期化
					Skill.skill_cutin_alpha = 0.0f;
					Skill.skill_cutin_pos_x = 480.0f;
					TouchManagement.skill_cut_touch = true;
					this.s = Charstatus.SKILL;
				}

				if(this.skill_timing && this.s == Charstatus.SKILL) break;
			}
		}

	}


	/**
	 * 敵との衝突チェック
	 * @return true衝突 false 非接触
	 */
	private boolean enemey_collision_chk(){

		int my_x = 0, my_y = 0;
		float offset[][] = {
			{-0.125f, -0.125f},
			{ 0.125f, -0.125f},
			{-0.125f,  0.125f},
			{ 0.125f,  0.125f}
		};

		for(int i = 0; i < 4; i++){	//四つ角チェック
			my_x =(int)((this.mX+0.125f + offset[i][0]) / 0.25f);
			my_y =(int)((this.mY+0.125f + offset[i][1]) / 0.25f);

			//敵との当たり判定
			for(int j = 0; j < GameMain.ENEMY_MAX; j++){
				if(GameMain.mSta_e[j].active_flg){
					if((my_x ==(int)((GameMain.mSta_e[j].mX+0.125f) / 0.25f) && my_y == (int)((GameMain.mSta_e[j].mY+0.125f) / 0.25f))){
						Log.d("衝突！",this.Name);
						return true;
					}
				}
			}
		}
		return false;
	}




	/**
	 * 敵キャラクターの向く方向を制御します
	 * @param flg
	 */
	public void direction_change(int flg){
		char char_direction[] ={	//左下0!左上1!なし2左上3！左4！上5！なし6右下7!下8！右9！
									0,3,0,2,2,2,0,1,1,3
								};
		int char_direction2[] ={	//上 下 なし 右 右上 右下 なし 左 左上 左下
									1, 6, 0, 4, 2, 7, 0, 3, 0, 5
								};
		//Log.d("",""+flg);
		if(flg > 0 && flg != 3 && flg != 7){	//念のため
			flg --;
			rect_numdir = char_direction[flg];
			vec = char_direction2[flg];
			//Log.d("rect_numdir","rect_numdir:"+rect_numdir);
		}

	}







	/**
	 * バトル処理
	 * @param enemy_No
	 */
	private void battle(int enemy_No, int Pow){

		//20Fごとに攻撃
		if(this.attack_anime != rect_num ||		//キャラクターの攻撃するアニメナンバーでない
		f_count != (int)( attack_frame[this.No][this.attack_anime] / 2) &&		//キャラクターの攻撃するアニメナンバーのその攻撃フレームでない
		attack_cnt < 20 ){
			return;
		}else{
			int damage_p;

			if(mSta_e[enemy_No].Hp <= 0){
				return;					//死んでたら追撃しない
			}

	        //ダメージ設定
			if(Math.floor( Math.random() * 100 ) / critical_hit[this.No] < 1){									//クリティカル発生

				//クリティカルダメージ計算式 (攻/2 * 1.2 - 防/8 ±乱数(ダメージの0.5~-0.5倍) )
				damage_p = (int) (Pow / 2 * 1.2f - mSta_e[enemy_No].Def/8);
				Log.d("クリティカル"," "+damage_p);												//要らなくなったら消してね

			}else{																					//通常攻撃

				//通常ダメージ計算式 (攻/2 - 防/4 ±乱数(ダメージの0.5~-0.5倍) )
				damage_p = Pow / 2 - mSta_e[enemy_No].Def/4;
			}


			//攻撃をミスしなければ
			if(mSta_e[enemy_No].Avoid == 0 || mSta_e[enemy_No].Avoid != 0 && Math.floor( Math.random() * 100 ) / mSta_e[enemy_No].Avoid >= 1){

				//乱数値を追加
		        damage_p = damage_p + (int)( (Math.floor( Math.random() ) - 0.4f) * damage_p);

				//プレイヤーが攻撃
		        mSta_e[enemy_No].Hp -= damage_p;
		        mSta_e[enemy_No].damage_anime = 1;

		        //エフェクト
		        effect_generate(mSta_e[enemy_No].mX, mSta_e[enemy_No].mY, this.mX, this.mY, this.No);
		      	for(int i = 0; i < 30; i++){
					if(!GameMain.mDamage[i].disp_flg){
						GameMain.mDamage[i].reset(damage_p, mSta_e[enemy_No].mX, mSta_e[enemy_No].mY);
						break;
					}
		      	}
			}else{
			  	for(int i = 0; i < 30; i++){
					if(!GameMain.mDamage[i].disp_flg){
						GameMain.mDamage[i].reset(0, mSta_e[enemy_No].mX, mSta_e[enemy_No].mY);
						break;
					}
			  	}
			}


	        if(mSta_e[enemy_No].Hp > 0){
	        	//Log.d("攻撃！ダメージ",""+mSta_e[enemy_No].Hp);
	        }else{

	        	mSta_e[enemy_No].Hp = 0;
	        	//Log.d("HP:",""+mSta_e[enemy_No].Hp+" 敵は死にました.....");
				if(Sound.music.sp != null){
					Sound.music.SE_play(19);		//やられた音
				}

	        	mSta_e[enemy_No].active_flg = false;		//敵（俺）死んだ！！
	        	mSta_e[enemy_No].die_flg = true;		//敵（俺）死んだ！！

	        	//倒された敵情報を取得
	        	GameMain.die_enemy_num[mSta_e[enemy_No].No][0]++;
	        	GameMain.die_enemy_num[mSta_e[enemy_No].No][1] = mSta_e[enemy_No].score;
	        	Log.d("撃墜！",mSta_e[enemy_No].Name);

	    		for(int i = 0; i < GameMain.DIE_EFE_NUM; i++){
	    			if(!GameMain.Die_efe[i].active_flg){
	    				GameMain.Die_efe[i].Die_efe_reset( mSta_e[enemy_No].mX, mSta_e[enemy_No].mY);		//空きを探して死亡エフェクトを生成
	    				break;
	    			}
	    		}

	        	for(int i = 0;i < GameMain.PLAYER_NUM; i++)
	        	{
		        	if(GameMain.mSta_p[i].battel_target == enemy_No)	//バトルターゲット俺じゃん！！
			        {
		        		GameMain.mSta_p[i].battel_flg = false;		//お前ら、俺、死んだから狙うなよ！
		        		GameMain.mSta_p[i].s = Charstatus.MOVE;
		        		GameMain.mSta_p[i].battel_target = -1;
		        		GameMain.mSta_p[i].idou =false;
		        		//Log.d("バトルターゲットいなくなった人",""+this.Name);
		        	}
	        	}


	        	GameInfo.play_score += mSta_e[enemy_No].score;		//プレイ中スコアの加算
	        }

//	        Log.d("",""+mSta_e[enemy_No].Name);
		}

	}






	/**
	 * スキルのバトル処理
	 * @param enemy_No
	 * @param Pow
	 */
	private void skill_battle(int enemy_No, int Pow){

		//20Fごとに攻撃
		if(/*this.skill_anime != rect_num ||		//キャラクターの攻撃するアニメナンバーでない
		skill_f_count != (int)( skill_frame[this.No][this.skill_anime] / 2) &&		//キャラクターの攻撃するアニメナンバーのその攻撃フレームでない
		*/attack_cnt < this.skill_speed ){
			return;
		}else{
			int damage_p;

	        //ダメージ設定
			if(Math.floor( Math.random() * 100 ) / critical_hit[this.No] < 1){									//クリティカル発生

				//クリティカルダメージ計算式 (攻/2 * 1.2 - 防/8 ±乱数(ダメージの0.5~-0.5倍) )
				damage_p = (int) (Pow / 2 * 1.2f - mSta_e[enemy_No].Def/8);
				Log.d("クリティカル"," "+damage_p);												//要らなくなったら消してね

			}else{																					//通常攻撃

				//通常ダメージ計算式 (攻/2 - 防/4 ±乱数(ダメージの0.5~-0.5倍) )
				damage_p = Pow / 2 - mSta_e[enemy_No].Def/4;
			}


			//攻撃をミスしなければ
			if(mSta_e[enemy_No].Avoid == 0 || mSta_e[enemy_No].Avoid != 0 && Math.floor( Math.random() * 100 ) / mSta_e[enemy_No].Avoid >= 1){

				//乱数値を追加
		        damage_p = damage_p + (int)( (Math.floor( Math.random() ) - 0.4f) * damage_p);

				//プレイヤーが攻撃
		        mSta_e[enemy_No].Hp -= damage_p;
		        mSta_e[enemy_No].damage_anime = 1;

		        //エフェクト
		        effect_generate(mSta_e[enemy_No].mX, mSta_e[enemy_No].mY, this.mX, this.mY, this.No);

	          	for(int i = 0; i < 30; i++){
					if(!GameMain.mDamage[i].disp_flg){
						GameMain.mDamage[i].reset(damage_p, mSta_e[enemy_No].mX, mSta_e[enemy_No].mY);
						break;
					}
	        	}

			}else{
				Log.d("ミス",mSta_e[enemy_No].Name+" 回避率："+mSta_e[enemy_No].Avoid+"%");	 	//要らなくなったら消してね

	          	for(int i = 0; i < 30; i++){
					if(!GameMain.mDamage[i].disp_flg){
						GameMain.mDamage[i].reset(0, mSta_e[enemy_No].mX, mSta_e[enemy_No].mY);
						break;
					}
	        	}
			}


	        if(mSta_e[enemy_No].Hp > 0){
	        	//Log.d("攻撃！ダメージ",""+mSta_e[enemy_No].Hp);
	        }else{

	        	mSta_e[enemy_No].Hp = 0;
	        	//Log.d("HP:",""+mSta_e[enemy_No].Hp+" 敵は死にました.....");
	        	mSta_e[enemy_No].active_flg = false;		//敵（俺）死んだ！！
	        	mSta_e[enemy_No].die_flg = true;		//敵（俺）死んだ！！

	        	//倒された敵情報を取得
	        	GameMain.die_enemy_num[mSta_e[enemy_No].No][0]++;
	        	GameMain.die_enemy_num[mSta_e[enemy_No].No][1] = mSta_e[enemy_No].score;


	    		for(int i = 0; i < GameMain.DIE_EFE_NUM; i++){
	    			if(!GameMain.Die_efe[i].active_flg){
	    				GameMain.Die_efe[i].Die_efe_reset( mSta_e[enemy_No].mX, mSta_e[enemy_No].mY);		//空きを探して死亡エフェクトを生成
	    				break;
	    			}
	    		}

	        	for(int i = 0;i < GameMain.PLAYER_NUM; i++)
	        	{
		        	if(GameMain.mSta_p[i].battel_target == enemy_No)	//バトルターゲット俺じゃん！！
			        {
		        		GameMain.mSta_p[i].battel_flg = false;		//お前ら、俺、死んだから狙うなよ！
		        		//GameMain.mSta_p[i].s = Charstatus.MOVE;
		        		GameMain.mSta_p[i].battel_target = -1;
		        		GameMain.mSta_p[i].idou =false;
		        		//Log.d("バトルターゲットいなくなった人",""+this.Name);
		        	}
	        	}


	        	GameInfo.play_score += mSta_e[enemy_No].score;		//プレイ中スコアの加算
	        }

//	        Log.d("",""+mSta_e[enemy_No].Name);
		}

	}




	int se_n = 0;

	/***
	 * 攻撃エフェクトジェネレーター
	 * @param play_x
	 * @param play_y
	 * @param ene_x
	 * @param ene_y
	 */
	private void effect_generate(float play_x, float play_y, float ene_x, float ene_y, int p_no){
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


		if(this.skill_active_flg){
			if(Sound.music.sp != null){
				Sound.music.SE_play(4);		//爆発音
			}

		}else if(p_no == 2){					//銃使いの攻撃SE
			if(Sound.music.sp != null){
				Sound.music.SE_play(17);	//攻撃SEの再生
			}
		}else if(p_no == 3){					//槍使いの攻撃SE
			if(Sound.music.sp != null){
				Sound.music.SE_play(18);	//攻撃SEの再生
			}
		}else if(p_no == 4){					//大剣の攻撃SE
			if(Sound.music.sp != null){
				Sound.music.SE_play(16);	//攻撃SEの再生
			}

		}else{
			if(Sound.music.sp != null){
				se_n++;
				if(se_n == 0){
					Sound.music.SE_play(8);	//攻撃SEの再生
				}else{
					Sound.music.SE_play(9);	//攻撃SEの再生
					se_n = -1;
				}
			}
		}
	}

	/***
	 * 影の描画
	 * @param gl
	 * @param texture
	 */
	void shadow_draw(GL10 gl, int texture){
		gl.glPushMatrix();
		{
			gl.glTranslatef(mX, mY, 0.01f);
			gl.glScalef(0.25f, 0.25f, 1.0f);
			//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
			GraphicUtil.drawTexture_pixel_custom2(gl, 32.0f, 32.0f, texture,
					 new float[] { 0.0f,   0.0f, 32.0f, 32.0f, 32.0f / 2, 32.0f / 2},
					0.0f, 0.0f, 0.0f, 0.3f);
		}
		gl.glPopMatrix();
	}



	/***
	 * 死亡エフェクト
	 * @param gl
	 * @param texture
	 */
	void die_efect(GL10 gl, int texture){
		gl.glPushMatrix();
		{
			gl.glTranslatef(mX, mY, 0.01f);
			gl.glRotatef(-(Camera_var.angle - 180), 0.0f, 0.0f, 1.0f);
			gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
			gl.glScalef(0.25f, 0.25f, 1.0f);
			//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
			GraphicUtil.drawTexture_pixel_custom2(gl, 32.0f, 32.0f, texture,
					 new float[] { 0.0f,   0.0f, 32.0f, 32.0f, 32.0f / 2, 32.0f + die_pos},
					1.0f, 1.0f, 1.0f, 0.5f);
			die_pos += 1 * die_vec;
			if(die_pos > 10 || die_pos < 0){
				die_vec *= -1;
			}
		}
		gl.glPopMatrix();
	}






	//------------------------------------------------------------------------------
	/***
	 *
	 *  移動変更時 (ダブルタップ時)
	 * @param start_x
	 * @param start_y
	 * @param end_x
	 * @param end_y
	 */
	//------------------------------------------------------------------------------
	public void move_change(int start_x, int start_y, int end_x, int end_y){

		int enemy_x;
		int enemy_y;
		int i;
		Log.d("pos","start_x"+ start_x+"start_y"+  start_y+ ", end_x"+end_x +"end_y"+ end_y);
		if(end_x <= 0 || end_y <= 0 ||
			GameMain.map_size_x <= end_x || GameMain.map_size_y  <= end_y){
			return;
		}

		for(i = 0; i < GameMain.ENEMY_MAX; i++){		//敵のいる場所をタッチしたか？
			if(GameMain.mSta_e[i].active_flg){
				enemy_x = (int)((GameMain.mSta_e[i].mX + 0.125f) / 0.25f);
				enemy_y	= (int)((GameMain.mSta_e[i].mY + 0.125f) / 0.25f);

				if(enemy_x == end_x  &&  enemy_y == end_y){		//敵のいる場所だった
					this.battel_flg = true;
					this.battel_target = i;
					break;
				}
			}
		}
		if(i == GameMain.ENEMY_MAX){
			this.battel_flg = false;
			this.battel_target = -1;
			this.s = Charstatus.MOVE;
		}

		//移動可能か？
		switch( mMapMove.ArrayGenerater(this, start_x, start_y, end_x, end_y) ){
			//新しい移動
			case 0:
				this.idou = true;
				this.point = 0;
				break;

			//移動キャンセル
			case 1:
				this.point = 0;
				break;
		//移動の変更なし
			case 2:
				Log.d("test","変更なし"+this.state_flg);
				this.idou = true;
				return;
		}

		//------------------------------------------------------------------------------
		//配列のコピー
		//------------------------------------------------------------------------------
		for(i = 0; i < 2; i++){
			System.arraycopy( mMapMove.chara_move_buf[i], 0, chara_move[i], 0, chara_move[i].length);
		}

	}


}
