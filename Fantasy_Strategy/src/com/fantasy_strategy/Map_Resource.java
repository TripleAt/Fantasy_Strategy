package com.fantasy_strategy;



	/*
	 * 各クラスの説明
	 *
	 * Map_Resource()
	 * 		起動時に呼び出される
	 *
	 * stage~~()
	 * 		ステージごとにクラス分けされたmainクラス
	 *
	 * loadTextures_stage~~()
	 * 		ここに画像を格納（ステージ毎に）
	 */

class TeisuuTeigi{
	public static final int[][] MAP_TIP_STAGE_SIZE = {		//+2は外枠用なので、消さないで！
													  {10 + 2, 20 + 2 },
													  { 9 + 2, 12 + 2 },
													  {20 + 2, 20 + 2 },
													  {20 + 2, 20 + 2 },


													  {10 + 2, 20 + 2 },
													  {18 + 2, 24 + 2 },
													  {20 + 2, 20 + 2 },
													  {20 + 2, 20 + 2 },
													};
	public static int stage_area = 0;				//現在のステージ番号


	static int[][][] stage_all_map = {
			{//     1    2    3    4    5    6    7    8    9   10//草原1
				{   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,},	//1
				{   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,},	//2
				{   0, 100, 101, 102, 103, 104,   1,   0,  -1,   0,},	//3
				{   0,   0,   0,   0,   0,   0,   1,   1,   2,   2,},	//4
				{  -1,  -1,  -1,  -1,  -1,  -1,  -1,   0,   0,   0,},	//5
				{   2,   2,   2,   2,   2,   2,   2,   0,   0,   0,},	//6
				{   0,   0,   0,  -1, 136,   0,   0,   0, 131,   0,},	//7
				{   0,  -1,   0,  -1,  -1,  -1,  -1,  -1,  -1,  -1,},	//8
				{   0 ,131,   0,   3,   3, 131,   3,   3,   3,   3,},	//9
				{   2,   0, 131,   0,   0, 131,   0,   0,   0, 131,},	//10
				{   2, 131,   0,   0, 131,   0,   0, 133,   0,   0,},	//11
				{  -1,  -1,  -1,  -1,  -1,  -1,  -1,   0, 131,   1,},	//12
				{   2, 132,   2,   2,   2,   2,   2,   0,   0,   1,},	//13
				{   0,   0,  -1,   0,   0,  -1,   0,   0,   0,   0,},	//14
				{   0,   0, 133, 131,   0,   0,   0, 131,   0, 132,},	//15
				{ 131 ,  0 ,  0,  -1,  -1,  -1,  -1,  -1,  -1,  -1,},	//16
				{   1,   1,   0,   3, 132,   3,   3,   3,   3,   3,},	//17
				{  -1,   1, 133,   0,   0,   0,   0,   0,   0,   0,},	//18
				{  -1,   1,   0,   1,   1,   0, 131,   0, 132,   0,},	//19
				{   0,   0,   0,   0, 131,   0,   0, 130,   0,   0,}	//20
			},
			{//     1    2    3    4    5    6    7    8    9//六畳一間1
				{   6,   7,   7,   7,   7,   8,  17,  14,  11,},	//1
				{   3, 100, 101, 102, 103, 104,  16,  13,  10,},	//2
				{   0,   1,   1,   1,   1,   2,  16,  13,  10,},	//3
				{  17,  14,  11,  17,  14,  11,  16,  13,  10,},	//4
				{  16,  13,  10,  16,  13,  10,  16, 131,  10,},	//5
				{  16,  13,  10,  16,  13,  10,  15,  12,   9,},	//6
				{  16,  13,  10,  16, 135,  10,  17,  14,  11,},	//7
				{  16, 135,  10,  19, -2,   19,  16,  13,  10,},	//8
				{  15,  12,   9,  15,  12,   9,  16,  13,  10,},	//9
				{   6,   7,   7,   7,   7,   8,  16, 135,  10,},	//10
				{   3,   4, 130,   4,   4, 130,  16,  13,  10,},	//11
 				{   0,   1,   1,   1,   1,   2,  15,  12,   9,},	//12
			},
			{//     1    2    3    4    5    6    7    8    9   10   11   12   13   14   15   16   17   18   19   20//ダンジョン1
				{   0,   0,   0,   0,   0,  -2,   0,   0,   0,   0,   0,   0,   0,  -2,   0,   0,   0,   0,   0,   0,},	//1
				{   0,   0,   0,   0,   0,  -2,   0, 100, 101, 102, 103, 104,   0,  -2,   0,   0,   0,   0,   0,   0,},	//2
				{   0,   0,   0,   0,   0,  -2,   0,   0,   0,   0,   0,   0,   0,  -2,   0,   0,   0,  -2,   0,   0,},	//3
				{   0,   0,   0,   0,   0,  -2,  -2,  -4,   0,   0,  -4,  -2,  -2,  -2,   0,   0,   0,  -3,   0,   0,},	//4
				{  -3,   0,   0,  -3,   0,   0,   0,   0,   0,   0,   0,  -2,   0,   0,   0,  -2,  -2,   0,   0,   0,},	//5
				{   0,   0,   0,  -2,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,},	//6
				{   0,   0,   0,  -2,  -2,   0,  -2,  -2,  -2,   0,  -3,  -2,  -3,  -2,  -2,  -2,  -3,   0,   0,  -3,},	//7
				{   0,   0,   0,  -2,   0,   0,  -3,   0,  -2,   0,  -3,   0,  -2,   0,   0,   0,   0,   0,   0,   0,},	//6
				{   0 ,  0,   0,   3,   3,   3,  -3,   3,  -2,  -2,  -3,   0,   0,   3,   3,   3,   3,   3,   3,   3,},	//9
				{   0,   0,   0,   0,   0,   0,   0,   0,  -3,   0,   0,   0,   0,   0,   0,  -2,  -4,  -4,   0,  -4,}, //10
				{   0,   0,   0,   0,   0,   0,   0,   0,  -2,   0,   0,   0,   0,   0,   0,  -2,  -4,  -4,   0,   0,},	//11
				{  -3,  -2,  -2,  -3,  -2,  -2,  -4,   0,  -4,  -2,  -2,  -2,  -2,   0,   0,  -2,  -2,  -4,   0,  -4,},	//12
				{   0,   1,   1,   0,   0,   0,   0,   0,   0,   1,   0, 131,   0,   0,   0,   0,  -2,  -2, 133,  -2,},	//13
				{   0,   0,  -3,   0,   0,  -3,   0,   0,   0 ,  0,   0, 131,   0,   0,  -3,   0,  -2,  -2, 131,  -2,},	//14
				{  -2,  -2,  -2,  -3,  -2,  -3,  -2,  -2,  -2,  -2,  -3,  -2,  -3,  -2,  -2,  -2,  -2,  -4, 131,  -4,},	//15
				{   0,   0,   0,  -2, 131,   0,   0,   0,   0,   0,   0,   0,   0, 131,   0,   0,   0, 135, 131,   0,},	//16
				{   1,   1,   0,  -5, 131,   0,  -4,   0, 135,  -5,   0,   0,  -4,   0,   1,  -5,   0,   3,   3,   3,},	//17
				{   0, 130,   0, 134,   0,   0,   0,   0,   0,   0,   0, 135,   0, 135,   0,   0,   0, 133,   0,   0,},	//18
				{   0,   1,   0,  -5, 131,   0,  -4,   0, 135,  -5,   0,   0,  -4,   0,   0,  -5,   0,   0, 131,   0,}, //19
				{   0,   0,   0,  -3, 131,   0,   0,   0,   0,   0,   0,   0,   0,   0, 131,   0,   0, 131,   0,   0,}	//20
			},
			{//     1    2    3    4    5    6    7    8    9   10   11   12   13   14   15   16   17   18   19   20//???1
				{   0,   0,   0,   0,   0,  -2,   0,   0,   0,   0,   0,   0,   0,  -2,   0,   0,   0,   0,   0,   0,},	//1
				{   0,   0,   0,   0,   0,  -2,   0, 100, 101, 102, 103, 104,   0,  -2,   0,   0,   0,   0,   0,   0,},	//2
				{   0,   0, 131,   0,   0,  -2,   0,   0,   0,   0,   0,   0,   0,  -2,   0,   1,   0,  -2,   0,   0,},	//3
				{   0,   0,   0,   0,   0,  -2,  -2,  -3,   0,   0,  -3,  -2,  -2,  -2,   0,   0,   1,   1,   0,   0,},	//4
				{  -3,   0,   0,  -3,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  -2,  -2,   0,   0,   0,},	//5
				{   0,   0,   0,  -2,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,},	//6
				{   0,   0,   0,  -2,   0,   0,   0,   0, 	0,   0,   0,   0,   0,   0,   0,   0,   0,   0, 133,   0,},	//7
				{   0,   0,   0,  -2,  -2,  -2,  -2,  -2,  -2,  -2,  -3,  -2,  -3,  -2,  -2,  -2,  -3,   0,   0,  -3,},	//8
				{   0 ,132,   0,   3,   3,   3,   3,   3,   3,   3,   0 ,132,   0,   3,   3,   3,   3,   3,   3,   3,},	//9
				{   0,   0, 134,   0,   0,   0,   0,   0,   0,   0,   0,   0, 133,   0,   0,   0,   0,   0, 132,   0,}, //10
				{ 131,   0,   0,   0,   0,   0,   0, 135, 135,   0, 135, 135,   0,   0, 131,   0,   0, 132,   0,   0,},	//11
				{   1, 131,   0,  -3,  -2,  -2,  -2,  -3,  -2,  -2,  -2,  -2,  -2,  -2,  -3,  -2,  -2,   0,   0,   1,},	//12
				{   0,   1,   1,   0,   0,   0,   0,   0,   0,   1,   0,   0,   0,   0,   0,   0,   0,   0, 133,   1,},	//13
				{   0,   0,  -3,   0,   0,  -3,   0,   0,  -3 ,  0,   0,  -3,   0,   0,  -3,   0,   0,  -3,   0,   0,},	//14
				{   0,   0,   0, 135,   0,   0,   0, 131,   0,   0,   0,   0,   0, 131,   0,   0,   0, 135,   0,   0,},	//15
				{  -2,  -2,  -2,  -2,  -2,  -2,  -2,  -2,  -2,   0, 131 ,  0 ,  0,  -2,  -2,  -2,  -2,  -2,  -2,  -2,},	//16
				{   1,   1,   0,   3,   3, 133, 133, 133, 133, 133, 133, 133, 133,   0,   1,   1,   0,   3,   3,   3,},	//17
				{  -2,   1, 132,   0,   0,   0,   0,  -2,  -3,   0,  -3,  -2, 132,   0,   0,   0,   0,   0,   0,   0,},	//18
				{  -2,   1,   0,   1,   1,   0,   0,  -2,   0, 134,   0,  -2,   0,   0,   0, 131,   0,   0, 131,   0,}, //19
				{   0,   0,   0,   0, 131,   0,   0,  -2,   0, 130,   0,  -2,   0,   0, 131,   0,   0, 131,   0,   0,}	//20
			},



			{//     1    2    3    4    5    6    7    8    9   10
				{   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,},	//1
				{   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,},	//2
				{   0, 100, 101, 102, 103, 104,   1,   0,  -1,   0,},	//3
				{   0,   0,   0,   0,   0,   0,   1,   1,   2,   2,},	//4
				{  -1,  -1,  -1,  -1,  -1,  -1,  -1,   0,   0,   0,},	//5
				{   2,   2,   2,   2,   2,   2,   2,   0,   0,   0,},	//6
				{   0,   0,   0,  -1,   0,   0,   0,   0,   0,   0,},	//7
				{   0,  -1,   0,  -1,  -1,  -1,  -1,  -1,  -1,  -1,},	//8
				{   0 ,132,   0,   3,   3,   3,   3,   3,   3,   3,},	//9
				{   2,   0, 133,   0,   0,   0,   0,   0,   0,   0,},	//10
				{   2,   2,   0,   0, 131,   0,   0, 132,   0,   0,},	//11
				{  -1,  -1,  -1,  -1,  -1,  -1,  -1,   0,   0,   1,},	//12
				{   2,   2,   2,   2,   2,   2,   2,   0,   0,   1,},	//13
				{   0,   0,  -1,   0,   0,  -1,   0,   0,   0,   0,},	//14
				{   0,   0, 135, 132,   0,   0,   0, 131,   0, 135,},	//15
				{ 131 ,  0 ,  0,  -1,  -1,  -1,  -1,  -1,  -1,  -1,},	//16
				{   1,   1,   0,   3,   3,   3,   3,   3,   3,   3,},	//17
				{  -1,   1, 134,   0,   0,   0,   0,   0,   0,   0,},	//18
				{  -1,   1,   0,   1,   1,   0,   0,   0,   0,   0,},	//19
				{   0,   0,   0,   0, 131,   0,   0, 130,   0,   0,}	//20
			},
			{//     1    2    3    4    5    6    7    8    9
				{   6,   7,   7,   7,   7,   8,  17,  14,  11,   6,   7,   7,   7,   7,   8,  17,  14,  11,},	//1
				{   3, 100, 101, 102, 103, 104,  16,  13,  10,   3,   4,   4,   4,   4,   4,  16,  13,  10,},	//2
				{   0,   1,   1,   1,   1,   2,  16,  13,  10,   0,   1,   1,   1,   1,   2,  16,  13,  10,},	//3
				{  17,  14,  11,  17,  14,  11,  16,  13,  10,  17,  14,  11,  17,  14,  11,  16,  13,  10,},	//4
				{  16,  13,  10,  16,  13,  10,  16, 131,  10,  16,  13,  10,  16,  13,  10,  16, 131,  10,},	//5
				{  16,  13,  10,  16,  13,  10,  15,  12,   9,  16,  13,  10,  16,  13,  10,  15,  12,   9,},	//6
				{  16,  13,  10,  16, 135,  10,  17,  14,  11,  16,  13,  10,  16, 135,  10,  17,  14,  11,},	//7
				{  16, 135,  10,  19, -2,   19,  16,  13,  10,  16, 135,  10,  19, -2,   19,  16,  13,  10,},	//8
				{  15,  12,   9,  15,  12,   9,  16,  13,  10,  15,  12,   9,  15,  12,   9,  16,  13,  10,},	//9
				{   6,   7,   7,   7,   7,   8,  16, 135,  10,   6,   7,   7,   7,   7,   8,  16, 135,  10,},	//10
				{   3,   4,   4,   4, 130,   4,  16,  13,  10,   3,   4,   4,   4, 130,   4,  16,  13,  10,},	//11
 				{   0,   1,   1,   1,   1,   2,  15,  12,   9,   0,   1,   1,   1,   1,   2,  15,  12,   9,},	//12
 				{   6,   7,   7,   7,   7,   8,  17,  14,  11,   6,   7,   7,   7,   7,   8,  17,  14,  11,},	//13
				{   3,   4,   4,   4,   4,   4,  16,  13,  10,   3,   4,   4,   4,   4,   4,  16,  13,  10,},	//14
				{   0,   1,   1,   1,   1,   2,  16,  13,  10,   0,   1,   1,   1,   1,   2,  16,  13,  10,},	//15
				{  17,  14,  11,  17,  14,  11,  16,  13,  10,  17,  14,  11,  17,  14,  11,  16,  13,  10,},	//16
				{  16,  13,  10,  16,  13,  10,  16, 131,  10,  16,  13,  10,  16,  13,  10,  16, 131,  10,},	//17
				{  16,  13,  10,  16,  13,  10,  15,  12,   9,  16,  13,  10,  16,  13,  10,  15,  12,   9,},	//18
				{  16,  13,  10,  16, 135,  10,  17,  14,  11,  16,  13,  10,  16, 135,  10,  17,  14,  11,},	//19
				{  16, 135,  10,  19, -2,   19,  16,  13,  10,  16, 135,  10,  19, -2,   19,  16,  13,  10,},	//20
				{  15,  12,   9,  15,  12,   9,  16,  13,  10,  15,  12,   9,  15,  12,   9,  16,  13,  10,},	//21
				{   6,   7,   7,   7,   7,   8,  16, 135,  10,   6,   7,   7,   7,   7,   8,  16, 135,  10,},	//22
				{   3,   4,   4,   4, 130,   4,  16,  13,  10,   3,   4,   4,   4, 130,   4,  16,  13,  10,},	//23
 				{   0,   1,   1,   1,   1,   2,  15,  12,   9,   0,   1,   1,   1,   1,   2,  15,  12,   9,},	//24
			},
			{//     1    2    3    4    5    6    7    8    9   10   11   12   13   14   15   16   17   18   19   20
				{   0,   0,   0,   0,   0,  -2,   0,   0,   0,   0,   0,   0,   0,  -2,   0,   0,   0,   0,   0,   0,},	//1
				{   0,   0,   0,   0,   0,  -2,   0, 100, 101, 102, 103, 104,   0,  -2,   0,   0,   0,   0,   0,   0,},	//2
				{   0,   0, 131,   0,   0,  -2,   0,   0,   0,   0,   0,   0,   0,  -2,   0,   1,   0,  -2,   0,   0,},	//3
				{   0,   0,   0,   0,   0,  -2,  -2,  -3,   0,   0,  -3,  -2,  -2,  -2,   0,   0,   1,   1,   0,   0,},	//4
				{  -3,   0,   0,  -3,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  -2,  -2,   0,   0,   0,},	//5
				{   0,   0,   0,  -2,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,},	//6
				{   0,   0,   0,  -2,   0,   0,   0,   0, 133,   0,   0,   0,   0,   0,   0,   0,   0,   0, 133,   0,},	//7
				{   0,   0,   0,  -2,  -2,  -2,  -2,  -2,  -2,  -2,  -3,  -2,  -3,  -2,  -2,  -2,  -3,   0,   0,  -3,},	//8
				{   0 ,132,   0,   3,   3,   3,  -2,   3,   3,   3,   0 ,132,   0,   3,   3,   3,   3,   3,   3,   3,},	//9
				{   0,   0, 133,   0,  -2,   0,  -2,   0,   0,   0,   0,   0, 133,   0,   0,   0,   0,   0,   0,   0,}, //10
				{   0,   0,   0,   0,  -2,   0,   0, 132,   0,   0,   0,   0,   0,   0, 131,   0,   0, 132,   0,   0,},	//11
				{  -2,   0,  -2,  -3,  -2,  -2,  -2,  -3,  -2,  -2,  -2,  -2,  -2,  -2,  -3,  -2,  -2,   0,   0,   1,},	//12
				{   0,   1,   1,   0,   0,   0,   0,   0,   0,   1,   0,   0,   0,   0,   0,   0,   0,   0,   0,   1,},	//13
				{   0,   0,  -3,   0,   0,  -3,   0,   0,  -3 ,  0,   0,  -3,   0,   0,  -3,   0,   0,  -3,   0,   0,},	//14
				{   0,   0,   0, 131,   0,   0,   0, 131,   0,   0,   0,   0,   0, 131,   0,   0,   0, 131,   0,   0,},	//15
				{   0,   0,   0,  -2,  -2,  -2,  -2,  -2,  -2,  -2, 131,   0,   0,  -2,  -2,  -2,  -2,  -2,  -2,  -2,},	//16
				{   1,   1,   0,   3,   3,   0,   0,   0,   0, 135,   0,   0,   0,   0,   1,   1,   0,   3,   3,   3,},	//17
				{  -2,   1,   0,   0,   0,   0,   0,  -2,  -3,   0,  -3,  -2,   0,   0,   0,   0,   0,   0,   0,   0,},	//18
				{  -2,   1,   0,   1,   1,   0,   0,  -2,   0,   0,   0,  -2,   0,   0,   0, 131,   0,   0, 131,   0,}, //19
				{   0,   0,   0,   0,   0,   0,   0,  -2,   0, 130,   0,  -2,   0,   0, 131,   0,   0, 131,   0,   0,}	//20
			},
			{//     1    2    3    4    5    6    7    8    9   10   11   12   13   14   15   16   17   18   19   20
				{   0,   0,   0,   0,   0,  -2,   0,   0,   0,   0,   0,   0,   0,  -2,   0,   0,   0,   0,   0,   0,},	//1
				{   0,   0,   0,   0,   0,  -2,   0, 100, 101, 102, 103, 104,   0,  -2,   0,   0,   0,   0,   0,   0,},	//2
				{   0,   0, 131,   0,   0,  -2,   0,   0,   0,   0,   0,   0,   0,  -2,   0,   1,   0,  -2,   0,   0,},	//3
				{   0,   0,   0,   0,   0,  -2,  -2,  -3,   0,   0,  -3,  -2,  -2,  -2,   0,   0,   1,   1,   0,   0,},	//4
				{  -3,   0,   0,  -3,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,  -2,  -2,   0,   0,   0,},	//5
				{   0,   0,   0,  -2,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,},	//6
				{   0,   0,   0,  -2,   0,   0,   0,   0, 133,   0,   0,   0,   0,   0,   0,   0,   0,   0, 133,   0,},	//7
				{   0,   0,   0,  -2,  -2,  -2,  -2,  -2,  -2,  -2,  -3,  -2,  -3,  -2,  -2,  -2,  -3,   0,   0,  -3,},	//8
				{   0 ,132,   0,   3,   3,   3,   3,   3,   3,   3,   0 ,132,   0,   3,   3,   3,   3,   3,   3,   3,},	//9
				{   0,   0, 133,   0,   0,   0,   0,   0,   0,   0,   0,   0, 133,   0,   0,   0,   0,   0,   0,   0,}, //10
				{   0,   0,   0,   0, 131,   0,   0, 132,   0,   0,   0,   0,   0,   0, 131,   0,   0, 132,   0,   0,},	//11
				{  -2,  -2,  -2,  -3,  -2,  -2,  -2,  -3,  -2,  -2,  -2,  -2,  -2,  -2,  -3,  -2,  -2,   0,   0,   1,},	//12
				{   0,   1,   1,   0,   0,   0,   0,   0,   0,   1,   0,   0,   0,   0,   0,   0,   0,   0,   0,   1,},	//13
				{   0,   0,  -3,   0,   0,  -3,   0,   0,  -3 ,  0,   0,  -3,   0,   0,  -3,   0,   0,  -3,   0,   0,},	//14
				{   0,   0,   0, 131,   0,   0,   0, 131,   0,   0,   0,   0,   0, 131,   0,   0,   0, 131,   0,   0,},	//15
				{ 131 ,  0 ,  0,  -2,  -2,  -2,  -2,  -2,  -2,  -2, 131 ,  0 ,  0,  -2,  -2,  -2,  -2,  -2,  -2,  -2,},	//16
				{   1,   1,   0,   3,   3, 133, 133, 133, 133, 133, 133, 133, 133,   0,   1,   1,   0,   3,   3,   3,},	//17
				{  -2,   1, 132,   0,   0,   0,   0,  -2,  -3,   0,  -3,  -2, 132,   0,   0,   0,   0,   0,   0,   0,},	//18
				{  -2,   1,   0,   1,   1,   0,   0,  -2,   0,   0,   0,  -2,   0,   0,   0, 131,   0,   0, 131,   0,}, //19
				{   0,   0,   0,   0, 131,   0,   0,  -2,   0, 130,   0,  -2,   0,   0, 131,   0,   0, 131,   0,   0,}	//20

			}
	};


