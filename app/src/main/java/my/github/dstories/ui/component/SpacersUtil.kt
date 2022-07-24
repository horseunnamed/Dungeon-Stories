package my.github.dstories.ui.component

inline fun <T> List<T>.forEachWithSpacers(
    onSpacer: (prevIndex: Int, nextIndex: Int) -> Unit,
    onItem: (T) -> Unit
) {
    forEachIndexed { index, item ->
        onItem(item)
        if (index != size - 1) {
            onSpacer(index, index + 1)
        }
    }
}
