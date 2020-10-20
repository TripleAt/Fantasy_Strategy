package com.fantasy_strategy;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Hashtable;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.opengl.GLUtils;
import android.util.Log;


public class GraphicUtil {

	// 配列オブジェクトを保持する
	private static Hashtable<Integer, float[]> verticesPool = new Hashtable<Integer, float[]>();
	private static Hashtable<Integer, float[]> colorsPool = new Hashtable<Integer, float[]>();
	private static Hashtable<Integer, float[]> coordsPool = new Hashtable<Integer, float[]>();
 	static Paint paint = new Paint();
    static FontMetrics font = paint.getFontMetrics(); // フォントメトリクス


	private static final BitmapFactory.Options options = new BitmapFactory.Options();
	static {
		options.inScaled = false;//リソースの自動リサイズをしない
		options.inPreferredConfig = Config.ARGB_8888;//32bit画像として読み込む
	}

	public static float[] getVertices(int n) {
		if (verticesPool.containsKey(n)) {
			return verticesPool.get(n);
		}
		float[] vertices = new float[n];
		verticesPool.put(n, vertices);
		return vertices;
	}

	public static float[] getColors(int n) {
		if (colorsPool.containsKey(n)) {
			return colorsPool.get(n);
		}
		float[] colors = new float[n];
		colorsPool.put(n, colors);
		return colors;
	}

	public static float[] getCoords(int n) {
		if (coordsPool.containsKey(n)) {
			return coordsPool.get(n);
		}
		float[] coords = new float[n];
		coordsPool.put(n, coords);
		return coords;
	}

	// バッファオブジェクトを保持する
	private static Hashtable<Integer, FloatBuffer> polygonVerticesPool = new Hashtable<Integer, FloatBuffer>();
	private static Hashtable<Integer, FloatBuffer> polygonColorsPool = new Hashtable<Integer, FloatBuffer>();
	private static Hashtable<Integer, FloatBuffer> texCoordsPool = new Hashtable<Integer, FloatBuffer>();

	public static final FloatBuffer makeVerticesBuffer(float[] arr) {
		FloatBuffer fb = null;
		if (polygonVerticesPool.containsKey(arr.length)) {
			fb = polygonVerticesPool.get(arr.length);
			fb.clear();
			fb.put(arr);
			fb.position(0);
			return fb;
		}
		fb = makeFloatBuffer(arr);
		polygonVerticesPool.put(arr.length, fb);
		return fb;
	}

	public static final FloatBuffer makeColorsBuffer(float[] arr) {
		FloatBuffer fb = null;
		if (polygonColorsPool.containsKey(arr.length)) {
			fb = polygonColorsPool.get(arr.length);
			fb.clear();
			fb.put(arr);
			fb.position(0);
			return fb;
		}
		fb = makeFloatBuffer(arr);
		polygonColorsPool.put(arr.length, fb);
		return fb;
	}

	public static final FloatBuffer makeTexCoordsBuffer(float[] arr) {
		FloatBuffer fb = null;
		if (texCoordsPool.containsKey(arr.length)) {
			fb = texCoordsPool.get(arr.length);
			fb.clear();
			fb.put(arr);
			fb.position(0);
			return fb;
		}
		fb = makeFloatBuffer(arr);
		texCoordsPool.put(arr.length, fb);
		return fb;
	}





	//--------------------------------------------------------------------------------------------
	//			画像の座標指定処理（ピクセル指定）
	//--------------------------------------------------------------------------------------------
	public static final void glTranslatef_pixel(GL10 gl, float tex_x, float tex_y, float tex_z)
	{
		gl.glTranslatef(-0.9f + tex_x / (float) 480 * 1.8f,
						1.6f  - tex_y / (float) 800 * 3.2f,
						tex_z);



	}




