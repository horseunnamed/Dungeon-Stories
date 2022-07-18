package my.github.dstories.features

import my.github.dstories.data.DefaultCharacters
import my.github.dstories.framework.MuDef
import my.github.dstories.framework.MuRuntime
import my.github.dstories.model.DndCharacter
import my.github.dstories.model.Id

object CharactersStoreMu :
    MuDef<CharactersStoreMu.Model, CharactersStoreMu.Msg, CharactersStoreMu.Cmd> {

    data class Model(
        val characters: Map<Id, DndCharacter>
    )

    sealed class Msg {
        data class Put(val character: DndCharacter) : Msg()
    }

    sealed class Cmd

    override fun update(model: Model, msg: Msg) = with(model) {
        when (msg) {
            is Msg.Put -> copy(
                characters = characters.toMutableMap()
                    .apply { put(msg.character.id, msg.character) }
            ) to emptySet<Cmd>()
        }
    }

    class Runtime : MuRuntime<Model, Msg, Cmd>(
        muDef = this,
        initialModel = Model(DefaultCharacters.associateBy { it.id })
    ) {
        override suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit) {}
    }

}