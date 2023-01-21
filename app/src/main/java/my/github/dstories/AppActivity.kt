package my.github.dstories

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.android.compose.saveState
import com.github.terrakok.modo.multiscreen.MultiScreenNavModel
import com.github.terrakok.modo.multiscreen.MultiScreenState
import com.github.terrakok.modo.stack.StackNavModel
import my.github.dstories.core.ui.theme.DungeonStoriesTheme
import my.github.dstories.feature.home.HomeScreen
import my.github.dstories.feature.monsters_catalog.MonstersCatalogScreen
import my.github.dstories.feature.monsters_catalog.MonstersContainerScreen

class AppActivity : ComponentActivity() {

    private var rootScreen: RootScreen? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        rootScreen = Modo.init(savedInstanceState, rootScreen) {
            RootScreen(
                StackNavModel(
                    HomeScreen(
                        MultiScreenNavModel(
                            MultiScreenState(
                                containers = listOf(
                                    MonstersContainerScreen(StackNavModel(MonstersCatalogScreen()))
                                ),
                                selected = 0
                            )
                        )
                    )
                )
            )
        }

        setContent {
            DungeonStoriesTheme {
                rootScreen?.Content()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        Modo.saveState(outState)
        super.onSaveInstanceState(outState, outPersistentState)
    }

}
