package my.github.dstories.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import my.github.dstories.ui.theme.PreviewTheme

@Composable
fun Modifier.skeleton(
    visible: Boolean = true,
    cornerRadius: Dp = 8.dp
): Modifier = placeholder(
    visible = visible,
    color = LocalContentColor.current.copy(alpha = 0.25f),
    shape = RoundedCornerShape(cornerRadius),
    highlight = PlaceholderHighlight.shimmer(
        highlightColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
    )
)

@Composable
fun Skeleton(
    modifier: Modifier,
    cornerRadius: Dp = 8.dp
) {
    Box(
        modifier = modifier.skeleton(visible = true, cornerRadius = cornerRadius)
    ) { }
}

@Preview
@Composable
private fun SkeletonPreview() {
    PreviewTheme {
        Skeleton(
            modifier = Modifier
                .width(120.dp)
                .height(20.dp)
        )
    }
}
