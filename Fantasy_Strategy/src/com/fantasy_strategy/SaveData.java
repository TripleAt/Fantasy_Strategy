package com.fantasy_strategy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.util.Log;


public class SaveData {

	private static final String LOCAL_FILE = "savedate.bin";
	static private Context context;
	public SaveData(Context con) {
		context = con;
	}

	/**
	 * 読み取る
	 * @return
	 */
	public int SaveFileInput(){

		InputStreamReader in;
	    int scoa = -1;
	    try {
	    	FileInputStream file = context.openFileInput(LOCAL_FILE); //LOCAL_FILE = "log.txt";
	    	in = new InputStreamReader(file);
	    	BufferedReader br = new BufferedReader(in);
	    	scoa = Integer.parseInt(br.readLine());
			in.close();
			file.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	        Log.e("SaveFileInput","ファイルの読み込みに失敗。新規ファイルを生成します");
	        SaveFileOutput(0);
	    }
		return scoa;
	}


	/**
	 * セーブする
	 * @param scoa
	 */
	public void SaveFileOutput(int score ){

		OutputStreamWriter out = null;
		try {
				FileOutputStream file = context.openFileOutput(LOCAL_FILE, Context.MODE_PRIVATE);
				out = new OutputStreamWriter(file);
				out.write(String.valueOf( score ));
				out.flush();
				file.close();
			} catch(IOException e) {
				e.printStackTrace();
		        Log.e("SaveFileOutput","ファイルの書き出しに失敗");
			}
	}
}