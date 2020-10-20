package com.fantasy_strategy;

import javax.microedition.khronos.opengles.GL10;

public class Stage_button
{
	public float Angle = 0;		//向いている方向   Angle は、０で右、９０で上、１８０で左、２７０で下

	public  float tX, tY;	//テクスチャのの座標

	public boolean touch_flg = false;
	public boolean touch_ovj_flg = false;

	//private TitleActivity titlescreen = new TitleActivity();

/**
 *
 * @param x
 * @param y
 * @param radius_width
 * @param radius_height
 */
	public Stage_button(float x, float y, float radius_width, float radius_height)
	{
																//衝突判定の情報を入れる
		this.touch_ovj_flg = true;
		float[] tColision = new float[6];
		tColision[0] = x;
		tColision[1] = y;
		tColision[2] = radius_width;		//Width
		tColision[3] = radius_height;		//height
		tColision[4] = (float)TouchManagement.list.size();		//ボタンごとの番号
		tColision[5] = 0;			//タッチアクションフラグ(ACTION_DOWN)
		TouchManagement.list.add(tColision);		//衝突判定の情報を格納した配列をリストとして格納する

	}


	//描画処理を行います(表示させるだけ)
	public void draw(GL10 gl, int x, int y, int texture, float tex_h, float tex_w, float rect[])
	{

		gl.glPushMatrix();
		{
			GraphicUtil.glTranslatef_pixel(gl, x, y, 0.0f);		//座標位置(ピクセル指定)

			GraphicUtil.drawTexture_pixel_custom(gl, tex_h, tex_w,
										texture, rect ,
										1.0f, 1.0f, 1.0f, 1.0f);	//画像で使う座標を設定
		}
		gl.glPopMatrix();

	}
	public void draw_shadow(GL10 gl, int x, int y, int texture, float tex_h, float tex_w, float rect[])
	{
		gl.glPushMatrix();
		{
			GraphicUtil.glTranslatef_pixel(gl, x+15.0f, y+15.0f, 0.0f);		//座標位置(ピクセル指定)

			GraphicUtil.drawTexture_pixel_custom(gl, tex_h, tex_w,
										texture, rect ,
										0.0f, 0.0f, 0.0f, 0.5f);	//画像で使う座標を設定
		}
		gl.glPopMatrix();
	}


}

