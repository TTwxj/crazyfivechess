package com.crazyfivechess;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class RoomActivity extends Activity {
	private BluetoothAdapter adapter;
	private BluetoothDevice device;
	private Button btn_create;
	private Button btn_search;
	private ListView devicesList;
	private ArrayAdapter listAdapter;
	private ArrayList<String> listData;//listview����
	private ArrayList<String> listAddressData;//�豸�����ַ
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// ��ʼ����
			if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {			
				System.out.println("��ʼ����");
			}
			// ��������
			if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				System.out.println("��������");
			}
			// �����豸
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// ��Intent�л�ȡ�豸����
				device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				System.out.println(device.getName());
				listData.add(device.getName());
				listAddressData.add(device.getAddress());//������ַ
				listAdapter.notifyDataSetChanged();				
			}
			if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
				if(BluetoothAdapter.getDefaultAdapter().isEnabled()){
					Toast.makeText(RoomActivity.this, "�����ѿ����������³��Դ���������", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.room_activity);
		
		btn_create = (Button) findViewById(R.id.button_create_room);
		btn_search = (Button) findViewById(R.id.button_search_room);
		devicesList = (ListView) findViewById(R.id.listview_devices);
		
		listData = new ArrayList<String>();
		listAddressData = new ArrayList<String>();
		listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,listData);
		devicesList.setAdapter(listAdapter);
		
		devicesList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {		
				Constant.address = listAddressData.get(position);
				if (device != null && Constant.address != null) {
					Intent intent = new Intent();				
					Constant.serverOrClient = false;
					intent.setClass(getApplicationContext(),ClientActivity.class);				
					startActivity(intent);
				}
			}			
		});
		
		adapter = BluetoothAdapter.getDefaultAdapter();
		// ע��㲥
		IntentFilter filterFound = new IntentFilter(
				BluetoothDevice.ACTION_FOUND);
		filterFound.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filterFound.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		filterFound.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(mReceiver, filterFound);
		
		btn_create.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(adapter.isEnabled()){				
					Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
					discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
					startActivityForResult(discoveryIntent, 1);
					//flag = true;
				}else{
					adapter.enable();
					Toast.makeText(RoomActivity.this, "����Ϊ����������", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!adapter.isEnabled()) {
					Toast.makeText(RoomActivity.this, "����Ϊ����������", Toast.LENGTH_SHORT).show();
					// ����ͨ��startActivityForResult()���������Intent������onActivityResult()�ص������л�ȡ�û���ѡ�񣬱����û�������Yes������
					// ��ô�����յ�RESULT_OK�Ľ����
					// ���RESULT_CANCELED������û���Ը�⿪������
					/*Intent mIntent = new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(mIntent, 1);*/
					// ��enable()����������������ѯ���û�(ʵ������Ϣ�Ŀ��������豸),��ʱ����Ҫ�õ�android.permission.BLUETOOTH_ADMINȨ�ޡ�
					adapter.enable();
					// mBluetoothAdapter.disable();//�ر�����
				}
				else if(adapter.isEnabled()){
					initData();
					adapter.startDiscovery();
				}
			}
		});																			
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		initData();
		super.onRestart();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == 300) {
				Intent intent = new Intent();
				System.out.println("�򿪷���������");
				intent.setClass(getApplicationContext(),
						ServerActivity.class);
				Constant.serverOrClient = true;
				startActivity(intent);
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "����ʧ��", Toast.LENGTH_SHORT).show();
				//finish();
			}
		}
	}
	
	private void initData(){
		listAddressData.removeAll(listAddressData);
		listData.removeAll(listData);
		listAdapter.notifyDataSetChanged();	
		Constant.address = null;
	}
	
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
}
