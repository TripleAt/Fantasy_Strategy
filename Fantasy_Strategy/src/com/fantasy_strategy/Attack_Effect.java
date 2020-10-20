package com.fantasy_strategy;


import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class Attack_Effect{

	//-----------------------------------------------------------
	//				変数宣言
	//-----------------------------------------------------------
	public float mX,mY;
	private float vecX,vecY;
	private int timer;
	private float fade = 1.0f;

	private float rand_ang = 0;
	private float ang = 0;
	private float size = 0.06f;
	private int rand_size = 0;
	public boolean Active_flg = false;


	/**
	 * 	１つエフェクトを発生させる
	 * @param x	発生位置x
	 * @param y	発生位置y
	 * @param vec_x 移動スピードx
	 * @param vec_y 移動スピードy
	 * @param time 移動時間
	 */
	public Attack_Effect(float x, float y, float vec_x, float  vec_y, int time){
        Random rnd = new Random();
		this.mX = x;
		this.mY = y;
		this.vecX = vec_x+(rnd.nextInt(3) / 100.0f)-0.015f;		//あとで考える
		this.vecY = vec_y+(rnd.nextInt(3) / 100.0f)-0.015f;
		this.timer = time;
		this.Active_flg = false;
		this.rand_ang = rnd.nextInt(20)+10;
		rand_size = rnd.nextInt(20);
		this.size = 0.04f + (float)(rand_size / 600.0f);

	}

	public void attac_efe(float x, float y, float vec_x, float  vec_y, int time)
	{
		Random rnd = new Random();
		this.mX = x;
		this.mY = y;
		this.vecX = vec_x+(rnd.nextInt(3) / 100.0f)-0.015f;		//あとで考える
		this.vecY = vec_y+(rnd.nextInt(3) / 100.0f)-0.015f;
		this.timer = time;
		this.Active_flg = true;
		this.rand_ang = rnd.nextInt(20)+10;
		rand_size = rnd.nextInt(20);
		this.size = 0.04f + (float)(rand_size / 600.0f);
		this.fade = 1.0f;
	}

	public void move()
	{
		if(this.timer <= 0){
			this.fade -= 0.2f;

			if(this.fade < 0){
				Active_flg = false;
			}
		}
		this.mX -= this.vecX;
		this.mY -= this.vecY;
		ang -= rand_ang;
		this.timer--;
	}

	public void draw(GL10 gl, int texture){
		float Angle = (Camera_var.angle - 180);		//向いている方向

		gl.glPushMatrix();
		{
			gl.glTranslatef(mX, mY, 0.4f);
			gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
			gl.glRotatef(ang, 0, 1.0f, 0.0f);
			gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
			gl.glScalef(size, size, 1.0f);

			//textureの後ろがUV座標（開始横、縦、サイズ横、縦）
			GraphicUtil.drawTexture_pixel_custom2(gl, 64.0f, 64.0f, texture,
					new float[] {0.0f,0.0f,64.0f,64.0f,64.0f / 2, 64.0f / 2},
					1.0f, 1.0f, 1.0f, this.fade);
		}
		gl.glPopMatrix();

/*
		Attack_Effect[] particles = GameMain.attack_efect;
		//頂点の配列
		//1つのパーティクルあたり6頂点×2要素
		float[] vertices = new float[6 * 2 * GameMain.PARTICLE＿NUM];

		//色の配列
		//1つのパーティクルあたりの6頂点×4要素(r,g,b,a)×最大のパーティクル数
		float[] colors = new float[6 * 4 * GameMain.PARTICLE＿NUM];

		//テクスチャマッピングの配列
		//1つのパーティクルあたり6頂点×2要素(x,y)×最大のパーティクル数
		float[] coords = new float[6 * 2 * GameMain.PARTICLE＿NUM];

		//アクティブなパーティクルのカウント
		int vertexIndex = 0;
		int colorIndex = 0;
		int texCoordIndex = 0;

		int activeParticleCount = 0;

		for (int i = 0; i < GameMain.PARTICLE＿NUM; i++) {
			// 　状態がアクティブのパーティクルのみ描画します
			if (particles[i].Active_flg) {
				//float[] rect = new float[] {0.0f,0.0f,64.0f,64.0f,64.0f / 2, 64.0f / 2};
				//
				float centerX = particles[i].mX;
				float centerY = particles[i].mY;
				float size = particles[i].size;
				float vLeft = -0.5f * size + centerX;
				float vRight = 0.5f * size + centerX;
				float vTop = 0.5f * size + centerY;
				float vBottom = -0.5f* size + centerY;

				//ポリゴン1
				vertices[vertexIndex++] = vLeft;
				vertices[vertexIndex++] = vTop;
				vertices[vertexIndex++] = vRight;
				vertices[vertexIndex++] = vTop;
				vertices[vertexIndex++] = vLeft;
				vertices[vertexIndex++] = vBottom;

				//ポリゴン2
				vertices[vertexIndex++] = vRight;
				vertices[vertexIndex++] = vTop;
				vertices[vertexIndex++] = vLeft;
				vertices[vertexIndex++] = vBottom;
				vertices[vertexIndex++] = vRight;
				vertices[vertexIndex++] = vBottom;


				for (int j = 0; j < 6; j++) {
					colors[colorIndex++] = 1.0f;
					colors[colorIndex++] = 1.0f;
					colors[colorIndex++] = 1.0f;
					colors[colorIndex++] = particles[i].fade;
				}

				//マッピング座標
				//ポリゴン1
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 1.0f;
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 1.0f;
				//ポリゴン2
				coords[texCoordIndex++] = 1.0f;
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 1.0f;
				coords[texCoordIndex++] = 1.0f;
				coords[texCoordIndex++] = 1.0f;

				//アクティブパーティクルの数を数えます
				activeParticleCount++;
			}
		}
		//gl.glTranslatef(mX, mY, 0.4f);
		gl.glRotatef(-Angle, 0.0f, 0.0f, 1.0f);
		//gl.glRotatef(ang, 0, 1.0f, 0.0f);
		gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		//gl.glScalef(size, size, 1.0f);

		FloatBuffer verticesBuffer = GraphicUtil.makeFloatBuffer(vertices);
		FloatBuffer colorBuffer = GraphicUtil.makeFloatBuffer(colors);
		FloatBuffer coordBuffer = GraphicUtil.makeFloatBuffer(coords);



		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, verticesBuffer);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coordBuffer);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, activeParticleCount * 6);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		Log.d("","デバッグ");
		*/
	}

}