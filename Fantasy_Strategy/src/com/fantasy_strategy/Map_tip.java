package com.fantasy_strategy;

import javax.microedition.khronos.opengles.GL10;

public class Map_tip
{
	public  float mX, mY;	//標的の位置
	public  int map_rect_num;		//矩形番号
	public  float mTurnAngle;	//標的の旋回角度
	public  int snag_num = 0;		//障害物フラグ

	int rect_num2 = 0;
	int rect_num3 = 0;
	int count = 0;
	int rect_sak = 0;
	private float rect[][] ={
			{   0.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},	//0草
			{  32.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},	//1花
			{  64.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},	//2砂
			{  96.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},	//3石畳
			{ 128.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},	//4砂（影）
			{ 160.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},	//5草（影）
			{ 192.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},	//6ヒヅミ
			{  224.0f,     0.0f,    32.0f,    32.0f,    16.0f,    16.0f},
			{  256.0f,     0.0f,    32.0f,    32.0f,    16.0f,    16.0f},
			{  288.0f,     0.0f,    32.0f,    32.0f,    16.0f,    16.0f},
			{  320.0f,     0.0f,    32.0f,    32.0f,    16.0f,    16.0f},
			{  352.0f,     0.0f,    32.0f,    32.0f,    16.0f,    16.0f},
			{  384.0f,     0.0f,    32.0f,    32.0f,    16.0f,    16.0f},
			{  416.0f,     0.0f,    32.0f,    32.0f,    16.0f,    16.0f},
			{  448.0f,     0.0f,    32.0f,    32.0f,    16.0f,    16.0f},
			{  480.0f,     0.0f,    32.0f,    32.0f,    16.0f,    16.0f},
			{    0.0f,    32.0f,    32.0f,    32.0f,    16.0f,    16.0f},
			{   32.0f,    32.0f,    32.0f,    32.0f,    16.0f,    16.0f},
			{   64.0f,    32.0f,    32.0f,    32.0f,    16.0f,    16.0f},
			{   96.0f,    32.0f,    32.0f,    32.0f,    16.0f,    16.0f},
			{  128.0f,    32.0f,    32.0f,    32.0f,    16.0f,    16.0f},
	};

	private float rect2[][] = {
				{  0.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},
				{ 32.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},
				{ 64.0f, 0.0f, 32.0f, 32.0f, 16.0f, 16.0f},
			  };

	// 画像の大きさ定数定義
	public static float size_x = 0.25f;
	public static float size_y = 0.25f;
	public float size_z = 1.0f;
	public int ene_flg = 0;
	private float blue_col = 0.0f;

	//攻撃範囲設定変数群
	public float attack_tenmetu = 1.0f;		//攻撃範囲の点滅
	private boolean attack_tenmetu_flg = true;	//点滅状態を見るフラグ
	public boolean attack_Active_flg = false;	//現在点滅が動いているかどうか
	public int play_num = -1;


	public Map_tip(float x, float y,int rectnumber)
	{
		this.mX = x;
		this.mY = y;

		this.snag_num = 0;

		this.map_rect_num = rectnumber;
	}


	//標的を移動させます
	public void move(float x, float y) {
		this.mX = x;
		this.mY = y;

		// カーソル専用あとで変える
		if(map_rect_num == 999){
			count++;
			if(ene_flg == 1){
				blue_col = 1.0f;
			}else{
				blue_col = 0.0f;
			}
			if(count == 5)
			{
				rect_num2++;
				rect_num3++;
			}
			rect_num2 = rect_num2 % 3;
			rect_num3 = rect_num3 % 4;
			count = count % 5;
		}
	}


	public void move() {

	}

