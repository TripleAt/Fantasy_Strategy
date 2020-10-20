package com.fantasy_strategy;

import javax.microedition.khronos.opengles.GL10;


public class Die_efe_enemy
{
	public  float mX, mY,die_pos_x,die_pos_y, alpha;	//標的の位置
	public  int cant = 0,die_vec = 1;	//
	public boolean active_flg = false;

	public Die_efe_enemy(){
		this.mX = 0;
		this.mY = 0;
		this.active_flg = false;
		this.cant = 0;
		this.die_vec =  1;
		this.die_pos_x = 0;
		this.die_pos_y = 0;
		this.alpha = 1;

	}
	public void Die_efe_reset(float x, float y){
		this.mX = x;
		this.mY = y;
		this.active_flg = true;
		this.cant = 0;
		this.die_vec =  1;
		this.die_pos_x = 0;
		this.die_pos_y = 0;
		this.alpha = 1;
	}


	public void move() {
		die_pos_x += 1 * die_vec;
		if(die_pos_x > 10 || die_pos_x < 0){
			die_vec *= -1;
		}
		die_pos_y ++;
		cant += 1;
		alpha -= 0.1;
		if(alpha < 0){
			active_flg = false;
		}

	}

	//マップチップを描画します
	public void draw(GL10 gl, int texture) {

		gl.glPushMatrix();
		{
			gl.glTranslatef(mX, mY, 0.01f);
			gl.glRotatef(-(Camera_var.angle - 180), 0.0f, 0.0f, 1.0f);
			gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
			gl.glScalef(0.25f, 0.25f, 1.0f);
			//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
			GraphicUtil.drawTexture_pixel_custom2(gl, 32.0f, 32.0f, texture,
					 new float[] { 0.0f,   0.0f, 32.0f, 32.0f, 32.0f / 2 + die_pos_x, 32.0f + die_pos_y },
					1.0f, 1.0f, 1.0f, alpha);
		}
		gl.glPopMatrix();
	}


}