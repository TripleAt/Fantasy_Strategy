package com.fantasy_strategy;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.opengl.GLUtils;
import android.util.Log;


public class FontTexture {
	 private static final int FONT_MAX=256;
	 private static final int FONT_TEXTURE=3;

	 //文字のテクスチャのid：0		数字のテクスチャid：1
	 public class TextureStruct{
		 float width;
		 float height;
		 int id[] = new int[FONT_TEXTURE];
		 Bitmap image;
		 Bitmap image_num;
		 Bitmap image_message;
		 float line_fd;
	 };

	 int tex_resid[] = new int[FONT_TEXTURE];
	 //public int texture_id;

	 public static FontTexture text_fTexture = new FontTexture();

	 private TextureStruct m_texture;
	 private Canvas m_canvas, canvas_num, canvas_message;
	 private Paint m_paint;

	 //ゲームメイン画面用
	 public static String chr_par[] = {"名前","レベル","HP","攻撃力","防御力","魔法防御"};
	 public static String stage[] = {"草原", "六畳一間","ダンジョン","？？？","草原2", "六畳一間じゃない","ダンジョン2","？？？2"};
	 public static String str_message[] = {"ステージセレクトにもどりますか？"};
	 public static String chr_num[] = {"0","1","2","3","4","5","6","7","8","9",":","."};
	 static int text_length = chr_par.length;
	 static int txtnum_length = chr_num.length;
	 public static float num_h; 	//数字の1文字の高さ

	 //リザルト画面用
	 public static String result_mk = "×　　＝" ;
	 static int result_gameclear_length = 5;


	//{登録時の文字の幅,登録時の文字の高さ,オフセット,登録した文字分の1行の長さ}
	float status_str[][] = new float[6][4];


	//一枚の大きなテクスチャにフォントを書いていくのでその描画位置
	 private int m_pre_draw_offset[] = new int[FONT_TEXTURE];
	 private int m_pre_draw_write_cnt[] = new int[FONT_TEXTURE];


	 private int m_size_x[][]=new int[FONT_TEXTURE][FONT_MAX];
	 private int m_size_y[][]=new int[FONT_TEXTURE][FONT_MAX];

	 //改行する時の1行分のheghtを格納する
	 float line_fd[][] = new float[FONT_TEXTURE][FONT_MAX];


	 //読み込み位置
	 private int m_pre_draw_read_cnt[] = new int[FONT_TEXTURE];
	 private int m_pre_draw_read_offset[] = new int[FONT_TEXTURE];


	 //フォントサイズ情報
	 private int m_font_top_offset[] = new int[FONT_TEXTURE];
	 private int m_font_bottom_offset[] = new int[FONT_TEXTURE];

	 int i;


	 //メモリを確保する
	 public void createTextBuffer(GL10 gl, Context mContext)
	 {
		 //一枚の巨大なテクスチャを定義する
 		m_texture=new TextureStruct();
 		m_texture.width=1024;
 		m_texture.height=1024;
		m_texture.image = Bitmap.createBitmap((int)m_texture.width, (int)m_texture.height, Config.ARGB_8888);
		m_texture.image_num = Bitmap.createBitmap((int)m_texture.width, (int)m_texture.height, Config.ARGB_8888);
		m_texture.image_message = Bitmap.createBitmap((int)m_texture.width / 2, (int)m_texture.height / 2, Config.ARGB_8888);
		m_texture.id[0]=0;
		m_texture.id[1]=0;
		m_texture.id[2]=0;

		//ビットマップが生成されなかったら
		if (m_texture.image_num == null) {
			Log.d("数字文字テクスチャ生成失敗","ERROR");
		}
		if (m_texture.image == null) {
			Log.d("文字テクスチャ生成失敗","ERROR");
		}
		if (m_texture.image_message == null) {
			Log.d("メッセージ文字テクスチャ生成失敗","ERROR");
		}

		//BMPへの描画コンテキストを取得する
		m_canvas = new Canvas(m_texture.image);
		canvas_num = new Canvas(m_texture.image_num);
		canvas_message = new Canvas(m_texture.image_message);
		m_paint =new Paint();


		//フォント定義
	 	m_paint.setTextSize(32);
	 	m_paint.setColor(Color.WHITE);
	 	m_paint.setStyle(Style.FILL);
//	 	m_paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));
	 	Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "KTEGAKI.ttf");			//フォント指定
	 	m_paint.setTypeface(typeface);

