

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Properties;


public class TCPClient extends Thread
{
	 TimeRequired timeRequired=new TimeRequired();
	public TCPClient(String threadName) {
		// TODO Auto-generated constructor stub
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
       int serverPort=Integer.parseInt(property.getProperty("ServerPort"));
       String serverIP=property.getProperty("ServerIP");
       try {        		    
    	     Socket client = new Socket(serverIP, serverPort);
             System.out.println("Just connected to " 
    		 + client.getRemoteSocketAddress());                  
             OutputStream outToServer = client.getOutputStream();         
             ObjectOutputStream oos=new ObjectOutputStream(outToServer);
     	
     		//1B =  1  
     		//1KB = 1024
     		//1MB = 1024*1024
                         
             
             /*Sending 1Byte packet =>*/
             int dataSize=1;
	     	 long end=0,start=0;	     	 
	    	 start = System.nanoTime() ;
	    	 
  			 oos.writeObject(getData(dataSize));  			 
  			 InputStream is = client.getInputStream();
  			 ObjectInputStream ois = new ObjectInputStream(is);
     		 Object inputReceivedFromServer =ois.readObject();  			
  			 if(inputReceivedFromServer instanceof String){
 	         	//System.out.println("\nMessage received from server: "+(String)inputReceivedFromServer);
  	  			 end=System.nanoTime();
  	  			 timeRequired.tcpTimeBYTE=end-start;
 			 }else{
 				System.err.println("Error occured in TCPClient");// println("\nOutput of different type ");
 			 }

  			 
  			 /*Sending packet of 1024 Byte */
   			 start=System.nanoTime();
  			 oos.writeObject(getData(1024));  			      		   			 
  			 inputReceivedFromServer =ois.readObject();  			
  			 if(inputReceivedFromServer instanceof String){
 	         	//System.out.println("\nSecond Message received from server: "+(String)inputReceivedFromServer);
  				end=System.nanoTime();
  	  			 timeRequired.tcpTimeKBYTE=end-start;
 			 }else{
 				System.err.println("Error occured in TCPClient 102400 Bytes size");  			 
 			 }


  			 /*Sending packet of 64000Byte*/
   			 start=System.nanoTime();
  			 oos.writeObject(getData(64000));  			      		   			 
  			 inputReceivedFromServer =ois.readObject();  			
  			 if(inputReceivedFromServer instanceof String){
 	         	//System.out.println("\nSecond Message received from server: "+(String)inputReceivedFromServer);
  				end=System.nanoTime();
  	  			 timeRequired.tcpTime64KBYTE=end-start;
 			 }else{
 				System.err.println("Error occured in TCPClient 102400 Bytes size");  			 
 			 }
  			 

  			 
  			 

  			 /*
  			  * Now sending packets of size "dataSize" for "total number of iteration "loopSize".
  			  * So total amount of data sending over network is dataSize * loopSize.
  			  */
  			long datasize=Integer.parseInt(property.getProperty("Datasize"));
  			long loopsize=Integer.parseInt(property.getProperty("Iteration"));
	  		start=System.nanoTime();
  			 for(int k=0;k<loopsize;k++){
  	  			 oos.writeObject(getData(datasize));  			      		   			 
  	  			 inputReceivedFromServer =ois.readObject();  			
  	  			 if(inputReceivedFromServer instanceof String){
  	 	         	//System.out.println("\nSecond Message received from server: "+(String)inputReceivedFromServer);
  	 			 }else{
  	 				System.err.println("Error occured in TCPClient Iteration operation");  			 
  	 			 }
  			 }
 			end=System.nanoTime();
	  	    timeRequired.tcpIteraiton=end-start;
  			 client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public TimeRequired getTimeRequired(){
		return timeRequired;
	}
/*
 * This will generate string of input size.
 * Parameter: 
 * 	1.	size : size of string
 * 	2   return : string of size "size"
 */
	String getData(long size){
		StringBuilder stringBuilder = new StringBuilder((int)size);			 			
       for (int j = 0; j < size; j++) {
			stringBuilder.append('z');
       }  			 
       return stringBuilder.toString();
	}
}
