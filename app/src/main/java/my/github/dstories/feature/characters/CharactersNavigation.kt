package my.github.dstories.feature.characters

import androidx.navigation.*
import androidx.navigation.compose.composable
import my.github.dstories.core.framework.DrawUi
import my.github.dstories.feature.characters.catalog.CharactersListTea
import my.github.dstories.feature.characters.editor.CharacterEditorTea
import org.koin.androidx.compose.get
import org.koin.core.parameter.parametersOf

object CharactersNavigation {
    const val containerRoute = "characters_container_route"

    const val catalogRoute = "characters_catalog_route"

    const val editorRoute = "character_editor_route"
    const val characterIdArg = "character_id_arg"
}

fun NavController.navigateToCharactersContainer() {
    with(CharactersNavigation) {
        navigate(containerRoute)
    }
}

fun NavController.navigateToCharacterEditor(characterId: String) {
    with(CharactersNavigation) {
        navigate("$editorRoute/${characterId}")
    }
}

fun NavGraphBuilder.charactersContainer() {
    with(CharactersNavigation) {
        navigation(
            route = containerRoute,
            startDestination = catalogRoute
        ) {
            composable(route = catalogRoute) {
                get<CharactersListTea.Runtime>().DrawUi { model, dispatch ->
                    CharactersListTea.View(model, dispatch)
                }
            }

            composable(
                route = editorRoute,
                arguments = listOf(
                    navArgument(characterIdArg) { type = NavType.StringType }
                )
            ) { navBackStackEntry ->
                get<CharacterEditorTea.Runtime>(
                    parameters = { parametersOf(navBackStackEntry.arguments?.getString(characterIdArg)) }
                ).DrawUi { model, dispatch ->
                    CharacterEditorTea.View(model, dispatch)
                }
            }
        }
    }
}
