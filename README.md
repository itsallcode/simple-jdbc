# simple-jdbc

Wrapper to simplify working with JDBC.

**This project is at an early development stage and the API will change without backwards compatibility.**

[![Java CI](https://github.com/itsallcode/simple-jdbc/actions/workflows/build.yml/badge.svg)](https://github.com/itsallcode/simple-jdbc/actions/workflows/build.yml)
[![CodeQL](https://github.com/itsallcode/simple-jdbc/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/itsallcode/simple-jdbc/actions/workflows/codeql-analysis.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Asimple-jdbc&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=org.itsallcode%3Asimple-jdbc)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Asimple-jdbc&metric=coverage)](https://sonarcloud.io/summary/new_code?id=org.itsallcode%3Asimple-jdbc)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Asimple-jdbc&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=org.itsallcode%3Asimple-jdbc)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Asimple-jdbc&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=org.itsallcode%3Asimple-jdbc)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Asimple-jdbc&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=org.itsallcode%3Asimple-jdbc)
[![Maven Central](https://img.shields.io/maven-central/v/org.itsallcode/simple-jdbc)](https://search.maven.org/artifact/org.itsallcode/simple-jdbc)

* [Changelog](CHANGELOG.md)

## Usage

This project requires Java 17 or later.

Add dependency to your gradle project:

```groovy
dependencies {
    implementation 'org.itsallcode:simple-jdbc:0.5.0'
}
```

```java
record Name(int id, String name) {
    Object[] toRow() {
        return new Object[] { id, name };
    }
}

ConnectionFactory connectionFactory = ConnectionFactory.create();
try (SimpleConnection connection = connectionFactory.create("jdbc:h2:mem:", "user", "password")) {
    connection.executeScriptFromResource("/schema.sql");
    connection.insert("NAMES", List.of("ID", "NAME"), Name::toRow,
            Stream.of(new Name(1, "a"), new Name(2, "b"), new Name(3, "c")));
    try (SimpleResultSet<Row> rs = connection.query("select * from names order by id")) {
        List<Row> result = rs.stream().toList();
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getColumnValue(0).getValue());
    }
}
```
## Development

### Check if dependencies are up-to-date

```sh
./gradlew dependencyUpdates
```

### Building

Install to local maven repository:

```sh
./gradlew clean publishToMavenLocal
```

### Publish to Maven Central

1. Add the following to your `~/.gradle/gradle.properties`:

    ```properties
    ossrhUsername=<your maven central username>
    ossrhPassword=<your maven central passwort>

    signing.keyId=<gpg key id (last 8 chars)>
    signing.password=<gpg key password>
    signing.secretKeyRingFile=<path to secret keyring file>
    ```

2. Increment version number in `build.gradle` and `README.md`, update [CHANGELOG.md](CHANGELOG.md), commit and push.
3. Optional: run the following command to do a dry-run:

    ```sh
    ./gradlew clean check build publishToSonatype closeSonatypeStagingRepository --info
    ```

4. Run the following command to publish to Maven Central:

    ```sh
    ./gradlew clean check build publishToSonatype closeAndReleaseSonatypeStagingRepository --info
    ```

5. Create a new [release](https://github.com/itsallcode/simple-jdbc/releases) on GitHub.
6. After some time the release will be available at [Maven Central](https://repo1.maven.org/maven2/org/itsallcode/simple-jdbc/).
