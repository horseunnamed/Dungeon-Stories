package my.github.dstories.features

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.android.compose.ComposeScreen
import com.github.terrakok.modo.back
import kotlinx.parcelize.Parcelize
import my.github.dstories.framework.MvuDef
import my.github.dstories.framework.MvuRuntime
import my.github.dstories.model.DndCharacter
import my.github.dstories.model.Id
import my.github.dstories.model.ImagePath
import my.github.dstories.ui.component.SelectableField
import org.koin.androidx.compose.get
import java.util.UUID

object CharacterCreationMvu :
    MvuDef<CharacterCreationMvu.Model, CharacterCreationMvu.Msg, CharacterCreationMvu.Cmd> {

    data class Model(
        val name: String,
        val portrait: ImagePath?,
        val race: DndCharacter.Race?,
        val dndClass: DndCharacter.DndClass?,
    ) {

        val canSave: Boolean
            get() {
                return name.isNotBlank() && race != null && dndClass != null
            }

        fun toCharacter(): DndCharacter? {
            return if (canSave) {
                DndCharacter(
                    id = Id(UUID.randomUUID().toString()),
                    name = name,
                    race = race!!,
                    dndClass = dndClass!!,
                    portrait = null
                )
            } else {
                null
            }
        }

        fun randomizeEmptyFields(): Model {
            var newName = name
            var newRace = race
            var newDndClass = dndClass

            if (name.isBlank()) {
                newName = DndCharacter.RandomNames.random()
            }

            if (newRace == null) {
                newRace = DndCharacter.Race.Available.random()
            }

            if (newDndClass == null) {
                newDndClass = DndCharacter.DndClass.Available.random()
            }

            return copy(
                name = newName,
                race = newRace,
                dndClass = newDndClass
            )
        }
    }

    sealed class Msg {
        data class SetName(val name: String) : Msg()
        data class SetRace(val race: DndCharacter.Race?) : Msg()
        data class SetClass(val dndClass: DndCharacter.DndClass) : Msg()
        object BackClick : Msg()
        object RandomizeEmptyFields : Msg()
        object Save : Msg()
    }

    sealed class Cmd {
        data class SaveAndClose(val character: DndCharacter?) : Cmd()
    }

    override val initialModel = Model(
        name = "",
        portrait = null,
        race = null,
        dndClass = null,
    )

    override fun update(model: Model, msg: Msg) = with(model) {
        when (msg) {
            is Msg.SetName -> copy(name = msg.name) to emptySet()
            is Msg.SetRace -> copy(race = msg.race) to emptySet()
            is Msg.SetClass -> copy(dndClass = msg.dndClass) to emptySet()
            Msg.RandomizeEmptyFields -> randomizeEmptyFields() to emptySet()

            Msg.Save -> {
                this to setOf(Cmd.SaveAndClose(toCharacter()))
            }

            Msg.BackClick -> {
                this to setOf(Cmd.SaveAndClose(toCharacter()))
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun View(model: Model, dispatch: (Msg) -> Unit) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text("New Character") },
                    navigationIcon = {
                        IconButton(onClick = { dispatch(Msg.BackClick) }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { dispatch(Msg.RandomizeEmptyFields) }) {
                            Icon(
                                imageVector = Icons.Filled.Build,
                                contentDescription = null
                            )
                        }
                    }
                )
            },
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(Modifier.padding(paddingValues)) {
                    MainInfoCard(
                        model = model,
                        onNameChange = { dispatch(Msg.SetName(it)) },
                        onRaceClick = { dispatch(Msg.SetRace(it)) },
                        onClassClick = { dispatch(Msg.SetClass(it)) }
                    )
                }
                Button(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    onClick = { dispatch(Msg.Save) },
                    enabled = model.canSave
                ) {
                    Text("Save Character")
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MainInfoCard(
        model: Model,
        onNameChange: (String) -> Unit,
        onRaceClick: (DndCharacter.Race) -> Unit,
        onClassClick: (DndCharacter.DndClass) -> Unit
    ) {
        ElevatedCard(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Column(
                Modifier.padding(16.dp)
            ) {
                Text(text = "Main Info", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = model.name,
                    onValueChange = { onNameChange(it) },
                    label = { Text("Name") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                RaceChooser(selected = model.race, onRaceClick = { onRaceClick(it) })
                Spacer(modifier = Modifier.height(8.dp))
                ClassChooser(selected = model.dndClass, onClassClick = { onClassClick(it) })
            }
        }
    }

    @Composable
    private fun RaceChooser(
        selected: DndCharacter.Race?,
        onRaceClick: (DndCharacter.Race) -> Unit
    ) {
        SelectableField(
            labelText = "Race",
            selectedValue = selected?.name ?: ""
        ) { dismiss ->
            DndCharacter.Race.Available.forEach { race ->
                DropdownMenuItem(
                    text = { Text(race.name) },
                    onClick = {
                        onRaceClick(race)
                        dismiss()
                    }
                )
            }
        }
    }

    @Composable
    private fun ClassChooser(
        selected: DndCharacter.DndClass?,
        onClassClick: (DndCharacter.DndClass) -> Unit
    ) {
        SelectableField(
            labelText = "Class",
            selectedValue = selected?.name ?: ""
        ) { dismiss ->
            DndCharacter.DndClass.Available.forEach { dndClass ->
                DropdownMenuItem(
                    text = { Text(dndClass.name) },
                    onClick = {
                        onClassClick(dndClass)
                        dismiss()
                    }
                )
            }
        }
    }

    class Runtime(
        private val modo: Modo,
        private val charactersStore: CharactersStoreMu.Runtime
    ) : MvuRuntime<Model, Msg, Cmd>(this) {
        override suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit) {
            when (cmd) {
                is Cmd.SaveAndClose -> {
                    modo.back()
                    cmd.character?.let {
                        charactersStore.dispatch(CharactersStoreMu.Msg.Add(cmd.character))
                    }
                }
            }
        }
    }

    @Parcelize
    data class Screen(
        override val screenKey: String = "CharacterCreationScreen"
    ) : ComposeScreen(screenKey) {

        @Composable
        override fun Content() {
            get<Runtime>().Content()
        }

    }

}