package my.github.dstories.feature.monster_info

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import my.github.dstories.core.data.DndGraphQlApi
import my.github.dstories.core.framework.AsyncRes
import my.github.dstories.core.framework.TeaRuntime
import my.github.dstories.core.model.ImagePath
import my.github.dstories.core.model.Monster

object MonsterInfoTea {

    data class Model(
        val monsterPreview: Monster.Preview,
        val monsterInfo: AsyncRes<Monster>
    )

    sealed class Msg {
        object RetryLoading : Msg()
        data class LoadMonsterResult(val result: AsyncRes<Monster>) : Msg()
    }

    sealed class Cmd {
        data class LoadMonsterInfo(val index: String, val portrait: ImagePath?) : Cmd()
    }

    fun update(model: Model, msg: Msg) = with(model) {
        when (msg) {
            is Msg.LoadMonsterResult -> copy(monsterInfo = msg.result) to emptySet<Cmd>()
            is Msg.RetryLoading -> this to setOf(
                Cmd.LoadMonsterInfo(
                    monsterPreview.index,
                    monsterPreview.portrait
                )
            )
        }
    }

    class Runtime(
        val monsterPreview: Monster.Preview,
        val dndGraphQlApi: DndGraphQlApi
    ) : TeaRuntime<Model, Msg, Cmd>(
        initialModel = Model(
            monsterPreview = monsterPreview,
            monsterInfo = AsyncRes.Loading
        ),
        initialCmd = { setOf(Cmd.LoadMonsterInfo(monsterPreview.index, monsterPreview.portrait)) },
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
