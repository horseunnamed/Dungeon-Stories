package my.github.dstories.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun Skeleton(
    modifier: Modifier,
    cornerRadius: Dp = 8.dp
) {
    Box(
        modifier
            .placeholder(
                visible = true,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(cornerRadius),
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = MaterialTheme.colorScheme.surface
                )
            )
    ) {

    }
}
