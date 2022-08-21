package my.github.dstories.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import my.github.dstories.ui.theme.DungeonStoriesTheme

object ScrimSurfaceDefaults {

    @Composable
    fun scrimColor() = DungeonStoriesTheme.colorScheme.surface2

    @Composable
    fun noScrimColor() = MaterialTheme.colorScheme.surface

    @Composable
    fun scrimElevation() = 4.dp

    @Composable
    fun offsetToScrim() = (-16).dp

}

@Composable
fun ScrimSurface(
    modifier: Modifier = Modifier,
    contentOffsetState: ContentOffsetState,
    scrimColor: Color = ScrimSurfaceDefaults.scrimColor(),
    noScrimColor: Color = ScrimSurfaceDefaults.noScrimColor(),
    scrimElevation: Dp = ScrimSurfaceDefaults.scrimElevation(),
    offsetToScrim: Dp = ScrimSurfaceDefaults.offsetToScrim(),
    content: @Composable () -> Unit
) {
    val offsetToScrimPx = LocalDensity.current.run { offsetToScrim.toPx() }
    val shouldScrim = contentOffsetState.contentOffset < offsetToScrimPx
    val targetColor = if (shouldScrim) scrimColor else noScrimColor
    val targetElevation = if (shouldScrim) scrimElevation else 0.dp

    val color = animateColorAsState(targetValue = targetColor)
    val elevation = animateDpAsState(targetValue = targetElevation)

    Surface(
        modifier = modifier,
        color = color.value,
        shadowElevation = elevation.value
    ) {
        content()
    }
}

@Stable
class ContentOffsetState(contentOffset: Float) {
    var contentOffset by mutableStateOf(contentOffset)

    companion object {
        val Saver: Saver<ContentOffsetState, *> = listSaver(
            save = { listOf(it.contentOffset) },
            restore = {
                ContentOffsetState(
                    contentOffset = it[0]
                )
            }
        )
    }
}

@Composable
fun rememberContentOffsetState(): ContentOffsetState {
    return rememberSaveable(saver = ContentOffsetState.Saver) {
        ContentOffsetState(0f)
    }
}

class ScrimOnScrollBehavior(
    val state: ContentOffsetState,
) {

    val nestedScrollConnection =
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                state.contentOffset += consumed.y
                return Offset.Zero
            }
        }
}
