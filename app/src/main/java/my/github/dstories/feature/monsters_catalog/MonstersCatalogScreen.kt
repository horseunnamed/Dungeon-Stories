package my.github.dstories.feature.monsters_catalog

import androidx.compose.runtime.Composable
import com.github.terrakok.modo.Screen
import com.github.terrakok.modo.ScreenKey
import com.github.terrakok.modo.generateScreenKey
import kotlinx.parcelize.Parcelize
import my.github.dstories.core.framework.DrawUi
import my.github.dstories.feature.monsters_catalog.ui.MonstersCatalogScaffold
import org.koin.androidx.compose.get

@Parcelize
data class MonstersCatalogScreen(
    override val screenKey: ScreenKey = generateScreenKey()
) : Screen {

    @Composable
    override fun Content() {

        get<MonstersCatalogTea.Runtime>().DrawUi { model, dispatch ->
            MonstersCatalogScaffold(model, dispatch)
        }
    }

}