package com.crazyfivechess;

import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.crazyfivechess.gameview.GameView;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(new GameView(this));
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == event.KEYCODE_BACK){
			for(int[] temp : Constant.pan)
				Arrays.fill(temp,0);		
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
