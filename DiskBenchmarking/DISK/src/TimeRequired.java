
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Properties;
 
public class TimeRequired {
    static int totalSizeOfData;
    public double readTimeBYTE,readTimeKBYTE,readTimeMBYTE,
    randomReadTimeBYTE,randomReadTimeKBYTE,randomReadTimeMBYTE,
        writeTimeBYTE,writeTimeKBYTE,writeTimeMBYTE,
        randomWriteTimeBYTE,randomWriteTimeKBYTE,randomWriteTimeMBYTE;
    static{
        Properties property=new Properties();
        try {
            property.load(new FileInputStream(new File("../config.property")));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        totalSizeOfData=Integer.parseInt(property.getProperty("totalFileSizeInBytes"));
    }
    public String toString() {
         
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(10);
         
        System.out.println("Total size of data : "+totalSizeOfData+"Bytes ");
         double timeConstant=1000,ltimeConstant=1;
        System.out.println("Sequential Read Operation =>");

        System.out.println("1.  1-BYTE block  => Latency: "+df.format((readTimeBYTE*1000)/(totalSizeOfData*ltimeConstant))+" msec , Throughput =>   "+ ((totalSizeOfData/(readTimeBYTE*1000))*timeConstant)+" mb/second");
        System.out.println("2.  1-KBYTE block => Latency: "+df.format((readTimeKBYTE)/(totalSizeOfData*ltimeConstant))    +" msec , Throughput =>   "+ ((totalSizeOfData/(readTimeKBYTE*1000))*timeConstant)+" mb/second");
        System.out.println("3.  1-MBYTE block => Latency: "+df.format((readTimeMBYTE)/(totalSizeOfData*ltimeConstant))    +" msec , Throughput =>   "+((totalSizeOfData/(readTimeMBYTE*1000))*timeConstant)+" mb/second"); 
        System.out.println("Random Read Operation =>");
        System.out.println("1.  1-BYTE block  => Latency: "+df.format((randomReadTimeBYTE)/(totalSizeOfData*ltimeConstant))  +" msec , Throughput  =>   "+ ((totalSizeOfData/randomReadTimeBYTE)*timeConstant)+" mb/second");
        System.out.println("2.  1-KBYTE block => Latency: "+df.format((randomReadTimeKBYTE)/(totalSizeOfData*ltimeConstant)) +" msec , Throughput  =>   "+ ((totalSizeOfData/randomReadTimeKBYTE)*timeConstant)+" mb/second");
        System.out.println("3.  1-MBYTE block => Latency: "+df.format((randomReadTimeMBYTE)/(totalSizeOfData*ltimeConstant)) +" msec , Throughput  =>   "+ ((totalSizeOfData/randomReadTimeMBYTE)*timeConstant)+" mb/second");
 
        System.out.println("\nSequential Write Operation =>");
        System.out.println("1.  1-BYTE block  => Latency: "+df.format((writeTimeBYTE)/(totalSizeOfData*ltimeConstant)) +" msec , Throughput =>   "+ ((totalSizeOfData/writeTimeBYTE)*timeConstant)+" mb/second");
        System.out.println("2.  1-KBYTE block => Latency: "+df.format((writeTimeKBYTE)/(totalSizeOfData*ltimeConstant))+" msec , Throughput =>   "+ ((totalSizeOfData/writeTimeKBYTE)*timeConstant)+" mb/second");
        System.out.println("3.  1-MBYTE block => Latency: "+df.format((writeTimeMBYTE)/(totalSizeOfData*ltimeConstant))+" msec , Throughput =>   "+ ((totalSizeOfData/writeTimeMBYTE)*timeConstant)+" mb/second");
         
        System.out.println("Random Write Operation =>");
        System.out.println("1.  1-BYTE block  => Latency: "+df.format((randomWriteTimeBYTE)/(totalSizeOfData*ltimeConstant))  +" msec ,Throughput =>   "+ ((totalSizeOfData/randomWriteTimeBYTE)*timeConstant)+" mb/second");
        System.out.println("1.  1-KBYTE block => Latency: "+df.format((randomWriteTimeKBYTE)/(totalSizeOfData*ltimeConstant)) +" msec ,Throughput =>   "+ ((totalSizeOfData/randomWriteTimeKBYTE)*timeConstant)+" mb/second");
        System.out.println("1.  1-MBYTE block => Latency: "+df.format((randomWriteTimeMBYTE)/(totalSizeOfData*ltimeConstant)) +" msec ,Throughput =>   "+ ((totalSizeOfData/randomWriteTimeMBYTE)*timeConstant)+" mb/second");
         
        return super.toString();
    }
 
}

