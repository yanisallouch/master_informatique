#include <string.h>
#include <stdio.h>//perror
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include <iostream>
#include <pthread.h>



// RMQ V2 c'est pour le numéro d'ordre ne l'utilise pas

struct strTImage{
	int *tab;
	int nbzonne;
	pthread_mutex_t v,v2;
	pthread_cond_t c;
};
 typedef struct strTImage tImage;

struct t_param{
	int num_ordre;
	int *var1,*var2;
	tImage *img;
	
};
void init(tImage *img,int nb){
	img->nbzonne =nb;
	img->tab=(int*)malloc(img->nbzonne * sizeof(int));
	pthread_mutex_init(&img->v,NULL);
	pthread_mutex_init(&img->v2,NULL);
	pthread_cond_init(&img->c,NULL);
	
}

void traiter_zonne(tImage *img,int pos, int mynum){
	printf("thread num %d zonne = %d \n",mynum,pos);
	img->tab[pos]++;
}

void * function_thread(void * args)
{
	
	struct t_param *p=(struct t_param*)args;

	
	if(p->num_ordre==0){
			

		for (int i = 0; i < p->img->nbzonne; ++i)
		{

			pthread_mutex_lock(&(p->img->v));
			traiter_zonne(p->img,i,p->num_ordre);
			*(p->var1)=*(p->var1)+1;
			pthread_cond_broadcast(&(p->img->c));
			/*int b;
			for (int j = 0; j < 10000000; ++j)
			{
				b=b+5*69/2;
			}*/
			pthread_mutex_unlock(&(p->img->v));
			
		}
	}else{
			for (int i = 0; i < p->img->nbzonne; ++i)
			{
				pthread_mutex_lock(&p->img->v);
				if(*(p->var2)==*(p->var1))
				{
					pthread_cond_wait(&p->img->c,&p->img->v);
				}
				traiter_zonne(p->img,i,p->num_ordre);
				*(p->var2)=*(p->var2)+1;
				pthread_mutex_unlock(&p->img->v);
			}
		}
		free(p);
		pthread_exit(0);
}
int main(int argc, char const *argv[])
{
	int nb=0;
	printf("inserez le nombre de zonne svp\n");
	scanf("%d",&nb);


	tImage img;
	init(&img,nb);

	struct t_param *p;
	
	

	pthread_t *threads;
	threads=(pthread_t *)malloc(2*sizeof(pthread_t));
	for (int i = 0; i < 2; ++i)
	{
		p=(struct t_param*)malloc(1*sizeof(struct t_param));
		p->num_ordre =i;
		p->img=&img;
		p->var1=(int*)malloc(sizeof(int));
		p->var2=(int*)malloc(sizeof(int));
		*(p->var1)=0;
		*(p->var2)=0;
		pthread_create(&threads[i],NULL,function_thread,p);
	}

	int *ret;
	ret=(int*)malloc(sizeof(int));
	for (int i = 0; i < 2; ++i)
	{
		pthread_join(threads[i],(void**)ret);
	}
	return 0;
}