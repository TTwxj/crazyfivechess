package  com.crazyfivechess;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyfivechess.gameview.ClientGameView;
import com.crazyfivechess.gameview.ServerGameView;
import com.crazyfivechess.widget.MyWaitingProgressDialog;

public class ClientActivity extends Activity {
	private MyWaitingProgressDialog progressDialog;
	public static Handler handler;
	// �������
	private BluetoothAdapter adapter;
	private static BluetoothSocket socket;
	private BluetoothDevice device;
	
	private EditText client_edit;
	private Button client_send;
	private TextView textView2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(new ClientGameView(this));
		/*
		setContentView(R.layout.client);
		textView2 = (TextView) findViewById(R.id.textView2);
		client_edit = (EditText) findViewById(R.id.client_edit);
		client_send = (Button) findViewById(R.id.client_send);
		*/
		 handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				// 1����Ҫ�ر�activity
				//2�����Ѿ������Ϸ������û�
				case Constant.CLIENT_CONNECTING:
					//finish();
					progressDialog.show();
					break;
				case Constant.CLIENT_CONNECT_OK:
					progressDialog.dismiss();
					break;
				case Constant.CLIENT_WIN:
					Toast.makeText(ClientActivity.this,  "�ҷ���ʤ", Toast.LENGTH_SHORT).show();
					break;
				case Constant.CLIENT_FAIL:
					Toast.makeText(ClientActivity.this, "�ҷ�ʧ��", Toast.LENGTH_SHORT).show();
					break;
					
				case Constant.CLIENT_CONNECT_ERROR:
					finish();
					break;
				
				default:
					break;
				}
				super.handleMessage(msg);
			}

		};
		// ����ProgressDialog����
		 progressDialog = new MyWaitingProgressDialog(this) {	
			@Override
			public void onBackPressed() {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = Constant.CLIENT_CONNECT_ERROR;
				handler.sendMessage(msg);
				super.onBackPressed();
			}
	
		};
		progressDialog.setMessage("����Ѱ������");	
		
		adapter = BluetoothAdapter.getDefaultAdapter();
		adapter.cancelDiscovery();
		device = adapter.getRemoteDevice(Constant.address);
		ConnectThread clientThread = new ConnectThread();
		clientThread.start();
		/*
		client_send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new MWriteThread("client").start();
			}
		});
		*/			
	}
	
	// �ͻ��˵��߳�
	private class ConnectThread extends Thread {
		public void run() {
			try {
				socket = device.createRfcommSocketToServiceRecord(Constant.uuid);
				Message msg = new Message();
				msg.what = 0;
				ClientActivity.handler.sendMessage(msg);
				socket.connect();
				System.out.println("���ӳɹ�");
				msg.what = 1;
				ClientActivity.handler.sendMessage(msg);
				new MReadThread().start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("aaaa" + e.toString());
				e.printStackTrace();
			}
		}

	}

	// ���������߳�
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
	// ��ȡ�����߳�
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
			while (true) {
				try {
					if ((bytes = mmInStream.read(buffer)) > 0) {
						ClientGameView.hand = true;
						byte[] buf_data = new byte[bytes];
						for (int i = 0; i < bytes; i++) {
							buf_data[i] = buffer[i];
						}
						String s = new String(buf_data);
						String[] temp = s.split(",");						
						Constant.pan[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])] = 1;
						if("1".equals(temp[2])){
							Message msg = new Message();					
							msg.what = 2;
							ClientGameView.ispaint = false;
							handler.sendMessage(msg);
						}else if("2".equals(temp[2])){
							Message msg = new Message();					
							msg.what = 3;
							ClientGameView.ispaint = false;
							handler.sendMessage(msg);
						}
					}
				} catch (IOException e) {
					try {
						mmInStream.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					break;
				}
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
