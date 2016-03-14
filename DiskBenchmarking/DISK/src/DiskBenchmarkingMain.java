

import java.util.Scanner;


public class DiskBenchmarkingMain 
{
	public static void main(String args[]){
		int threadSelected=0;
		int performOperation=0;		
		
		/*User can select number of threads to start
		 * */
		
		System.out.println("Perform diskBenchmarking operation using number thread :\n1. 1 Thread\n2. 2 Threads : ");
		Scanner sc=new Scanner(System.in);
		threadSelected=Integer.parseInt(sc.nextLine());		
		DiskThread dthread[]=new DiskThread[threadSelected];
		System.out.println("Thread selected are "+threadSelected);
		while(performOperation!=threadSelected){
				dthread[performOperation]=new DiskThread();
				dthread[performOperation].start();
				performOperation++;
		}					
		try{			
				performOperation=0;
				while(performOperation!=threadSelected){
						dthread[performOperation].join();
						//System.out.println(dthread[performOperation]);
					  performOperation++;
			    }
		}catch(InterruptedException e){				
			e.printStackTrace();
		}
		if(threadSelected>1){
			new DiskBenchmarkingMain().averageTimeRequired(dthread[0].getTimeRequired(),dthread[1].getTimeRequired());
		}else{
			System.out.println(dthread[0]);
		}		
	}
	
	/*
	 * This function will give the average value of two Thread's output
	 */
	public void averageTimeRequired(TimeRequired timeRequired1,TimeRequired timeRequired2){	
		TimeRequired timeRequired=new TimeRequired();
		timeRequired.readTimeBYTE=(timeRequired1.readTimeBYTE+timeRequired2.readTimeBYTE)/2;
		timeRequired.readTimeKBYTE=(timeRequired1.readTimeKBYTE+timeRequired2.readTimeKBYTE)/2;
		timeRequired.readTimeMBYTE=(timeRequired1.readTimeMBYTE+timeRequired2.readTimeMBYTE)/2;		
		timeRequired.writeTimeBYTE=(timeRequired1.writeTimeBYTE+timeRequired2.writeTimeBYTE)/2;
		timeRequired.writeTimeKBYTE=(timeRequired1.writeTimeKBYTE+timeRequired2.writeTimeKBYTE)/2;
		timeRequired.writeTimeMBYTE=(timeRequired1.writeTimeMBYTE+timeRequired2.writeTimeMBYTE)/2;
		timeRequired.randomReadTimeBYTE=(timeRequired1.randomReadTimeBYTE+timeRequired2.randomReadTimeBYTE)/2;
		timeRequired.randomReadTimeKBYTE=(timeRequired1.randomReadTimeKBYTE+timeRequired2.randomReadTimeKBYTE)/2;
		timeRequired.randomReadTimeMBYTE=(timeRequired1.randomReadTimeMBYTE+timeRequired2.randomReadTimeMBYTE)/2;		
		timeRequired.randomWriteTimeBYTE=(timeRequired1.randomWriteTimeBYTE+timeRequired2.writeTimeBYTE)/2;
		timeRequired.randomWriteTimeKBYTE=(timeRequired1.randomWriteTimeKBYTE+timeRequired2.writeTimeKBYTE)/2;
		timeRequired.randomWriteTimeMBYTE=(timeRequired1.randomWriteTimeMBYTE+timeRequired2.writeTimeMBYTE)/2;

		timeRequired.toString();		
	}	
}
