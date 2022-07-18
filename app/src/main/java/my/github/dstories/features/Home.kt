package my.github.dstories.features

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.github.terrakok.modo.MultiScreenState
import com.github.terrakok.modo.NavigationState
import com.github.terrakok.modo.android.compose.ComposeScreen
import com.github.terrakok.modo.android.compose.MultiComposeScreen
import com.github.terrakok.modo.android.compose.MultiScreenStateParceler
import com.github.terrakok.modo.selectStack
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import my.github.dstories.app

object Home {

    private enum class NavigationItem(
        val icon: ImageVector,
        val text: String,
        val screenProvider: () -> ComposeScreen
    ) {
        Characters(
            icon = Icons.Filled.List,
            text = "Characters",
            screenProvider = { CharactersListMvu.Screen() }
        ),
        DicesItem(
            icon = Icons.Filled.Star,
            text = "Dices",
            screenProvider = { DicesMvu.Screen() }
        )
    }

    private val DefaultMultiScreenState =
        MultiScreenState(
            stacks = NavigationItem.values()
                .map { NavigationState(listOf(it.screenProvider())) },
            selectedStack = 0
        )

    @Parcelize
    @TypeParceler<MultiScreenState, MultiScreenStateParceler>
    data class Screen(
        override var saveableMultiScreenState: MultiScreenState = DefaultMultiScreenState,
        override val screenKey: String = "HomeScreen"
    ) : MultiComposeScreen(saveableMultiScreenState, screenKey) {

        @Composable
        override fun Content(innerContent: @Composable () -> Unit) {
            val modo = LocalContext.current.app.modo
            Column {
                Box(Modifier.weight(1f)) {
                    innerContent()
                }
                NavigationBar {
                    NavigationItem.values().forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.text) },
                            selected = multiScreenState.selectedStack == index,
                            onClick = { modo.selectStack(index) }
                        )
                    }
                }
            }
        }

    }

}
