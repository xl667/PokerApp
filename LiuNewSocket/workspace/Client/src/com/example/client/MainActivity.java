package com.example.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import android.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	Button button;
	TextView textView;
	EditText editText;
	Handler handler;
	MyHandle threadHandler;
	Connect connect=null;
	Object myLock=new Object();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button=(Button) findViewById(R.id.button1);
		textView=(TextView) findViewById(R.id.textView1);
		editText=(EditText) findViewById(R.id.editText1);
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Bundle bundle=msg.getData();
				textView.setText(bundle.getString("string"));
			}
			
		};
		
		
		Thread receiveThread=new Thread(
				
				new Runnable() {
					public void run() {
						connect=new Connect();
						try {
							while(true){
								if(connect.selector.select()==0)
									continue;
								connect.play();
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					};
				}
			);
		receiveThread.start();
		
		Thread sendThread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				threadHandler=new MyHandle(Looper.myLooper());
				Looper.loop();

			}
		});
		sendThread.start();
			
		/*Thread myThread=new Thread(
			
			new Runnable() {
				public void run() {
					connect=new Connect();
					Looper.prepare();
					threadHandler=new MyHandle(Looper.myLooper());
					try {
						while(true){
							Looper.loop();
							if(connect.selector.select(100)==0)
								continue;
							connect.play();
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				};
			}
		);
		myThread.start();*/
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String str=editText.getText().toString();
				Message msg=Message.obtain();
				Bundle bundle=new Bundle();
				bundle.putString("string", str);
				msg.setData(bundle);
				threadHandler.sendMessage(msg);
				
				/*MyMessage message=new MyMessage();
				message.setb((byte)9);
				message.setstr(str);
				synchronized (myLock) {
					try {
						connect.sendBuffer.clear();
						connect.sendBuffer.put(message.Message2Byte());
						
						
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}*/
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public class MyHandle extends Handler{
		public MyHandle(){
			
		}
		public MyHandle(Looper looper){
			super(looper);
		}
		public void handleMessage(Message msg){
			String str=msg.getData().getString("string");
			//send message to server
			MyMessage message=new MyMessage();
			message.setb((byte)9);
			message.setstr(str);
			try {
				connect.sendBuffer.clear();
				connect.sendBuffer.put(message.Message2Byte());
				connect.sendBuffer.flip();
				connect.socketChannel.write(connect.sendBuffer);
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
		}
	}
	
	public class Connect {
		SocketChannel socketChannel=null;
		public ByteBuffer sendBuffer=ByteBuffer.allocate(1024);
		ByteBuffer receiveBuffer=ByteBuffer.allocate(1024);
		Selector selector;
		public Connect() {
			// TODO Auto-generated constructor stub
			try {
				socketChannel=SocketChannel.open();
				SocketAddress remoteAddress=new InetSocketAddress("58.198.95.225", 4322);
				socketChannel.connect(remoteAddress);
				socketChannel.configureBlocking(false);
				System.out.println("与服务器的连接建立成功");
			    selector=Selector.open();
			    socketChannel.register(selector, SelectionKey.OP_READ);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		public void play() throws IOException{
			
			//socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
				Set readyKeySet=selector.selectedKeys();
				Iterator<SelectionKey> iterator=readyKeySet.iterator();
				SelectionKey key=null;
				while(iterator.hasNext()){
					
					key=(SelectionKey) iterator.next();
					MyMessage message;
					if(key.isReadable()){ //receive message from server
						receiveBuffer.clear();
						int count=socketChannel.read(receiveBuffer);
						if (count > 0) {  
							receiveBuffer.flip();  
			                message = MyMessage.byte2Message(receiveBuffer.array());  
			                System.out.println("Receive : "+message.getb()+","+message.getStr());
			                Message msg=Message.obtain();
			                Bundle bundle=new Bundle();
			                bundle.putInt("int", message.getb());
			                bundle.putString("string", message.getStr());
			                msg.setData(bundle);
			                handler.sendMessage(msg);
						}
					}/*else if (key.isWritable()) {
						//sendBuffer.flip(); //把极限设为位置
						synchronized (myLock) {
							sendBuffer.flip();
					        socketChannel.write(sendBuffer);
					        sendBuffer.compact();
						}
					}*/
				}
				readyKeySet.clear();
		}
	}

}
