package com.example.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MyMessage {
	private byte b;
	private String userName;
	private String password;
	private int valid;
	private int ready;
	
	//Change up here for the type of message I'm looking for when receiving
	
	
	public byte[] Message2Byte() throws IOException{
		byte[] messagebyte;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// �ֽ������  
        DataOutputStream dos = new DataOutputStream(baos);// ������������ڰ�װ�ֽ������  
        
        dos.write(b);
        
        switch (b) {
    	case 1://Login
    		dos.writeUTF(userName);
    		dos.writeUTF(password);
    		break;
    	case 2://Check Password
    		dos.write(valid);
    		break;
    	case 3://Client 'Ready' to play
    		dos.write(ready);
		default:
			break;
        }       
        
        messagebyte=baos.toByteArray();// ��д�������ת�����ֽ����� 
        dos.close();  
        baos.close();  
        
        return messagebyte;
	}
	
	
	public static MyMessage byte2Message(byte[] messagebyte) throws IOException{
		
		MyMessage mymessage=new MyMessage();
		ByteArrayInputStream bais = new ByteArrayInputStream(messagebyte);// �ֽ�������  
        DataInputStream dis = new DataInputStream(bais);// �������������ڰ�װ�ֽ�������
        
        
        mymessage.setb(dis.readByte());
        
        switch(mymessage.b){
    	
    		case 1://Login
    			mymessage.setUsername(dis.readUTF());
    			mymessage.setPassword(dis.readUTF());
    			break;
    	
    		case 2://Check Password
    			mymessage.setValid(dis.read());
    			break;
    	
    		case 3://Client 'Ready' to play
    			mymessage.setReady(dis.read());
    			break;
        }
               
        return mymessage;
	}
	
	public void setb(byte i){
		this.b=i;
	}
	
	public void setUsername(String userName){
		this.userName=userName;
	}
	
	public void setPassword(String strps){
		this.password=strps;
	}
	
	public void setReady(int ready){
		this.ready = ready;
	}
	
	public void setValid(int valid){
		this.valid=valid;
	}
	
	public byte getb(){
		return b;
	}
	
	public String getUsername(){
		return userName;
	}
	
	public String getPassword(){
		return password;
	}
	
	public int getReady(){
		return ready;
	}
	
	public int getValid(){
		return valid;
	}
	
}