	 	m_paint.setAntiAlias(true);

	 	//フォントサイズの取得
	 	FontMetrics fontMetrics = m_paint.getFontMetrics();
	 	m_font_top_offset[0] = (int)Math.ceil(0 + fontMetrics.top);	//ベースライン上ピクセル
	 	m_font_bottom_offset[0] = (int)Math.ceil(0 + fontMetrics.bottom);	//ベースライン下ピクセル
	 	m_font_top_offset[1] = (int)Math.ceil(0 + fontMetrics.top);	//ベースライン上ピクセル
	 	m_font_bottom_offset[1] = (int)Math.ceil(0 + fontMetrics.bottom);	//ベースライン下ピクセル
	 	m_font_top_offset[2] = (int)Math.ceil(0 + fontMetrics.top);	//ベースライン上ピクセル
	 	m_font_bottom_offset[2] = (int)Math.ceil(0 + fontMetrics.bottom);	//ベースライン下ピクセル

	 	//テクスチャを生成する
	 	alocTexture(gl);
	 }

	 private void alocTexture(GL10 gl)
	 {
		//テクスチャを生成する
	 	int textures[]=new int[1];
	 	int textures_num[]=new int[1];
	 	int textures_message[]=new int[1];
	 	gl.glGenTextures(1, textures, 0);
	 	gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
	 	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
	 	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	 	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
	 	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
	 	GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, m_texture.image, 0);
		m_texture.id[0]=textures[0];


	 	gl.glGenTextures(1, textures_num, 0);
	 	gl.glBindTexture(GL10.GL_TEXTURE_2D, textures_num[0]);
	 	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
	 	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	 	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
	 	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
	 	GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, m_texture.image_num, 0);
		m_texture.id[1]=textures_num[0];

		gl.glGenTextures(1, textures_message, 0);
	 	gl.glBindTexture(GL10.GL_TEXTURE_2D, textures_message[0]);
	 	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
	 	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	 	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
	 	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
	 	GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, m_texture.image_num, 0);
		m_texture.id[2]=textures_message[0];



		//OpenGLへの転送が完了したので、VMメモリ上に作成したBitmapを破棄する
		//m_texture.image.recycle();

		//TextManagerに登録する
		TextManager.addTexture(m_texture.id[0], textures[0]);
		TextManager.addTexture(m_texture.id[1], textures_num[0]);
		TextManager.addTexture(m_texture.id[2], textures_message[0]);

		tex_resid[0] = textures[0];
		tex_resid[1] = textures_num[0];
		tex_resid[2] = textures_message[0];

		//文字を書き込み必要な情報を初期化する
		preDrawBegin();

	 }


	 //テクスチャを復元する
	 public void onResume(GL10 gl)
	 {
		 TextureManager.deleteTexture(gl, tex_resid[0]);
		 TextureManager.deleteTexture(gl, tex_resid[1]);
		 TextureManager.deleteTexture(gl, tex_resid[2]);
		 alocTexture(gl);
	 }

	 //メモリを開放する
	 public void onDestroy(GL10 gl)
	 {
		 TextureManager.deleteTexture(gl, tex_resid[0]);
		 TextureManager.deleteTexture(gl, tex_resid[1]);
		 TextureManager.deleteTexture(gl, tex_resid[2]);
	 }


	 //1フレームで必要な文字を全て先行して描画しておく
	 public void preDrawBegin()
	 {
		//書き込み位置初期化
	 	m_pre_draw_offset[0]=0;
	 	m_pre_draw_write_cnt[0]=0;

	 	m_pre_draw_offset[1]=0;
	 	m_pre_draw_write_cnt[1]=0;

	 	m_pre_draw_offset[2]=0;
	 	m_pre_draw_write_cnt[2]=0;


	 	//読み込み位置初期化
	 	m_pre_draw_read_cnt[0]=0;
	 	m_pre_draw_read_offset[0]=0;

	 	m_pre_draw_read_cnt[1]=0;
	 	m_pre_draw_read_offset[1]=0;

	 	m_pre_draw_read_cnt[2]=0;
	 	m_pre_draw_read_offset[2]=0;
	 }



	 /*
	  * 	sxはpxで指定
	  */
	 public void drawStringToTexture(GL10 gl,String text,int sx, int tex_id)
	 {
	 	if(m_pre_draw_write_cnt[tex_id]>=FONT_MAX)
	 		return;
	 	int y=m_pre_draw_offset[tex_id];

	 	//今回描画したフォントの累計幅と累計高さ
	 	int height=0;
	 	int width=0;

	 	//横幅sxで折り返してBMPに描画
	 	int line_end_index = 1;
	 	int string_draw_index = 0;

	 	while(line_end_index!=0){
	 		String mesureString=text.substring(string_draw_index);
	 		line_end_index=m_paint.breakText(mesureString, true, (float)sx, null);	//折り返しは自分でやる必要 指定したpx幅ずつ改行する
	 		if(line_end_index!=0){
	 			String line = text.substring(string_draw_index, string_draw_index + line_end_index);

	 			//drawTextはベースライン指定
	 			//トップラインから天井の位置を計算して描画する
	 			int line_height=(-m_font_top_offset[tex_id]+m_font_bottom_offset[tex_id]);
	 			int from_y=y+height-m_font_top_offset[tex_id];

	 			//描画先がオーバフローする場合は描画しない
	 			if(y+line_height>=m_texture.height){
	 				Log.e("drawError!!","描画する範囲を超えました");
	 				return;
	 			}

	 			//描画先
	 			switch(tex_id){
	 		 		case 0:
	 		 			m_canvas.drawText(line, 0, from_y, m_paint);
	 		 			break;

	 		 		case 1:
	 		 			canvas_num.drawText(line, 0, from_y, m_paint);
	 		 			break;

	 		 		case 2:
	 		 			canvas_message.drawText(line, 0, from_y, m_paint);
	 		 			break;

	 		 		default:
	 		 			Log.e("ERROR","エラー");
	 			}


	 			//1ラインの幅を計算する
	 			int line_width=(int)m_paint.measureText(line);
	 			if(width<line_width){
	 				width=line_width;
	 			}

	 			//次の行へ
	 			height += line_height;
	 			string_draw_index += line_end_index;

	 			//1行分の高さを格納する
	 			line_fd[tex_id][m_pre_draw_write_cnt[tex_id]] = line_height;
	 		}
	 	}

	 	//今回描画したもののサイズを登録しておく
	 	m_size_x[tex_id][m_pre_draw_write_cnt[tex_id]]=width;
	 	m_size_y[tex_id][m_pre_draw_write_cnt[tex_id]]=height;
	 	m_pre_draw_offset[tex_id]+=height;
	 	m_pre_draw_write_cnt[tex_id]++;

		//テクスチャを更新する処理を行う
		preDrawEnd(gl,tex_id);

		/*//	canvasをクリアする
		m_canvas.drawColor(0, PorterDuff.Mode.CLEAR);*/
	 }



	 public void preDrawEnd(GL10 gl, int tex_id)
	 {
		 //テクスチャを更新する
		 int textures[]=new int[FONT_TEXTURE];
		 textures[tex_id]=m_texture.id[tex_id];
		 gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[tex_id]);
