package my.github.dstories.feature.monsters_catalog

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.github.terrakok.modo.stack.StackNavModel
import com.github.terrakok.modo.stack.StackScreen
import kotlinx.parcelize.Parcelize

@Parcelize
internal class MonstersContainerScreen(
    private val navigationModel : StackNavModel
) : StackScreen(navigationModel) {

    @Composable
    override fun Content() {
        Text("Monster catalog container")
    }

}