	//MAP[][]の中身を数値化したもの
//	public static final int M_START = 0;			//スタート位置
	public static final int M_SOUGEN = 0;
	public static final int M_HANA = 1;
	public static final int M_KUSA1 =2;
	public static final int M_KUSA2 = 3;
	public static final int M_PLAYER1 = 100;
	public static final int M_PLAYER2 = 101;
	public static final int M_PLAYER3 = 102;
	public static final int M_PLAYER4 = 103;
	public static final int M_PLAYER5 = 104;

	public static final int M_HIZUMI =130;
	public static final int M_SLIME = 131;
	public static final int M_KINOKO = 132;
	public static final int M_NIWATORI =133;
	public static final int M_DORAGON =134;
	public static final int M_GOBURIN =135;
	public static final int M_SLIME2 = 136;

	public static final int M_SNAG = -1;			//障害物
	public static final int M_SYOKI = 0;			//初期値（移動可能な地形）
	public static final int M_SYOKI_UME = -9;		//使われない初期値を変換する用
}


public class Map_Resource {

	 //座標

	public static int[][] map;

	int enemy_flg = 0;

	int stage = 0;

	//マップ地形
	public static int[][] stage_map;


	public Map_Resource(int stage){

		this.stage = stage;
		map = new int[TeisuuTeigi.MAP_TIP_STAGE_SIZE[TeisuuTeigi.stage_area][1]][TeisuuTeigi.MAP_TIP_STAGE_SIZE[TeisuuTeigi.stage_area][0]];
		//マップ初期化
		syokika();

	}


