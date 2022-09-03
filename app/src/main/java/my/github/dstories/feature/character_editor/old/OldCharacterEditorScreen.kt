package my.github.dstories.feature.character_editor.old

import androidx.compose.runtime.Composable
import com.github.terrakok.modo.android.compose.ComposeScreen
import kotlinx.parcelize.Parcelize
import my.github.dstories.core.framework.DrawUi
import my.github.dstories.core.model.Id
import org.koin.androidx.compose.get
import org.koin.core.parameter.parametersOf

@Parcelize
data class OldCharacterEditorScreen(
    val characterId: Id,
    override val screenKey: String = "CharacterEditorScreen",
) : ComposeScreen(screenKey) {

    @Composable
    override fun Content() {
        get<OldCharacterEditorTea.Runtime>(
            parameters = { parametersOf(characterId) }
        ).DrawUi { model, dispatch ->
            OldCharacterEditorTea.View(model, dispatch)
        }
    }

}