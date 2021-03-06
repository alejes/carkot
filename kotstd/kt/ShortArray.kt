package kotlin

external fun kotlinclib_short_array_get_ix(dataRawPtr: Int, index: Int): Short
external fun kotlinclib_short_array_set_ix(dataRawPtr: Int, index: Int, value: Short)
external fun kotlinclib_short_size(): Int


class ShortArray(var size: Int) {
    val dataRawPtr: Int

    /** Returns the number of elements in the array. */
    //size: Int

    init {
        this.dataRawPtr = malloc_array(kotlinclib_short_size() * this.size)
        var index = 0
        while (index < this.size) {
            set(index, 0)
            index = index + 1
        }
    }

    /** Returns the array element at the given [index]. This method can be called using the index operator. */
    operator fun get(index: Int): Short {
        return kotlinclib_short_array_get_ix(this.dataRawPtr, index)
    }


    /** Sets the element at the given [index] to the given [value]. This method can be called using the index operator. */
    operator fun set(index: Int, value: Short) {
        kotlinclib_short_array_set_ix(this.dataRawPtr, index, value)
    }


    fun clone(): ShortArray {
        val newInstance = ShortArray(this.size)
        var index = 0
        while (index < this.size) {
            val value = this.get(index)
            newInstance.set(index, value)
            index = index + 1
        }

        return newInstance
    }

}

fun ShortArray.print() {
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

fun ShortArray.println() {
    this.print()
    //println()
}


fun ShortArray.copyOf(newSize: Int): ShortArray {
    val newInstance = ShortArray(newSize)
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

fun ShortArray.copyOfRange(fromIndex: Int, toIndex: Int): ShortArray {
    val newInstance = ShortArray(toIndex - fromIndex)
    var index = fromIndex
    while (index < toIndex) {
        val value = this.get(index)
        newInstance.set(index - fromIndex, value)
        index = index + 1
    }

    return newInstance
}

operator fun ShortArray.plus(element: Short): ShortArray {
    val index = size
    val result = this.copyOf(index + 1)
    result[index] = element
    return result
}

operator fun ShortArray.plus(elements: ShortArray): ShortArray {
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