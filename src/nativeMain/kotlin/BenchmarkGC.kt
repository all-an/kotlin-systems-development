import kotlinx.cinterop.*
import platform.posix.*

@OptIn(ExperimentalForeignApi::class)
fun benchmarkGC() {
    println("=== Benchmark with GC (Automatic Memory Management) ===\n")

    val iterations = 10_000
    val bufferSize = 1000

    memScoped {
        val startTime = alloc<timespec>()
        clock_gettime(CLOCK_MONOTONIC, startTime.ptr)
        val startNs = startTime.tv_sec * 1_000_000_000L + startTime.tv_nsec

        for (i in 0 until iterations) {
            // Allocate new array each iteration (GC will clean up)
            val buffer = IntArray(bufferSize)

            // Fill buffer with data
            for (j in 0 until bufferSize) {
                buffer[j] = i + j
            }

            // Process data
            var sum = 0
            for (j in 0 until bufferSize) {
                sum += buffer[j]
            }
            // buffer goes out of scope, GC will collect it
        }

        val endTime = alloc<timespec>()
        clock_gettime(CLOCK_MONOTONIC, endTime.ptr)
        val endNs = endTime.tv_sec * 1_000_000_000L + endTime.tv_nsec

        val durationMs = (endNs - startNs) / 1_000_000.0

        println("Iterations: $iterations")
        println("Buffer size: $bufferSize")
        println("Time taken: $durationMs ms")
        println("Throughput: ${iterations * bufferSize / durationMs * 1000} ops/sec")
    }

    println("\n=== Benchmark Complete (GC) ===")
}
