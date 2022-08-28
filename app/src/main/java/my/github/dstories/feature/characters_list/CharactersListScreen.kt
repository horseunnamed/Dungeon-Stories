package my.github.dstories.feature.characters_list

import androidx.compose.runtime.Composable
import com.github.terrakok.modo.android.compose.ComposeScreen
import kotlinx.parcelize.Parcelize
import my.github.dstories.core.framework.DrawUi
import org.koin.androidx.compose.get

@Parcelize
data class CharactersListScreen(
    override val screenKey: String = "CharactersScreen"
) : ComposeScreen(screenKey) {

    @Composable
    override fun Content() {
        get<CharactersListTea.Runtime>().DrawUi { model, dispatch ->
            CharactersListTea.View(model, dispatch)
        }
    }

}