

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;


public class Main implements Runnable
{
	UDPServer udpServer[] = new UDPServer[3];
	TimeRequired timeRequired=new TimeRequired();
	static int clientCount;
	UDPClient[] udpClient;
	static TCPClient threadClient[];
	public Main() {
		// TODO Auto-generated constructor stub
	}	
	public static void main(String args[]){
		try{
        	Thread thread=new Thread(new Main());
        	thread.start();			
		} catch (Exception e) {
			e.printStackTrace();
		}                   
		
		/*
		 * Select number of thread to start
		 */
		System.out.println("\nPerform Network Benchmarking operation using number thread :\n1. 1 Thread\n2. 2 Threads : ");
		Scanner sc=new Scanner(System.in);
		clientCount=Integer.parseInt(sc.nextLine());	
		threadClient=new TCPClient[clientCount];
				
		
		switch(clientCount){
			case 1: //System.out.println("Selected 1 client");
					threadClient[0]=new TCPClient("Thread 1");
					threadClient[0].start();					
					break;
					
			case 2: //System.out.println("Selected 2 Client");
					threadClient[0]=new TCPClient("Thread 1");
					threadClient[0].start();
					threadClient[1]=new TCPClient("Thread 2");
					threadClient[1].start();
					break;
			default: System.out.println("Invalid choice");
					break;
		}		
		
	}
	public void run(){
		/*
		 * Get the server-ip address and port number from property file.
		 */
	    Properties property=new Properties();
		try {
			property.load(new FileInputStream(new File("../config.property")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
       int serverPort=Integer.parseInt(property.getProperty("ServerPort"));		
	        ServerSocket serverSocket;int count=0;	    				
			try {
				serverSocket = new ServerSocket(serverPort);				
		        while(true){
					try {
	 					  Socket server= serverSocket.accept();
					      TCPServer tcpServer=new TCPServer(server);
					      tcpServer.start();
					      count++;
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(count==clientCount)
						break;					
		        }
		        
		        int serverPortUDP=Integer.parseInt(property.getProperty("ServerPortUDP"));
		        DatagramSocket sock = null;
		        udpClient=new UDPClient[clientCount];
		        sock = new DatagramSocket(serverPortUDP);
      	        
					if(clientCount==1||clientCount==2){
					udpClient[0]=new UDPClient(threadClient[0].getTimeRequired());
					udpClient[0].start();
					}
					if(clientCount==2){
					udpClient[1]=new UDPClient(threadClient[1].getTimeRequired());
					udpClient[1].start();
					}															

		/*
		 * Starts three UDP-server each for packet size 1Byte, 1KByte and 64KByte
		 */
		        {
		        	try{
		                byte[] buffer = new byte[1];
		                DatagramPacket[] incoming=new DatagramPacket[3]; 
		                
		                incoming[0]= new DatagramPacket(buffer, buffer.length);
		                udpServer[0]=new UDPServer(incoming[0],sock,buffer.length);		        		  
		      		    udpServer[0].start();	  	

		      		    buffer = new byte[1024];
		                incoming[1] = new DatagramPacket(buffer, buffer.length);
		                udpServer[1]=new UDPServer(incoming[1],sock,buffer.length);		        		  
		                udpServer[1].start();	  	

		                buffer = new byte[64000];
		                incoming[2] = new DatagramPacket(buffer, buffer.length);
		                udpServer[2]=new UDPServer(incoming[2],sock,buffer.length);		        		  
		      		    udpServer[2].start();	  	

		        	}catch(Exception e){
		        		System.err.println("Error occured");
		        		e.printStackTrace();		        		
		        	}
		        }		        
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			try{			
				count=0;
				while(count!=clientCount){
					udpClient[count].join() ;
						count++;
			    }
			}catch(InterruptedException e){				
				e.printStackTrace();
			}
			try{			
				count=0;
				while(count!=clientCount){
					threadClient[count].join() ;
						count++;
			    }
			}catch(InterruptedException e){				
				e.printStackTrace();
			}

			
			if(clientCount>1){
				threadClient[0].timeRequired=merge(threadClient[0].getTimeRequired(),udpClient[0].getTimeRequired());
				threadClient[1].timeRequired=merge(threadClient[1].getTimeRequired(),udpClient[1].getTimeRequired());
				new Main().averageTimeRequired(threadClient[0].getTimeRequired(),threadClient[1].getTimeRequired());
			}else{
				threadClient[0].timeRequired=merge(threadClient[0].getTimeRequired(),udpClient[0].getTimeRequired());
				System.out.println(threadClient[0].getTimeRequired());
			}							

	}
	
	/*
	 * This function will merge the time required for UDP operation and TCP operation into single time object.
	 */
	
	TimeRequired merge(TimeRequired timeRequiredTCP,TimeRequired timeRequiredUDP){
		timeRequiredTCP.udpTimeBYTE=timeRequiredUDP.udpTimeBYTE;
		timeRequiredTCP.udpTimeKBYTE=timeRequiredUDP.udpTimeKBYTE;
		timeRequiredTCP.udpTime64KBYTE=timeRequiredUDP.udpTime64KBYTE;		
		timeRequiredTCP.udpIteration=timeRequiredUDP.udpIteration;		
		return timeRequiredTCP;
	}
	
	
	/*
	 * This function will calculate the average time required for both threads.
	 */
	public void averageTimeRequired(TimeRequired timeRequired1,TimeRequired timeRequired2){	
		TimeRequired timeRequired=new TimeRequired();
		timeRequired.tcpTimeBYTE=(timeRequired1.tcpTimeBYTE+timeRequired2.tcpTimeBYTE)/2;
		//System.out.println((timeRequired1.tcpTimeBYTE+timeRequired2.tcpTimeBYTE)/2);
		//System.out.println(timeRequired.tcpTimeBYTE);
		timeRequired.tcpTimeKBYTE=(timeRequired1.tcpTimeKBYTE+timeRequired2.tcpTimeKBYTE)/2;
		timeRequired.tcpTime64KBYTE=(timeRequired1.tcpTime64KBYTE+timeRequired2.tcpTime64KBYTE)/2;		
		timeRequired.udpTimeBYTE=(timeRequired1.udpTimeBYTE+timeRequired2.udpTimeBYTE)/2;
		timeRequired.udpTimeKBYTE=(timeRequired1.udpTimeKBYTE+timeRequired2.udpTimeKBYTE)/2;
		timeRequired.udpTime64KBYTE=(timeRequired1.udpTime64KBYTE+timeRequired2.udpTime64KBYTE)/2;

		timeRequired.udpIteration=(timeRequired1.udpIteration+timeRequired2.udpIteration)/2;
		timeRequired.tcpIteraiton=(timeRequired1.tcpIteraiton+timeRequired2.tcpIteraiton)/2;

		timeRequired.toString();		
	}	
	
}