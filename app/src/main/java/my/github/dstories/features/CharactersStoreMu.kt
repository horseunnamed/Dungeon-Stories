package my.github.dstories.features

import my.github.dstories.data.DefaultCharacters
import my.github.dstories.framework.MuDef
import my.github.dstories.framework.MuRuntime
import my.github.dstories.model.DndCharacter

object CharactersStoreMu :
    MuDef<CharactersStoreMu.Model, CharactersStoreMu.Msg, CharactersStoreMu.Cmd> {

    data class Model(
        val characters: List<DndCharacter>
    )

    sealed class Msg {
        data class Add(val character: DndCharacter) : Msg()
    }

    sealed class Cmd

    override fun update(model: Model, msg: Msg) = with(model) {
        when (msg) {
            is Msg.Add -> copy(characters = characters + msg.character) to emptySet<Cmd>()
        }
    }

    class Runtime : MuRuntime<Model, Msg, Cmd>(this, Model(DefaultCharacters)) {
        override suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit) {}
    }

}