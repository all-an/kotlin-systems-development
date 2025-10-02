# Benchmark Results

**Date:** 2025-10-02
**Platform:** Windows (mingwX64)

---

## Release Build Results (Optimized)

| Benchmark | Time (ms) | Throughput (ops/sec) | Performance vs GC |
|-----------|-----------|----------------------|-------------------|
| **No GC (Manual Memory)** | 7.10 | 1,408,133,378 | **+96% (FASTEST)** |
| **GC Optimized (Memory Reuse)** | 13.18 | 758,771,397 | +5.5% |
| **GC (Automatic)** | 13.91 | 719,088,196 | Baseline (100%) |

---

## Debug Build Results

| Benchmark | Time (ms) | Throughput (ops/sec) | Performance vs GC |
|-----------|-----------|----------------------|-------------------|
| **GC (Automatic)** | 112.63 | 88,789,681 | Baseline (100%) |
| **GC Optimized (Memory Reuse)** | 126.26 | 79,201,083 | -11% |
| **No GC (Manual Memory)** | 3,167.84 | 3,156,727 | -96% |

---

## Detailed Results

### Release Build

#### 1. No GC Benchmark (Manual Memory Management)

**Configuration:**
- Iterations: 10,000
- Buffer size: 1,000
- GC Mode: Disabled (`-Xbinary=gc=noop`)

**Results:**
- **Time taken:** 7.10 ms
- **Throughput:** 1,408,133,378 ops/sec

**Command:**
```bash
./gradlew runBenchmarkNoGCReleaseExecutableNative
```

---

#### 2. GC Benchmark (Automatic Memory Management)

**Configuration:**
- Iterations: 10,000
- Buffer size: 1,000
- GC Mode: Default

**Results:**
- **Time taken:** 13.91 ms
- **Throughput:** 719,088,196 ops/sec

**Command:**
```bash
./gradlew runBenchmarkGCReleaseExecutableNative
```

---

#### 3. GC Optimized Benchmark (Memory Reuse)

**Configuration:**
- Iterations: 10,000
- Buffer size: 1,000
- GC Mode: Default with memory reuse optimization

**Results:**
- **Time taken:** 13.18 ms
- **Throughput:** 758,771,397 ops/sec

**Command:**
```bash
./gradlew runBenchmarkGCOptimizedReleaseExecutableNative
```

---

### Debug Build

#### 1. No GC Benchmark (Manual Memory Management)

**Configuration:**
- Iterations: 10,000
- Buffer size: 1,000
- GC Mode: Disabled (`-Xbinary=gc=noop`)

**Results:**
- **Time taken:** 3,167.84 ms
- **Throughput:** 3,156,727 ops/sec

**Command:**
```bash
./gradlew runBenchmarkNoGCDebugExecutableNative
```

---

#### 2. GC Benchmark (Automatic Memory Management)

**Configuration:**
- Iterations: 10,000
- Buffer size: 1,000
- GC Mode: Default

**Results:**
- **Time taken:** 112.63 ms
- **Throughput:** 88,789,681 ops/sec

**Command:**
```bash
./gradlew runBenchmarkGCDebugExecutableNative
```

---

#### 3. GC Optimized Benchmark (Memory Reuse)

**Configuration:**
- Iterations: 10,000
- Buffer size: 1,000
- GC Mode: Default with memory reuse optimization

**Results:**
- **Time taken:** 126.26 ms
- **Throughput:** 79,201,083 ops/sec

**Command:**
```bash
./gradlew runBenchmarkGCOptimizedDebugExecutableNative
```

---

## Analysis

### Release vs Debug Performance Comparison

| Benchmark | Debug Time | Release Time | Speedup |
|-----------|------------|--------------|---------|
| No GC | 3,167.84 ms | 7.10 ms | **446x faster** |
| GC | 112.63 ms | 13.91 ms | **8x faster** |
| GC Optimized | 126.26 ms | 13.18 ms | **9.6x faster** |

### Key Insights

1. **Compiler optimizations matter enormously for native interop**
   - No GC benchmark improves by 446x in release mode
   - Native heap allocation overhead is almost completely eliminated with optimizations

2. **Different winners for debug vs release**
   - **Debug**: Automatic GC wins (88.8M ops/sec vs 3.2M ops/sec)
   - **Release**: No GC wins (1.4B ops/sec vs 719M ops/sec)

3. **GC overhead is measurable but modest in release builds**
   - Manual memory management is ~2x faster than automatic GC
   - Memory reuse provides ~5.5% improvement over repeated allocations

4. **Use release builds for accurate performance testing**
   - Debug builds can give misleading results
   - Native interop is especially sensitive to optimization levels

5. **Why is No GC slower in debug builds?**
   - The No GC benchmark uses `nativeHeap.allocArray<IntVar>()` which allocates native C memory through interop
   - In debug mode, every access to native memory goes through C interop safety checks and bounds checking
   - The GC benchmarks use Kotlin's managed `IntArray` which has optimized debug performance
   - Without compiler optimizations, the overhead of:
     - C interop boundary crossings
     - Pointer dereferencing
     - Type conversions between Kotlin and C types

     ...completely dominates the performance, making it 28x slower than managed arrays
   - In release builds, the compiler optimizes away most of this overhead through inlining and removing safety checks

## Conclusion

**For production workloads** (release builds):
- Manual memory management with GC disabled provides the best performance (~2x faster)
- Memory reuse strategies provide modest benefits (~5.5%)

**For development** (debug builds):
- Stick with automatic GC for better developer experience
- Performance testing in debug mode can be misleading

**Recommendation:** Use automatic GC during development, and only optimize to manual memory management if profiling shows GC as a bottleneck in release builds.