	//マップチップを描画します
	public void draw(GL10 gl, int texture) {
		gl.glPushMatrix();
		{
			if(map_rect_num == 999)
				gl.glTranslatef(mX, mY, 0.002f);		//座標位置
			if(map_rect_num != 999)
				gl.glTranslatef(mX, mY, 0.0f);		//座標位置

			//gl.glRotatef(0.0f, 0.0f, 0.0f, 1.0f);	//回転
			//gl.glScalef(0.25f, 0.25f, 1.0f);	//画像の大きさ指定

			if(map_rect_num != 999){
				GraphicUtil.drawTexture_pixel_custom3(gl, 512.0f, 512.0f, texture,
						rect[map_rect_num],
						1.0f, 1.0f, 1.0f, 1.0f);	//画像で使う座標を設定
			}
			else{
				GraphicUtil.drawTexture_pixel_custom3(gl, 128.0f, 32.0f, texture,
						rect2[rect_num2],
						1.0f-blue_col, 1.0f-blue_col, blue_col, 1.0f);	//画像で使う座標を設定
			}
		}
		gl.glPopMatrix();
	}

	/**
	 * カーソル描画2
	 * @param gl
	 * @param texture
	 */
	public void draw_csl(GL10 gl, int texture){

		float Angle = (Camera_var.angle - 180);		//向いている方向
		gl.glPushMatrix();
		{
			gl.glTranslatef(mX-0.001f, mY+0.001f, 0.01f);
			gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
			gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
			gl.glScalef(0.25f, 0.25f, 0.25f);	//画像の大きさ指定


			//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
			GraphicUtil.drawTexture_pixel_custom2(gl, 128.0f, 64.0f,
					texture,
					 new float[] { 32.0f * rect_num3,   32.0f * Math.abs(blue_col-1.0f), 32.0f, 32.0f, 32.0f / 2, 32.0f },
					1.0f, 1.0f, 1.0f, 1.0f);	//画像で使う座標を設定
		}
		gl.glPopMatrix();
	}



	//障害物を描画します
	public void draw_snag(GL10 gl, int texture, int flg) {

		float Angle = (Camera_var.angle - 180);		//向いている方向
		gl.glPushMatrix();
		{
			gl.glTranslatef(mX, mY, 0.01f);
			gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
			if(flg == 0)
				gl.glScalef(0.25f, 0.25f, 0.25f);	//画像の大きさ指定
			else
				gl.glScalef(0.25f, 0.25f, 0.50f);	//画像の大きさ指定

			gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);

			//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
			GraphicUtil.drawTexture(gl, 0.0f, 0.0f, 1.0f, 1.0f,
					texture,
					0.0f, 0.0f, 1.0f, 1.0f,
					true,
					1.0f, 1.0f, 1.0f, 1.0f);	//画像で使う座標を設定
		}
		gl.glPopMatrix();
	}


	/***
	 * 柵の表示
	 * @param gl
	 * @param texture
	 * @param num
	 */
	public void draw_sak(GL10 gl, int texture, int num){
		float Angle = 0;		//向いている方向
		gl.glPushMatrix();
		{
			//count++;
			//Angle = (float)count;
			switch(num){

				case 0:		//右
					Angle = 90;		//Angle -45.0f;
					if(Camera_var.angle  % 360 <= 180 )
					{
						Angle -= 180;
					}
					gl.glTranslatef(mX - 0.125f, mY, 0.01f);
					rect_sak = 4;
					break;

				case 1:		//上
					Angle = 360;	//Angle +45.0f;
					if(Camera_var.angle  % 360 >= 270 || Camera_var.angle  % 360 <= 90 )
					{
						Angle -= 180;
					}
					gl.glTranslatef(mX, mY - 0.125f+0.01f, 0.01f);
					rect_sak = 0;
					break;

				case 2:		//下
					Angle = 360;	//Angle +45.0f;
					if(Camera_var.angle  % 360 >= 270 || Camera_var.angle  % 360 <= 90 )
					{
						Angle -= 180;
					}
					gl.glTranslatef(mX, mY+ 0.125f, 0.01f);
					rect_sak = 4;
					break;

				case 3:		//左
					Angle = 90;		//Angle -45.0f;
					if(Camera_var.angle  % 360 <= 180 )
					{
						Angle -= 180;
					}
					gl.glTranslatef(mX + 0.125f - 0.01f, mY, 0.01f);
					rect_sak = 0;
					break;

			}
			gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
			//gl.glScalef(0.25f, 0.25f, 0.25f);	//画像の大きさ指定
			gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
			float rect2[][] = {
					{  0.0f,  0.0f, 32.0f, 64.0f, 16.0f, 64.0f},		//0
					{ 32.0f,  0.0f, 32.0f, 64.0f, 16.0f, 64.0f},		//1
					{ 64.0f,  0.0f, 32.0f, 64.0f, 16.0f, 64.0f},		//2
					{ 96.0f,  0.0f, 32.0f, 64.0f, 16.0f, 64.0f},		//3

					{  0.0f, 64.0f, 32.0f, 64.0f, 16.0f, 64.0f},		//4
					{ 32.0f, 64.0f, 32.0f, 64.0f, 16.0f, 64.0f},		//5
					{ 64.0f, 64.0f, 32.0f, 64.0f, 16.0f, 64.0f},		//6
					{ 96.0f, 64.0f, 32.0f, 64.0f, 16.0f, 64.0f},		//7
				  };
			//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
			GraphicUtil.drawTexture_pixel_custom3(gl, 128.0f, 128.0f, texture,
								rect2[rect_sak],
								1.0f, 1.0f, 1.0f, 1.0f);	//画像で使う座標を設定
		}
		gl.glPopMatrix();
	}



