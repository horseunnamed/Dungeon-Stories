package my.github.dstories.features

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.android.compose.ComposeScreen
import com.github.terrakok.modo.back
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import my.github.dstories.data.DndRestApi
import my.github.dstories.framework.AsyncContent
import my.github.dstories.framework.AsyncRes
import my.github.dstories.framework.MvuDef
import my.github.dstories.framework.MvuRuntime
import my.github.dstories.model.DndCharacter
import my.github.dstories.model.Id
import my.github.dstories.model.ImagePath
import my.github.dstories.ui.component.SelectableField
import org.koin.androidx.compose.get
import org.koin.core.parameter.parametersOf

object CharacterEditorMvu :
    MvuDef<CharacterEditorMvu.Model, CharacterEditorMvu.Msg, CharacterEditorMvu.Cmd> {

    data class Model(
        val title: String,
        val characterId: Id,
        val name: String,
        val portrait: ImagePath?,
        val race: DndCharacter.Race?,
        val raceInfo: AsyncRes<DndCharacter.RaceInfo>,
        val dndClass: DndCharacter.DndClass?,
        val abilityScoresValues: DndCharacter.AbilityScoresValues,
    ) {

        val canSave: Boolean
            get() = getEmptyFields().isEmpty()

        fun getEmptyFields() = buildList {
            if (name.isBlank()) add(CharacterField.Name)
            if (race == null) add(CharacterField.Race)
            if (dndClass == null) add(CharacterField.DndClass)
        }

        fun toCharacter(): DndCharacter? {
            return if (canSave) {
                DndCharacter(
                    id = characterId,
                    name = name,
                    race = race!!,
                    dndClass = dndClass!!,
                    portrait = portrait,
                    abilityScoresValues = abilityScoresValues
                )
            } else {
                null
            }
        }

    }

    enum class CharacterField {
        Name, Race, DndClass
    }

    sealed class Msg {
        data class SetName(val name: String) : Msg()
        data class SetRace(val race: DndCharacter.Race) : Msg()
        data class SetClass(val dndClass: DndCharacter.DndClass) : Msg()
        data class RaceInfoResult(val raceInfo: AsyncRes<DndCharacter.RaceInfo>) : Msg()
        data class IncAbilityScore(val abilityScore: DndCharacter.AbilityScore) : Msg()
        data class DecAbilityScore(val abilityScore: DndCharacter.AbilityScore) : Msg()
        object RandomizeEmptyFields : Msg()
        object BackClick : Msg()
        object Save : Msg()
    }

    sealed class Cmd {
        data class SaveAndClose(val character: DndCharacter?) : Cmd()
        data class FetchRaceInfo(val race: DndCharacter.Race) : Cmd()
        data class RandomizeFields(val fields: List<CharacterField>) : Cmd()
    }

    override fun update(model: Model, msg: Msg) = with(model) {
        when (msg) {
            is Msg.SetName -> copy(name = msg.name) to emptySet()
            is Msg.SetRace -> copy(race = msg.race) to setOf(Cmd.FetchRaceInfo(msg.race))
            is Msg.SetClass -> copy(dndClass = msg.dndClass) to emptySet()

            Msg.RandomizeEmptyFields -> {
                this to setOf(Cmd.RandomizeFields(getEmptyFields()))
            }

            Msg.Save -> {
                this to setOf(Cmd.SaveAndClose(toCharacter()))
            }

            Msg.BackClick -> {
                this to setOf(Cmd.SaveAndClose(toCharacter()))
            }

            is Msg.RaceInfoResult -> {
                val abilityRaceBonus = msg.raceInfo.getOrNull()?.abilityBonuses
                val abilitiesWithRaceBonus =
                    abilityRaceBonus?.let { abilityScoresValues.applyRaceBonuses(it) }
                        ?: abilityScoresValues

                copy(
                    raceInfo = msg.raceInfo,
                    abilityScoresValues = abilitiesWithRaceBonus
                ) to emptySet()
            }

            is Msg.IncAbilityScore -> copy(
                abilityScoresValues = abilityScoresValues.increase(msg.abilityScore)
            ) to emptySet()

            is Msg.DecAbilityScore -> copy(
                abilityScoresValues = abilityScoresValues.decrease(msg.abilityScore)
            ) to emptySet()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun View(model: Model, dispatch: (Msg) -> Unit) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text(model.title) },
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
                LazyColumn(Modifier.padding(paddingValues)) {
                    item("main_info") {
                        MainInfoCard(
                            model = model,
                            onNameChange = { dispatch(Msg.SetName(it)) },
                            onRaceClick = { dispatch(Msg.SetRace(it)) },
                            onClassClick = { dispatch(Msg.SetClass(it)) }
                        )
                    }

                    item("ability_scores") {
                        AbilityScoresCard(
                            abilityScoresValues = model.abilityScoresValues,
                            onIncreaseAbilityScore = { dispatch(Msg.IncAbilityScore(it)) },
                            onDecreaseAbilityScore = { dispatch(Msg.DecAbilityScore(it)) }
                        )
                    }

                    if (model.raceInfo !is AsyncRes.Empty) {
                        item("race_info") {
                            RaceInfoCard(model.raceInfo)
                        }
                    }

                    item("bottom_spacer") {
                        Spacer(Modifier.height(72.dp))
                    }
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
        ) {
            Column(
                Modifier.padding(16.dp)
            ) {
                Text(text = "Main Info", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth(),
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun RaceInfoCard(
        raceInfo: AsyncRes<DndCharacter.RaceInfo>
    ) {
        ElevatedCard(
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Box(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                AsyncContent(
                    res = raceInfo,
                    onLoading = {
                        Text(
                            text = "Loading race info",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    onValue = { raceInfo ->
                        Column(Modifier.fillMaxWidth()) {
                            Text(
                                "${raceInfo.name} race info",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Alignment: ")
                                    }
                                    append(raceInfo.alignment)
                                }
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Speed: ")
                                    }
                                    append(raceInfo.speed.toString())
                                }
                            )
                        }
                    },
                    onError = {
                        Text(
                            text = "Race info loading error :(",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun AbilityScoresCard(
        abilityScoresValues: DndCharacter.AbilityScoresValues,
        onIncreaseAbilityScore: (DndCharacter.AbilityScore) -> Unit,
        onDecreaseAbilityScore: (DndCharacter.AbilityScore) -> Unit
    ) {
        ElevatedCard(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
        ) {
            Column(
                Modifier.padding(16.dp)
            ) {
                Text(text = "Ability Scores", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Free points: ${abilityScoresValues.freePoints}")
                Spacer(modifier = Modifier.height(8.dp))
                DndCharacter.AbilityScore.values().forEach { abilityScore ->
                    AbilityScoreRow(
                        abilityScore = abilityScore,
                        abilityScoreValue = abilityScoresValues[abilityScore],
                        canIncrease = abilityScoresValues.canIncrease(abilityScore),
                        canDecrease = abilityScoresValues.canDecrease(abilityScore),
                        increaseCost = abilityScoresValues[abilityScore].increaseCost,
                        onIncreaseClick = { onIncreaseAbilityScore(abilityScore) },
                        onDecreaseClick = { onDecreaseAbilityScore(abilityScore) }
                    )
                }
            }
        }
    }

    @Composable
    private fun AbilityScoreRow(
        abilityScore: DndCharacter.AbilityScore,
        abilityScoreValue: DndCharacter.AbilityScoreValue,
        canIncrease: Boolean,
        canDecrease: Boolean,
        increaseCost: Int,
        onIncreaseClick: () -> Unit,
        onDecreaseClick: () -> Unit
    ) {
        Column {

        }
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${abilityScore.name.uppercase()}: ")
                        }
                        append(abilityScoreValue.total.toString())
                    }
                )
                if (abilityScoreValue.raceBonus != 0) {
                    Text(
                        text = "Race bonus: +${abilityScoreValue.raceBonus}",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (canIncrease) {
                Text("Inc cost: $increaseCost")
                Spacer(Modifier.width(8.dp))
            }
            IconButton(onClick = onIncreaseClick, enabled = canIncrease) {
                Text("+")
            }
            IconButton(onClick = onDecreaseClick, enabled = canDecrease) {
                Text("-")
            }
        }
    }

    private fun DndCharacter.toEditorModel(): Model {
        return Model(
            title = "Edit Character",
            characterId = id,
            name = name,
            portrait = portrait,
            race = race,
            raceInfo = AsyncRes.Empty,
            dndClass = dndClass,
            abilityScoresValues = abilityScoresValues
        )
    }

    private fun CharactersStoreMu.Runtime.getInitialEditorModel(characterId: Id): Model? {
        return stateValue.characters[characterId]?.toEditorModel()
    }

    class Runtime(
        characterId: Id,
        private val modo: Modo,
        private val charactersStore: CharactersStoreMu.Runtime,
        private val dndApi: DndRestApi
    ) : MvuRuntime<Model, Msg, Cmd>(
        mvuDef = this,
        initialModel = charactersStore.getInitialEditorModel(characterId) ?: Model(
            title = "New Character",
            characterId = characterId,
            name = "",
            portrait = null,
            race = null,
            raceInfo = AsyncRes.Empty,
            dndClass = null,
            abilityScoresValues = DndCharacter.AbilityScoresValues.Default
        ),
        initialCmd = { model ->
            buildSet {
                model.race?.let { add(Cmd.FetchRaceInfo(it)) }
            }
        }
    ) {

        override suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit) {
            when (cmd) {
                is Cmd.SaveAndClose -> {
                    modo.back()
                    cmd.character?.let {
                        charactersStore.dispatch(CharactersStoreMu.Msg.Put(cmd.character))
                    }
                }

                is Cmd.FetchRaceInfo -> {
                    withContext(Dispatchers.IO) {
                        AsyncRes.from(
                            action = { dndApi.getRaceInfo(cmd.race.apiIndex).toDomain() },
                            dispatch = { dispatch(Msg.RaceInfoResult(it)) }
                        )
                    }
                }

                is Cmd.RandomizeFields -> {
                    cmd.fields.forEach { field ->
                        when (field) {
                            CharacterField.Name -> dispatch(Msg.SetName(DndCharacter.SampleNames.random()))
                            CharacterField.Race -> dispatch(Msg.SetRace(DndCharacter.Race.Available.random()))
                            CharacterField.DndClass -> dispatch(Msg.SetClass(DndCharacter.DndClass.Available.random()))
                        }
                    }
                }
            }
        }
    }

    @Parcelize
    data class Screen(
        val characterId: Id,
        override val screenKey: String = "CharacterEditorScreen",
    ) : ComposeScreen(screenKey) {

        @Composable
        override fun Content() {
            get<Runtime>(
                parameters = { parametersOf(characterId) }
            ).Content()
        }

    }

}