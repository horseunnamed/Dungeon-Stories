package my.github.dstories.feature.monsters_catalog

import androidx.compose.runtime.Composable
import com.github.terrakok.modo.android.compose.ComposeScreen
import kotlinx.parcelize.Parcelize
import my.github.dstories.feature.monsters_catalog.ui.MonstersCatalogScaffold
import my.github.dstories.core.framework.DrawUi
import org.koin.androidx.compose.get

@Parcelize
data class MonstersCatalogScreen(
    override val screenKey: String = "MonstersScreen"
) : ComposeScreen(screenKey) {

    @Composable
    override fun Content() {
        get<MonstersCatalogTea.Runtime>().DrawUi { model, dispatch ->
            MonstersCatalogScaffold(model, dispatch)
        }
    }

}