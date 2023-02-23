# README

## Prerequisites

- JDK 11
- Maven
- Firefox browser
- Firefox webdriver <https://github.com/mozilla/geckodriver/releases> in `PATH` variable
- Chrome browser
- Chrome webdriver <https://chromedriver.chromium.org/downloads> in `PATH` variable

```
$env:M2_HOME = 'C:\a\software\apache-maven-3.8.7-java11'
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-11.0.16.1'
$env:PATH = $env:M2_HOME + '\bin;' + $env:JAVA_HOME + '\bin;' + 'C:\a\software\selenium\webdrivers\chromedriver-110.0.5481.77;C:\a\software\selenium\webdrivers\geckodriver-v0.32.2-win-aarch64;' + $env:PATH
```

## Compile

```bash
mvn clean package
```

## Run

```bash
java -jar ./target/mangar-0.0.1-SNAPSHOT.jar

./mvnw spring-boot:run
```

## Maven upgrade

```
# update parent
mvn versions:update-parent "-DparentVersion=(2.7.0,3.0.0)" "-DgenerateBackupPoms=false"

# build and check that application works
mvn clean package
```
