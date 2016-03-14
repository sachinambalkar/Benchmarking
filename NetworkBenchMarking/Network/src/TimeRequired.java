

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Properties;

 public class TimeRequired {
	int totalFiles;
	 public double tcpTimeBYTE,tcpTimeKBYTE,tcpTime64KBYTE,
		udpTimeBYTE,udpTimeKBYTE,udpTime64KBYTE,tcpIteraiton,udpIteration;
	  public String toString() {
		  //System.out.println("TCP ITERATI: "+tcpIteraiton);
		  
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(10);
	    Properties property=new Properties();
		try {
			property.load(new FileInputStream(new File("../config.property")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		long datasize=Integer.parseInt(property.getProperty("Datasize"));
		long loopsize=Integer.parseInt(property.getProperty("Iteration"));

		System.out.println("\nTCP Evaluation =>");		
	  	System.out.println("Time required sending "+datasize+"Byte over "+loopsize+" iteration.\nTotal data send is "+(datasize*loopsize)+"Bytes in "+tcpIteraiton);
	  	double executionTime=tcpIteraiton,totalData=datasize*loopsize;
	  	System.out.println("Throughput:  "+df.format((totalData/executionTime)*1000)+" MB/Sec");
	  	System.out.println("Latency   :  "+df.format((executionTime/(totalData)))+" msec");
		System.out.println("\n");

		System.out.println("UDP Evaluation =>");		
	  	System.out.println("Time required sending "+datasize+"Byte over "+loopsize+" iteration.\nTotal data send is "+(datasize*loopsize)+"Bytes in "+udpIteration);
	  	executionTime=udpIteration;totalData=datasize*loopsize;
	  	System.out.println("Throughput:  "+(totalData/executionTime)*1000+" MB/Sec");
	  	System.out.println("Latency   :  "+(executionTime/(totalData))+" msec");
		System.out.println("\n");

		
		
//        df.format(tcpTimeBYTE/1000000000.00)
		int timeConstant=1000;
		System.out.println("TCP Operation time required for communicating with  data of ");
		System.out.println("1.  1-BYTE packet size => Latency: "+((tcpTimeBYTE)/1) +" msec , Throughput =>   "+ ((1/(tcpTimeBYTE))*timeConstant)+" mb/second");
		System.out.println("2.  1-KBYTE packet size=> Latency: "+((tcpTimeKBYTE)/1024) +" msec , Throughput =>   "+ ((1024/(tcpTimeKBYTE))*timeConstant)+" mb/second");
		System.out.println("3. 64-KBYTE packet size=> Latency: "+((tcpTime64KBYTE)/64000) +" msec , Throughput =>   "+ ((64000/(tcpTime64KBYTE))*timeConstant)+" mb/second");
		System.out.println("\nUDP Operation time required for communicating with  data of ");
		System.out.println("1.  1-BYTE packet size=> Latency: "+((udpTimeBYTE)/1) +" msec , Throughput =>   "+ ((1/(udpTimeBYTE))*timeConstant)+" mb/second");
		System.out.println("2.  1-KBYTE packet size=> Latency: "+((udpTimeKBYTE)/1024) +" msec , Throughput =>   "+ ((1024/(udpTimeKBYTE))*timeConstant)+" mb/second");
		System.out.println("3. 64-KBYTE packet size=> Latency: "+((udpTime64KBYTE)/64000) +" msec , Throughput =>   "+ ((64000/(udpTime64KBYTE))*timeConstant)+" mb/second");		
		return super.toString();
	}

}
