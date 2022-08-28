package my.github.dstories.feature.character_editor

import androidx.compose.runtime.Composable
import com.github.terrakok.modo.android.compose.ComposeScreen
import kotlinx.parcelize.Parcelize
import my.github.dstories.core.framework.DrawUi
import my.github.dstories.core.model.Id
import org.koin.androidx.compose.get
import org.koin.core.parameter.parametersOf

@Parcelize
data class Screen(
    val characterId: Id,
    override val screenKey: String = "CharacterEditorScreen",
) : ComposeScreen(screenKey) {

    @Composable
    override fun Content() {
        get<CharacterEditorTea.Runtime>(
            parameters = { parametersOf(characterId) }
        ).DrawUi { model, dispatch ->
            CharacterEditorTea.View(model, dispatch)
        }
    }

}