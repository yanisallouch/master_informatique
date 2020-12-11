#include <string.h>
#include <stdio.h>//perror
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include <iostream>
#include <pthread.h>

int cptTh=0;
pthread_mutex_t v0 =PTHREAD_MUTEX_INITIALIZER;

struct Tparam{
int *tab;
int nbzonne;
int *d;
int nbThread;
pthread_mutex_t v1,v2;
pthread_cond_t c;
};

void init(struct Tparam *p){
	p->tab=(int*)malloc((p->nbzonne)*sizeof(int));
	for (int i = 0; i < p->nbzonne; ++i)
	{
		p->tab[i]=0;
	}
	p->d=(int*)malloc((p->nbThread)*sizeof(int));
	for (int i = 0; i < p->nbThread; ++i)
	{
		p->d[i]=0;
	}
	pthread_mutex_init(&p->v1,NULL);
	pthread_mutex_init(&p->v2,NULL);
	pthread_cond_init(&p->c,NULL);
}

void traiterZonne(struct Tparam *p, int i,int mynum){
 
 		p->tab[i] =p->tab[i]+1;

 		printf("thread n° %d zonne =%d\n",mynum,i);
 	//	sleep(2);
 		int cacl=0;
 		for (int i = 0; i < 1000000; ++i)
 		{
 			cacl=cacl+1*500/93%1000;
 		}

}


void *functionThread(void *param){

	pthread_mutex_lock(&v0);
	int mynum =cptTh;
	cptTh++;
	pthread_mutex_unlock(&v0);

	struct Tparam *p =(struct Tparam *) param;

	if (mynum==0)
	{
		for (int i = 0; i < p->nbzonne; ++i)
		{
			pthread_mutex_lock(&p->v1);
			traiterZonne(p,i,mynum);
			pthread_mutex_unlock(&p->v1);
			pthread_mutex_lock(&p->v2);
			p->d[mynum]=p->d[mynum]+1;
			pthread_cond_broadcast(&p->c);
			pthread_mutex_unlock(&p->v2);
		}
	}else{

		for (int i = 0; i < p->nbzonne; ++i)
		{
			pthread_mutex_lock(&p->v2);
			while (p->d[mynum]>=p->d[mynum -1])
			{
				pthread_cond_wait(&p->c,&p->v2);
			}
			pthread_mutex_unlock(&p->v2);
			pthread_mutex_lock(&p->v1);
			traiterZonne(p,i,mynum);
			pthread_mutex_unlock(&p->v1);
			pthread_mutex_lock(&p->v2);
			p->d[mynum]=p->d[mynum]+1;
			pthread_cond_broadcast(&p->c);
			pthread_mutex_unlock(&p->v2);
		}
	}
	//pthread_exit(0);

}

int main(int argc, char const *argv[])
{
	int nbz=0,nbt=0, *ret;
	printf("inserez le nombre de zonnes \n");
	scanf("%d",&nbz);
	printf("inserez le nombre de threads \n");
	scanf("%d",&nbt);
	ret=(int*)malloc(sizeof(int));

	pthread_t *threads;
	threads=(pthread_t*)malloc(nbt*sizeof(pthread_t));

	struct Tparam *param =(struct Tparam*)malloc(sizeof(Tparam));
	param->nbzonne=nbz;
	param->nbThread=nbt;
	init(param);
	for (int i = 0; i < nbt; ++i)
	{
		if (pthread_create(&threads[i],NULL,functionThread,param)!=0)
		{
			perror("erreur creation thread");
     		exit(1);
		}
	}

	
	for (int i = 0; i < nbt; ++i)
	{
		pthread_join(threads[i],(void**)&ret);
	}

	return 0;
}