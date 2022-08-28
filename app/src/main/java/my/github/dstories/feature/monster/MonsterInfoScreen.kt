package my.github.dstories.feature.monster

import androidx.compose.runtime.Composable
import com.github.terrakok.modo.android.compose.ComposeScreen
import kotlinx.parcelize.Parcelize
import my.github.dstories.feature.monster.ui.MonsterInfoScaffold
import my.github.dstories.core.model.ShortMonster
import my.github.dstories.core.framework.DrawUi
import org.koin.androidx.compose.get
import org.koin.core.parameter.parametersOf

@Parcelize
class MonsterInfoScreen(
    private val shortMonster: ShortMonster,
    override val screenKey: String = "MonsterInfoScreenKey",
) : ComposeScreen(screenKey) {

    @Composable
    override fun Content() {
        get<MonsterInfoTea.Runtime>(
            parameters = { parametersOf(shortMonster) }
        ).DrawUi { model, dispatch ->
            MonsterInfoScaffold(model, dispatch)
        }
    }

}