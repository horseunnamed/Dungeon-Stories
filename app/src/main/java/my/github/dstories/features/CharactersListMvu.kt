package my.github.dstories.features

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
import com.github.terrakok.modo.android.compose.ComposeScreen
import com.github.terrakok.modo.forward
import kotlinx.parcelize.Parcelize
import my.github.dstories.framework.*
import my.github.dstories.model.DndCharacter
import org.koin.androidx.compose.get

object CharactersListMvu :
    MvuDef<CharactersListMvu.Model, CharactersListMvu.Msg, CharactersListMvu.Cmd> {

    data class Model(
        val characters: List<DndCharacter>,
    )

    sealed class Msg {
        object Add : Msg()
        data class CharactersUpdate(val characters: List<DndCharacter>) : Msg()
    }

    sealed class Cmd {
        object SubCharactersStore : Cmd()
        object OpenCharacterCreation : Cmd()
    }

    override val initialModel: Model
        get() = Model(characters = emptyList())

    override fun update(model: Model, msg: Msg): Upd<Model, Cmd> = with(model) {
        when (msg) {
            is Msg.Add -> this to setOf(Cmd.OpenCharacterCreation)
            is Msg.CharactersUpdate -> copy(characters = msg.characters) to emptySet()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun View(model: Model, dispatch: (Msg) -> Unit) {
        Scaffold(
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
                        CharacterCard(character)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CharacterCard(character: DndCharacter) {
        OutlinedCard(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { }
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

    class Runtime(
        private val modo: Modo,
        private val charactersStore: CharactersStoreMu.Runtime
    ) : MvuRuntime<Model, Msg, Cmd>(this) {

        init {
            perform(Cmd.SubCharactersStore)
        }

        override suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit) {
            when (cmd) {
                Cmd.OpenCharacterCreation -> modo.forward(CharacterCreationMvu.Screen())
                Cmd.SubCharactersStore -> {
                    charactersStore.stateFlow
                        .collect { dispatch(Msg.CharactersUpdate(it.characters)) }
                }
            }
        }
    }

    @Parcelize
    data class Screen(
        override val screenKey: String = "CharactersScreen"
    ) : ComposeScreen(screenKey) {

        @Composable
        override fun Content() {
            get<Runtime>().Content()
        }

    }

}


