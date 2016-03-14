


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;


public class UDPClient extends Thread
{
	DatagramSocket sock = null;
	TimeRequired timeRequired=new TimeRequired();
	long start,end;
	InetAddress host;
	int serverPort;
	public UDPClient(TimeRequired timeRequired) {
		this.timeRequired=timeRequired;
	}
	synchronized public void run() {
		
	    Properties property=new Properties();
		try {
			property.load(new FileInputStream(new File("../config.property")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
       serverPort=Integer.parseInt(property.getProperty("ServerPortUDP"));
       String serverIP=property.getProperty("ServerIP");

       
              
       String s;        
       BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));        
       try
       {
           sock = new DatagramSocket();            
            host = InetAddress.getByName(serverIP);            
        	   int size=1024;
        	   callUDPservice(1,0); // 1 Byte
        	   //System.out.println("1byte done");
        	   callUDPservice(1024,0);// 1 KByte
        	   callUDPservice(64000,0); // 64 KByte
           	   //System.out.println("64kbyte done");
           	 
     			long datasize=Integer.parseInt(property.getProperty("Datasize"));
      			long loopsize=Integer.parseInt(property.getProperty("Iteration"));
        	   callUDPservice((int)datasize,(int)loopsize-1);
        	   //System.out.println("udp iteration done");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	
	public void callUDPservice(int size,int iteration){
	       try {
		 	   byte[] b = getData(size).getBytes();                
		       DatagramPacket  dp = new DatagramPacket(b , b.length , host , serverPort);
		       //System.out.println("udp Sending data");               
			   start = System.nanoTime() ;               
			   int itr;
			   for(itr=0;itr<iteration;itr++){
				   sock.send(dp);
				   byte[] buffer = new byte[size];
			       DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			      // System.out.println("udpClient waiting to receive data "+itr);			       
			       sock.receive(reply);
			   }
		       //byte[] data = reply.getData();
		       end = System.nanoTime() ;
		       if(iteration==0){
			       if(size==1){
			    	   timeRequired.udpTimeBYTE=(end-start);
			       }
			       else if(size==1024){
			    	   timeRequired.udpTimeKBYTE=(end-start);
			       }
			       else if(size==64000){
			    	   timeRequired.udpTime64KBYTE=(end-start);
			       }
		       }else{
		    	   timeRequired.udpIteration=(end-start);
		    	}
			   //String s = new String(data, 0, reply.getLength());               

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Return total time required for performing UDP opertions.
	 */
	
	public TimeRequired getTimeRequired(){
		return timeRequired;
	}

	public String getData(int size){	    
	    StringBuilder stringBuilder = new StringBuilder(size);
	    for (int j = 0; j < size; j++) {
			stringBuilder.append('z');
	    }
	    return stringBuilder.toString();
	}
	
}
