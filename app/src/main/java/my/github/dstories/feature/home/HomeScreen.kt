package my.github.dstories.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.github.terrakok.modo.multiscreen.MultiScreen
import com.github.terrakok.modo.multiscreen.MultiScreenNavModel
import com.github.terrakok.modo.multiscreen.selectContainer
import kotlinx.parcelize.Parcelize
import my.github.dstories.R

@Parcelize
internal class HomeScreen(
    private val multiScreenNavModel: MultiScreenNavModel
) : MultiScreen(multiScreenNavModel) {

    private enum class NavigationItem(
        val iconContent: @Composable () -> Unit,
        val text: String,
    ) {
        Characters(
            iconContent = { Icon(Icons.Filled.List, null) },
            text = "Characters",
        ),
        Monsters(
            iconContent = { Icon(painterResource(R.drawable.ic_cute_monster), null) },
            text = "Monsters",
        ),
    }

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.navigationBarsPadding()
        ) {
            Box(Modifier.weight(1f)) {
                navigationState.containers[navigationState.selected].Content()
            }
            NavigationBar {
                NavigationItem.values().forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { item.iconContent() },
                        label = { Text(item.text) },
                        selected = navigationState.selected == index,
                        onClick = { selectContainer(index) }
                    )
                }
            }
        }
    }

}

