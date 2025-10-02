[![official JetBrains project](https://jb.gg/badges/official-plastic.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)

# Kotlin/Native Template

A Kotlin/Native project with multiple executable benchmarks for testing garbage collection performance.

## Prerequisites

- JDK 11 or higher
- Gradle (included via wrapper)

## Building the Project

To build all executables:

```bash
./gradlew build
```

On Windows:

```bash
gradlew.bat build
```

## Running the Project

This project includes four executables:

### Main Application

```bash
./gradlew runMainDebugExecutableNative
```

### Benchmarks

**No GC Benchmark:**
```bash
./gradlew runBenchmarkNoGCDebugExecutableNative
```

**GC Benchmark:**
```bash
./gradlew runBenchmarkGCDebugExecutableNative
```

**Optimized GC Benchmark:**
```bash
./gradlew runBenchmarkGCOptimizedDebugExecutableNative
```

### Running Release Builds

For release builds (optimized):

```bash
./gradlew runMainReleaseExecutableNative
./gradlew runBenchmarkNoGCReleaseExecutableNative
./gradlew runBenchmarkGCReleaseExecutableNative
./gradlew runBenchmarkGCOptimizedReleaseExecutableNative
```

## Running Tests

```bash
./gradlew nativeTest
```

## Code of conduct

Please read [our code of conduct](https://github.com/jetbrains#code-of-conduct).

## License

The [kmp-native-wizard template](https://github.com/Kotlin/kmp-native-wizard/) is licensed under [CC0](https://creativecommons.org/publicdomain/zero/1.0/deed.en).
