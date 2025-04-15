package de.mm20.launcher2.ktx

import java.util.*

fun <T> List<T>.randomElement(): T {
    if (isEmpty()) throw IndexOutOfBoundsException("List is empty")
    return get(Random().nextInt(size))
}

fun <T> List<T>.randomElementOrNull(): T? {
    if (isEmpty()) return null
    return get(Random().nextInt(size))
}

fun <T> List<T>?.ifNullOrEmpty(block: () -> List<T>): List<T> {
    return if (this.isNullOrEmpty()) block() else this
}

fun <T> List<T>.distinctByEquality(equalityPredicate: (T, T) -> Boolean): List<T> {
    if (size < 2) return this

    val ret = mutableListOf<T>()

    for (item in this) {
        if (ret.none { equalityPredicate(it, item) }) ret.add(item)
    }

    return ret
}

fun <T> List<List<T>>.flattenedIndices(): List<IntRange> {
    val indexList = mutableListOf<IntRange>()
    var lastEnd: Int? = null
    for (sublist in this) {
        val start = lastEnd?.let { it + 1 } ?: 0
        val end = start + sublist.lastIndex
        indexList.add(start..end)
        lastEnd = end
    }
    return indexList
}

fun <T, V: Comparable<V>> List<T>.firstIndexThatMinimizes(predicate: (T) -> V): Int {
    var min: V? = null
    var idx = -1
    forEachIndexed { i, t ->
        val v = predicate(t)
        if (min == null || v < min) {
            min = v
            idx = i
        }
    }
    return idx
}
