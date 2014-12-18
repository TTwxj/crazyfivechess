package  com.crazyfivechess;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.crazyfivechess.gameview.ServerGameView;
import com.crazyfivechess.widget.MyWaitingProgressDialog;

public class ServerActivity extends Activity{
	private MyWaitingProgressDialog progressDialog;
	public static Handler handler;
	// 蓝牙组件
	private BluetoothServerSocket mServerSocket;
	private BluetoothAdapter adapter;
	private static BluetoothSocket socket;
	/*
	private EditText serevr_edit;
	private Button serevr_send;
	private TextView textView;
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new ServerGameView(this));
		//setContentView(R.layout.server);
		handler = new Handler() {
			
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				// 1代表要关闭activity
				//2代表已经连接上服务器用户
				case Constant.SERVER_CONNECTING:
					//finish();
					progressDialog.show();
					break;
				case Constant.SERVER_CONNECT_OK:
					progressDialog.dismiss();
					break;
				case Constant.SERVER_WIN:
					Toast.makeText(ServerActivity.this,"我方获胜", Toast.LENGTH_SHORT).show();
					break;
				case Constant.SERVER_FAIL:
					Toast.makeText(ServerActivity.this,"我方失利", Toast.LENGTH_SHORT).show();
					break;
					
				case Constant.SERVER_CONNECT_ERROR:
					finish();
					break;					
				default:
					break;
				}
				super.handleMessage(msg);
			}			
		};
		
		
		progressDialog = new MyWaitingProgressDialog(this) {
			@Override
			public void onBackPressed() {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = Constant.SERVER_CONNECT_ERROR;
				handler.sendMessage(msg);
				super.onBackPressed();
			}
		};
		progressDialog.setMessage("正在等待连接");
		
		adapter = BluetoothAdapter.getDefaultAdapter();
		AcceptThread serverThread = new AcceptThread();
		serverThread.start();		
	}
	
	// 服务器段的线程
	private class AcceptThread extends Thread {
		public void run() {
			try {
				mServerSocket = adapter.listenUsingRfcommWithServiceRecord(
						"fiveChess", Constant.uuid);			
				Message msg = new Message();
				msg.what = 0;
				ServerActivity.handler.sendMessage(msg);
				socket = mServerSocket.accept();
				System.out.println("连接成功");
				msg.what = 1;
				ServerActivity.handler.sendMessage(msg);
				new MReadThread().start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 读取操作线程
	private class MReadThread extends Thread {
		public void run() {
			byte[] buffer = new byte[1024];
			int bytes;
			InputStream mmInStream = null;

			try {
				mmInStream = socket.getInputStream();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				while ((bytes = mmInStream.read(buffer)) > 0) {
					ServerGameView.hand = true;
					byte[] buf_data = new byte[bytes];
					for (int i = 0; i < bytes; i++) {
						buf_data[i] = buffer[i];
					}
					String s = new String(buf_data);							
					String[] temp = s.split(",");
					Constant.pan[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])] = 2;
					if("1".equals(temp[2])){
						Message msg = new Message();					
						msg.what = 2;
						ServerGameView.ispaint = false;
						handler.sendMessage(msg);
					}else if("2".equals(temp[2])){
						Message msg = new Message();					
						msg.what = 3;
						ServerGameView.ispaint = false;
						handler.sendMessage(msg);
					}
				}
			} catch (IOException e) {
				try {
					mmInStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	// 发送数据线程
	public static class MWriteThread extends Thread {
		private String data;

		public MWriteThread(String string) {
			data = string;
		}

		@Override
		public void run() {
			OutputStream mmOutStream = null;
			try {
				mmOutStream = socket.getOutputStream();
				mmOutStream.write(data.getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
	}	
	
	@Override
	protected void onDestroy() {		
		if(progressDialog!=null){
			progressDialog.dismiss();
		}
		super.onDestroy();
	}
}
