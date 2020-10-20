package com.fantasy_strategy;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;



public class Global {

	// MainActivity
	public static MainActivity mainActivity;

	// TitleActivity
	public static TitleActivity titleActivity;

	// ResulltActivity
	public static ResultActivity ResultActivity;

	//Stage_Select_Activity
	public static Stage_Select_Activity SelectActivity;

	//GLコンテキストを保持する変数
	public static GL10 gl;

	//ランダムな値を生成する
	public static Random rand = new Random(System.currentTimeMillis());

	//デバックモードであるか
	public static boolean isDebuggable;

}
