# simple-jdbc

Wrapper to simplify working with JDBC

[![Java CI](https://github.com/itsallcode/simple-jdbc/actions/workflows/build.yml/badge.svg)](https://github.com/itsallcode/simple-jdbc/actions/workflows/build.yml)
[![CodeQL](https://github.com/itsallcode/simple-jdbc/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/itsallcode/simple-jdbc/actions/workflows/codeql-analysis.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Asimple-jdbc&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=org.itsallcode%3Asimple-jdbc)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Asimple-jdbc&metric=coverage)](https://sonarcloud.io/summary/new_code?id=org.itsallcode%3Asimple-jdbc)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Asimple-jdbc&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=org.itsallcode%3Asimple-jdbc)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Asimple-jdbc&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=org.itsallcode%3Asimple-jdbc)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=org.itsallcode%3Asimple-jdbc&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=org.itsallcode%3Asimple-jdbc)
[![Maven Central](https://img.shields.io/maven-central/v/org.itsallcode/simple-jdbc)](https://search.maven.org/artifact/org.itsallcode/simple-jdbc)

* [Changelog](CHANGELOG.md)

## Development

### Check if dependencies are up-to-date

```bash
$ ./gradlew dependencyUpdates
```

### Building

Install to local maven repository:
```bash
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
3. Run the following command:

    ```bash
    $ ./gradlew clean check build publish closeAndReleaseRepository --info
    ```

4. Create a new [release](https://github.com/kaklakariada/fritzbox-java-api/releases) on GitHub.
5. After some time the release will be available at [Maven Central](https://repo1.maven.org/maven2/com/github/kaklakariada/fritzbox-java-api/).
