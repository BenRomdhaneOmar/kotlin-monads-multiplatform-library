# Kotlin Monads library

[![Test developing version](https://github.com/BenRomdhaneOmar/kotlin-monads-multiplatform-library/actions/workflows/dev-test.yaml/badge.svg)](https://github.com/BenRomdhaneOmar/kotlin-monads-multiplatform-library/actions/workflows/dev-test.yaml)
[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=BenRomdhaneOmar_kotlin-monads-multiplatform-library)](https://sonarcloud.io/summary/new_code?id=BenRomdhaneOmar_kotlin-monads-multiplatform-library)

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