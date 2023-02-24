package my.github.dstories.feature.characters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import my.github.dstories.core.data.FakeCharacters
import my.github.dstories.core.model.DndCharacter
import my.github.dstories.core.model.Id


internal class CharactersRepository {

    private val _state = MutableStateFlow(
        State(
            characters = FakeCharacters.associateBy { it.id }
        )
    )

    val state: StateFlow<State> = _state

    fun updateCharacter(id: Id, update: (DndCharacter) -> DndCharacter) {
        _state.update { state ->
            state.characters[id]?.let { character ->
                state.copy(
                    characters = state.characters + (id to update(character))
                )
            } ?: state
        }
    }

    fun addCharacter(character: DndCharacter) {
        _state.update { state ->
            state.copy(
                characters = state.characters + (character.id to character)
            )
        }
    }

    data class State(
        val characters: Map<Id, DndCharacter>
    )

}