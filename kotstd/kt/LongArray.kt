package kotlin

external fun kotlinclib_long_array_get_ix(dataRawPtr: Int, index: Int): Long
external fun kotlinclib_long_array_set_ix(dataRawPtr: Int, index: Int, value: Long)
external fun kotlinclib_long_size(): Int


class LongArray(var size: Int) {
    val dataRawPtr: Int

    /** Returns the number of elements in the array. */
    //size: Int

    init {
        this.dataRawPtr = malloc_array(kotlinclib_long_size() * this.size)
        var index = 0
        while (index < this.size) {
            set(index, 0)
            index = index + 1
        }
    }

    /** Returns the array element at the given [index]. This method can be called using the index operator. */
    operator fun get(index: Int): Long {
        return kotlinclib_long_array_get_ix(this.dataRawPtr, index)
    }


    /** Sets the element at the given [index] to the given [value]. This method can be called using the index operator. */
    operator fun set(index: Int, value: Long) {
        kotlinclib_long_array_set_ix(this.dataRawPtr, index, value)
    }


    fun clone(): LongArray {
        val newInstance = LongArray(this.size)
        var index = 0
        while (index < this.size) {
            val value = this.get(index)
            newInstance.set(index, value)
            index = index + 1
        }

        return newInstance
    }
}

fun LongArray.print() {
    var index = 0
    print('[')
    while (index < size) {
        print(get(index))
        index++
        if (index < size) {
            print(';')
            print(' ')
        }
    }
    print(']')
}

fun LongArray.println() {
    this.print()
    //println()
}

fun LongArray.copyOf(newSize: Int): LongArray {
    val newInstance = LongArray(newSize)
    var index = 0
    val end = if (newSize > this.size) this.size else newSize
    while (index < end) {
        val value = this.get(index)
        newInstance.set(index, value)
        index = index + 1
    }

    while (index < newSize) {
        newInstance.set(index, 0)
        index = index + 1
    }

    return newInstance
}

fun LongArray.copyOfRange(fromIndex: Int, toIndex: Int): LongArray {
    val newInstance = LongArray(toIndex - fromIndex)
    var index = fromIndex
    while (index < toIndex) {
        val value = this.get(index)
        newInstance.set(index - fromIndex, value)
        index = index + 1
    }

    return newInstance
}

operator fun LongArray.plus(element: Long): LongArray {
    val index = size
    val result = this.copyOf(index + 1)
    result[index] = element
    return result
}

operator fun LongArray.plus(elements: LongArray): LongArray {
    val thisSize = size
    val arraySize = elements.size
    val resultSize = thisSize + arraySize
    val newInstance = this.copyOf(resultSize)
    var index = thisSize

    while (index < resultSize) {
        val value = elements.get(index - thisSize)
        newInstance.set(index, value)
        index = index + 1
    }

    return newInstance
}