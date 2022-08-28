package my.github.dstories.feature.characters_list

import my.github.dstories.core.data.FakeCharacters
import my.github.dstories.core.framework.TeaRuntime
import my.github.dstories.core.model.DndCharacter
import my.github.dstories.core.model.Id

object CharactersStoreTea {

    data class Model(
        val characters: Map<Id, DndCharacter>
    )

    sealed class Msg {
        data class Put(val character: DndCharacter) : Msg()
    }

    sealed class Cmd

    fun update(model: Model, msg: Msg) = with(model) {
        when (msg) {
            is Msg.Put -> copy(
                characters = characters.toMutableMap()
                    .apply { put(msg.character.id, msg.character) }
            ) to emptySet<Cmd>()
        }
    }

    class Runtime : TeaRuntime<Model, Msg, Cmd>(
        initialModel = Model(FakeCharacters.associateBy { it.id }),
        update = CharactersStoreTea::update
    ) {
        override suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit) {}
    }

}