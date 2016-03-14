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
	printf("\n\nFloating point operations per second (FLOPS) required are : %lf sec", (averageFLOPstime));
	double number_of_FLOPS = OPERATION_COUNT / averageFLOPstime;
	printf("\nNumber of FLOPS performed per sec      : %lf",number_of_FLOPS);
	double number_of_GFLOPS = number_of_FLOPS / 1000000000 ;
	printf("\nNumber of Giga FLOPS performed per sec : %.10lf\n",number_of_GFLOPS);

	printf("\nInteger operations per second (IOPS) required are : %f sec", (averageIOPStime));
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
	clock_t startflops_time,startiops_time;        
	double i=0;
	double itime,ftime,sum=0.0,sum2=0.0,sum3=0.0;
	double isum=0,isum2=0,isum3=0;	
        FILE *fp;
	para=(double*)t;
	startflops_time = clock();
	int time=1;int previous=0;

	for(i=0;i<OPERATION_COUNT;i++){
		sum=(sum3+1.99)*(1.90+sum3);
		sum2=sum2*sum3*100000;
		sum3=sum*sum2*sum3;
	}
	//printf("diff time= %lf",(double)(clock()-flops_time)/(double)CLOCKS_PER_SEC);
	//printf("clockpersec = %lf",(double)CLOCKS_PER_SEC);

	ftime=(double)(clock()-startflops_time)/(double)CLOCKS_PER_SEC;
	(*para)=ftime;
//	printf("\n%d FLOPS time: %f",ip,ftime);

	time=1;previous=0;
	startiops_time = clock();
	for(i=0;i<OPERATION_COUNT;i++){
		isum=(isum3+1.99)*(1.90+isum3);
		isum2=isum2*isum3*100000;
		isum3=isum*isum2*isum3;
	}
	itime=(double)(clock()-startiops_time)/(double)CLOCKS_PER_SEC;
	(*(para+1))=itime;
//	printf("\tMAIN %d FLOPS time: %f IOPS time: %f\n",ip,ftime,itime);
	ip++;


	return NULL;
}

