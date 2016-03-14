#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <string.h>
double OPERATION_COUNT=1000000000;
static int fileCounter=0;
void * cpmBenchmarking();
static int ip=0;
main()
{
    int choice,i;
    double time;
    void *res;
    printf("Perform CPU Benchmarking on \n1. 1 Thread\n2. 2 Thread\n3. 4 Thread : ");
    scanf("%d",&choice);
 
   printf("\nTotal number of operations to be performed are %lf\n",OPERATION_COUNT);   

  if(choice>0&&choice<4){
    	pthread_t thread[choice];	
	if(choice==3)
		choice=4;
        double *val=(double *)malloc(sizeof(double)*choice*2);
	for(i=0;i<choice;i++){
		pthread_create(&thread[i], NULL, cpmBenchmarking,(void *) (val+(i*2)));			
	}
	for(i=0;i<choice;i++)
	        pthread_join(thread[i], NULL);

	double averageFLOPstime=0,averageIOPStime=0;
	for(i=0;i<choice;i++)
        {
	        printf("\nThread %d => FLOPS: %f",i,*(val+(i*2)));
	        printf(", IOPS: %f",*(val+(i*2)+1));
			
		averageFLOPstime=averageFLOPstime+*(val+(i*2));
		averageIOPStime=averageIOPStime+*(val+(i*2)+1);
	}
	averageFLOPstime=averageFLOPstime/choice;
	averageIOPStime=averageIOPStime/choice;



	//Printing Values for FLOPS and Giga FLOPS
	printf("\n\nFloating point operations per second (FLOPS) performed are   : %lf ", (averageFLOPstime/OPERATION_COUNT));
	double number_of_FLOPS = OPERATION_COUNT / averageFLOPstime;
	printf("\nNumber of FLOPS performed per sec      : %lf",number_of_FLOPS);
	double number_of_GFLOPS = number_of_FLOPS / 1000000000 ;
	printf("\nNumber of Giga FLOPS performed per sec : %.10lf\n",number_of_GFLOPS);

	printf("\nInteger operations per second (IOPS) performed are          : %f ", (averageIOPStime/OPERATION_COUNT));
	double number_of_IOPS = OPERATION_COUNT / averageIOPStime;
	printf("\nNumber of IOPS performed per sec      : %f",number_of_IOPS);
	double number_of_GIOPS = number_of_IOPS / 1000000000 ;
	printf("\nNumber of Giga IOPS performed per sec : %.10lf",number_of_GIOPS);



    }else
    {
     	printf("Invalid choice...!!!");
    }
     exit(EXIT_SUCCESS);
}

void* cpmBenchmarking(void *t){
	double *para;
	clock_t startflops_time,flops_time,iops_time,startiops_time;        
	double i=0;
	double itime,ftime,sum=0.0,sum2=0.0,sum3=0.0;
	double isum=0,isum2=0,isum3=0;	
	int flopsData[600],iopsData[600];
        FILE *fp;
	para=(double*)t;
	startflops_time = clock();
	flops_time=clock();
	int time=1;int previous=0;

	for(i=0;i<OPERATION_COUNT&&time<600;i++){
		sum=(sum3+1.99)*(1.90+sum3);
		sum2=sum2*sum3*100000;
		sum3=sum*sum2*sum3;
		if(time<600){
			if(((double)(clock()-flops_time)/(double)CLOCKS_PER_SEC)>=1){
				flopsData[time-1]=i;
				//printf("%d sec => %lf\t",time,(i-previous));
				time++;
				flops_time = clock();
				previous=i;
			}
		}
	}
	//printf("diff time= %lf",(double)(clock()-flops_time)/(double)CLOCKS_PER_SEC);
	//printf("clockpersec = %lf",(double)CLOCKS_PER_SEC);

	ftime=(double)(clock()-startflops_time)/(double)CLOCKS_PER_SEC;
	(*para)=ftime;
//	printf("\n%d FLOPS time: %f",ip,ftime);

	time=1;previous=0;
	startiops_time = clock();
	iops_time= clock();
	for(i=0;i<OPERATION_COUNT&&time<600;i++){
		isum=(isum3+1.99)*(1.90+isum3);
		isum2=isum2*isum3*100000;
		isum3=isum*isum2*isum3;
		if(time<600){
			if(((double)(clock()-iops_time)/(double)CLOCKS_PER_SEC)>=1){
				//printf("%d sec => %lf\t",time,(i-previous));
				iopsData[time-1]=i;
				time++;
				iops_time = clock();
				previous=i;				
			}
		}

	}
	itime=(double)(clock()-startiops_time)/(double)CLOCKS_PER_SEC;
	(*(para+1))=itime;
//	printf("\tMAIN %d FLOPS time: %f IOPS time: %f\n",ip,ftime,itime);
	ip++;

	char filename1[10],filename2[10];
	switch(fileCounter){
		case 0: strcpy(filename1,"flops1.txt");
			strcpy(filename2,"iops1.txt");
			break;
		case 1:strcpy(filename1,"flops2.txt");
			strcpy(filename2,"iops2.txt");
			break;
		case 2:strcpy(filename1,"flops3.txt");
			strcpy(filename2,"iosps3.txt");
			break;
		case 3:strcpy(filename1,"flops4.txt");
			strcpy(filename2,"iops4.txt");
			break;
		default: printf("Error");
			break;
	}
	fileCounter++;
	
	  if((fp=fopen(filename1, "ab+"))==NULL) {
	    printf("Cannot open file.\n");
	  }
	   int digit;
	   for (digit = 0; digit < time-1; ++digit){
		char arr[sizeof(flopsData[digit])];
		snprintf(arr, 12, "%d", flopsData[digit]);
	        fputs(arr,fp);
	        fputs("\n",fp);

	   }
	 fclose(fp);


	  if((fp=fopen(filename2, "ab+"))==NULL) {
	    printf("Cannot open file.\n");
	  }
	   for (digit = 0; digit < time-1; ++digit)
	   {
		char arr[sizeof(iopsData[digit])];
		snprintf(arr, 12, "%d", iopsData[digit]);
	        fputs(arr,fp);
	        fputs("\n",fp);
	   }
	 fclose(fp);



	return NULL;
}

