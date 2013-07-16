package mytcp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GamePlayer {
	private byte b;
	private String username;
	private String password; 
	int chips;
	int minBet;
	private int valid;
	private int ready;
	
	public byte[] Message2Byte() throws IOException{
		
		byte[] messagebyte;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// �ֽ������  
        DataOutputStream dos = new DataOutputStream(baos);// ������������ڰ�װ�ֽ������  
        
        dos.write(b);
        
        switch (b) {
        	case 1://Login
        		dos.writeUTF(username);
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
	
	public static GamePlayer byte2Message(byte[] messagebyte) throws IOException{
		GamePlayer mymessage=new GamePlayer();
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
	
	public void setb(byte b){
		this.b=b;
	}
	
	public void setUsername(String username){
		this.username=username;
	}
	
	public void setPassword(String strps){
		this.password=strps;
	}
	
	public void setReady(int ready){
		this.ready=ready;
	}
	
	public void setValid(int valid){
		this.valid=valid;
	}
	
	public byte getb(){
		return b;
	}
	
	public String getUsername(){
		return username;
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