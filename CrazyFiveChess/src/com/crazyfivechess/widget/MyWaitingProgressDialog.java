package com.crazyfivechess.widget;

import android.app.ProgressDialog;
import android.content.Context;

public class MyWaitingProgressDialog extends ProgressDialog {
	public MyWaitingProgressDialog(Context context) {
		super(context);
		// ���ý�������񣬷��ΪԲ�Σ���ת��  
		setProgressStyle(ProgressDialog.STYLE_SPINNER);  
        // ����ProgressDialog ����  
       // setTitle("��ʾ");  
        // ����ProgressDialog ��ʾ��Ϣ  
        //setMessage("����һ��Բ�ν������Ի���");  
        // ����ProgressDialog �Ľ������Ƿ���ȷ  
        setIndeterminate(false);
        //���õ���Ի�������Ĳ��ֲ���ʧ
        setCanceledOnTouchOutside(false);
        // ����ProgressDialog �Ƿ���԰��˻ذ���ȡ��  
       setCancelable(true);           
		// TODO Auto-generated constructor stub
	}
	public MyWaitingProgressDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

}

