###################################################################################################
## How to use :
## 		-make all      = Compilation
## 		-make clean    = delete object files (*.o)
## 		-make cleanall = delete object files (*.o) and the executable
## 		-make tar      = create an archive .tar with all the source code, headers and this makefile
## 		-make zip 	   = create an archive .zip with all the source code, headers and this makefile
## 		-make init	   = init the workspace
## 		-make test 	   = Execute un fichier test
##
###################################################################################################


CXX      = g++
CXXFLAGS = -std=c++2a -Wextra -Wall -pedantic# -g -O2
LDFLAGS  = -lpthread

CXXFLAGS += -Iinc/json/ #json-c
LDFLAGS += -Wl,-Rlib/ -Llib/ -ljson-c

SRCFILE = src
INCFILE = inc
OBJFILE = obj
EXEFILE = .

DIRECTORIES = $(subst $(SRCFILE),$(OBJFILE),$(shell find $(SRCFILE) -type d))

CLIENT = client
SERVER = serveur
TEST = test_cloud # nom du programme de test


SRC     = $(SRCFILE)/cloud.cpp $(SRCFILE)/site.cpp $(SRCFILE)/reservation.cpp $(SRCFILE)/define.cpp $(SRCFILE)/protocol.cpp  # ajout tout les sources annexe au main
INC     = $(wildcard $(INCFILE)/*.hpp) $(wildcard $(INCFILE)/**/*.hpp)
OBJ     = $(SRC:$(SRCFILE)/%.cpp=$(OBJFILE)/%.o)
ARCHIVE_NAME = projet#$(shell date +%D)

ENDCOLOR    = \033[m

REDCOLOR	= \033[0;31m
GREENCOLOR  = \033[0;32m
YELLOWCOLOR = \033[0;33m
BLUECOLOR	= \033[0;34m
PURPLECOLOR = \033[0;35m
CYANCOLOR	= \033[0;36m
GREYCOLOR 	= \033[0;37m

LREDCOLOR	 = \033[1;31m
LGREENCOLOR	 = \033[1;32m
LYELLOWCOLOR = \033[1;33m
LBLUECOLOR   = \033[1;34m
LPURPLECOLOR = \033[1;35m
LCYANCOLOR	 = \033[1;36m
LGREYCOLOR	 = \033[1;37m

OKSTRING   = $(LGREENCOLOR)[SUCCES]$(ENDCOLOR)
WARSTRING  = $(LYELLOWCOLOR)[WARNING]$(ENDCOLOR)
ERRSTRING  = $(LREDCOLOR)[ERROR]$(ENDCOLOR)

all: $(CLIENT) $(SERVER)

$(CLIENT): $(OBJFILE)/client.o $(OBJ)
	@mkdir -p $(EXEFILE)
	@echo "$(LGREENCOLOR)-------------------------------------------------------------------$(ENDCOLOR)"
	@echo "$(LGREENCOLOR)| Linking:    $(ENDCOLOR)$(LYELLOWCOLOR)$^$(ENDCOLOR)"
	@$(CXX) $^ -o $(EXEFILE)/$(CLIENT) $(LDFLAGS)
	@echo "$(LGREENCOLOR)-------------------------------------------------------------------$(ENDCOLOR)"
	@echo "$(LGREENCOLOR)| Executable: $(ENDCOLOR)$(LPURPLECOLOR)$(EXEFILE)/$(CLIENT)$(ENDCOLOR)"

$(SERVER): $(OBJFILE)/serveur.o $(OBJ)
	@mkdir -p $(EXEFILE)
	@echo "$(LGREENCOLOR)-------------------------------------------------------------------$(ENDCOLOR)"
	@echo "$(LGREENCOLOR)| Linking:    $(ENDCOLOR)$(LYELLOWCOLOR)$^$(ENDCOLOR)"
	@$(CXX) $^ -o $(EXEFILE)/$(SERVER) $(LDFLAGS)
	@echo "$(LGREENCOLOR)-------------------------------------------------------------------$(ENDCOLOR)"
	@echo "$(LGREENCOLOR)| Executable: $(ENDCOLOR)$(LPURPLECOLOR)$(EXEFILE)/$(SERVER)$(ENDCOLOR)"

test : $(OBJFILE)/test_code_decode.o $(OBJ)
	@mkdir -p $(EXEFILE)
	@echo "$(LGREENCOLOR)-------------------------------------------------------------------$(ENDCOLOR)"
	@echo "$(LGREENCOLOR)| Linking:    $(ENDCOLOR)$(LYELLOWCOLOR)$^$(ENDCOLOR)"
	@$(CXX) $^ -o $(EXEFILE)/test $(LDFLAGS)
	@echo "$(LGREENCOLOR)-------------------------------------------------------------------$(ENDCOLOR)"
	@echo "$(LGREENCOLOR)| Executable: $(ENDCOLOR)$(LPURPLECOLOR)$(EXEFILE)/test$(ENDCOLOR)"


$(OBJFILE)/%.o: $(SRCFILE)/%.cpp
	@mkdir -p $(DIRECTORIES)
	@printf "%-75b %s" "$(LGREENCOLOR)| Compiling:  $(ENDCOLOR)$(LCYANCOLOR)$<$(ENDCOLOR)"
	@-$(CXX) $(CXXFLAGS) -c $< -o $@ -I $(INCFILE)
	@$(eval COMPILRESULT := $(shell echo $$?))

ifneq ($(COMPILRESULT), 0)
ifneq ($(LOGFILE), "")
	@printf "%-20b" "$(LGREENCOLOR)[SUCCES]  |$(ENDCOLOR)\\n"
else
	@printf "%-20b" "$(LYELLOWCOLOR)[WARNING]|$(ENDCOLOR)\\n"
endif
else
	@printf "%-20b" "$(LREDCOLOR)[ERROR]  |$(ENDCOLOR)\\n"
endif

cleanall: clean
	@rm -f $(EXEFILE)/$(CLIENT) $(EXEFILE)/$(SERVER) $(EXEFILE)/test

clean:
	@rm -f $(OBJFILE)/*.o

init:
	@echo "$(LGREENCOLOR)| Creating workspace:    $(ENDCOLOR)$(LYELLOWCOLOR)$^$(ENDCOLOR)"
	@mkdir -p $(SRCFILE) $(INCFILE) $(OBJFILE)
	@echo "$(DIRECTORIES)"

tar :
	@echo "$(LGREENCOLOR)| Creating $(ARCHIVE_NAME).tar.gz:    $(ENDCOLOR)$(LYELLOWCOLOR)$^$(ENDCOLOR)"
	@mkdir -p ../projet/
	@rm -rf ../projet/*
	@cp -r makefile $(SRCFILE) $(INCFILE) lib conf README.md description.pdf ../projet/
	@tar -czvf $(ARCHIVE_NAME).tar.gz ../projet/
zip :
	@echo "$(LGREENCOLOR)| Creating $(ARCHIVE_NAME).zip:    $(ENDCOLOR)$(LYELLOWCOLOR)$^$(ENDCOLOR)"
	@zip -r $(ARCHIVE_NAME).zip makefile $(SRCFILE) $(INCFILE) lib conf README.md description.pdf
