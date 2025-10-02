import kotlinx.cinterop.*
import platform.posix.*

@OptIn(ExperimentalForeignApi::class)
fun benchmarkGCOptimized() {
    println("=== Benchmark with GC (Optimized - Memory Reuse) ===\n")

    val iterations = 10_000
    val bufferSize = 1000

    memScoped {
        val startTime = alloc<timespec>()
        clock_gettime(CLOCK_MONOTONIC, startTime.ptr)
        val startNs = startTime.tv_sec * 1_000_000_000L + startTime.tv_nsec

        // Optimization 1: Allocate buffer ONCE outside the loop and reuse it
        val buffer = IntArray(bufferSize)

        for (i in 0 until iterations) {
            // Fill buffer with data (reusing same array)
            for (j in 0 until bufferSize) {
                buffer[j] = i + j
            }

            // Process data
            var sum = 0
            for (j in 0 until bufferSize) {
                sum += buffer[j]
            }
        }
        // GC will clean up buffer when it goes out of scope (only once)

        val endTime = alloc<timespec>()
        clock_gettime(CLOCK_MONOTONIC, endTime.ptr)
        val endNs = endTime.tv_sec * 1_000_000_000L + endTime.tv_nsec

        val durationMs = (endNs - startNs) / 1_000_000.0

        println("Iterations: $iterations")
        println("Buffer size: $bufferSize")
        println("Time taken: $durationMs ms")
        println("Throughput: ${iterations * bufferSize / durationMs * 1000} ops/sec")
    }

    println("\n=== Benchmark Complete (GC Optimized) ===")
}
