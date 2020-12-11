#include <stdio.h>
#include <stdlib.h>
#include <netdb.h>
#include <netinet/in.h>
#include <string.h>
#include <stdio.h>//perror
#include <sys/types.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <signal.h>

int calcul(){
	return 10;
}

int val = calcul();

void handler(int sig)
{
	val += 5;
}
int main()
{
	printf("%d\n",val );
  // 	printf("val %d\n", val);
	// pid_t pid;
	// signal(SIGCHLD, handler);
	// printf("val2 %d\n", val);
	// sleep(2);
  // if ((pid = fork()) == 0)
	// {
  //   printf("val av- %d\n", val);
	// 	val -= 3;
  //   printf("val ap- %d\n", val);
	// 	exit(0);
	// }
  // printf("val av wait %d\n", val);
	// waitpid(pid, NULL, 0);
  // printf("val ap wait %d\n", val);
	// printf("val = %d\n", val);
	exit(0);
}
