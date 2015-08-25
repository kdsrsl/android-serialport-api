package com.example.serialportsdemo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android_serialport_api.SerialPort;

public class MainActivity extends Activity {

	SerialPort mSerialPort;
	EditText et_Receive ;
	EditText et_send ;
	Button btn_send ;
	InputStream in ;
	byte[] buffer ;
	String msgStr ;
	Receive rece;
	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				int size;
				try {
					buffer= new byte[64];
					if (in == null) {
						return;
					}
					size = in.read(buffer);
					if (size > 0) {
						Message msg=handler.obtainMessage(1);
						msg.obj =new String(buffer, 0, size) ;
						handler.sendMessage(msg) ;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
	Handler handler =new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==1){
				String s =(String)msg.obj ;
				et_Receive.append(s);
				et_Receive.append("\n");
			}
		}
	} ;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		et_Receive=(EditText)findViewById(R.id.et_Receive) ;
		et_send=(EditText)findViewById(R.id.et_send) ;
		btn_send=(Button)findViewById(R.id.btn_send) ;

		try {
//			mSerialPort =new SerialPort(new File("/dev/ttyS1"),115200,0) ;
			mSerialPort =new SerialPort(new File("/dev/ttyS1"),9600,0) ;
			in=mSerialPort.getInputStream() ;
			ReadThread readThread =new ReadThread() ;
			readThread.start() ;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		
		
		btn_send.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String sendTxt =et_send.getText().toString() ;
				if(sendTxt.equalsIgnoreCase("")){
					Toast.makeText(MainActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
				}else{
					try {
//						mSerialPort =new SerialPort(new File("/dev/ttyS1"),115200,0) ;
						mSerialPort =new SerialPort(new File("/dev/ttyS1"),9600,0) ;
						OutputStream mOutputStream =mSerialPort.getOutputStream() ;
						byte[] bytes=sendTxt.getBytes() ;
						mOutputStream.write(bytes,0,3);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			}
		});
	}
}