	//--------------------------------------------------------------------------------
	//			ナンバーの表示
	//--------------------------------------------------------------------------------
	public static final void drawNumbers(GL10 gl, float x, float y, float width, float height, int texture, int number, int figures, float r, float g, float b, float a) {
		float totalWidth = width * (float)figures;//n文字分の横幅
		float rightX = x + (totalWidth * 0.5f);//右端のx座標
		float fig1X = rightX - width * 0.5f;//一番右の桁の中心のx座標
		for (int i = 0; i < figures; i++) {
			float figNX = fig1X - (float)i * width;//n桁目の中心のx座標
			int numberToDraw = number % 10;
			number = number / 10;
			drawNumber(gl, figNX, y, width, height, texture, numberToDraw, 1.0f, 1.0f, 1.0f, 1.0f);
		}
	}

	public static final void drawNumber(GL10 gl, float x, float y, float w, float h, int texture, int number, float r, float g, float b, float a) {
		float u = (float)(number % 4) * 0.25f;
		float v = (float)(number / 4) * 0.25f;
		drawTexture(gl, x, y, w, h, texture, u, v, 0.25f, 0.25f, false, r, g, b, a);
	}



	/***
	 * 		ドローテクスチャを新バージョンテクスチャへ以降（消してもいいがめんどい）
	 * 		急遽変更したため,このような作りになりました。
	 *
	 * @param gl
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param texture
	 * @param u
	 * @param v
	 * @param tex_w
	 * @param tex_h
	 * @param poly
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public static final void drawTexture(GL10 gl, float x, float y, float width, float height, int texture, float u, float v, float tex_w, float tex_h, boolean poly, float r, float g, float b, float a) {
		if(poly)
		{
			drawTexture(gl, x, y, width, height, texture, u, v, tex_w, tex_h, 1, r, g, b, a);
		}else{
			drawTexture(gl, x, y, width, height, texture, u, v, tex_w, tex_h, 0, r, g, b, a);
		}
	}


	//--------------------------------------------------------------------------------------------
	//			画像の描画処理（ピクセル指定）
	//--------------------------------------------------------------------------------------------
	public static final void drawTexture_pixel(GL10 gl, float texture_width, float texture_height, int texture,float rect[], float tex_w, float tex_h, float r, float g, float b, float a)
	{
		/*
		 *
		 * rect の中身
		 * rect[0] 始点ｘ
		 * rect[1]　始点y
		 * rect[2]　幅ｘ
		 * rect[3] 幅ｙ
		 * */
		 float[] vertices = getVertices(8);
		 float left   = -((float) rect[2]  / (float) 480) * 1.8f / 2.0f;
		 float top    = -((float) rect[3] / (float) 800) * 3.2f / 2.0f;
		 float right  = left + ((float) rect[2]  / (float) 480) * 1.8f;
		 float bottom = top  + ((float) rect[3] / (float) 800) * 3.2f;

		vertices[0] = left ; 	vertices[1] = top;
		vertices[2] = right; 	vertices[3] = top;
		vertices[4] = left; 	vertices[5] = bottom;
		vertices[6] = right; 	vertices[7] = bottom;

		float[] colors = getColors(16);
		for (int i = 0; i < 16; i++) {
			colors[i++] = r;
			colors[i++] = g;
			colors[i++] = b;
			colors[i]   = a;
		}
		left   = ((float) rect[0] / (float) texture_width);
		top    = ((float) rect[1] / (float) texture_height);
		right  = left + ((float) rect[2]  / (float) texture_width);
		bottom = top  + ((float) rect[3] / (float) texture_height);


		float[] coords = getCoords(8);
		coords[0] = left; coords[1] = bottom;
		coords[2] = right; coords[3] = bottom;
		coords[4] = left; coords[5] = top;
		coords[6] = right; coords[7] = top;

		FloatBuffer polygonVertices = makeVerticesBuffer(vertices);
		FloatBuffer polygonColors = makeColorsBuffer(colors);
		FloatBuffer texCoords = makeTexCoordsBuffer(coords);

