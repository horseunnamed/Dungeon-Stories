package my.github.dstories.framework

import androidx.compose.runtime.Composable

typealias Upd<Model, Cmd> = Pair<Model, Set<Cmd>>

interface MuDef<Model, Msg, Cmd> {

    fun update(model: Model, msg: Msg): Upd<Model, Cmd>

}

interface MvuDef<Model, Msg, Cmd> : MuDef<Model, Msg, Cmd> {

    @Composable
    fun View(model: Model, dispatch: (Msg) -> Unit)

}