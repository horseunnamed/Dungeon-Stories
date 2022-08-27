package my.github.dstories.features.monster

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import my.github.dstories.data.DndGraphQlApi
import my.github.dstories.features.monster.model.Monster
import my.github.dstories.features.monsters.model.ShortMonster
import my.github.dstories.framework.AsyncRes
import my.github.dstories.framework.TeaRuntime
import my.github.dstories.model.ImagePath

object MonsterInfoTea {

    data class Model(
        val shortMonster: ShortMonster,
        val monsterInfo: AsyncRes<Monster>
    )

    sealed class Msg {
        data class LoadMonsterResult(val result: AsyncRes<Monster>) : Msg()
    }

    sealed class Cmd {
        data class LoadMonsterInfo(val index: String, val portrait: ImagePath?) : Cmd()
    }

    fun update(model: Model, msg: Msg) = with(model) {
        when (msg) {
            is Msg.LoadMonsterResult -> copy(monsterInfo = msg.result) to emptySet<Cmd>()
        }
    }

    class Runtime(
        val shortMonster: ShortMonster,
        val dndGraphQlApi: DndGraphQlApi
    ) : TeaRuntime<Model, Msg, Cmd>(
        initialModel = Model(
            shortMonster = shortMonster,
            monsterInfo = AsyncRes.Loading
        ),
        initialCmd = { setOf(Cmd.LoadMonsterInfo(shortMonster.index, shortMonster.portrait)) },
        update = ::update
    ) {
        override suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit) {
            when (cmd) {
                is Cmd.LoadMonsterInfo -> {
                    withContext(Dispatchers.IO) {
                        AsyncRes.from(
                            action = { dndGraphQlApi.getMonster(cmd.index, cmd.portrait) },
                            onResult = { dispatch(Msg.LoadMonsterResult(it)) }
                        )
                    }
                }
            }
        }
    }

}
