package my.github.dstories.feature.characters

import android.net.Uri
import androidx.navigation.*
import androidx.navigation.compose.composable
import my.github.dstories.core.framework.DrawUi
import my.github.dstories.core.model.Id
import my.github.dstories.feature.characters.catalog.CharactersCatalogRoute
import my.github.dstories.feature.characters.editor.CharacterEditorTea
import org.koin.androidx.compose.get
import org.koin.core.parameter.parametersOf

object CharactersNavigation {
    const val containerRoute = "characters_container_route"

    const val catalogRoute = "characters_catalog_route"

    const val editorRoute = "character_editor_route"
    const val characterIdArg = "character_id_arg"
}

fun NavController.navigateToCharactersContainer(navOptions: NavOptions? = null) {
    with(CharactersNavigation) {
        navigate(containerRoute, navOptions)
    }
}

fun NavController.navigateToCharacterEditor(characterId: Id?) {
    with(CharactersNavigation) {
        navigate("$editorRoute/${characterId?.value?.let(Uri::encode)}")
    }
}

fun NavGraphBuilder.charactersContainer(navController: NavController) {
    with(CharactersNavigation) {
        navigation(
            route = containerRoute,
            startDestination = catalogRoute
        ) {
            composable(route = catalogRoute) {
                CharactersCatalogRoute(
                    navigateToCharacterEditor = {
                        navController.navigateToCharacterEditor(it)
                    }
                )
            }

            composable(
                route = "$editorRoute/{$characterIdArg}",
                arguments = listOf(
                    navArgument(characterIdArg) { type = NavType.StringType }
                )
            ) { navBackStackEntry ->
                get<CharacterEditorTea.Runtime>(
                    parameters = {
                        parametersOf(navBackStackEntry.arguments?.getString(characterIdArg))
                    }
                ).DrawUi { model, dispatch ->
                    CharacterEditorTea.View(model, dispatch)
                }
            }
        }
    }
}
