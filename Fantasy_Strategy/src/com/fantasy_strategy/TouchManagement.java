package com.fantasy_strategy;

import android.util.Log;
import android.view.MotionEvent;


public class TouchManagement  {

	public TouchManagement(){

	}

	//画面がタッチされたときに呼ばれるメソッド
	public void touched(MotionEvent e,float x, float y) {
	//	Log.d("touched!"," x = " + x + ", y = " +  y);
		switch(e.getAction()){

		case MotionEvent.ACTION_DOWN:
			Log.d("TouchEvent", "getAction()" + "ACTION_DOWN");
			break;
		case MotionEvent.ACTION_UP:		//指を離した
			Log.d("TouchEvent", "getAction()" + "ACTION_UP");
			 Camera_var.x += Camera_var.x_move;
			 Camera_var.y += Camera_var.y_move;
			 Camera_var.x_move = 0.0f;
			 Camera_var.y_move = 0.0f;
			break;
		case MotionEvent.ACTION_MOVE:
			//Log.d("TouchEvent", "getAction()" + "ACTION_MOVE");
			break;
		case MotionEvent.ACTION_CANCEL:
			Log.d("TouchEvent", "getAction()" + "ACTION_CANCEL");
			break;

		}
	}

	//スクロールした距離が返ってくるメソッド
	public void scroll(MotionEvent e, float x, float y){

		//画面座標でフィールドを動かしたとき実際にはどのように動くか計算する
		int angle_s1 = Camera_var.angle - 90;		//スクロール計算用angle
		if(angle_s1 < 0){			//見た目の角度に合わせる
			angle_s1 = 360 + angle_s1;
		}
		int angle_s2;
		float atai;		//値コントロールで出た値を保存しておく
		float ans_x = 0.0f, ans_y = 0.0f;		//一時的に保存するｘ,y

		/**
		 * 分かりづらいけど、カメラの制御に応じて、ディスプレイがワールドに対しての座標計算を変えている。
		 * 0～180度、180～360度
		 * 90～270度、270～90度
		 * それぞれで、割合の計算をしている。
		 * 
		 * 例えば、angle_s1が0度のときはｘを+方向に動かしたときの割合は
		 * X：1
		 * Y：0
		 * となる
		 * 
		 * angle_s1が45度の場合、ｘを+方向に動かしたときの割合は
		 * X:0.5
		 * Y:-0.5
		 * となる
		 * **/
		
		if(angle_s1 / 180 == 0){		//0～180
			angle_s2 = angle_s1 % 180;
			atai = atai_control(angle_s2, 0, 180, 1.0f, -1.0f);		//比例の値を返す
			ans_x = ((y / 800.0f) * atai);
			ans_y = ((x / 800.0f) * atai);
			
			//Log.d("ans_x","うえ"+ atai + "," + angle_s1);

		}else{						//180~360
			angle_s2 = angle_s1 % 180;
			atai = atai_control(angle_s2, 0, 180, -1.0f, 1.0f);
			ans_x = ((y / 800.0f) * atai);
			

			ans_y = ((x / 800.0f) * atai);
			
			//Log.d("ans_x","した" + "," + angle_s1);
		}

		angle_s1 += 90;
		angle_s1 %= 360;
		if(angle_s1 / 180 == 0 ){		//90~270	
			angle_s2 = angle_s1 % 180;
			atai = atai_control(angle_s2, 0, 180, 1.0f, -1.0f);
			ans_y += ((y / 800.0f) * atai);
			
			angle_s2 = angle_s1 % 180;
			atai = atai_control(angle_s2, 0, 180, -1.0f, 1.0f);
			ans_x += ((x / 800.0f) * atai);
			//Log.d("ans_y","うえ"+atai + "," + (angle_s1-90));
		}else{							//270~90
			angle_s2 = angle_s1 % 180;
			atai = atai_control(angle_s2, 0, 180, -1.0f, 1.0f);
			ans_y += ((y / 800.0f) * atai);
			

			angle_s2 = angle_s1 % 180;
			atai = atai_control(angle_s2, 0, 180, 1.0f, -1.0f);
			ans_x += ((x / 800.0f) * atai);
			//Log.d("ans_y","した"+ atai + "," + (angle_s1-90));
		}

		Camera_var.x_move = ans_x;
		Camera_var.y_move = ans_y;

	}


	//-----------------------------------------------------------------------------
	//  与えられる値によって比例した値を返す(値の渡し方により反比例にもなる)
	//-----------------------------------------------------------------------------
	float atai_control(int moto_atai, int moto_min, int moto_max, float kahen_min, float kahen_max )
	{
		float moto_sa, moto_ookisa;
		float kahen_ookisa, ans;

		kahen_ookisa = kahen_max - kahen_min;		//何の値からどの値まで変化させるか
		moto_ookisa = (float)(moto_max - moto_min);	//もう片っ方の値もどの値からどの値まで変化するか
		moto_sa = (float)(moto_atai - moto_min);	//現在の大きさ

		//%を計算し、値を返す！
		ans = (float)((moto_sa / moto_ookisa) * kahen_ookisa + kahen_min);	//どの大きさにするかの比例％* 可変する大きさ + 最小値

		return ans;
	}
}