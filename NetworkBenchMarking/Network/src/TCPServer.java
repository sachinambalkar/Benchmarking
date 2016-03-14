

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class TCPServer extends Thread
{
	Socket server;
	public TCPServer(Socket socket){
		server=socket;
	}	
	synchronized public void run(){    	
		try {		
			/*
			 * Get the port number required to start server from "config.property file"
			 */
		    Properties property=new Properties();
			try {
				property.load(new FileInputStream(new File("../config.property")));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			/*
			 * Total requests accepts by server from client are "iteration". 
			 * 
			 */
	       int iteration=Integer.parseInt(property.getProperty("Iteration"));
			
			InputStream is = server.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			Object inputReceivedFromServer =ois.readObject();

			if(inputReceivedFromServer instanceof String){
	         	//System.out.println("\nMessage from client: "+(String)inputReceivedFromServer);
			}else{
				System.out.println("\nOutput of different type ");
			}			
			 OutputStream outfromServer = server.getOutputStream();
		     ObjectOutputStream oos;
	  	     oos = new ObjectOutputStream(outfromServer);	  	     
	         oos.writeObject(inputReceivedFromServer);		    	          
			  for(int k=0;k<(iteration+2);k++){
					 inputReceivedFromServer =ois.readObject();
					 if(inputReceivedFromServer instanceof String){
				        	//System.out.println("\nSecond message from client: "+(String)inputReceivedFromServer);
					  }else{
						System.out.println("\nOutput of different type ");
					  }
					 oos.writeObject(inputReceivedFromServer);	        	 
		         }
			  //System.out.println("Done");
	         server.close();	   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}							
	}
}
