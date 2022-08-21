package my.github.dstories.features.monster

import my.github.dstories.features.monster.model.Monster
import my.github.dstories.features.monsters.model.ShortMonster
import my.github.dstories.framework.AsyncRes
import my.github.dstories.framework.TeaRuntime
import my.github.dstories.model.Id

object MonsterInfoTea {

    data class Model(
        val shortMonster: ShortMonster,
        val monsterInfo: AsyncRes<Monster>
    )

    sealed class Msg {

    }

    sealed class Cmd {

    }

    fun update(model: Model, msg: Msg) = with(model) {
        when (msg) {
            else -> model to emptySet<Cmd>()
        }
    }

    class Runtime(val shortMonster: ShortMonster) : TeaRuntime<Model, Msg, Cmd>(
        initialModel = Model(
            shortMonster = shortMonster,
            monsterInfo = AsyncRes.Loading
        ),
        update = ::update
    ) {
        override suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit) {
        }
    }

}
