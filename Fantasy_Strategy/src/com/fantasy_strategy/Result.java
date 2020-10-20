package com.fantasy_strategy;

import javax.microedition.khronos.opengles.GL10;



public class Result {
	public float Angle = 0;		//向いている方向   Angle は、０で右、９０で上、１８０で左、２７０で下

	public  float tX, tY;	//テクスチャのの座標

	public boolean touch_flg = false;
	public boolean touch_ovj_flg = false;
	private float thouch_num = -1;

	private ResultActivity resultscreen = new ResultActivity();


	public Result()
	{
		//

	}

	public Result(float x, float y, float radius )	//衝突判定の情報を入れる
	{
		float[] tColision = new float[6];

		this.touch_ovj_flg = true;

		tColision[0] = x;
		tColision[1] = y;
		tColision[2] = radius;		//Width
		tColision[3] = radius;		//height
		tColision[4] = (float)TouchManagement.list.size();		//ボタンごとの番号
		tColision[5] = 0;			//タッチアクションフラグ(ACTION_DOWN)
		this.thouch_num = tColision[4];

		TouchManagement.list.add(tColision);		//衝突判定の情報を格納した配列をリストとして格納する

	}


	//アニメーションの処理
	public static void animation(GL10 gl,float angle)
	{
		//gl.glTranslatef(0.0f, 0.0f, 0.0f);		//座標位置
		gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);	//回転
		//this.mTurnAngle += 4.0f;
	}


	//描画処理を行います(表示させるだけ)
	public void draw(GL10 gl, int x, int y, int texture, float tex_h, float tex_w, float rect[])
	{
/*
		gl.glPushMatrix();
		{
			GraphicUtil.glTranslatef_pixel(gl, x, y, 0.0f);		//座標位置(ピクセル指定)
			gl.glRotatef(0.0f, 0.0f, 0.0f, 1.0f);	//回転
			//gl.glScalef(0.25f, 0.25f, 1.0f);

			GraphicUtil.drawTexture_pixel(gl, tex_h, tex_w,
										texture, rect , 0.5f, 0.5f,
										1.0f, 1.0f, 1.0f, 1.0f);	//画像で使う座標を設定
		}
		gl.glPopMatrix();
*/
	}


	//アニメーション描画処理を行います（回転させる）
	public void draw_Rotate(GL10 gl, int x, int y, int texture, float tex_h, float tex_w, float aninm_rect[], float angle)
	{/*9
		Angle = Camera_var.angle - 180;

		gl.glPushMatrix();
		{
			GraphicUtil.glTranslatef_pixel(gl, x, y, 0.0f);		//座標位置(ピクセル指定)
			gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);	//回転
			//gl.glScalef(0.25f, 0.25f, 1.0f);

			GraphicUtil.drawTexture_pixel_custom(gl, tex_h, tex_w,
										texture, aninm_rect ,
										1.0f, 1.0f, 1.0f, 1.0f);	//画像で使う座標を設定
		}
		gl.glPopMatrix();*/
	}


	//アニメーション描画処理を行います（色を指定して描画させる）
	public void draw_Color_Custom(GL10 gl, float x, float y, int texture, float tex_h, float tex_w, float rect[], float r, float g, float b, float a)
	{
		if(this.touch_ovj_flg)
		{
			this.touch_flg = TouchManagement.touch_chk(this.thouch_num);
		}




		gl.glPushMatrix();
		{
			GraphicUtil.glTranslatef_pixel(gl, x, y, 0.0f);		//座標位置(ピクセル指定)
			//gl.glRotatef(0.0f, 0.0f, 0.0f, 1.0f);	//回転
			//gl.glScalef(0.25f, 0.25f, 1.0f);

			GraphicUtil.drawTexture_pixel_custom(gl, tex_h, tex_w,
										texture, rect ,
										r, g, b, a);	//画像で使う座標を設定
		}
		gl.glPopMatrix();

	
	}
}
