package  com.crazyfivechess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MenuActivity extends Activity{
	private Animation animation;
	private Button two_btn;
	private Button one_btn;
	private Button online_btn;
	private Button exit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		init();
	}
	
	private void init(){
		animation = AnimationUtils.loadAnimation(this, R.anim.welcome_button);
				
		two_btn = (Button) findViewById(R.id.two_button);
		two_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				animation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						Intent i = new Intent(MenuActivity.this,MainActivity.class);
						Constant.CUR_MODE = Constant.TWO_MODE;
						startActivity(i);
					}
				});
				two_btn.startAnimation(animation);
				two_btn.setVisibility(View.INVISIBLE);
			}
		});
		
		one_btn = (Button) findViewById(R.id.one_button);
		one_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				animation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						Intent i = new Intent(MenuActivity.this,MainActivity.class);
						Constant.CUR_MODE = Constant.ONE_MODE;
						startActivity(i);
					}
				});
				one_btn.startAnimation(animation);
				one_btn.setVisibility(View.INVISIBLE);
			}
		});
		
		online_btn = (Button) findViewById(R.id.online_button);
		online_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				animation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						Intent i = new Intent(MenuActivity.this,RoomActivity.class);
						startActivity(i);
					}
				});
				online_btn.startAnimation(animation);
				online_btn.setVisibility(View.INVISIBLE);
			}
		});
		
		exit = (Button) findViewById(R.id.exit);
		exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				animation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						finish();
					}
				});
				exit.startAnimation(animation);
				exit.setVisibility(View.INVISIBLE);
			}
		});
		
	}			
	
	@Override
	protected void onResume() {
		two_btn.setVisibility(View.VISIBLE);
		one_btn.setVisibility(View.VISIBLE);
		online_btn.setVisibility(View.VISIBLE);
		exit.setVisibility(View.VISIBLE);
		super.onResume();
	}
}
