package my.github.dstories.feature

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
import androidx.navigation.compose.rememberNavController
import my.github.dstories.R
import my.github.dstories.feature.characters.CharactersNavigation
import my.github.dstories.feature.monsters.MonstersNavigation

object Home {

    private enum class NavigationItem(
        val iconContent: @Composable () -> Unit,
        val text: String,
        val destination: String
    ) {
        Characters(
            iconContent = { Icon(Icons.Filled.List, null) },
            text = "Characters",
            destination = CharactersNavigation.containerRoute
        ),
        Monsters(
            iconContent = { Icon(painterResource(R.drawable.ic_cute_monster), null) },
            text = "Monsters",
            destination = MonstersNavigation.containerRoute
        ),
    }

    @Composable
    fun Content() {
        val navController = rememberNavController()
        Column(
            modifier = Modifier.navigationBarsPadding()
        ) {
            Box(Modifier.weight(1f)) {
                AppNavHost(navController = navController)
            }
            NavigationBar {
                NavigationItem.values().forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { item.iconContent() },
                        label = { Text(item.text) },
                        selected = navController.currentDestination?.route == item.destination,
                        onClick = { navController.navigate(item.destination) }
                    )
                }
            }
        }
    }

}
