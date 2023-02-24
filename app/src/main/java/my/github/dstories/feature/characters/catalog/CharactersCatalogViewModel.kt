package my.github.dstories.feature.characters.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import my.github.dstories.feature.characters.CharactersRepository


class CharactersCatalogViewModel internal constructor(
    charactersRepository: CharactersRepository
) : ViewModel() {

    val uiState = charactersRepository.state
        .map { CharactersCatalogUiState(characters = it.characters.values.toList()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = CharactersCatalogUiState(emptyList())
        )

}