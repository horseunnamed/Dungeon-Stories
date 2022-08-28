package my.github.dstories.feature.dices

import android.os.Parcelable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.terrakok.modo.android.compose.ComposeScreen
import kotlinx.parcelize.Parcelize
import my.github.dstories.core.framework.DrawUi
import my.github.dstories.core.framework.TeaRuntime
import org.koin.androidx.compose.get
import kotlin.random.Random

object DicesTea {

    enum class Dice(val sides: Int) {
        D4(4),
        D6(6),
        D8(8),
        D10(10),
        D12(12),
        D100(100)
    }

    @Parcelize
    data class DiceRoll(
        val dice: Dice,
        val rollResult: Int?
    ) : Parcelable

    @Parcelize
    data class Model(
        val diceRolls: List<DiceRoll>,
    ) : Parcelable {
        val total: Int?
            get() {
                val rollResults = diceRolls.mapNotNull { it.rollResult }
                return when {
                    rollResults.isEmpty() -> null
                    else -> rollResults.sum()
                }
            }
    }

    sealed class Msg {
        data class AddDice(val dice: Dice) : Msg()
        data class RemoveDice(val index: Int) : Msg()
        data class RollResult(val rollResult: List<DiceRoll>) : Msg()
        object RollDices : Msg()
    }

    sealed class Cmd {
        data class RollDices(val dices: List<Dice>) : Cmd()
    }

    private fun update(model: Model, msg: Msg) = with(model) {
        when (msg) {
            is Msg.AddDice -> model.copy(
                diceRolls = diceRolls + DiceRoll(
                    msg.dice,
                    null
                )
            ) to emptySet()

            is Msg.RemoveDice -> model.copy(
                diceRolls = diceRolls.filterIndexed { index, _ -> index != msg.index }
            ) to emptySet()

            is Msg.RollResult -> model.copy(
                diceRolls = msg.rollResult
            ) to emptySet()

            Msg.RollDices -> model to setOf(Cmd.RollDices(model.diceRolls.map { it.dice }))
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun View(model: Model, dispatch: (Msg) -> Unit) {
        Scaffold(
            modifier = Modifier.statusBarsPadding(),
            topBar = {
                SmallTopAppBar(title = { Text("Dice roller") })
            },
        ) { paddingValues ->
            Column(Modifier.padding(paddingValues)) {
                Box(
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    DiceRollsColumn(
                        diceRolls = model.diceRolls,
                        onRemoveClick = { dispatch(Msg.RemoveDice(it)) })

                    if (model.diceRolls.isNotEmpty()) {
                        Button(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            onClick = { dispatch(Msg.RollDices) }
                        ) {
                            Text("Roll!")
                        }
                    }

                }
                Column {
                    DicesRow(onDiceClick = { dispatch(Msg.AddDice(it)) })
                    RollTotalRow(total = model.total)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun DiceRollsColumn(
        diceRolls: List<DiceRoll>,
        onRemoveClick: (Int) -> Unit
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            diceRolls.forEachIndexed { index, diceRoll ->
                item(key = "dice_$index") {
                    Card(
                        Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .fillMaxWidth()
                    ) {
                        Row {
                            val diceName = diceRoll.dice.name
                            val resultText = diceRoll.rollResult?.let { ": $it" } ?: ""
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp),
                                text = diceName + resultText,
                                style = MaterialTheme.typography.titleLarge
                            )
                            IconButton(onClick = { onRemoveClick(index) }) {
                                Icon(Icons.Filled.Clear, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun DicesRow(onDiceClick: (Dice) -> Unit) {
        LazyRow(
            modifier = Modifier.padding(8.dp)
        ) {
            Dice.values().forEachIndexed { index, dice ->
                item(key = dice.name) {
                    SuggestionChip(
                        onClick = { onDiceClick(dice) },
                        enabled = true,
                        label = { Text(dice.name) }
                    )
                    if (index != Dice.values().size - 1) {
                        Spacer(Modifier.width(4.dp))
                    }
                }
            }
        }
    }

    @Composable
    private fun RollTotalRow(total: Int?) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondary
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = total?.let { "Total: $total" } ?: "Choose dices and roll it!"
            )
        }
    }

    class Runtime : TeaRuntime<Model, Msg, Cmd>(
        initialModel = Model(emptyList()),
        update = DicesTea::update
    ) {
        override suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit) {
            when (cmd) {
                is Cmd.RollDices -> {
                    val diceRolls = cmd.dices.map { dice ->
                        DiceRoll(dice, Random.nextInt(1, dice.sides + 1))
                    }
                    dispatch(Msg.RollResult(diceRolls))
                }
            }
        }
    }

    const val Qualifier = "Dices"

    @Parcelize
    data class Screen(
        override val screenKey: String = Qualifier
    ) : ComposeScreen(screenKey) {

        @Composable
        override fun Content() {
            get<Runtime>().DrawUi { model, dispatch ->
                View(model, dispatch)
            }

        }

    }

}