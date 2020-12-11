OPTI		=	-O3
CFLAGSALL	=	-fsyntax-only -fmax-errors=n -Wpedantic -pedantic-errors -w -Wextra -Wall -Waddress -Waddress-of-packed-member -Waggregate-return -Waligned-new -Walloc-zero -Walloc-size-larger-than=byte-size -Walloca -Walloca-larger-than=byte-size -Wno-aggressive-loop-optimizations -Warray-bounds -Warray-bounds=n -Wno-attributes -Wattribute-alias=n -Wbool-compare -Wbool-operation -Wno-builtin-declaration-mismatch -Wno-builtin-macro-redefined -Wc99-c11-compat -Wc11-c2x-compat -Wcast-align -Wcast-align=strict -Wcast-function-type -Wcast-qual -Wchar-subscripts -Wcatch-value -Wcatch-value=n -Wclobbered -Wcomment -Wconditionally-supported -Wconversion -Wcoverage-mismatch -Wno-cpp -Wdangling-else -Wdate-time -Wdelete-incomplete -Wno-attribute-warning -Wno-deprecated -Wno-deprecated-declarations -Wno-designated-init -Wdisabled-optimization -Wno-discarded-qualifiers -Wno-discarded-array-qualifiers -Wno-div-by-zero -Wdouble-promotion -Wduplicated-branches -Wduplicated-cond -Wempty-body -Wenum-compare -Wno-endif-labels -Wexpansion-to-defined -Werror -Werror=* -Wextra-semi -Wfatal-errors -Wfloat-equal -Wformat -Wformat=2 -Wno-format-contains-nul -Wno-format-extra-args -Wformat-nonliteral -Wformat-overflow=n -Wformat-security -Wformat-signedness -Wformat-truncation=n -Wformat-y2k -Wframe-address -Wframe-larger-than=byte-size -Wno-free-nonheap-object -Wjump-misses-init -Whsa -Wif-not-aligned -Wignored-qualifiers -Wignored-attributes -Wincompatible-pointer-types -Wimplicit -Wimplicit-fallthrough -Wimplicit-fallthrough=n -Wimplicit-function-declaration -Wimplicit-int -Winit-self -Winline -Wno-int-conversion -Wint-in-bool-context -Wno-int-to-pointer-cast -Winvalid-memory-model -Wno-invalid-offsetof -Winvalid-pch -Wlarger-than=byte-size -Wlogical-op -Wlogical-not-parentheses -Wlong-long -Wmain -Wmaybe-uninitialized -Wmemset-elt-size -Wmemset-transposed-args -Wmisleading-indentation -Wmissing-attributes -Wmissing-braces -Wmissing-field-initializers -Wmissing-format-attribute -Wmissing-include-dirs -Wmissing-noreturn -Wmissing-profile -Wno-multichar -Wmultistatement-macros -Wnonnull -Wnonnull-compare -Wnull-dereference -Wodr -Wno-overflow -Wopenmp-simd -Woverride-init-side-effects -Woverlength-strings -Wpacked -Wpacked-bitfield-compat -Wpacked-not-aligned -Wpadded -Wparentheses -Wno-pedantic-ms-format -Wplacement-new -Wplacement-new=n -Wpointer-arith -Wpointer-compare -Wno-pointer-to-int-cast -Wno-pragmas -Wno-prio-ctor-dtor -Wredundant-decls -Wrestrict -Wno-return-local-addr -Wreturn-type -Wsequence-point -Wshadow -Wno-shadow-ivar -Wshadow=global, -Wshadow=local, -Wshadow=compatible-local -Wshift-overflow -Wshift-overflow=n -Wshift-count-negative -Wshift-count-overflow -Wshift-negative-value -Wsign-compare -Wsign-conversion -Wfloat-conversion -Wno-scalar-storage-order -Wsizeof-pointer-div -Wsizeof-pointer-memaccess -Wsizeof-array-argument -Wstack-protector -Wstack-usage=byte- size -Wstrict-aliasing -Wstrict-aliasing=n -Wstrict-overflow -Wstrict-overflow=n -Wstringop-overflow=n -Wstringop-truncation -Wsubobject-linkage -Wsuggest-final-types -Wsuggest-final-methods -Wsuggest-override -Wswitch -Wswitch-bool -Wswitch-default -Wswitch-enum -Wswitch-unreachable -Wsync-nand -Wsystem-headers -Wtautological-compare -Wtrampolines -Wtrigraphs -Wtype-limits -Wundef -Wuninitialized -Wunknown-pragmas -Wunsuffixed-float-constants -Wunused -Wunused-function -Wunused-label -Wunused-local-typedefs -Wunused-macros -Wunused-parameter -Wno-unused-result -Wunused-value -Wunused-variable -Wunused-const-variable -Wunused-const-variable=n -Wunused-but-set-parameter -Wunused-but-set-variable -Wuseless-cast -Wvariadic-macros -Wvector-operation-performance -Wvla -Wvla-larger-than=byte-size -Wvolatile-register-var -Wwrite-strings -Wzero-as-null-pointer-constant
CFLAGS		=	-Wall -Wextra -pedantic
LIBS		=	-pthread -lpthread
CMD			=	clear
MANUEL		=	0
COMPIL 		=	gcc
PORT		=	9999
IP			=	127.0.0.1
CLESERVEUR	=	./bin/cleServeur
CLECLIENT	=	./bin/cleClient


all:
	${CMD}
	${COMPIL} ${CFLAGS} ${LIBS} ./src/serveurConcurrent.c -o ./bin/ServeurConcurrent
	${COMPIL} ${CFLAGS} ${LIBS} ./src/client2.c -o ./bin/Client2
	ls -la ./

serveurconcurrent:
	${CMD}
	./bin/ServeurConcurrent ${PORT} ${CLESERVEUR} ${MANUEL}

client2:
	${CMD}
	./bin/Client2 ${IP} ${PORT} ${CLECLIENT}

nettoyer:
	rm ./*.o ./*.*~ ./bin/ServeurConcurrent ./bin/Client2

propre:
	make nettoyer
	rm ${CLESERVEUR} ${CLECLIENT}
	touch ${CLESERVEUR}
	touch ${CLECLIENT}