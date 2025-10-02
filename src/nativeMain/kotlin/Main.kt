import kotlinx.cinterop.*

@OptIn(ExperimentalForeignApi::class)
fun main() {
    println("=== Manual Memory Management with GC Disabled ===\n")

    // Example 1: Simple integer allocation
    println("Example 1: Manual integer allocation")
    val ptr = nativeHeap.alloc<IntVar>()
    ptr.value = 42
    println("Value: ${ptr.value}")
    nativeHeap.free(ptr.ptr)  // Must free manually
    println("Freed successfully\n")

    // Example 2: Array allocation
    println("Example 2: Manual array allocation")
    val size = 10
    val buffer = nativeHeap.allocArray<IntVar>(size)
    for (i in 0 until size) {
        buffer[i] = i * 10
    }
    println("Array values:")
    for (i in 0 until size) {
        print("${buffer[i]} ")
    }
    println()
    nativeHeap.free(buffer)  // Must free manually
    println("Array freed successfully\n")

    // Example 3: Byte buffer
    println("Example 3: Byte buffer manipulation")
    val byteBuffer = nativeHeap.allocArray<ByteVar>(5)
    byteBuffer[0] = 'H'.code.toByte()
    byteBuffer[1] = 'e'.code.toByte()
    byteBuffer[2] = 'l'.code.toByte()
    byteBuffer[3] = 'l'.code.toByte()
    byteBuffer[4] = 'o'.code.toByte()

    print("Buffer contents: ")
    for (i in 0 until 5) {
        print(byteBuffer[i].toInt().toChar())
    }
    println()
    nativeHeap.free(byteBuffer)  // Must free manually
    println("Buffer freed successfully\n")

    // Example 4: Multiple allocations
    println("Example 4: Multiple allocations")
    val ptr1 = nativeHeap.alloc<LongVar>()
    val ptr2 = nativeHeap.alloc<LongVar>()
    val ptr3 = nativeHeap.alloc<LongVar>()

    ptr1.value = 100L
    ptr2.value = 200L
    ptr3.value = 300L

    println("ptr1: ${ptr1.value}")
    println("ptr2: ${ptr2.value}")
    println("ptr3: ${ptr3.value}")

    nativeHeap.free(ptr1.ptr)
    nativeHeap.free(ptr2.ptr)
    nativeHeap.free(ptr3.ptr)
    println("All pointers freed successfully\n")

    println("=== All memory manually managed - No GC! ===")
}