		gl.glClearColor(0.3f, 0.3f, 1.0f, 0.0f);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, polygonVertices);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, polygonColors);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoords);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}




	/**
	 *
	 * 		中心座標を任意に指定できる画像の描画処理（ピクセル指定）
	* <p>
	* rect の中身<br>
	* rect[0] 始点ｘ<br>
	* rect[1] 始点y<br>
	* rect[2] 幅ｘ<br>
	* rect[3] 幅ｙ<br>
	* rect[4] ポリゴンの中心x座標<br>
	* rect[5] ポリゴンの中心y座標<br>
	* </p>
	 * @param gl
	 * @param texture_width 元絵の幅
	 * @param texture_height 元絵の高さ
	 * @param texture テクスチャ番号
	 * @param rect 矩形
	 * @param r		色指定レッド
	 * @param g		色指定グリーン
	 * @param b		色指定ブルー
	 * @param a		色指定アルファ（透明度）
	 */
	//
	//--------------------------------------------------------------------------------------------
	public static final void drawTexture_pixel_custom2(GL10 gl, float texture_width, float texture_height, int texture,float rect[], float r, float g, float b, float a)
	{
		 float[] vertices = getVertices(8);
		 float left   = -((float)rect[4] /  (float)rect[2]) * (rect[2] / 32.0f);
		 float bottom =  (float)((float)rect[5] /  (float)rect[3]) * (rect[3] / 32.0f);
		 float top    = bottom - (rect[3] / 32.0f);
		 float right  = left + (rect[2] / 32.0f);

		 //ポリゴンの生成
		vertices[0] = left ; 	vertices[1] = top;
		vertices[2] = right; 	vertices[3] = top;
		vertices[4] = left; 	vertices[5] = bottom;
		vertices[6] = right; 	vertices[7] = bottom;
		//Log.d("sa",""+right+bottom);

		float[] colors = getColors(16);
		//if
		for (int i = 0; i < 16; i++) {
			colors[i++] = r;
			colors[i++] = g;
			colors[i++] = b;
			colors[i]   = a;
		}
		left   = ((float) rect[0] / (float) texture_width);
		top    = ((float) rect[1] / (float) texture_height);
		right  = left + ((float) rect[2]  / (float) texture_width);
		bottom = top  + ((float) rect[3] / (float) texture_height);


		float[] coords = getCoords(8);	//uvの生成
		coords[0] = left; coords[1] = bottom;
		coords[2] = right; coords[3] = bottom;
		coords[4] = left; coords[5] = top;
		coords[6] = right; coords[7] = top;

/*
		coords[0] = u; coords[1] = v + tex_h;
		coords[2] = u + tex_w; coords[3] = v + tex_h;
		coords[4] = u; coords[5] = v;
		coords[6] = u + tex_w; coords[7] = v;
		*/

		FloatBuffer polygonVertices = makeVerticesBuffer(vertices);
		FloatBuffer polygonColors = makeColorsBuffer(colors);
		FloatBuffer texCoords = makeTexCoordsBuffer(coords);

		gl.glClearColor(0.3f, 0.3f, 1.0f, 0.0f);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, polygonVertices);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, polygonColors);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoords);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}


	/**
	 *
	 * 		中心座標を任意に指定できる画像の描画処理（ピクセル指定）
	* <p>
	* rect の中身<br>
	* rect[0] 始点ｘ<br>
	* rect[1] 始点y<br>
	* rect[2] 幅ｘ<br>
	* rect[3] 幅ｙ<br>
	* rect[4] ポリゴンの中心x座標<br>
	* rect[5] ポリゴンの中心y座標<br>
	* </p>
	 * @param gl
	 * @param texture_width 元絵の幅
	 * @param texture_height 元絵の高さ
	 * @param texture テクスチャ番号
	 * @param rect 矩形
	 * @param r		色指定レッド
	 * @param g		色指定グリーン
	 * @param b		色指定ブルー
	 * @param a		色指定アルファ（透明度）
	 */
	//
	//--------------------------------------------------------------------------------------------
	public static final void drawTexture_pixel_custom3(GL10 gl, float texture_width, float texture_height, int texture,float rect[], float r, float g, float b, float a)
	{
		 float[] vertices = getVertices(8);
		 float left   = -((float)rect[4] /  (float)rect[2]) * (rect[2] / 32.0f);
		 float bottom =  (float)((float)rect[5] /  (float)rect[3]) * (rect[3] / 32.0f);
		 float top    = bottom - (rect[3] / 32.0f);
		 float right  = left + (rect[2] / 32.0f);

		 //Log.d("a",""+left +"," + bottom + "," + top + "," + right);
		 //ポリゴンの生成
		vertices[0] = left / 4; 	vertices[1] = top / 4;
		vertices[2] = right / 4; 	vertices[3] = top / 4;
		vertices[4] = left / 4; 	vertices[5] = bottom / 4;
		vertices[6] = right / 4; 	vertices[7] = bottom / 4;
		//Log.d("sa",""+right+bottom);

		float[] colors = getColors(16);
		//if
		for (int i = 0; i < 16; i++) {
			colors[i++] = r;
			colors[i++] = g;
			colors[i++] = b;
			colors[i]   = a;
		}
		left   = ((float) rect[0] / (float) texture_width);
		top    = ((float) rect[1] / (float) texture_height);
		right  = left + ((float) rect[2]  / (float) texture_width);
		bottom = top  + ((float) rect[3] / (float) texture_height);


		float[] coords = getCoords(8);	//uvの生成
		coords[0] = left; coords[1] = bottom;
		coords[2] = right; coords[3] = bottom;
		coords[4] = left; coords[5] = top;
		coords[6] = right; coords[7] = top;

/*
		coords[0] = u; coords[1] = v + tex_h;
		coords[2] = u + tex_w; coords[3] = v + tex_h;
		coords[4] = u; coords[5] = v;
		coords[6] = u + tex_w; coords[7] = v;
		*/

		FloatBuffer polygonVertices = makeVerticesBuffer(vertices);
		FloatBuffer polygonColors = makeColorsBuffer(colors);
		FloatBuffer texCoords = makeTexCoordsBuffer(coords);

		gl.glClearColor(0.3f, 0.3f, 1.0f, 0.0f);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, polygonVertices);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, polygonColors);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoords);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}


	/***
	 * 中心座標を任意に指定できる画像の描画処理（ピクセル指定）
	 * @param gl
	 * @param texture_width
	 * @param texture_height
	 * @param texture
	 * @param rect
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public static final void drawTexture_pixel_custom(GL10 gl, float texture_width, float texture_height, int texture, float rect[], float r, float g, float b, float a)
	{
		/*
		 *
		 * rect の中身
		 * rect[0] 始点ｘ
		 * rect[1] 始点y
		 * rect[2] 幅ｘ
		 * rect[3] 幅ｙ
		 * rect[4] ポリゴンの中心x座標
		 * rect[5] ポリゴンの中心y座標
		 * */
		 float[] vertices = getVertices(8);
		 float left   = -(rect[4] / (float) 480) * 1.8f;
		 float bottom = (rect[5] / (float) 800) * 3.2f;
		 float top    = bottom - ((float) rect[3] / (float) 800) * 3.2f; /*/ 2.0f*/
		 float right  = left + ((float) rect[2]  / (float) 480) * 1.8f;

