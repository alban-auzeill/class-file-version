# class-file-version

Display or change the version of java class files. 

## Download

```shell
curl -sfLS -o class-file-version-1.0.jar https://github.com/alban-auzeill/class-file-version/releases/download/1.0/class-file-version-1.0.jar 
```

## Usage

* Show usage if there is no arguments

```shell
java -jar class-file-version-1.0.jar
```

* Display class file versions

```shell
java -jar class-file-version-1.0.jar target/classes/org/example/Main.class target/classes/org/example/Utils.class
```
```
61.0 (Java 17)
55.0 (Java 11)
```

* Prefix with file path using `-p`

```shell
java -jar class-file-version-1.0.jar -p target/classes/org/example/Main.class target/classes/org/example/Utils.class
```
```
target/classes/org/example/Main.class 61.0 (Java 17)
target/classes/org/example/Utils.class 55.0 (Java 11)
```

* :warning: **Change** class file versions if greater than the given value `--limit-version=<value>`

```shell
java -jar class-file-version-1.0.jar -p --limit-version=55.0 target/classes/org/example/Main.class target/classes/org/example/Utils.class
java -jar class-file-version-1.0.jar -p target/classes/org/example/Main.class target/classes/org/example/Utils.class
```
```
target/classes/org/example/Main.class 61.0 (Java 17)
  change version 61.0 (Java 17) => 55.0 (Java 11)
target/classes/org/example/Utils.class 55.0 (Java 11)
```

* Show versions of all class files in a jar could be done with a bash function

```bash
function jar_version() {
  local JAR_PATH
  for JAR_PATH in "$@"; do
    echo "$JAR_PATH"
    local JAR_FILE_NAME=""
    JAR_FILE_NAME="$(basename "$JAR_PATH")"
    local EXTRACT_PATH=""
    EXTRACT_PATH="$(mktemp -d -t "${JAR_FILE_NAME%.*}")"
    unzip -q "$JAR_PATH" -d "${EXTRACT_PATH}" || return 1
    find "${EXTRACT_PATH}" -type f -name "*.class" | \
      xargs java -jar "${HOME}/libs/class-file-version-1.0.jar" | sort | uniq -c | \
      sed 's/^/    /'
    rm -rf "${EXTRACT_PATH}" || return 1
  done
}
```

* Change the class file versions in a jar

```bash
$ mkdir my-app
$ unzip -d my-app my-app.jar
$ find my-app -name "*.class" | xargs java -jar "${HOME}/libs/class-file-version-1.0.jar" -p --limit-version=55
$ (cd my-app && zip -r ../my-app-2.jar .)
```

## Build

* Prerequisite: Java >= 11

```shell
$ ./gradlew build 
```
