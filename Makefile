.PHONY: build test clean check deploy

build:
	./gradlew build

test:
	./gradlew test

check:
	./gradlew check

clean:
	./gradlew clean

run:
	./gradlew run

shadow:
	./gradlew shadowJar

deploy-dev:
	~/bin/deployer deploy2dev
