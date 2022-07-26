package my.github.dstories.features.monsters.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import my.github.dstories.features.monsters.MonstersCatalogTea
import my.github.dstories.framework.AsyncContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonstersCatalogScaffold(
    model: MonstersCatalogTea.Model,
    dispatch: (MonstersCatalogTea.Msg) -> Unit
) {
    Scaffold(
        topBar = {
            MonstersTopBar(
                showSearchBar = model.showSearchBar,
                shouldFocusSearchBar = model.shouldFocusSearchBar,
                showActions = model.monsters.isReady,
                searchText = model.searchText,
                onSearchInput = { dispatch(MonstersCatalogTea.Msg.OnSearchInput(it)) },
                onSearchBarFocused = { dispatch(MonstersCatalogTea.Msg.OnSearchBarFocus) },
                onOpenSearchClick = { dispatch(MonstersCatalogTea.Msg.OnOpenSearchClick) },
                onCloseSearchClick = { dispatch(MonstersCatalogTea.Msg.OnCloseSearchClick) }
            )
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
                onValue = {
                    MonstersContent(
                        monsters = model.filteredMonsters ?: it,
                        onMonsterClick = { /*TODO*/ }
                    )
                },
                onError = { MonstersLoadingError(onRetryClick = { /*TODO*/ }) },
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
    onSearchInput: (String) -> Unit,
    onSearchBarFocused: () -> Unit,
    onOpenSearchClick: () -> Unit,
    onCloseSearchClick: () -> Unit
) {
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
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Star, contentDescription = null)
                }
            }
        }
    )
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


