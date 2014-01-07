# Commands
JAVA=java
JAVAC=javac
JFLEX=./jflex/bin/jflex
JAVACUP=$(JAVA) java_cup.Main
RM=rm
MV=mv

# Compiler component names
LEXER=Lexer
PARSER=Parser
SYMBOL=Symbol

# Test names
LEXERTEST=LexerTest
PARSERTEST=ParserTest

LIB=/lib/i386-linux-gnu/libc.so.6
ifeq ($(wildcard $(LIB)),)
LIB=-L/usr/lib -lc
endif

# Shortcuts
COMPILE=$(JAVAC) $<
ASSEMBLE=as -32 -o $@ $<
LINK=ld -m elf_i386 -o $@ $< $(LIB) -dynamic-linker /lib/ld-linux.so.2
CMCOMPILE=java cminor.Compiler $<

# Source files
GENERATED_JAVA_SRC=cminor/lexer/$(LEXER).java cminor/parser/$(PARSER).java cminor/parser/$(SYMBOL).java
JAVA_SRC=$(GENERATED_JAVA_SRC) $(shell find cminor -name '*.java')
JAVA_CLASS=$(JAVA_SRC:.java=.class)
JAVA_CUP_CLASS=java_cup/Main.class

all: $(JAVA_CLASS)

%.class: %.java
	$(COMPILE)

cminor/lexer/$(LEXER).class: cminor/lexer/$(LEXER).java cminor/parser/$(SYMBOL).java
	$(COMPILE)

cminor/parser/$(PARSER).class: cminor/parser/$(PARSER).java cminor/parser/$(PARSER).java cminor/lexer/$(LEXER).java
	$(COMPILE)

cminor/lexer/$(LEXER).java: cminor/lexer/$(LEXER).flex
	$(JFLEX) -q $<

cminor/parser/$(PARSER).java cminor/parser/$(SYMBOL).java: cminor/parser/$(PARSER).cup $(JAVA_CUP_CLASS)
	$(JAVACUP) -nosummary -nowarn -package cminor.parser -symbols $(SYMBOL) -parser $(PARSER) < $<
	$(MV) $(SYMBOL).java $(PARSER).java cminor/parser

%.s: %.cm $(JAVA_CLASS)
	$(CMCOMPILE)

%.o: %.s
	$(ASSEMBLE)

test/ast/test%: test/ast/test%.o
	$(LINK)

test/ms/test.s: test/ms/test.cminor $(JAVA_CLASS)
	$(CMCOMPILE)

test/ms/test: test/ms/test.o
	$(LINK)

clean: classclean
	$(RM) -f $(GENERATED_JAVA_SRC) cminor/lexer/$(LEXER).java~

cleanall: clean libclean

classclean:
	find cminor -name '*.class' -exec rm '{}' ';'

libclean:
	find java_cup -name '*.class' -exec rm '{}' ';'

