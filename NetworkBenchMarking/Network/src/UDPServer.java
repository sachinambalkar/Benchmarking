
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Properties;


public class UDPServer extends Thread
{
	DatagramPacket datagramPacket;
	DatagramSocket datagramSocket;
	int datasize;
	public UDPServer( DatagramPacket datagramPacket,DatagramSocket datagramSocket,int datasize) {
		this.datagramPacket=datagramPacket;
		this.datagramSocket=datagramSocket;
		this.datasize=datasize;
	}
	
	/*  UDP server will run continuously to give service to UDP client.
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	
	public void run(){		

		
		/*
		 * Take UDP server port number from config.property file.
		 */
		    int increase=0;
		    Properties property=new Properties();
			try {
				property.load(new FileInputStream(new File("../config.property")));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
  			int dsize=Integer.parseInt(property.getProperty("Datasize"));
  			int loopsize=Integer.parseInt(property.getProperty("Iteration"));
  			if(datasize==dsize)
  				increase=loopsize;

			
	while(true){	
		try{			
			datagramSocket.receive(datagramPacket);
		 //   System.out.println("Received data..!!!");
	        byte[] data = datagramPacket.getData();
	        String s = new String(data, 0, datagramPacket.getLength());
	    //    System.out.println(datagramPacket.getAddress().getHostAddress() + " : " + datagramPacket.getPort() + " - " + s);
	        s = "OK : " + s;
	        DatagramPacket dp = new DatagramPacket(s.getBytes() , s.getBytes().length , datagramPacket.getAddress() , datagramPacket.getPort());
	        datagramSocket.send(dp);		        		  

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	}
}