/*
		top    = -top;
		bottom = -bottom;
		*/
		vertices[0] = left ; 	vertices[1] = top;
		vertices[2] = right; 	vertices[3] = top;
		vertices[4] = left; 	vertices[5] = bottom;
		vertices[6] = right; 	vertices[7] = bottom;
		//Log.d("sa",""+right+bottom);

		float[] colors = getColors(16);
		for (int i = 0; i < 16; i++) {
			colors[i++] = r;
			colors[i++] = g;
			colors[i++] = b;
			colors[i]   = a;
		}
		left   = ((float) rect[0] / (float) texture_width);
		top    = ((float) rect[1] / (float) texture_height);
		right  = left + ((float) rect[2]  / (float) texture_width);
		bottom = top  + ((float) rect[3] / (float) texture_height);


		float[] coords = getCoords(8);
		coords[0] = left; coords[1] = bottom;
		coords[2] = right; coords[3] = bottom;
		coords[4] = left; coords[5] = top;
		coords[6] = right; coords[7] = top;

/*
		coords[0] = u; coords[1] = v + tex_h;
		coords[2] = u + tex_w; coords[3] = v + tex_h;
		coords[4] = u; coords[5] = v;
		coords[6] = u + tex_w; coords[7] = v;
		*/

		FloatBuffer polygonVertices = makeVerticesBuffer(vertices);
		FloatBuffer polygonColors = makeColorsBuffer(colors);
		FloatBuffer texCoords = makeTexCoordsBuffer(coords);

		gl.glClearColor(0.3f, 0.3f, 1.0f, 0.0f);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, polygonVertices);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, polygonColors);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoords);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}


	/***
	 *	画像の描画処理（ピクセル指定でない）
	 * @param gl
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param texture
	 * @param u
	 * @param v
	 * @param tex_w
	 * @param tex_h
	 * @param poly
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public static final void drawTexture(GL10 gl, float x, float y, float width, float height, int texture, float u, float v, float tex_w, float tex_h, int poly, float r, float g, float b, float a) {
		float[] vertices = getVertices(8);
		switch(poly)
		{
			case 0:
				vertices[0] = -0.5f * width + x; vertices[1] = -0.5f * height + y;
				vertices[2] =  0.5f * width + x; vertices[3] = -0.5f * height + y;
				vertices[4] = -0.5f * width + x; vertices[5] =  0.5f * height + y;
				vertices[6] =  0.5f * width + x; vertices[7] =  0.5f * height + y;
				break;

			case 1:
				vertices[0] = -0.5f * width + x; vertices[1] = -0.0f * height + y;
				vertices[2] =  0.5f * width + x; vertices[3] = -0.0f * height + y;
				vertices[4] = -0.5f * width + x; vertices[5] =  1.0f * height + y;
				vertices[6] =  0.5f * width + x; vertices[7] =  1.0f * height + y;
				break;

			case 2:
				vertices[0] = -0.0f * width + x; vertices[1] = -0.5f * height + y;
				vertices[2] =  1.0f * width + x; vertices[3] = -0.5f * height + y;
				vertices[4] = -0.0f * width + x; vertices[5] =  0.5f * height + y;
				vertices[6] =  1.0f * width + x; vertices[7] =  0.5f * height + y;
				break;

			default:
				Log.e("drawTexture","drawTextureのpolyの値の指定ミス！！！！！！！！！！！！！");
				break;
		}

		float[] colors = getColors(16);
		for (int i = 0; i < 16; i++) {
			colors[i++] = r;
			colors[i++] = g;
			colors[i++] = b;
			colors[i]   = a;
		}

		float[] coords = getCoords(8);
		coords[0] = u; 		   coords[1] = v + tex_h;
		coords[2] = u + tex_w; coords[3] = v + tex_h;
		coords[4] = u; 		   coords[5] = v;
		coords[6] = u + tex_w; coords[7] = v;


		FloatBuffer polygonVertices = makeVerticesBuffer(vertices);
		FloatBuffer polygonColors = makeColorsBuffer(colors);
		FloatBuffer texCoords = makeTexCoordsBuffer(coords);

		gl.glClearColor(0.3f, 0.3f, 1.0f, 0.0f);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, polygonVertices);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, polygonColors);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoords);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}


	/***
	 * もじの描画。
	 * @param gl
	 * @param font_num
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public static final void drawText(GL10 gl,int font_num, float r,float g, float b, float a) {

		drawTexture(gl, -0.0f, -0.0f, 1.0f, 1.0f, font_num, 0.0f, 0.0f, 1.0f, 1.0f, 2, r, g, b, a);
	}


	//-------------------------------------------------------------------------------
	//		テクスチャーの読み込み命令
	//-------------------------------------------------------------------------------
	public static final int loadTexture(GL10 gl, Resources resources, int resId) {
		int[] textures = new int[1];

		//Bitmapの作成
		Bitmap bmp = BitmapFactory.decodeResource(resources, resId, options);
		//ビットマップが生成されなかったら
		if (bmp == null) {
			return 0;
		}

		// OpenGL用のテクスチャを生成します
		{
			gl.glGenTextures(1, textures, 0);		//オブジェクトの生成の命令,いくつ生成するか、テクスチャごとの適当な番号,?
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);			//テクスチャを貼る(テクスチャのバインド)。,何次元のオブジェクトか,テクスチャの指定
			//  拡大、縮小フィルタ設定
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);		//　縮小するときピクセルの中心に最も近いテクスチャ要素で補完
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);		//　拡大するときピクセルの中心付近の線形で補完

			//gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);	//指定の番号のテクスチャオブジェクトをバインドします
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);			//テクスチャのマッピング？テクスチャの中身をつくる,何次元のオブジェクトか,?,ビットマップ,?
		}
		//OpenGLへの転送が完了したので、VMメモリ上に作成したBitmapを破棄する
		bmp.recycle();

		//TextureManagerに登録する
		TextureManager.addTexture(resId, textures[0]);

		return textures[0];
	}


	/***********************
	  * 文字列の幅の取得
	  * @param text
	  * @return 文字列の幅
	  ***********************/
	 public static int getWidth(String text) {
	  return (int) (paint.measureText(text) + 0.5f);
	 }

	public static final void drawCircle(GL10 gl, float x, float y, int divides, float radius, float r, float g, float b, float a) {
		float[] vertices = getVertices(divides * 3 * 2);

		int vertexId = 0;//頂点配列の要素の番号を記憶しておくための変数
		for (int i = 0; i < divides; i++) {
			//円周上のi番目の頂点の角度(ラジアン)を計算します
			float theta1 = 2.0f / (float)divides * (float)i * (float)Math.PI;
			//円周上の(i + 1)番目の頂点の角度(ラジアン)を計算します
			float theta2 = 2.0f / (float)divides * (float)(i+1) * (float)Math.PI;
			//i番目の三角形の0番目の頂点情報をセットします
			vertices[vertexId++] = x;
			vertices[vertexId++] = y;

			//i番目の三角形の1番目の頂点の情報をセットします (円周上のi番目の頂点)
			vertices[vertexId++] = (float)Math.cos((double)theta1) * radius + x;//x座標
			vertices[vertexId++] = (float)Math.sin((double)theta1) * radius + y;//y座標

			//i番目の三角形の2番目の頂点の情報をセットします (円周上のi+1番目の頂点)
			vertices[vertexId++] = (float)Math.cos((double)theta2) * radius + x;//x座標
			vertices[vertexId++] = (float)Math.sin((double)theta2) * radius + y;//y座標
		}
		FloatBuffer polygonVertices = makeVerticesBuffer(vertices);

		//ポリゴンの色を指定します
		gl.glColor4f(r, g, b, a);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, polygonVertices);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, divides * 3);
	}

	public static final void drawRectangle(GL10 gl, float x, float y, float width, float height, float r, float g, float b, float a) {
		float[] vertices = getVertices(8);
		vertices[0] = -0.5f * width + x; vertices[1] = -0.5f * height + y;
		vertices[2] =  0.5f * width + x; vertices[3] = -0.5f * height + y;
		vertices[4] = -0.5f * width + x; vertices[5] =  0.5f * height + y;
		vertices[6] =  0.5f * width + x; vertices[7] =  0.5f * height + y;

		float[] colors = getColors(16);
		for (int i = 8; i < 16; i++) {
			colors[i++] = r;
			colors[i++] = g;
			colors[i++] = b;
			colors[i]   = a;
		}
		for (int j = 0; j < 8; j++) {
			colors[j++] = 1.0f;
			colors[j++] = 1.0f;
			colors[j++] = 1.0f;
			colors[j]   = 1.0f;
		}

		FloatBuffer polygonVertices = makeVerticesBuffer(vertices);
		FloatBuffer polygonColors = makeColorsBuffer(colors);

		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, polygonVertices);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, polygonColors);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}

	public static final void drawSquare(GL10 gl, float x, float y, float r, float g, float b, float a) {
		drawRectangle(gl, x, y, 1.0f, 1.0f, r, g, b, a);
	}

	public static final void drawSquare(GL10 gl, float r, float g, float b, float a) {
		drawSquare(gl, 0.0f, 0.0f, r, g, b, a);
	}

	public static final void drawSquare(GL10 gl) {
		drawSquare(gl, 1.0f, 0.0f, 0.0f, 1.0f);
	}

	public static final FloatBuffer makeFloatBuffer(float[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);
		fb.position(0);
		return fb;
	}
}
