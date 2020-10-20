package com.fantasy_strategy;

import java.util.LinkedList;
import java.util.Stack;

import android.util.Log;

//------------------------------------------------------------------
//Map_copy[][]の中身の数字！ 定数定義
//------------------------------------------------------------------
//class route{
//
//	static final int M_START = -99;
//	static final int R_SNAG = -1;		//障害物
//	static final int R_SYOKI = 0;		//初期値（移動可能な地形）
////	static final int R_SNAG = -1;
//
//}

//--------------------------------------------------------------------
//
//  *** キャラクターのマップ移動 ***

//  マップ初期化 → サーチ（マップに移動数値を入れる）→ 移動位置を格納(ゴールからさかのぼって)    を行う
//
//-----------------------------------------------------------------------

public class MapMove {

	//キューの使い方
	//http://www.javadrive.jp/start/linkedlist/index5.html

	//マップサイズ
	public static int MAP_X;
	public static int MAP_Y;

	//MAP[][] 代入用配列
	private int MAP_copy[][];

	//キュー（FIFO法）処理中の座標を格納
	LinkedList<Integer> que_MAPsearchY = new LinkedList<Integer>();
	LinkedList<Integer> que_MAPsearchX = new LinkedList<Integer>();


	public int chara_move_buf[][] = new int[2][MAP_Y * MAP_X ];		//キャラの移動ルート
	private boolean flg;											//処理継続フラグ

	//終了位置
	private int eX;
	private int eY;

	//Map_idou()用
	private int bunkiY;
	private int bunkiX;

	private int MAP_idou_cnt;

	//初期化処理
	public MapMove () {
		 MAP_X = TeisuuTeigi.MAP_TIP_STAGE_SIZE[TeisuuTeigi.stage_area][0];
		 MAP_Y = TeisuuTeigi.MAP_TIP_STAGE_SIZE[TeisuuTeigi.stage_area][1];
		 MAP_copy = new int[MAP_Y][MAP_X];
		 chara_move_buf = new int[2][MAP_Y * MAP_X ];

	}

