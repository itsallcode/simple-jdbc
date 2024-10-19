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
    implementation 'org.itsallcode:simple-jdbc:0.8.0'
}
```

```java
// Define a model record or class
record Name(int id, String name) {
    Object[] toRow() {
        return new Object[] { id, name };
    }
}

import org.itsallcode.jdbc.ConnectionFactory;
import org.itsallcode.jdbc.SimpleConnection;
import org.itsallcode.jdbc.resultset.SimpleResultSet;

// Execute query and fetch result
ConnectionFactory connectionFactory = ConnectionFactory.create();
try (SimpleConnection connection = connectionFactory.create("jdbc:h2:mem:", "user", "password")) {
    connection.executeScript(readResource("/schema.sql"));
    connection.batchInsert(Name.class)
        .into("NAMES", List.of("ID", "NAME"))
        .rows(Stream.of(new Name(1, "a"), new Name(2, "b"), new Name(3, "c")))
        .mapping(Name::setPreparedStatement)
        .start();
    try (SimpleResultSet<Row> rs = connection.query("select * from names order by id")) {
        List<Row> result = rs.stream().toList();
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).get(0).value());
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
./gradlew publishToMavenLocal
```

### Test Coverage

To calculate and view test coverage:

```sh
./gradlew check jacocoTestReport
open build/reports/jacoco/test/html/index.html
```

### Publish to Maven Central

#### Preparations

1. Checkout the `main` branch, create a new branch.
2. Update version number in `build.gradle` and `README.md`.
3. Add changes in new version to `CHANGELOG.md`.
4. Commit and push changes.
5. Create a new pull request, have it reviewed and merged to `main`.

#### Perform the Release

1. Start the release workflow
  * Run command `gh workflow run release.yml --repo itsallcode/simple-jdbc --ref main`
  * or go to [GitHub Actions](https://github.com/itsallcode/simple-jdbc/actions/workflows/release.yml) and start the `release.yml` workflow on branch `main`.
2. Update title and description of the newly created [GitHub release](https://github.com/itsallcode/simple-jdbc/releases).
3. After some time the release will be available at [Maven Central](https://repo1.maven.org/maven2/org/itsallcode/simple-jdbc/).
