package my.github.dstories.feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.github.terrakok.modo.MultiScreenState
import com.github.terrakok.modo.NavigationState
import com.github.terrakok.modo.android.compose.ComposeScreen
import com.github.terrakok.modo.android.compose.MultiComposeScreen
import com.github.terrakok.modo.android.compose.MultiScreenStateParceler
import com.github.terrakok.modo.selectStack
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import my.github.dstories.R
import my.github.dstories.app
import my.github.dstories.feature.monsters_catalog.MonstersCatalogScreen

object Home {

    private enum class NavigationItem(
        val iconContent: @Composable () -> Unit,
        val text: String,
        val screenProvider: () -> ComposeScreen
    ) {
        Characters(
            iconContent = { Icon(Icons.Filled.List, null) },
            text = "Characters",
            screenProvider = { my.github.dstories.feature.characters_list.CharactersListScreen() }
        ),
        Monsters(
            iconContent = { Icon(painterResource(R.drawable.ic_cute_monster), null) },
            text = "Monsters",
            screenProvider = { MonstersCatalogScreen() }
        ),
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
            Column(
                modifier = Modifier.navigationBarsPadding()
            ) {
                Box(Modifier.weight(1f)) {
                    innerContent()
                }
                NavigationBar {
                    NavigationItem.values().forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = { item.iconContent() },
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
