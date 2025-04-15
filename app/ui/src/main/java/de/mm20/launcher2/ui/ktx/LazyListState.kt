package de.mm20.launcher2.ui.ktx

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.MutableState

suspend fun LazyListState.animateScrollToItem(index: Int, scrollOffset: Int = 0, programmaticScroll: MutableState<Boolean>) {
    try {
        programmaticScroll.value = true
        animateScrollToItem(index, scrollOffset)
    } finally {
        programmaticScroll.value = false
    }
}