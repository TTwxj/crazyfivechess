package com.crazyfivechess.gameview;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;

import com.crazyfivechess.ClientActivity;
import com.crazyfivechess.Constant;
import com.crazyfivechess.R;
import com.crazyfivechess.WelcomActivity;
import com.crazyfivechess.util.Util;

public class ClientGameView extends SurfaceView implements Runnable, Callback{

	Activity context;
	private Thread th = new Thread(this);
	Paint paint = new Paint();
	private SurfaceHolder sfh;
	private Canvas canvas;
	public static boolean ispaint = true;
	
	private Resources res;
	private Bitmap bg;
	
	private Bitmap ai;
	private Bitmap human;
	private int chessW;
	
	public static boolean hand = true;
	private boolean istart = true;
	
	
	
	public ClientGameView(Activity context) {
		super(context);
		this.context = context;
		init();
	}

	private void init(){			
		sfh = this.getHolder();
		sfh.addCallback(this);
		setFocusable(true);
		
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		
		res = getResources();
		bg = BitmapFactory.decodeResource(res, R.drawable.wood_background);
		
		ai = BitmapFactory.decodeResource(res, R.drawable.ai);
		human = BitmapFactory.decodeResource(res, R.drawable.human);
		chessW = ai.getWidth()/2;
		initBluetooth();
	}
	
	/**
	 * 初始化蓝牙设备，开启客户端线程
	 * **/
	private void initBluetooth(){
	
	}		

	
	private void draw(){	
		canvas  = sfh.lockCanvas();		
		drawbg();
		drawPan();
		drawChess();
		drawHand();
		if(canvas!=null){
			sfh.unlockCanvasAndPost(canvas);
		}		
	}
	
	private void drawbg(){
		if(canvas!=null)
			canvas.drawBitmap(bg, null, new RectF(0, 0,Constant.ScreenWidth, Constant.ScreenHeight), paint);
	}
	
	private void drawPan(){
		if(canvas==null) return;	
		//画横线s
		for(int i=0;i<=Constant.lineNum;i++){
			if(i==0 || i==Constant.lineNum){
				paint.setStrokeWidth((float) 7.0);
			}else{
				paint.setStrokeWidth((float) 3.0);
			}
			canvas.drawLine(Constant.padding,Constant.paddingTop+i*Constant.rect, 
					Constant.padding+Constant.lineNum*Constant.rect, Constant.paddingTop+i*Constant.rect,paint);
		}
		//画竖线
		for(int i=0;i<=Constant.lineNum;i++){
			if(i==0 || i==Constant.lineNum){
				paint.setStrokeWidth((float) 7.0);
			}else{
				paint.setStrokeWidth((float) 3.0);
			}
			canvas.drawLine(Constant.padding+i*Constant.rect,Constant.paddingTop, 
					Constant.padding+i*Constant.rect,Constant.paddingTop+Constant.lineNum*Constant.rect,paint);
		}
	}
	
	private void drawChess(){
		if(canvas==null) return;
		for(int i=0;i<=Constant.lineNum;i++)
			for(int j=0;j<=Constant.lineNum;j++)
				if(Constant.pan[i][j]==1){
					canvas.drawBitmap(
							BitmapFactory.decodeResource(getResources(), R.drawable.human), 
							Constant.padding+i*Constant.rect-chessW, 
							Constant.paddingTop+j*Constant.rect-chessW, paint);
				}else if(Constant.pan[i][j]==2){
					canvas.drawBitmap(
							BitmapFactory.decodeResource(getResources(), R.drawable.ai), 
							Constant.padding+i*Constant.rect-chessW, 
							Constant.paddingTop+j*Constant.rect-chessW, paint);
				}
	}
	
	public void drawWin(){
		if(canvas==null) return;
		if(Util.checkWin()){
			draw();
			Looper.prepare();
			if(Constant.whowin==1){				
				Toast.makeText(getContext(), "白子获胜", Toast.LENGTH_LONG).show();				
			}else if(Constant.whowin==2){
				Toast.makeText(getContext(), "黑子获胜", Toast.LENGTH_LONG).show();
			}
			Looper.loop();
			istart = false;
		}
	}
	
	private void drawHand(){
		if(canvas==null) return;
		paint.setTextSize(Constant.CurScreenW*3/16);
		paint.setTypeface(Typeface.SANS_SERIF);
		if(hand){
			if(Constant.ScreenWidth< Constant.ScreenHeight)
				canvas.drawText("黑方执子", Constant.CurScreenW/8,(float) (Constant.paddingTop+Constant.CurScreenW*5/4),paint);
			else{
				paint.setTextSize((Constant.CurScreenH-Constant.CurScreenW)*3/16);
				canvas.drawText("黑方执子", (float) (Constant.CurScreenW),Constant.CurScreenW/2,paint);
			}				
		}else{
			paint.setColor(Color.WHITE);
			if(Constant.ScreenWidth< Constant.ScreenHeight)
				canvas.drawText("白方执子", Constant.CurScreenW/8,(float) (Constant.paddingTop+Constant.CurScreenW*5/4),paint);
			else{
				paint.setTextSize((Constant.CurScreenH-Constant.CurScreenW)*3/16);
				canvas.drawText("白方执子", (float) (Constant.CurScreenW),Constant.CurScreenW/2,paint);
			}		
		}
		paint.setColor(Color.BLACK);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!istart) return false;
		float x = event.getX();
		float y = event.getY();
		int[] result = Util.getCloseXY(x, y);
		if(result[0]!=-1&&result[1]!=-1 && Constant.pan[result[0]][result[1]] == 0){			
			if(hand){
				Constant.pan[result[0]][result[1]] = 2;	
				int temp = Util.checkWin() ? Constant.whowin: 0;
				new ClientActivity.MWriteThread(result[0]+","+result[1]+","+temp).start();
				hand = false;
			}
			drawWin();			
		}		
		
		return super.onTouchEvent(event);
	}
	
	@Override
	public void run() {
		while(istart){
			draw();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		ispaint = true;
		istart = true;
		th = new Thread(this);
		th.start();		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		
		WelcomActivity.instant.init();//重新加载屏幕数据	
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {	
		istart = false;
	}
	
	
}
