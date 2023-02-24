package my.github.dstories.feature

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import my.github.dstories.feature.characters.CharactersNavigation
import my.github.dstories.feature.characters.charactersContainer
import my.github.dstories.feature.monsters.monstersContainer

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = CharactersNavigation.containerRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        charactersContainer(navController)
        monstersContainer()
    }
}