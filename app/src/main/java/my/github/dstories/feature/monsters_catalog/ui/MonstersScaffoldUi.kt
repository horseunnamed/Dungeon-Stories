package my.github.dstories.feature.monsters_catalog.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import my.github.dstories.R
import my.github.dstories.feature.monsters_catalog.MonstersCatalogTea
import my.github.dstories.core.framework.AsyncContent
import my.github.dstories.core.ui.component.ChipIcon
import my.github.dstories.core.ui.component.ScrimOnScrollBehavior
import my.github.dstories.core.ui.component.ScrimSurface
import my.github.dstories.core.ui.component.rememberContentOffsetState
import my.github.dstories.core.ui.theme.TransparentTopAppBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonstersCatalogScaffold(
    model: MonstersCatalogTea.Model,
    dispatch: (MonstersCatalogTea.Msg) -> Unit
) {
    val contentOffsetState = rememberContentOffsetState()
    val scrimOnScrollBehavior = remember { ScrimOnScrollBehavior(contentOffsetState) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrimOnScrollBehavior.nestedScrollConnection),
        topBar = {
            ScrimSurface(contentOffsetState = contentOffsetState) {
                Column {
                    MonstersTopBar(
                        showSearchBar = model.showSearchBar,
                        shouldFocusSearchBar = model.shouldFocusSearchBar,
                        showActions = model.monsters.isReady,
                        searchText = model.searchText,
                        filteredByChallengeRating = model.filter.let {
                            it.challengeRatingFrom != null || it.challengeRatingTo != null
                        },
                        filteredByMonsterType = model.filter.monsterTypes.isNotEmpty(),
                        onSearchInput = { dispatch(MonstersCatalogTea.Msg.OnSearchInput(it)) },
                        onSearchBarFocused = { dispatch(MonstersCatalogTea.Msg.OnSearchBarFocus) },
                        onOpenSearchClick = { dispatch(MonstersCatalogTea.Msg.OnOpenSearchClick) },
                        onCloseSearchClick = { dispatch(MonstersCatalogTea.Msg.OnCloseSearchClick) },
                        onOpenFilterClick = { dispatch(MonstersCatalogTea.Msg.OnOpenFilterClick) },
                    )

                }
            }
        }
    ) { paddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            AsyncContent(
                res = model.monsters,
                onLoading = { MonstersLoadingColumn() },
                onValue = { monsters ->
                    MonstersContent(
                        monsters = model.filteredMonsters ?: monsters,
                        onMonsterClick = { dispatch(MonstersCatalogTea.Msg.OnMonsterClick(it)) }
                    )
                },
                onError = {
                    MonstersLoadingError(
                        onRetryClick = { dispatch(MonstersCatalogTea.Msg.Load) }
                    )
                },
                onEmpty = { MonstersLoadingColumn() }
            )
        }
    }
}

@Composable
private fun MonstersTopBar(
    showSearchBar: Boolean,
    shouldFocusSearchBar: Boolean,
    showActions: Boolean,
    searchText: String,
    filteredByMonsterType: Boolean,
    filteredByChallengeRating: Boolean,
    onSearchInput: (String) -> Unit,
    onSearchBarFocused: () -> Unit,
    onOpenSearchClick: () -> Unit,
    onCloseSearchClick: () -> Unit,
    onOpenFilterClick: () -> Unit
) {
    Column {
        SmallTopAppBar(
            title = {
                if (showSearchBar) {
                    MonstersSearchBar(
                        searchText = searchText,
                        shouldFocus = shouldFocusSearchBar,
                        onFocused = { onSearchBarFocused() },
                        onSearchInput = { onSearchInput(it) },
                        onCloseSearchClick = { onCloseSearchClick() }
                    )
                } else {
                    Text("Monsters")
                }
            },
            actions = {
                if (showActions) {
                    if (!showSearchBar) {
                        IconButton(onClick = { onOpenSearchClick() }) {
                            Icon(Icons.Default.Search, contentDescription = null)
                        }
                    }
                    IconButton(onClick = { onOpenFilterClick() }) {
                        Icon(painterResource(R.drawable.ic_filter_list), contentDescription = null)
                    }
                }
            },
            colors = TransparentTopAppBarColors
        )
        FilterPanel(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            filteredByChallengeRating = filteredByChallengeRating,
            filteredByMonsterType = filteredByMonsterType,
            onFilterClick = { onOpenFilterClick() }
        )
    }
}

@Composable
private fun MonstersSearchBar(
    searchText: String,
    shouldFocus: Boolean,
    onFocused: () -> Unit,
    onSearchInput: (String) -> Unit,
    onCloseSearchClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    if (shouldFocus) {
        SideEffect { focusRequester.requestFocus() }
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (it.isFocused) {
                    onFocused()
                }
            },
        singleLine = true,
        value = searchText,
        onValueChange = { onSearchInput(it) },
        textStyle = MaterialTheme.typography.bodyLarge,
        trailingIcon = {
            IconButton(onClick = { onCloseSearchClick() }) {
                Icon(Icons.Default.Clear, contentDescription = null)
            }
        },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterPanel(
    modifier: Modifier,
    filteredByChallengeRating: Boolean,
    filteredByMonsterType: Boolean,
    onFilterClick: () -> Unit
) {
    if (!filteredByChallengeRating && !filteredByMonsterType) return

    Column(modifier.padding(vertical = 8.dp)) {
        Text("Filtered by:", style = MaterialTheme.typography.labelMedium)
        Row {
            if (filteredByChallengeRating) {
                FilterChip(
                    selected = true,
                    onClick = onFilterClick,
                    label = { Text("Challenge Rating") },
                    selectedIcon = { ChipIcon(painterResource(R.drawable.ic_fire)) }
                )
                Spacer(Modifier.width(8.dp))
            }
            if (filteredByMonsterType) {
                FilterChip(
                    selected = true,
                    onClick = onFilterClick,
                    label = { Text("Monster Type") },
                    selectedIcon = { ChipIcon(painterResource(R.drawable.ic_cute_monster)) }
                )
            }
        }
    }
}

