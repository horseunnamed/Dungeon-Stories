package my.github.dstories.feature.monsters

import androidx.compose.runtime.Composable
import com.github.terrakok.modo.android.compose.ComposeScreen
import kotlinx.parcelize.Parcelize
import my.github.dstories.feature.monsters.ui.MonstersFilterScaffold
import my.github.dstories.core.framework.DrawUi
import org.koin.androidx.compose.get

@Parcelize
class MonstersFilterScreen(
    override val screenKey: String = "FilterScreen"
) : ComposeScreen(screenKey) {

    @Composable
    override fun Content() {
        get<MonstersCatalogTea.Runtime>().DrawUi { model, dispatch ->
            MonstersFilterScaffold(model, dispatch)
        }
    }

}
