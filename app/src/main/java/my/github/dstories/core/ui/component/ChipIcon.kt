package my.github.dstories.core.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ChipIcon(painter: Painter) {
    Icon(
        modifier = Modifier.size(18.dp),
        painter = painter,
        contentDescription = null
    )
}

@Composable
fun ChipIcon(imageVector: ImageVector) {
    Icon(
        modifier = Modifier.size(18.dp),
        imageVector = imageVector,
        contentDescription = null
    )
}
