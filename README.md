# Otus Spring Framework Developer

- Student: Andrei Bogomja
- Course: Nov-2018

## Homework 4. Spring Boot

- Create library application
  - Use Spring JDBC and relational database
  - Optional: use some real relational DB, otherwise use H2
  - Create tables for authors, books and genres
  - Cover with tests as much as possible

## How to get the project running

Clone repository from GitHub:

```shell
git clone git@github.com:ifqthenp/otus-spring-hw-04-spring-jdbc
```

Change into the cloned folder:

```shell
cd otus-spring-hw-04-spring-jdbc
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
java -jar build/libs/otus-spring-hw-04-spring-jdbc.jar
```
