package my.github.dstories.feature.monsters

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import my.github.dstories.core.framework.DrawUi
import my.github.dstories.feature.monsters.details.MonsterInfoTea
import my.github.dstories.feature.monsters.details.ui.MonsterInfoScaffold
import org.koin.androidx.compose.get
import org.koin.core.parameter.parametersOf

object MonstersNavigation {
    const val containerRoute = "monsters_container_route"

    const val catalogRoute = "monsters_catalog_route"

    const val detailsRoute = "monster_details"
    const val monsterIdArg = "monster_id"
}

fun NavController.navigateToMonstersContainer() {
    with(MonstersNavigation) {
        navigate(containerRoute)
    }
}

fun NavController.navigateToMonsterDetails(monsterId: String) {
    with(MonstersNavigation) {
        navigate("$detailsRoute/${monsterId}")
    }
}

fun NavGraphBuilder.monstersContainer() {
    with(MonstersNavigation) {
        navigation(
            route = containerRoute,
            startDestination = catalogRoute
        ) {
            composable(
                route = detailsRoute,
                arguments = listOf(
                    navArgument(monsterIdArg) { type = NavType.StringType }
                )
            ) { navBackStackEntry ->
                get<MonsterInfoTea.Runtime>(
                    parameters = { parametersOf() }
                ).DrawUi { model, dispatch ->
                    MonsterInfoScaffold(model, dispatch)
                }
            }
        }
    }
}
