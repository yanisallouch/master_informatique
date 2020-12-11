GXX=gcc
FLAGS=-Wall -Wextra -pedantic -std=c99
SRC=$(wildcard *.c)
BIN=$(basename $(SRC))


all: $(BIN)

.c:
	$(GXX) $(FLAGS) -o $@ $<


clean:
	$(RM) *~ $(BIN)
	@clear
