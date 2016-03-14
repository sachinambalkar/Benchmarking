

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;


public class DiskThread extends Thread
{
	
	Class abc;
	
	//This is folder-name where all files which are crated from write operations will be saved.
	//Read operation will read file from this folder.
	
	String fileFolderName="CreatedFile";
	TimeRequired timeRequired=new TimeRequired();
	public DiskThread(){	
	}	
	synchronized public void run() {		
		//1B =  1  
		//1KB = 1024
		//1MB = 1024*1024		
	    Properties property=new Properties();
		try {
			property.load(new FileInputStream(new File("../config.property")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
       int totalFileSizeInBytes=Integer.parseInt(property.getProperty("totalFileSizeInBytes"));
		
       
       
		timeRequired.totalSizeOfData=totalFileSizeInBytes;
		
		/*
		 * This is sequential write function.
		 */
		timeRequired.writeTimeBYTE=writeFile(1,totalFileSizeInBytes,"BYTE");
		timeRequired.writeTimeKBYTE=writeFile(1024,totalFileSizeInBytes,"KBYTE");
		timeRequired.writeTimeMBYTE=writeFile(1024*1024,totalFileSizeInBytes,"MBYTE");

		/*
		 * This is sequential read function.
		 */
		timeRequired.readTimeBYTE=readRegular(1,totalFileSizeInBytes,"BYTE");
		timeRequired.readTimeKBYTE=readRegular(1024,totalFileSizeInBytes,"KBYTE");
		timeRequired.readTimeMBYTE=readRegular(1024*1024,totalFileSizeInBytes,"MBYTE");
		
		/*
		 * This is random write function.
		 */
		timeRequired.randomWriteTimeBYTE=writeFileRandom(1,totalFileSizeInBytes,"BYTE_RND");
		timeRequired.randomWriteTimeKBYTE=writeFileRandom(1024,totalFileSizeInBytes,"KBYTE_RND");
		timeRequired.randomWriteTimeMBYTE=writeFileRandom(1024*1024,totalFileSizeInBytes,"MBYTE_RND");
	
		/*
		 * This is random read function.
		 */
		timeRequired.randomReadTimeBYTE=readRandom(1,totalFileSizeInBytes,"BYTE");
		timeRequired.randomReadTimeKBYTE=readRandom(1024,totalFileSizeInBytes,"KBYTE");
		timeRequired.randomReadTimeMBYTE=readRandom(1024*1024,totalFileSizeInBytes,"MBYTE");
}
	
	public String toString() {
		timeRequired.toString();
		return super.toString();
	}
	
	/*
	 * This function return time required for all operations. 
	 */
	public TimeRequired getTimeRequired(){
		return timeRequired;
	}
	
	/*
	 * This is regular read operation.
	 * Parameters:
	 * 	1. blocksize = Size of a block required to read
	 *  2. totalFileSizeInBytes = This is total size of a file required to read in bytes.
	 *  3. filePrefix = This is file name which will be read from disk.  
	 */
	long readRegular(int blockSize,int totalFileSizeInBytes,String filePrefix){		
		long end=0,start=0;
		int offset = 0;
		long length = 0;
			try{
				File file=new File(fileFolderName+"/"+filePrefix+".txt");
				InputStream insputStream = new FileInputStream(file);				
				byte[] bytes = new byte[blockSize];
				length = file.length();
				start = System.nanoTime() ;
				while((length-offset)<blockSize){
					insputStream.read(bytes, offset,(int)((length-offset)>blockSize?blockSize:(length-offset)));
					offset=offset+blockSize;				
				}
				insputStream.close();		
				end = System.nanoTime();				
			}catch(Exception e){
				System.out.println("offset: "+offset+" length: "+length+" Errro "+(int)((length-offset)>blockSize?blockSize:(length-offset)));
				e.printStackTrace();
			}
		return end-start;
	}

	
	/*
	 * This is regular write operation.
	 * Parameters:
	 * 	1. blocksize = Size of a block required to write.
	 *  2. totalFileSizeInBytes = This is total size (bytes) of a file required to write in disk.
	 *  3. filePrefix = This is file name which will be read from disk.  
	 */
	long writeFile(int blockSize,int totalFileSize,String filePrefix){
		long end=0,start=0;
		try {			
			if(!new File(fileFolderName).exists()){
				new File(fileFolderName).mkdir();
			}				
			byte[] bytez=new byte[blockSize];
			for(int i=0;i<blockSize;i++){
				bytez[i]='a';
			}
			String filename = fileFolderName+"/"+filePrefix + ".txt";
			new File(filename).createNewFile();
			BufferedOutputStream bos = null;
			FileOutputStream fos = new FileOutputStream(new File(filename));
			bos = new BufferedOutputStream(fos);			
			start = System.nanoTime();
			for (int i = 0; i <= (totalFileSize/blockSize); i++){
					bos.write(bytez);								
		    }				
		    end = System.nanoTime();
		    bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return end-start;
	}

	
	/* This is random write operation.
	 * Parameters:
	 * 	1. blocksize = Size of a block required to write.
	 *  2. totalFileSizeInBytes = This is total size (bytes) of a file required to write in disk.
	 *  3. filePrefix = This is file name which will be read from disk.  
	 */	
	
	long writeFileRandom(int blockSize,int totalFileSize,String filePrefix){		
		Random random=new Random();
		long end=0,start=0;
			try {
				String file = fileFolderName+"/"+filePrefix+".txt";				
				new File(file).createNewFile();
	            RandomAccessFile fileStore = new RandomAccessFile(file, "rw");
	            
				byte[] bytez=new byte[blockSize];
				for(int i=0;i<blockSize;i++){
					if(i%2==0)
						bytez[i]='a';
					else
						bytez[i]='b';
				}	               
				start=System.nanoTime();
				long iteration;

				if(totalFileSize<=blockSize)
					iteration=1;
				else
					iteration=(totalFileSize/blockSize);
					
	            for(int i=0;i<iteration;i++){
		            fileStore.seek(random.nextInt((totalFileSize-blockSize)>0?(totalFileSize-blockSize):blockSize));
		            fileStore.write(bytez);					
	            }
	            end=System.nanoTime();
	            fileStore.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }		
		return end-start;
	}
	
	
	/*
	 * This is random read operation.
	 * Parameters:
	 * 	1. blocksize = Size of a block required to read
	 *  2. totalFileSizeInBytes = This is total size of a file required to read in bytes.
	 *  3. filePrefix = This is file name which will be read from disk.  
	 */	
	long readRandom(int blockSize,int totalFileSize,String filePrefix){		
		long end=0,start=0;
		start = System.nanoTime() ;
			try{
				RandomAccessFile randomFile = 
				new RandomAccessFile(fileFolderName+"/"+filePrefix+".txt", "rw");
				if(!(new File(fileFolderName+"/"+filePrefix+".txt")).exists()){
					System.out.println("File does not exist !!!");
				}
				else{
						int fileSize=(int)randomFile.length();						
						Random random=new Random();
						byte []b=new byte[blockSize];
						for(int iterate = 0; iterate <(fileSize/blockSize); iterate++){
							 randomFile.seek(random.nextInt((fileSize-blockSize)>0?(fileSize-blockSize):blockSize));
							 randomFile.read(b); 
						}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		end = System.nanoTime();		
		return end-start;
	}

	
	/*
	 * 	This function will return string of required size.
	 * Parameter:
	 * 		1. Size => Size of string. 
	 */
	String getString(int size)
	{	
		String completeString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		StringBuilder stringBuffer = new StringBuilder(size);
		for(int index=0;index<size;index++ ) 
		stringBuffer.append(completeString.charAt(random.nextInt(completeString.length()) ) );		
		return stringBuffer.toString();
	}
	
}
