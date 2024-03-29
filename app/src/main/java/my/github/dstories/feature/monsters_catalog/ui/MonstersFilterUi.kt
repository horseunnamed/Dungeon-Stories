package my.github.dstories.feature.monsters_catalog.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import my.github.dstories.R
import my.github.dstories.core.model.ChallengeRating
import my.github.dstories.core.model.Monster
import my.github.dstories.core.ui.component.ChipIcon
import my.github.dstories.core.ui.component.DropdownChip
import my.github.dstories.feature.monsters_catalog.MonstersCatalogTea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonstersFilterScaffold(
    model: MonstersCatalogTea.Model,
    dispatch: (MonstersCatalogTea.Msg) -> Unit
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            SmallTopAppBar(
                title = { Text("Filter Monsters") },
                navigationIcon = {
                    IconButton(onClick = { dispatch(MonstersCatalogTea.Msg.OnCloseFilterClick) }) {
                        Icon(Icons.Default.Close, null)
                    }
                },
                actions = {
                    if (!model.filter.isEmpty) {
                        TextButton(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            onClick = { dispatch(MonstersCatalogTea.Msg.Filter.Clear) }
                        ) {
                            Text("Clear")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ChallengeFilters(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                selectedFrom = model.filter.challengeRatingFrom,
                selectedTo = model.filter.challengeRatingTo,
                availableFromValues = model.filter.availableChallengeRatingFrom,
                availableToValues = model.filter.availableChallengeRatingTo,
                onFromSelected = {
                    dispatch(MonstersCatalogTea.Msg.Filter.OnFromChallengeRatingSelected(it))
                },
                onToSelected = {
                    dispatch(MonstersCatalogTea.Msg.Filter.OnToChallengeRatingSelected(it))
                }
            )
            MonsterTypeFilters(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                monsterTypes = Monster.Type.values().toList(),
                selectedMonsterTypes = model.filter.monsterTypes,
                onMonsterTypeClick = {
                    dispatch(MonstersCatalogTea.Msg.Filter.OnMonsterTypeClick(it))
                }
            )

            val filteredMonsters = model.filteredMonsters
            if (!model.filter.isEmpty && filteredMonsters != null) {
                Box(Modifier.padding(vertical = 32.dp)) {
                    if (filteredMonsters.isNotEmpty()) {
                        Button(
                            onClick = { dispatch(MonstersCatalogTea.Msg.OnCloseFilterClick) }
                        ) {
                            Text("Show ${filteredMonsters.size} results")
                        }
                    } else {
                        Text(
                            text = "No monsters match this filter",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChallengeFilters(
    modifier: Modifier = Modifier,
    selectedFrom: ChallengeRating?,
    selectedTo: ChallengeRating?,
    availableFromValues: List<ChallengeRating>,
    availableToValues: List<ChallengeRating>,
    onFromSelected: (ChallengeRating?) -> Unit,
    onToSelected: (ChallengeRating?) -> Unit
) {
    Column(modifier) {
        Row(Modifier.padding(vertical = 8.dp)) {
            Icon(painterResource(R.drawable.ic_fire), null)
            Spacer(Modifier.width(4.dp))
            Text("Challenge Rating")
        }
        Row {
            val fromLabelText = when (selectedFrom) {
                null -> "From"
                else -> "From $selectedFrom"
            }

            val toLabelText = when (selectedTo) {
                null -> "To"
                else -> "To $selectedTo"
            }

            DropdownChip(
                labelText = fromLabelText,
                selected = selectedFrom != null
            ) { dismiss ->
                DropdownMenuItem(
                    text = { Text("Any") },
                    onClick = {
                        onFromSelected(null)
                        dismiss()
                    }
                )
                availableFromValues.forEach { challengeRating ->
                    DropdownMenuItem(
                        text = { Text(challengeRating.toString()) },
                        onClick = {
                            onFromSelected(challengeRating)
                            dismiss()
                        }
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            DropdownChip(
                labelText = toLabelText,
                selected = selectedTo != null
            ) { dismiss ->
                DropdownMenuItem(
                    text = { Text("Any") },
                    onClick = {
                        onToSelected(null)
                        dismiss()
                    }
                )
                availableToValues.forEach { challengeRating ->
                    DropdownMenuItem(
                        text = { Text(challengeRating.toString()) },
                        onClick = {
                            onToSelected(challengeRating)
                            dismiss()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MonsterTypeFilters(
    modifier: Modifier = Modifier,
    monsterTypes: List<Monster.Type>,
    selectedMonsterTypes: Set<Monster.Type>,
    onMonsterTypeClick: (Monster.Type) -> Unit
) {
    Column(modifier) {
        Row(Modifier.padding(vertical = 8.dp)) {
            Icon(painterResource(R.drawable.ic_cute_monster), null)
            Spacer(Modifier.width(4.dp))
            Text("Monster Type")
        }
        Spacer(Modifier.height(8.dp))
        FlowRow(
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 0.dp,
            mainAxisAlignment = MainAxisAlignment.Start,
            mainAxisSize = SizeMode.Wrap
        ) {
            monsterTypes.forEach { monsterType ->
                val isSelected = monsterType in selectedMonsterTypes
                FilterChip(
                    modifier = Modifier.padding(0.dp),
                    selected = isSelected,
                    selectedIcon = {
                        ChipIcon(Icons.Default.Check)
                    },
                    onClick = { onMonsterTypeClick(monsterType) },
                    label = { Text(monsterType.name, style = MaterialTheme.typography.labelLarge) },
                )
            }
        }
    }
}
