package my.github.dstories.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun VerticalSpacer(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun HorizontalSpacer(width: Dp) {
    Spacer(modifier = Modifier.width(width))
}

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
