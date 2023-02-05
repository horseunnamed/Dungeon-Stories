package my.github.dstories.feature

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navOptions
import my.github.dstories.R
import my.github.dstories.feature.characters.CharactersNavigation
import my.github.dstories.feature.characters.navigateToCharactersContainer
import my.github.dstories.feature.monsters.MonstersNavigation
import my.github.dstories.feature.monsters.navigateToMonstersContainer

enum class TopLevelDestination(
    val iconContent: @Composable () -> Unit,
    val text: String,
    val destination: String
) {
    Characters(
        iconContent = { Icon(Icons.Filled.List, null) },
        text = "Characters",
        destination = CharactersNavigation.catalogRoute
    ),
    Monsters(
        iconContent = { Icon(painterResource(R.drawable.ic_cute_monster), null) },
        text = "Monsters",
        destination = MonstersNavigation.catalogRoute
    ),
}

fun NavController.navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
    val topLevelNavOptions = navOptions {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }

    when (topLevelDestination) {
        TopLevelDestination.Characters -> navigateToCharactersContainer(topLevelNavOptions)
        TopLevelDestination.Monsters -> navigateToMonstersContainer(topLevelNavOptions)
    }
}
