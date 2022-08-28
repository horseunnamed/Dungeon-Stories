package my.github.dstories.features.monster

import androidx.compose.runtime.Composable
import com.github.terrakok.modo.android.compose.ComposeScreen
import kotlinx.parcelize.Parcelize
import my.github.dstories.features.monster.ui.MonsterInfoScaffold
import my.github.dstories.features.monsters.model.ShortMonster
import my.github.dstories.framework.DrawUi
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