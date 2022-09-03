package my.github.dstories.feature.character_editor

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun TopBar(
    modifier: Modifier,
    titleText: String,
    scrollBehavior: TopBarScrollBehavior
) {
    val collapsedHeightPx: Float
    val expandedTitleTopSpacePx: Float
    val expandedTitleBottomSpacePx: Float

    with(LocalDensity.current) {
        collapsedHeightPx = 64.dp.toPx()
        expandedTitleTopSpacePx = 32.dp.toPx()
        expandedTitleBottomSpacePx = 20.dp.toPx()
    }

    val collapsedFraction = scrollBehavior.state.collapsedFraction

    Surface {
        TopBarLayout(
            modifier = modifier,
            topBarScrollState = scrollBehavior.state,
            collapsedHeightPx = collapsedHeightPx,
            expandedTitleTopSpacePx = expandedTitleTopSpacePx,
            expandedTitleBottomSpacePx = expandedTitleBottomSpacePx,
            titleText = titleText,
            titleScale = 1 - 0.222222f * collapsedFraction,
            collapsedFraction = collapsedFraction
        )
    }
}

@Composable
fun TopBarLayout(
    modifier: Modifier,
    topBarScrollState: TopBarScrollState,
    collapsedHeightPx: Float,
    expandedTitleTopSpacePx: Float,
    expandedTitleBottomSpacePx: Float,
    titleText: String,
    titleScale: Float,
    collapsedFraction: Float
) {
    Layout(
        content = {
            Text(
                modifier = Modifier
                    .layoutId("expandedTitle")
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(0f, 0f)
                        scaleY = titleScale
                        scaleX = titleScale
                    }
                    .alpha(1 - collapsedFraction),
                text = titleText,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                modifier = Modifier
                    .layoutId("collapsedTitle")
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(0f, 0f)
                        scaleY = titleScale
                        scaleX = titleScale
                    }
                    .alpha(collapsedFraction),
                text = titleText,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Red)
                    .layoutId("background")
            )
        },
        modifier = modifier.then(Modifier.padding(horizontal = 16.dp))
    ) { measurables, constraints ->
        val expandedTitlePlaceable = measurables.first { it.layoutId == "expandedTitle" }
            .measure(constraints)

        val collapsedTitlePlaceable = measurables.first { it.layoutId == "collapsedTitle" }
            .measure(constraints)

        val backgroundPlaceable = measurables.first { it.layoutId == "background" }
            .measure(constraints)

        val maxExpansionPx = expandedTitleTopSpacePx +
                expandedTitlePlaceable.height + expandedTitleBottomSpacePx

        topBarScrollState.heightOffsetLimit = -maxExpansionPx

        val expandedHeightPx = collapsedHeightPx + maxExpansionPx

        val layoutHeightPx = lerp(expandedHeightPx, collapsedHeightPx, collapsedFraction)

        val expandedTitleY =
            expandedHeightPx - expandedTitlePlaceable.height - expandedTitleBottomSpacePx

        val collapsedTitleY = (collapsedHeightPx - collapsedTitlePlaceable.height * titleScale) / 2

        val titleY = lerp(expandedTitleY, collapsedTitleY, collapsedFraction).roundToInt()

        topBarScrollState.totalTopBarHeight = layoutHeightPx

        layout(constraints.maxWidth, layoutHeightPx.roundToInt()) {
            backgroundPlaceable.placeRelative(x = 0, y = 0)
            expandedTitlePlaceable.placeRelative(
                x = 0,
                y = titleY
            )
            collapsedTitlePlaceable.placeRelative(
                x = 0,
                y = titleY
            )
        }
    }
}

private fun lerp(a: Float, b: Float, fraction: Float): Float {
    return a + fraction * (b - a)
}

@Stable
class TopBarScrollState(
    initialHeightOffsetLimit: Float,
    initialHeightOffset: Float,
    initialContentOffset: Float
) {

    var totalTopBarHeight by mutableStateOf(0f)

    var heightOffsetLimit by mutableStateOf(initialHeightOffsetLimit)

    var heightOffset: Float
        get() = _heightOffset.value
        set(newOffset) {
            _heightOffset.value = newOffset.coerceIn(
                minimumValue = heightOffsetLimit,
                maximumValue = 0f
            )
        }
    var contentOffset by mutableStateOf(initialContentOffset)

    val collapsedFraction: Float
        get() = if (heightOffsetLimit != 0f) {
            heightOffset / heightOffsetLimit
        } else {
            0f
        }

    companion object {
        val Saver: Saver<TopBarScrollState, *> = listSaver(
            save = { listOf(it.heightOffsetLimit, it.heightOffset, it.contentOffset) },
            restore = {
                TopBarScrollState(
                    initialHeightOffsetLimit = it[0],
                    initialHeightOffset = it[1],
                    initialContentOffset = it[2]
                )
            }
        )
    }

    private var _heightOffset = mutableStateOf(initialHeightOffset)
}

