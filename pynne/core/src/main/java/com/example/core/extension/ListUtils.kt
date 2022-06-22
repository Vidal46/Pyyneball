package com.example.core.extension

inline fun <T, R : Comparable<R>> Iterable<T>.toMutableListSortedByDescending(crossinline selector: (T) -> R?): MutableList<T> {
    return sortedWith(compareByDescending(selector)).toMutableList()
}