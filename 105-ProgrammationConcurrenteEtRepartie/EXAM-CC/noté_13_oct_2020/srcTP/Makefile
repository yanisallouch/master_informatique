########################################
#~ définitions
########################################

# nom de l'executable
BIN=./bin/jeu

# liste des fichiers sources a completer 
SRCS=afaire.c

default: $(BIN)

########################################
#~ regles pour l'executable
########################################

obj/%.o: %.c
	gcc -Wall -Iinclude -c $< -o $@

bin/jeu: $(SRCS:%.c=obj/%.o)
	gcc  -o $@ obj/jeu.o $+ -lpthread

clean:
	rm -f $(BIN) obj/afaire.o *~
