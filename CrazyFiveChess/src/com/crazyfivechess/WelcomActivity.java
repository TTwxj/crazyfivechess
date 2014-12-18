package  com.crazyfivechess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class WelcomActivity extends Activity{
	public static WelcomActivity instant;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcom);
		instant = this;
		init();
		new Thread(){
			public void run() {
				/*
				Thread.sleep(1000);
				*/
				final ImageView infoOperatingIV = (ImageView) findViewById(R.id.infoOperating);
				Animation operatingAnim = AnimationUtils.loadAnimation(WelcomActivity.this, R.anim.tip);
				LinearInterpolator lin = new LinearInterpolator();
				operatingAnim.setInterpolator(lin);
				operatingAnim.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						startActivity(new Intent(WelcomActivity.this,MenuActivity.class));
						finish();
					}
				});
				
				infoOperatingIV.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						infoOperatingIV.clearAnimation();						
					}
				});
				if (operatingAnim != null) {
					infoOperatingIV.startAnimation(operatingAnim);
				}							
			};
		}.start();
	}
	
	public void init(){		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Constant.ScreenWidth = dm.widthPixels;
		Constant.ScreenHeight = dm.heightPixels;
		Constant.CurScreenW = Constant.ScreenWidth>Constant.ScreenHeight?Constant.ScreenHeight:Constant.ScreenWidth;
		Constant.CurScreenH = Constant.ScreenWidth<Constant.ScreenHeight?Constant.ScreenHeight:Constant.ScreenWidth;
		Constant.rect = (Constant.CurScreenW-Constant.padding*2)/Constant.lineNum;
		Constant.maxDistance = (Constant.rect/2)*(Constant.rect/2);
		Constant.originalMin = 100000+Constant.ScreenHeight*Constant.ScreenHeight+Constant.ScreenWidth*Constant.ScreenWidth;
	}
}
