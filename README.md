# class-file-version

Display or change the version of java class files. 

## Download

curl -sfLS -o class-file-version-1.0.jar https://github.com/alban-auzeill/class-file-version/releases/download/1.0/class-file-version-1.0.jar 

## Usage

* Show usage if there is no arguments

```shell
$ java -jar class-file-version-1.0.jar
```

* Display class file versions

```shell
$ java -jar class-file-version-1.0.jar target/classes/org/example/Main.class target/classes/org/example/Utils.class
61.0 (Java 17)
55.0 (Java 11)
```

* Prefix with file path using `-p`

```shell
$ java -jar class-file-version-1.0.jar -p target/classes/org/example/Main.class target/classes/org/example/Utils.class
target/classes/org/example/Main.class 61.0 (Java 17)
target/classes/org/example/Utils.class 55.0 (Java 11)
```

* :warning: **Change** class file versions if greater than the given value `--limit-version=<value>`

```shell
$ java -jar class-file-version-1.0.jar -p --limit-version=55.0 target/classes/org/example/Main.class target/classes/org/example/Utils.class
target/classes/org/example/Main.class 61.0 (Java 17)
  change version 61.0 (Java 17) => 55.0 (Java 11)
target/classes/org/example/Utils.class 55.0 (Java 11)
```

## Build

* Prerequisite: Java >= 11

```shell
$ ./gradlew build 
```
