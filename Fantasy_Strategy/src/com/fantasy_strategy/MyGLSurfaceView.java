package com.fantasy_strategy;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public  class MyGLSurfaceView extends GLSurfaceView
implements      GestureDetector.OnGestureListener,
GestureDetector.OnDoubleTapListener {

	//画面サイズ
	private float mWidth;
	private float mHeight;

	//MyRendererを保持する
	//private MyRenderer mMyRenderer;
	private TouchManagement touch;
	private GestureDetector ges;

	//Context
	public Context mContext;


	public MyGLSurfaceView(Context context) {
		super(context);
		setFocusable(true);		//タッチイベントが取得できるようにする
		ges = new GestureDetector(context,this);
		touch = new TouchManagement();
		mContext = context;
	}

	@Override
	public void setRenderer(Renderer renderer) {
		super.setRenderer(renderer);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		super.surfaceChanged(holder, format, w, h);
		this.mWidth = w;
		this.mHeight = h;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = (event.getX() / (float)mWidth) * 2.0f - 1.0f;
		float y = (event.getY() / (float)mHeight) * -3.0f + 1.5f;
		ges.onTouchEvent(event);

		touch.touched(event,x, y);

		return true;
	}



	@Override
	public boolean onSingleTapConfirmed(MotionEvent paramMotionEvent) {
	//	Log.d("onSingleTapConfirmed","タッチ");
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent paramMotionEvent) {
		//Log.d("onDoubleTap","タッチ");
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent paramMotionEvent) {
	//	Log.d("onDoubleTapEvent","タッチ");
		return false;
	}

	@Override
	public boolean onDown(MotionEvent paramMotionEvent) {
		//Log.d("onDown","タッチ");
		return false;
	}

	@Override
	public void onShowPress(MotionEvent paramMotionEvent) {
		//Log.d("onShowPress","タッチ");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent paramMotionEvent) {
	//	Log.d("onSingleTapUp","タッチ");
		return false;
	}

	@Override
	public void onLongPress(MotionEvent paramMotionEvent) {
		//Log.d("onLongPress","タッチ");
	}

	//フリック
	@Override
	public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
		//Log.d("onFling","タッチ");
		return false;
	}

	//スクロール
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

		float x = e1.getRawX() - e2.getRawX();		//xの移動距離を求める
		float y = e1.getRawY() - e2.getRawY();		//yの移動距離を求める

		if(GameMain.scroll_screen){
			//上下のみスクロール
			ResultMain.setCameraPotision(x, y);
		}else{
			//ゲーム画面用のスクロール
			touch.scroll(e2, x, y );
		}

		return false;
	}


}
