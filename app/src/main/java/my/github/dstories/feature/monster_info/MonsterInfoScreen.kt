package my.github.dstories.feature.monster_info

import androidx.compose.runtime.Composable
import com.github.terrakok.modo.android.compose.ComposeScreen
import kotlinx.parcelize.Parcelize
import my.github.dstories.core.framework.DrawUi
import my.github.dstories.core.model.Monster
import my.github.dstories.feature.monster_info.ui.MonsterInfoScaffold
import org.koin.androidx.compose.get
import org.koin.core.parameter.parametersOf

@Parcelize
class MonsterInfoScreen(
    private val monsterPreview: Monster.Preview,
    override val screenKey: String = "MonsterInfoScreenKey",
) : ComposeScreen(screenKey) {

    @Composable
    override fun Content() {
        get<MonsterInfoTea.Runtime>(
            parameters = { parametersOf(monsterPreview) }
        ).DrawUi { model, dispatch ->
            MonsterInfoScaffold(model, dispatch)
        }
    }

}