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
* [API JavaDoc](https://blog.itsallcode.org/simple-jdbc/javadoc/org.itsallcode.jdbc/module-summary.html)
* [Test report](https://blog.itsallcode.org/simple-jdbc/reports/tests/test/index.html)
* [Coverage report](https://blog.itsallcode.org/simple-jdbc/reports/jacoco/test/html/index.html)

## Usage

This project requires Java 17 or later.

### Add Dependency

Add dependency to your Gradle project:

```groovy
dependencies {
    implementation 'org.itsallcode:simple-jdbc:0.9.0'
}
```

Add dependency to your Maven project:

```xml
<dependency>
  <groupId>org.itsallcode</groupId>
  <artifactId>simple-jdbc</artifactId>
  <version>0.9.0</version>
</dependency>
```

### Features

See features and API documentation in the [API documentation](https://blog.itsallcode.org/simple-jdbc/javadoc/org.itsallcode.jdbc/module-summary.html).

### Examples

See complete example code in [ExampleTest](src/test/java/org/itsallcode/jdbc/example/ExampleTest.java).

#### Imports

```java
import org.itsallcode.jdbc.ConnectionFactory;
import org.itsallcode.jdbc.SimpleConnection;
import org.itsallcode.jdbc.Transaction;
import org.itsallcode.jdbc.resultset.batch.BatchInsert;
import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.itsallcode.jdbc.resultset.generic.Row;
```

#### Create `SimpleConnection`

```java
final ConnectionFactory connectionFactory = ConnectionFactory.create(Context.builder().build());
try (SimpleConnection connection = connectionFactory.create("jdbc:h2:mem:", "user", "password")) {
    // Use connection...
}
```

#### Batch Insert Using Rows

```java
// Define a model record or class
record Name(int id, String name) {
    static void setPreparedStatement(final Name row, final PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, row.id);
        stmt.setString(2, row.name);
    }
}

connection.batchInsert(Name.class)
        .into("NAMES", List.of("ID", "NAME"))
        .rows(Stream.of(new Name(1, "a"), new Name(2, "b"), new Name(3, "c")))
        .mapping(Name::setPreparedStatement)
        .start();
```

#### Direct Batch Insert

This allows using batch inserts without creating objects for each row to avoid memory allocations.

```java
try (BatchInsert batch = transaction.batchInsert().into("NAMES", List.of("ID", "NAME")).build()) {
    for (int i = 0; i < 5; i++) {
        final int id = i + 1;
        batch.add(ps -> {
            ps.setInt(1, id);
            ps.setString(2, "name" + id);
        });
    }
}
```

#### Transactions

`Transaction` implements `DbOperations` which provides most of the methods as `SimpleConnection`. Transactions will be automatically rolled back during close unless you commit before.

```java
try (Transaction transaction = connection.startTransaction()) {
    // Use transaction
    // ...
    // Commit successful transaction
    transaction.commit();
}
```

#### Query Using Generic Row Type

```java
try (SimpleResultSet<Row> rs = connection.query("select * from names order by id")) {
    final List<Row> result = rs.stream().toList();
    assertEquals(3, result.size());
    assertEquals(1, result.get(0).get(0).value());
}
```

#### Query Using Row Mapper and Prepared Statement

```java
try (SimpleResultSet<Name> result = connection.query("select id, name from names where id = ?",
        ps -> ps.setInt(1, 2),
        (rs, idx) -> new Name(rs.getInt("id"), rs.getString("name")))) {
    final List<Name> names = result.stream().toList();
    assertEquals(1, names.size());
    assertEquals(new Name(2, "b"), names.get(0));
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

### View Generated Javadoc

```sh
./gradlew javadoc
open build/docs/javadoc/index.html
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
