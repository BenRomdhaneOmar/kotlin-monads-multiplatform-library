# Kotlin Monads library

[![Test developing version](https://github.com/BenRomdhaneOmar/kotlin-monads-multiplatform-library/actions/workflows/dev-test.yaml/badge.svg)](https://github.com/BenRomdhaneOmar/kotlin-monads-multiplatform-library/actions/workflows/dev-test.yaml)
[![Deploy developing version](https://github.com/BenRomdhaneOmar/kotlin-monads-multiplatform-library/actions/workflows/dev-deploy.yaml/badge.svg?branch=dev)](https://github.com/BenRomdhaneOmar/kotlin-monads-multiplatform-library/actions/workflows/dev-deploy.yaml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=BenRomdhaneOmar_kotlin-monads-multiplatform-library&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=BenRomdhaneOmar_kotlin-monads-multiplatform-library)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=BenRomdhaneOmar_kotlin-monads-multiplatform-library&metric=bugs)](https://sonarcloud.io/summary/new_code?id=BenRomdhaneOmar_kotlin-monads-multiplatform-library)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=BenRomdhaneOmar_kotlin-monads-multiplatform-library&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=BenRomdhaneOmar_kotlin-monads-multiplatform-library)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=BenRomdhaneOmar_kotlin-monads-multiplatform-library&metric=coverage)](https://sonarcloud.io/summary/new_code?id=BenRomdhaneOmar_kotlin-monads-multiplatform-library)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=BenRomdhaneOmar_kotlin-monads-multiplatform-library&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=BenRomdhaneOmar_kotlin-monads-multiplatform-library)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=BenRomdhaneOmar_kotlin-monads-multiplatform-library&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=BenRomdhaneOmar_kotlin-monads-multiplatform-library)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=BenRomdhaneOmar_kotlin-monads-multiplatform-library&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=BenRomdhaneOmar_kotlin-monads-multiplatform-library)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=BenRomdhaneOmar_kotlin-monads-multiplatform-library&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=BenRomdhaneOmar_kotlin-monads-multiplatform-library)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=BenRomdhaneOmar_kotlin-monads-multiplatform-library&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=BenRomdhaneOmar_kotlin-monads-multiplatform-library)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=BenRomdhaneOmar_kotlin-monads-multiplatform-library&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=BenRomdhaneOmar_kotlin-monads-multiplatform-library)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=BenRomdhaneOmar_kotlin-monads-multiplatform-library&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=BenRomdhaneOmar_kotlin-monads-multiplatform-library)

## Run tests for all targets:

```shell
./gradlew clean allTests
```

## Run tests for jvm targets:

```shell
./gradlew clean jvmTest
```

## Run Kotlin/Native tests for linuxX64 target:

```shell
./gradlew clean linuxX64Test
```

## Run wasmJs tests inside browser using karma and webpack:

```shell
./gradlew clean wasmJsBrowserTest
```

## Run JS tests for all platforms:

```shell
./gradlew clean wasmJsTest
```

## Run tests for androidMain:

```shell
./gradlew clean testAndroidHostTest
```

## Run tests for all android variants:

```shell
./gradlew clean testAndroid
```

## Run tests for androidMain on connected devices:

```shell
./gradlew clean connectedAndroidDeviceTest
```

## Run tests for all flavors on connected devices:

```shell
./gradlew clean connectedAndroidTest
```

## Publish to local maven:

```shell
./gradlew clean publishToMavenLocal
```