@Composable
fun rememberTopBarScrollState(
    initialHeightOffsetLimit: Float = -Float.MAX_VALUE,
    initialHeightOffset: Float = 0f,
    initialContentOffset: Float = 0f
): TopBarScrollState {
    return rememberSaveable(saver = TopBarScrollState.Saver) {
        TopBarScrollState(
            initialHeightOffsetLimit,
            initialHeightOffset,
            initialContentOffset
        )
    }
}

class TopBarScrollBehavior(
    val state: TopBarScrollState,
    val flingAnimationSpec: DecayAnimationSpec<Float>?,
) {

    @Composable
    fun topBarHeight(): Dp {
        return with(LocalDensity.current) {
            state.totalTopBarHeight.toDp()
        }
    }

    val nestedScrollConnection = object : NestedScrollConnection {

        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            // Don't intercept if scrolling down.
            if (available.y > 0f) return Offset.Zero

            val prevHeightOffset = state.heightOffset
            state.heightOffset = state.heightOffset + available.y
            return if (prevHeightOffset != state.heightOffset) {
                // We're in the middle of top app bar collapse or expand.
                // Consume only the scroll on the Y axis.
                available.copy(x = 0f)
            } else {
                Offset.Zero
            }
        }


        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            state.contentOffset += consumed.y

            if (available.y < 0f || consumed.y < 0f) {
                // When scrolling up, just update the state's height offset.
                val oldHeightOffset = state.heightOffset
                state.heightOffset = state.heightOffset + consumed.y
                return Offset(0f, state.heightOffset - oldHeightOffset)
            }

            if (consumed.y == 0f && available.y > 0) {
                // Reset the total content offset to zero when scrolling all the way down. This
                // will eliminate some float precision inaccuracies.
                state.contentOffset = 0f
            }

            if (available.y > 0f) {
                // Adjust the height offset in case the consumed delta Y is less than what was
                // recorded as available delta Y in the pre-scroll.
                val oldHeightOffset = state.heightOffset
                state.heightOffset = state.heightOffset + available.y
                return Offset(0f, state.heightOffset - oldHeightOffset)
            }
            return Offset.Zero
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            var result = super.onPostFling(consumed, available)
            // Check if the app bar is partially collapsed/expanded.
            // Note that we don't check for 0f due to float precision with the collapsedFraction
            // calculation.
            if (state.collapsedFraction > 0.01f && state.collapsedFraction < 1f) {
                result += flingTopAppBar(
                    state = state,
                    initialVelocity = available.y,
                    flingAnimationSpec = flingAnimationSpec
                )
                // snapTopAppBar(state)
            }
            return result
        }

    }

}

private suspend fun flingTopAppBar(
    state: TopBarScrollState,
    initialVelocity: Float,
    flingAnimationSpec: DecayAnimationSpec<Float>?
): Velocity {
    var remainingVelocity = initialVelocity
    // In case there is an initial velocity that was left after a previous user fling, animate to
    // continue the motion to expand or collapse the app bar.
    if (flingAnimationSpec != null && abs(initialVelocity) > 1f) {
        var lastValue = 0f
        AnimationState(
            initialValue = 0f,
            initialVelocity = initialVelocity,
        )
            .animateDecay(flingAnimationSpec) {
                val delta = value - lastValue
                val initialHeightOffset = state.heightOffset
                state.heightOffset = initialHeightOffset + delta
                val consumed = abs(initialHeightOffset - state.heightOffset)
                lastValue = value
                remainingVelocity = this.velocity
                // avoid rounding errors and stop if anything is unconsumed
                if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
            }
    }
    return Velocity(0f, remainingVelocity)
}

@Composable
fun topBarScrollBehavior(): TopBarScrollBehavior {
    val heightOffsetLimitPx: Float
    with(LocalDensity.current) {
        heightOffsetLimitPx = -Float.MAX_VALUE
    }
    return TopBarScrollBehavior(
        state = rememberTopBarScrollState(
            initialHeightOffsetLimit = heightOffsetLimitPx
        ),
        flingAnimationSpec = rememberSplineBasedDecay()
    )

}
