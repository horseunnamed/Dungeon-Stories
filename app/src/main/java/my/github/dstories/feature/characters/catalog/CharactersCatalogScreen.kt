package my.github.dstories.feature.characters.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import my.github.dstories.core.model.DndCharacter
import my.github.dstories.core.model.Id
import org.koin.androidx.compose.koinViewModel

data class CharactersCatalogUiState(
    val characters: List<DndCharacter>
)

@Composable
fun CharactersCatalogRoute(
    viewModel: CharactersCatalogViewModel = koinViewModel(),
    navigateToCharacterEditor: (Id?) -> Unit
) {
    CharactersCatalogScreen(
        uiState = viewModel.uiState.collectAsState().value,
        onCharacterClick = { navigateToCharacterEditor(it.id) },
        onAddCharacterClick = { navigateToCharacterEditor(null) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersCatalogScreen(
    uiState: CharactersCatalogUiState,
    onCharacterClick: (DndCharacter) -> Unit,
    onAddCharacterClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            SmallTopAppBar(
                title = { Text("Characters") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Create") },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = null
                    )
                },
                onClick = onAddCharacterClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            uiState.characters.forEach { character ->
                item(key = character.id.value) {
                    CharacterCard(
                        character = character,
                        onClick = { onCharacterClick(character) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterCard(character: DndCharacter, onClick: () -> Unit) {
    OutlinedCard(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Row {
            AsyncImage(
                modifier = Modifier.size(56.dp),
                model = character.portrait?.value,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${character.race.name} * ${character.dndClass.name}",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

