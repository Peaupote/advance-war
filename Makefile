NAME = advance-war
BIN_FOLDER = bin/
DOC_FOLDER = doc/
DEPS_FOLDER = ".:"

MAIN_PATH = "fr/main/App"
MAIN_PATH_MANIFEST = "fr.main.App"

MANIFEST = "MANIFEST.MF"
JAR = $(NAME).jar


# If the first argument is "run"...
ifeq (run,$(firstword $(MAKECMDGOALS)))
  # use the rest as arguments for "run"
  RUN_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  # ...and turn them into do-nothing targets
  $(eval $(RUN_ARGS):;@:)
endif

all :
	@mkdir -p $(BIN_FOLDER)
	@find -name "*.java" > sources.txt
	@javac -cp $(DEPS_FOLDER) -d $(BIN_FOLDER) @sources.txt 
	@echo "Main-Class: "$(MAIN_PATH_MANIFEST) > $(MANIFEST)
	@jar -cvmf $(MANIFEST) $(JAR) -C $(BIN_FOLDER) ./

run:
	@java -jar $(JAR) $(RUN_ARGS)

clean:
	@rm -rf $(BIN_FOLDER)
	@rm -rf $(MANIFEST)

fclean: clean
	@rm -rf $(JAR)

doc:
	javadoc -subpackages fr -charset utf-8 -d $(DOC_FOLDER)