//---------------------------------------------------------------------------------
	/**
	 *	攻撃範囲を動かす
	 * @param x
	 * @param y
	 */
	public void attac_move(float x, float y)
	{
		this.mX = (float)(x * 0.25f);	//現在位置を持ってくる
		this.mY = (float)(y * 0.25f);
		if(attack_tenmetu < 0)		//透明度が0
			attack_tenmetu_flg = false;

		if(attack_tenmetu > 1.0f)		//透明度がマックス
			attack_tenmetu_flg = true;

		if(attack_tenmetu_flg)		//点滅状態を見て判断
			attack_tenmetu -= 0.1f;
		else
			attack_tenmetu += 0.1f;
	}



	/***
	 * 攻撃範囲の表示
	 */
	public void attack_Area_drow(GL10 gl, int texture)
	{
		gl.glPushMatrix();
		{
			gl.glTranslatef(mX, mY, 0.002f);		//座標位置
			gl.glRotatef(0.0f, 0.0f, 0.0f, 1.0f);	//回転
			//gl.glScalef(0.25f, 0.25f, 1.0f);	//画像の大きさ指定
			GraphicUtil.drawTexture_pixel_custom3(gl, 32.0f, 32.0f, texture,
					new float[] { 0.0f,  0.0f, 32.0f, 32.0f, 16.0f, 16.0f},
					1.0f, 1.0f, 1.0f, attack_tenmetu);	//画像で使う座標を設定
		}
		gl.glPopMatrix();
	}



	/***
	 * 攻撃範囲の表示
	 */
	public void attack_Area_drow2(GL10 gl, int texture)
	{
		gl.glPushMatrix();
		{
			gl.glTranslatef(mX, mY, 0.001f);		//座標位置
			gl.glRotatef(0.0f, 0.0f, 0.0f, 1.0f);	//回転
			gl.glScalef(0.25f, 0.25f, 1.0f);	//画像の大きさ指定
			GraphicUtil.drawTexture_pixel_custom2(gl, 32.0f, 32.0f, texture,
					new float[] { 0.0f,  0.0f, 32.0f, 32.0f, 16.0f, 16.0f},
					1.0f, 1.0f, 1.0f, attack_tenmetu);	//画像で使う座標を設定
		}
		gl.glPopMatrix();
	}
//---------------------------------------------------------------------------------
}