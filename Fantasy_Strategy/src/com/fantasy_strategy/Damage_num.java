package com.fantasy_strategy;

import javax.microedition.khronos.opengles.GL10;


public class Damage_num
{
	public  float mX, mY, mZ;	//標的の位置
	public boolean disp_flg = false;
	private float alpha = 1.0f;
	private int num;

	Damage_num(){
		disp_flg = false;
		mX = 0;
		mY = 0;
		mZ = 0.01f;
		alpha = 1.0f;
		num = 0;
	}

	public void reset(int num, float x, float y){
		disp_flg = true;
		this.num = num;
		mX = x;
		mY = y;
		mZ = 0.4f;
		alpha = 1.0f;
	}


	public void move() {
		alpha -= 0.02f;
		mZ	  += 0.01f;
		if(alpha < 0){
			disp_flg = false;
		}
	}


	public void draw(GL10 gl){

		float Angle = (Camera_var.angle - 180);		//向いている方向

		FontTexture.text_fTexture.draw_num5(gl, GameMain.strnum_Texture, 1.0f,
				mX -0.01f, mY +0.01f, mZ, -Angle,
				num, GameInfo.num_str_param[2],
				1.0f, 0.0f, 0.0f, alpha);

	}
}