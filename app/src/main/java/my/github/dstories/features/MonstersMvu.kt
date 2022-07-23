package my.github.dstories.features

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.github.terrakok.modo.android.compose.ComposeScreen
import kotlinx.parcelize.Parcelize

object MonstersMvu {

    @Parcelize
    data class Screen(
        override val screenKey: String = ""
    ): ComposeScreen(screenKey) {

        @Composable
        override fun Content() {
            Text("Monsters Screen")
        }

    }

}