# Otus Spring Framework Developer

- Student: Andrei Bogomja
- Course: Jun-2018

## Homework 3. Spring Boot

- Move Quiz App from [Homework 2](https://github.com/ifqthenp/otus-spring-hw-02-java-annotations-config)
to Spring Boot
- Move all app properties to `application.yml`
- Make your own banner for the app
- Write/move tests and use `spring-boot-test-starter`

## How to get the project running

Clone repository from GitHub:

```shell
git clone git@github.com:ifqthenp/otus-spring-hw-03-spring-boot.git
```

Change into the cloned folder:

```shell
cd otus-spring-hw-03-spring-boot
```

Make `gradlew` script executable (or use `gradlew.bat` if running on Windows):

```shell
chmod +x gradlew 
```

Build executable `jar`:

```shell
./gradlew clean test bootJar
```

Run the program:

```shell
java -jar build/libs/otus-spring-hw-03-spring-boot.jar
```

## Changing application locale

Application's default locale is `en-GB`. To change locale to `ru-RU` or `lv-LV`
change locale in `application.properties` file and re-build the project using
`./gradlew clean fatJar` command. Another option is to pass environment variable
to the JVM:

```shell
java -jar -Dapplication.locale.i18n=ru-RU build/libs/otus-spring-hw-03-spring-boot.jar
```
