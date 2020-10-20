package com.fantasy_strategy;

import javax.microedition.khronos.opengles.GL10;

public class Map_tip
{
	public  float mX, mY;	//標的の位置
	public  int map_rect_num;		//矩形番号
	public  float mTurnAngle;	//標的の旋回角度

	// 画像の大きさ定数定義
	public static float size_x = 0.25f;
	public static float size_y = 0.25f;
	public float size_z = 1.0f;

	public Map_tip(float x, float y,int rectnumber)
	{
		this.mX = x;
		this.mY = y;
		this.map_rect_num = rectnumber;
	}


	//標的を移動させます
	public void move() {

		//this.mTurnAngle += 4.0f;
	}



	//標的を描画します
	public void draw(GL10 gl, int texture) {
		gl.glPushMatrix();
		{
			gl.glTranslatef(mX, mY, 0.0f);		//座標位置
			gl.glRotatef(0.0f, 0.0f, 0.0f, 1.0f);	//回転
			gl.glScalef(0.25f, 0.25f, 1.0f);	//画像の大きさ指定
			//gl.glScalef(0.9f, 1.6f, 1.0f);	//画像の大きさ指定

			//GraphicUtil.drawTexture(gl, 0.0f, 0.0f, 1.0f, 1.0f, texture, 1.0f, 1.0f, 1.0f, 1.0f);	//画像で使う座標を設定

			GraphicUtil.drawTexture(gl, 0.0f, 0.0f, 1.0f, 1.0f, texture, 0.5f * (float)(map_rect_num % 2), 0.5f * (float)(map_rect_num / 2), 0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 1.0f);	//画像で使う座標を設定
			//GraphicUtil.drawRectangle(gl, 0.0f, 0.0f, 1.0f, 1.0f, texture, 0.5f * (float)(map_rect_num % 2), 0.5f * (float)(map_rect_num / 2), 0.5f);	//画像で使う座標を設定
		}
		gl.glPopMatrix();
	}
}