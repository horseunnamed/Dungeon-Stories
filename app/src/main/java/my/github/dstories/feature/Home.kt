package my.github.dstories.feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

object Home {

    @Composable
    fun Content() {
        val navController = rememberNavController()
        val currentDestination = navController.currentBackStackEntryAsState().value
        Column(
            modifier = Modifier.navigationBarsPadding()
        ) {
            Box(Modifier.weight(1f)) {
                AppNavHost(navController = navController)
            }
            NavigationBar {
                TopLevelDestination.values().forEachIndexed { _, item ->
                    NavigationBarItem(
                        icon = { item.iconContent() },
                        label = { Text(item.text) },
                        selected = currentDestination?.destination?.route== item.destination,
                        onClick = { navController.navigateToTopLevelDestination(item) }
                    )
                }
            }
        }
    }

}