//		 long start = System.currentTimeMillis();

		 switch(tex_id){
		 	case 0:
		 		GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, 0, 0, m_texture.image);
		 		break;

		 	case 1:
		 		GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, 0, 0, m_texture.image_num);
		 		break;

		 	case 2:
		 		GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, 0, 0, m_texture.image_message);
		 		break;

		 	default:
		 		Log.e("TextureErrot!","テクスチャを更新できませんでした！");
		 }

//		 long end = System.currentTimeMillis();
//		 Log.i("",""+(end-start)+"ms");
	 }


	 //2パス目に順番に文字を取得してUV座標を変更しながら文字を描画する
	 public int getTexture(int tex_id){
	 	return m_texture.id[tex_id];
	 }

	 //
	 public float getWidth(int tex_id){
		if(m_pre_draw_read_cnt[tex_id]>=m_pre_draw_write_cnt[tex_id])
			return 1;
	 	return m_size_x[tex_id][m_pre_draw_read_cnt[tex_id]]/* / m_texture.width*/;
	 }

	 public float getHeight(int tex_id){
		if(m_pre_draw_read_cnt[tex_id]>=m_pre_draw_write_cnt[tex_id])
			return 1;
		 return m_size_y[tex_id][m_pre_draw_read_cnt[tex_id]] /*/ m_texture.height*/;
	 }

	 public float getOffset(int tex_id){
		if(m_pre_draw_read_cnt[tex_id]>=m_pre_draw_write_cnt[tex_id])
			return 0;
		 return m_pre_draw_read_offset[tex_id] /*/ m_texture.height*/;
	 }

	 public void nextReadPoint(int tex_id){
		m_pre_draw_read_offset[tex_id]+=m_size_y[tex_id][m_pre_draw_read_cnt[tex_id]];
	 	m_pre_draw_read_cnt[tex_id]++;
	 	if(m_pre_draw_read_cnt[tex_id]>=FONT_MAX)
			m_pre_draw_read_cnt[tex_id]=FONT_MAX-1;
	 }

	 //1行分の高さを返すメソッド
	 public float lineHeight(int tex_id){
		 if(m_pre_draw_read_cnt[tex_id]>=m_pre_draw_write_cnt[tex_id]){
			 return 1;
		 }
		 return line_fd[tex_id][m_pre_draw_read_cnt[tex_id]] /*/ m_texture.height*/;
	 }




	 /***
	  * 文字で数字の描画
	  * @param gl
	  * @param texture テクスチャ番号
	  * @param tex_size フォントサイズ
	  * @param tx		x座標
	  * @param ty		y座標
	  * @param rect
	  * @param r red
	  * @param g green
	  * @param b blue
	  * @param a alpha
	  */
	 public void draw_num(GL10 gl, int texture, float tex_size, int tx, int ty, float rect, float r, float g, float b, float a)
		{
			//draw_string_text(gl,tx, tx, str);
			i += 5;
			gl.glPushMatrix();
			{
				//gl.glTranslatef(0.0f, 0.0f, -0.1f);		//座標位置
				GraphicUtil.glTranslatef_pixel(gl, tx, ty, -0.1f);
				gl.glRotatef(0.0f, 0.0f, 0.0f, 1.0f);	//回転
				gl.glScalef(tex_size, tex_size, 1.0f);	//画像の大きさ指定

				GraphicUtil.drawTexture_pixel_custom(gl,m_texture.width,m_texture.height,texture,
									new float[]{0.0f,rect,/*18.0f*/num_h ,num_h/*42.0f*/,0.0f,0.0f},
									r, g, b, a);
				/*GraphicUtil.drawTexture(gl,0.0f,0.0f,1.0f,1.0f,texture,
										0.0f,offset,sx, sy,false, 1.0f,1.0f,1.0f, 1.0f);*/
			}
			gl.glPopMatrix();
		}

	 /**
	  * 数字の描画2
	  * @param gl
	  * @param texture
	  * @param tex_size テクスチャーの大きさの指定
	  * @param tx 横位置
	  * @param ty 縦位置
	  * @param num 実際の数
	  * @param rect num_str_paramを指定
	  * @param r 色
	  * @param g
	  * @param b
	  * @param a
	  */
	 public void draw_num2(GL10 gl, int texture, float tex_size, int tx, int ty, int num, float rect[], float r, float g, float b, float a)
	 {
		 int num2 = num;
		 int numx = tx;
		draw_num( gl,texture, tex_size, numx,  ty, rect[num2 % 10], r, g, b, a);
		if((num2 / 10) > 0){
			numx -= FontTexture.num_h * 0.8f * tex_size;
			draw_num( gl,texture, tex_size, numx,  ty, rect[(num2 / 10) % 10], r, g, b, a);
		}
		if((num2 / 100) > 0){

			numx -= FontTexture.num_h * 0.8f * tex_size;
			draw_num( gl,texture, tex_size, numx,  ty, rect[(num2 / 100) % 10], r, g, b, a);
		}
		//千以上は未対応
	 }



	/**
	 * 数字の描画3　数字の桁に合わせて表示
	 * @param gl
	 * @param texture
	 * @param tex_size
	 * @param tx
	 * @param ty
	 * @param num
	 * @param rect
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	 public void draw_num3(GL10 gl, int texture, float tex_size, int tx, int ty, int num, float rect[], float r, float g, float b, float a)
	 {
		 if(num < 0){		//初回起動時エラー回避
			 num = 0;
		 }

		int digits = 0;	//桁数
		int num2 = num;
		int numx = tx;

		do{
			digits++;
			num /= 10;
		}while(num > 0);

		for(int i = 0; i < digits; i++){
			draw_num( gl,texture, tex_size, numx,  ty, rect[num2 % 10], r, g, b, a);
			numx -= FontTexture.num_h * 0.8f * tex_size;
			num2 /= 10;
		}
	 }






	 /***
	  * 文字で数字の描画
	  * @param gl
	  * @param texture テクスチャ番号
	  * @param tex_size フォントサイズ
	  * @param tx		x座標
	  * @param ty		y座標
	  * @param rect
	  * @param r red
	  * @param g green
	  * @param b blue
	  * @param a alpha
	  */
	 public void draw_num4(GL10 gl, int texture, float tex_size, float tx, float ty,float tz, float ang, float rect, float r, float g, float b, float a)
		{
			//draw_string_text(gl,tx, tx, str);
			i += 5;
			gl.glPushMatrix();
			{
				//gl.glTranslatef(0.0f, 0.0f, -0.1f);		//座標位置
				gl.glTranslatef( tx, ty, tz);
				gl.glRotatef(ang, 0.0f, 0.0f, 1.0f);	//回転
				gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
				gl.glScalef(tex_size, tex_size, 1.0f);	//画像の大きさ指定

				GraphicUtil.drawTexture_pixel_custom(gl,m_texture.width,m_texture.height,texture,
									new float[]{0.0f,rect,num_h ,num_h/*42.0f*/,0.0f,0.0f},
									r, g, b, a);
			}
			gl.glPopMatrix();
		}




	 public void draw_num5(GL10 gl, int texture, float tex_size, float tx, float ty,float tz, float ang, int num, float rect[], float r, float g, float b, float a)
	 {
		 int num2 = num;
		 float numx = tx;
		 draw_num4( gl,texture, tex_size, numx,  ty, tz, ang, rect[num2 % 10], r, g, b, a);
		if((num2 / 10) > 0){
			numx += 0.06f;
			draw_num4( gl,texture, tex_size, numx,  ty, tz, ang, rect[(num2 / 10) % 10], r, g, b, a);
		}
		if((num2 / 100) > 0){

			numx += 0.06f;
			draw_num4( gl,texture, tex_size, numx,  ty, tz, ang, rect[(num2 / 100) % 10], r, g, b, a);
		}
		//千以上は未対応
	 }






	/***
	 * 文字を描画します
	 * @param gl
	 * @param texture テクスチャ番号
	 * @param tex_size	フォントサイズ
	 * @param tx		x座標
	 * @param ty		y座標
	 * @param offset
	 * @param sx
	 * @param sy
	 */
	public void draw_str(GL10 gl, int texture, float tex_size, int tx, int ty, float offset,float sx,float sy)
	{
		//draw_string_text(gl,tx, tx, str);
		i += 1;
		gl.glPushMatrix();
		{
			//gl.glTranslatef(0.0f, 0.0f, -0.1f);		//座標位置
			GraphicUtil.glTranslatef_pixel(gl, tx, ty, -0.1f);
			gl.glRotatef(0.0f, 0.0f, 0.0f, 1.0f);	//回転
			gl.glScalef(tex_size, tex_size, 1.0f);	//画像の大きさ指定

			GraphicUtil.drawTexture_pixel_custom(gl,m_texture.width,m_texture.height,texture,
								new float[]{0.0f,offset,sx,sy,0.0f,0.0f},
								1.0f,1.0f,1.0f,1.0f);
			/*GraphicUtil.drawTexture(gl,0.0f,0.0f,1.0f,1.0f,texture,
									0.0f,offset,sx, sy,false, 1.0f,1.0f,1.0f, 1.0f);*/
		}
		gl.glPopMatrix();
	}

}
