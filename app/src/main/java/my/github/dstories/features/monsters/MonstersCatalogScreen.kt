package my.github.dstories.features.monsters

import androidx.compose.runtime.Composable
import com.github.terrakok.modo.android.compose.ComposeScreen
import kotlinx.parcelize.Parcelize
import my.github.dstories.features.monsters.ui.MonstersCatalogScaffold
import my.github.dstories.framework.DrawUi
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