	//-------------------------------
	//実行関数
	//
	//目的地を指定して移動
	//-------------------------------
	public int ArrayGenerater(Status_player it, int start_x, int start_y, int end_x, int end_y){

		if(it.idou == true && this.chara_move_buf[0][it.point] > 0){	//現在移動中なら次のポイント（マス）からスタート
			start_x = this.chara_move_buf[0][it.point];
			start_y = this.chara_move_buf[1][it.point];
		}

		//MyRenderのMAP配列を代入する (マップ初期化)
		for(int i = 0; i < MAP_Y; i++){
			System.arraycopy( Map_Resource.map[i], 0, MAP_copy[i], 0, Map_Resource.map[i].length);
		}

		//移動先が障害物の時ログを出すMAP_copy[start_y][start_x] == -1 || 、
		if(MAP_copy[start_y][start_x] == -1){
			Log.e("MapMove87行目","現在地が障害物扱いになっている！！　chara_moveを確認!!"+ it.Name);
			//chara_move[]の中身（移動ルート）
			Log.e("chara_move"," "+chara_move_buf[0][0] + " "+chara_move_buf[0][1] + " "+chara_move_buf[0][2] + " "+chara_move_buf[0][3] + " "+chara_move_buf[0][4]);
			Log.e("chara_move"," "+chara_move_buf[1][0] + " "+chara_move_buf[1][1] + " "+chara_move_buf[1][2] + " "+chara_move_buf[1][3] + " "+chara_move_buf[1][4]);
		}

		//移動先が現在地、０以下なら終了（立ち止まる）
		if(MAP_copy[end_y][end_x] == -1 || ( start_x == end_x && start_y == end_y) || start_y < 1 || start_x < 1){
			Log.e("MapMoveのArrayGenerater",""+start_x+","+start_y+ it.Name);
			if(start_y < 1 || start_x < 1){
				Log.e("MapMove92行目","現在位置の座標がエリア外になっている!! x="+ start_x +" y="+ start_y);
			}else if(MAP_copy[end_y][end_x] == -1){
				Log.e("","指定先が障害物");
			}else if(start_x == end_x && start_y == end_y){
				Log.e("","現在位置と指定先が同じ");
			}
			//移動終了の値を格納する

			for(int j=0; j < chara_move_buf[0].length; j++){
				chara_move_buf[0][j] = -1;
				chara_move_buf[1][j] = -1;
			}
			return 1;				//移動しない
		}

		//end_y,end,xが同じなら前の移動のまま
		if(this.eX == end_x && this.eY == end_y){
			return 2;				//移動変更なし
		}

		this.eX = end_x;
		this.eY = end_y;
		this.flg = true;
		this.MAP_idou_cnt = 0;


		//現在位置の座標をキューに格納
		que_MAPsearchY.offer(start_y);
		que_MAPsearchX.offer(start_x);

		//-----------------------------------------------------------------------------
		//ルートを探索する。MAP_copyに対して値を入れていく
		//-----------------------------------------------------------------------------
		{
			//キューの値を取り出してMAP_search()へ	//1マスずつ行う
			MAP_search( que_MAPsearchY.poll(), que_MAPsearchX.poll());
			//キューが空になるか、ゴール座標を見つけるまでループ
			while( que_MAPsearchY.peek() != null && flg){

				MAP_search( que_MAPsearchY.poll(), que_MAPsearchX.poll());
			}

			//移動に関係ない 0 (初期値)を埋める
			MAP_copy_ume();

			MAP_copy[start_y][start_x] = TeisuuTeigi.M_SYOKI;			// スタート座標を0で上書き
		}
		//-----------------------------------------------------------------------------


		//移動ルート作成
		MAP_idou(this.eX,this.eY);			//移動ルートをスタックを用いてchara_move[]に格納する

//		//------------------------------------------------------------------------------
//		//Status_enemyのchara_moveに設定 (マップ初期化)
//		//------------------------------------------------------------------------------
//		for(int i = 0; i < 2; i++){
//			System.arraycopy( chara_move[i], 0, mStatus[No].chara_move[i], 0, mStatus[No].chara_move[i].length);
//		}
//		mStatus[No].idou = true;

		//chara_move[]の中身（移動ルート）
		Log.d("chara_move"," "+chara_move_buf[0][0] + " "+chara_move_buf[0][1] + " "+chara_move_buf[0][2] + " "+chara_move_buf[0][3] + " "+chara_move_buf[0][4]);
		Log.d("chara_move"," "+chara_move_buf[1][0] + " "+chara_move_buf[1][1] + " "+chara_move_buf[1][2] + " "+chara_move_buf[1][3] + " "+chara_move_buf[1][4]);

		que_MAPsearchY.clear();
		que_MAPsearchX.clear();

		return 0;				//移動する

	}

	//--------------------------------------
	//MAP_copy[][]に移動数を格納する関数　
	//--------------------------------------
	public void MAP_search(int Y, int X){

		//処理終了条件
		if(Y == this.eY && X == this.eX){
			flg = false;
			return;
		}

		// 周りの座標が0なら++1 そして、 キューに座標を格納する
		for(int i=-1;i<2;i=i+2){
			for(int j=-1;j<2;j=j+2){
				//斜め   (隣接する縦横に-1が無く、その座標が0なら)
				if(MAP_copy[Y+i][X] != -1 && MAP_copy[Y][X+j] != -1 && MAP_copy[Y+i][X+j] == TeisuuTeigi.M_SYOKI){
					MAP_copy[Y+i][X+j]=MAP_copy[Y][X]+1;
					que_MAPsearchY.offer(Y+i);
					que_MAPsearchX.offer(X+j);
				}
			}
			//縦
			if(MAP_copy[Y+i][X] == TeisuuTeigi.M_SYOKI){
				MAP_copy[Y+i][X]=MAP_copy[Y][X]+1;
				que_MAPsearchY.offer(Y+i);
				que_MAPsearchX.offer(X);
			}
			//横
			if(MAP_copy[Y][X+i] == TeisuuTeigi.M_SYOKI){
				MAP_copy[Y][X+i]=MAP_copy[Y][X]+1;
				que_MAPsearchY.offer(Y);
				que_MAPsearchX.offer(X+i);
			}
		}
	}

