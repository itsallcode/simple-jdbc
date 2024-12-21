# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.9.0] - unreleased

- [PR #33](https://github.com/itsallcode/simple-jdbc/pull/33): Update dependencies
- [#32](https://github.com/itsallcode/simple-jdbc/issues/32): Add support for data sources
- [#31](https://github.com/itsallcode/simple-jdbc/issues/31): Avoid unnecessary `setAutoCommit()` calls in `Transaction`

## [0.8.0] - 2024-11-03

- [PR #27](https://github.com/itsallcode/simple-jdbc/pull/27): Update dependencies
- [PR #28](https://github.com/itsallcode/simple-jdbc/pull/28): Refactored batch inserts (**Breaking change**)
- [PR #29](https://github.com/itsallcode/simple-jdbc/pull/29): Setting values for a `PreparedStatement` (**Breaking change**)
- [PR #30](https://github.com/itsallcode/simple-jdbc/pull/30): Add transaction support

## [0.7.1] - 2024-09-01

- [PR #26](https://github.com/itsallcode/simple-jdbc/pull/26): Update dependencies

## [0.7.0] - 2024-05-04

- [PR #22](https://github.com/itsallcode/simple-jdbc/pull/22): Added `module-info.java`

## [0.6.1] - 2024-01-14

- [PR #16](https://github.com/itsallcode/simple-jdbc/pull/16): Improve test coverage

## [0.6.0] - 2023-12-16

- [PR #15](https://github.com/itsallcode/simple-jdbc/pull/15): Refactor row mapper, add DB dialect detection

## [0.5.0] - 2023-11-26

- [PR #13](https://github.com/itsallcode/simple-jdbc/pull/13): Allow converting legacy types Timestamp, Time & Date

## [0.4.0] - 2023-10-21

- [PR #10](https://github.com/itsallcode/simple-jdbc/pull/10): Code cleanup, add Javadoc comments
- [PR #9](https://github.com/itsallcode/simple-jdbc/pull/9): Upgrade dependencies

## [0.3.0] - 2022-02-19

Added simple insert method, validate parameters in batch insert.

## [0.2.0] - 2022-01-23

Simplify batch inserts.

## [0.1.0] - 2022-01-16

Initial release: Support for executing queries and batch updates.