	/*--------------------------------------------------
	 * 		マップの読み込み
	 ----------------------------------------------------*/
	public void load_stage(int stage){
		GameMain.map_size_x = TeisuuTeigi.MAP_TIP_STAGE_SIZE[stage][0];
		GameMain.map_size_y = TeisuuTeigi.MAP_TIP_STAGE_SIZE[stage][1];
		stage_map = TeisuuTeigi.stage_all_map[stage];
	}


	//初期化処理
	public void syokika(){

		//-------------------------------------------------------------------------
		//           マップ情報配列 初期化
		//-------------------------------------------------------------------------
		for(int i=1; i < TeisuuTeigi.MAP_TIP_STAGE_SIZE[stage][1]-2; i++){
			for(int j=1; j < TeisuuTeigi.MAP_TIP_STAGE_SIZE[stage][0]-2; j++){
				map[i][j] = TeisuuTeigi.M_SYOKI;
			}
		}
		//マップ情報配列初期化 外枠！
		for(int i=0; i < TeisuuTeigi.MAP_TIP_STAGE_SIZE[stage][1]; i++){
			map[i][0] = TeisuuTeigi.M_SNAG;
			map[i][ TeisuuTeigi.MAP_TIP_STAGE_SIZE[stage][0]-1] = -1;
			for(int j=0; j < TeisuuTeigi.MAP_TIP_STAGE_SIZE[stage][0]; j++){
				map[0][j] = TeisuuTeigi.M_SNAG;
				map[ TeisuuTeigi.MAP_TIP_STAGE_SIZE[stage][1]-1][j] = TeisuuTeigi.M_SNAG;
			}
		}
	}



}