	//----------------------------------------------------------------------
	//移動に関係ない 0 (初期値)を埋める
	//-----------------------------------------------------------------------
	private void MAP_copy_ume(){

		//移動ログ( MAP_copy[][]の中身 )
//		for(int i=0; i<12; i++){
//			Log.d("tes",""+MAP_copy[i][0]+MAP_copy[i][1]+MAP_copy[i][2]+MAP_copy[i][3]+MAP_copy[i][4]+MAP_copy[i][5]);
//		}

		for(int i=0; i < MAP_Y; i++){
			for(int j=0; j < MAP_X; j++){
				if( MAP_copy[i][j] == 0 ){
//					MAP_copy[i][j] = TeisuuTeigi.M_SYOKI_UME;
					MAP_copy[i][j] = -9;
				}
			}
		}
	}


	//-----------------------------------------------------------------------------------------------------
	//移動ルートをスタックを用いてchara_move[]に格納する
	//------------------------------------------------------------------------------------------------------
	private void MAP_idou(int x, int y){

		//
		int idouY = y;
		int idouX = x;
		MAP_idou_cnt++;

		//スタック（LIFO法）配列の中身の順番を逆に格納するのに使う
		Stack<Integer> stack_MAPidouY = new Stack<Integer>();
		Stack<Integer> stack_MAPidouX = new Stack<Integer>();

		//移動ルートを格納
		stack_MAPidouY.push(idouY);
		stack_MAPidouX.push(idouX);


		//移動ルート格納ループ
		while(MAP_copy[idouY][idouX] != -1){

loop:		for(int i=-1;i<2;i=i+2){				//移動ルート最適化のため、斜めを先に (ソースを見やすく直すと頭悪い移動しちゃうので、さわらないで！)
				for(int j=-1;j<2;j=j+2){
					//斜め
					if( MAP_copy[idouY+i][idouX] != -1 && MAP_copy[idouY][idouX+j] != -1 &&  MAP_copy[idouY+i][idouX+j] == (MAP_copy[idouY][idouX]-1) ){
						bunkiY = idouY+i;
						bunkiX = idouX+j;
						break loop;
					}
				}
			}

			for(int i=-1;i<2;i=i+2){			//移動ルート最適化のため、次に縦横移動  （同上）
				//縦
				if(MAP_copy[idouY+i][idouX] == (MAP_copy[idouY][idouX]-1) ){
					bunkiY = idouY+i;
					bunkiX = idouX;
					break;
				}
				//横
				if(MAP_copy[idouY][idouX+i] == (MAP_copy[idouY][idouX]-1) ){
					bunkiY = idouY;
					bunkiX = idouX+i;
					break;
				}
			}

			MAP_idou_cnt++;


			//移動ルートを格納
			stack_MAPidouY.push(bunkiY);
			stack_MAPidouX.push(bunkiX);

			if(MAP_idou_cnt > MAP_Y * MAP_X - 2){
				Log.e("MapMove:266行あたり","ルート探索失敗！！！！！！！！！！！！");
				chara_move_buf[0][0] = -1;
				chara_move_buf[1][0] = -1;
				return;
			}

			//スタート座標に来たら終了
			if(MAP_copy[bunkiY][bunkiX] == TeisuuTeigi.M_SYOKI){
				Log.d("すたーと","はっけん");
				break;
			}

			idouY = bunkiY;
			idouX = bunkiX;
		}

		//chara_move[][]に格納する。順番を逆にして格納。
		int i=0;
		for(int j=0; j < chara_move_buf.length; j++){
			chara_move_buf[0][j] = 0;
			chara_move_buf[1][j] = 0;
		}
		while( !stack_MAPidouY.empty() ){
			chara_move_buf[0][i] = stack_MAPidouX.pop();	//Y軸
			chara_move_buf[1][i] = stack_MAPidouY.pop();	//X軸
			i++;
		}

		//スタック初期化
		stack_MAPidouY.clear();
		stack_MAPidouX.clear();

		//終了の目印
		chara_move_buf[0][i] = -1;
		chara_move_buf[1][i] = -1;
	}




}
