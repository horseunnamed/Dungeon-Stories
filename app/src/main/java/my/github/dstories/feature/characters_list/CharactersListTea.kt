package my.github.dstories.feature.characters_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.forward
import my.github.dstories.core.framework.TeaRuntime
import my.github.dstories.core.model.DndCharacter
import my.github.dstories.core.model.Id
import my.github.dstories.feature.character_editor.old.OldCharacterEditorScreenScreen

object CharactersListTea {

    data class Model(
        val characters: List<DndCharacter>,
    )

    sealed class Msg {
        object Add : Msg()
        data class CharactersUpdate(val characters: List<DndCharacter>) : Msg()
        data class CharacterClick(val character: DndCharacter) : Msg()
    }

    sealed class Cmd {
        object SubCharactersStore : Cmd()
        data class OpenCharacterEditor(val characterId: Id?) : Cmd()
    }

    private fun update(model: Model, msg: Msg) = with(model) {
        when (msg) {
            is Msg.Add -> this to setOf(Cmd.OpenCharacterEditor(null))
            is Msg.CharacterClick -> this to setOf(Cmd.OpenCharacterEditor(msg.character.id))
            is Msg.CharactersUpdate -> copy(characters = msg.characters) to emptySet()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun View(model: Model, dispatch: (Msg) -> Unit) {
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
                    onClick = {
                        dispatch(Msg.Add)
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                model.characters.forEach { character ->
                    item(key = character.id.value) {
                        CharacterCard(
                            character = character,
                            onClick = { dispatch(Msg.CharacterClick(character)) }
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

    private fun CharactersStoreTea.Model.toList() = characters.values.toList()

    class Runtime(
        private val modo: Modo,
        private val charactersStore: CharactersStoreTea.Runtime
    ) : TeaRuntime<Model, Msg, Cmd>(
        initialModel = Model(charactersStore.stateValue.toList()),
        update = CharactersListTea::update
    ) {

        init {
            perform(Cmd.SubCharactersStore)
        }

        override suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit) {
            when (cmd) {
                is Cmd.OpenCharacterEditor -> {
                    modo.forward(OldCharacterEditorScreenScreen(cmd.characterId ?: Id.random()))
                }
                Cmd.SubCharactersStore -> {
                    charactersStore.stateFlow
                        .collect { dispatch(Msg.CharactersUpdate(it.toList())) }
                }
            }
        }
    }

}


