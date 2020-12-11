#include "commun.h"

// lire le fichier commun.h pour la documentation et la structure utilisée

int jetonsEtape_init(JetonsEtape * v, int k){
	int res = 0;

	v->max = k;
	v->nbJetonPris = 0;
	res = pthread_mutex_init(&(v->mut), NULL);
	if(res != 0){
		
		exit(1);
	}
	
	res = pthread_cond_init(&(v->cond), NULL);
	if(res != 0){
		
		exit(1);
	}

	return res;
}

int demande_jeton(JetonsEtape * v){

	int res = 0;
	
	res = pthread_mutex_lock(&(v->mut));
	if(res != 0){
		
		exit(-1);
	}
	// session critique début

	int nbMaxDeJoueurs = v->max;
	int nbDeJoueursEnCours = v->nbJetonPris;
	
	if( (nbMaxDeJoueurs - nbDeJoueursEnCours) >= 1 ){
		v->nbJetonPris = v->nbJetonPris + 1;
	}else{
		pthread_cond_wait(&(v->cond),&(v->mut));
		res = pthread_mutex_unlock(&(v->mut));
		// révéil par un autre thread
		// relance le travail de demande_jeton()
		res = demande_jeton(v);
		if(res != 0){
			
			exit(-1);
		}
	}

	// session critique fin
	res = pthread_mutex_unlock(&(v->mut));
	if(res != 0){
		
		exit(-1);
	}


	return res;
}

int liberer_jeton(JetonsEtape * v){

	int res = 0;
	
	res = pthread_mutex_lock(&(v->mut));
	if(res != 0){
		
		exit(-1);
	}
	// session critique début

	v->nbJetonPris = v->nbJetonPris - 1;
	
	res = pthread_cond_broadcast(&(v->cond));
	if(res != 0){
		
		exit(-1);
	}

	// session critique fin
	res = pthread_mutex_unlock(&(v->mut));
	if(res != 0){
		
		exit(-1);
	}

	return res;
}

int jetonsEtape_destroy(JetonsEtape * v){

	int res = 0;

	res = pthread_mutex_unlock(&(v->mut));
	/* Je force le dévérrouillage du mutex au cas ou ! Cela dis :
	Je peux faire ça, parce que la documentation dans commun.h me garantit que cette fonction
	est éxécuté a la fin de chaque thread et que donc aucun thread ne sera impacté par cette action !
	*/
	if(res != 0){
		
		exit(-1);
	}

	res = pthread_mutex_destroy(&(v->mut));
	if(res != 0){
		
		exit(-1);
	}

	res = pthread_cond_destroy(&(v->cond));
	if(res != 0){
		
		exit(-1);
	}

	return res;
}