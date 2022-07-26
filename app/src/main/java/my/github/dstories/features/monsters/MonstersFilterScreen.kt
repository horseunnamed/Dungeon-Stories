package my.github.dstories.features.monsters

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.github.terrakok.modo.android.compose.ComposeScreen
import kotlinx.parcelize.Parcelize

@Parcelize
class MonstersFilterScreen(
    override val screenKey: String = "FilterScreen"
) : ComposeScreen(screenKey) {

    @Composable
    override fun Content() {
        Text("Filters Screen")
    }

}
