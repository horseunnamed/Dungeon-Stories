package my.github.dstories.framework

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface CmdHandler<Cmd, Msg> {
    suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit)
}

abstract class MuRuntime<Model, Msg, Cmd>(
    private val muDef: MuDef<Model, Msg, Cmd>,
    initialModel: Model? = null
) : CmdHandler<Cmd, Msg> {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _stateFlow = MutableStateFlow(initialModel ?: muDef.initialModel)
    val stateFlow: StateFlow<Model> = _stateFlow

    fun dispatch(msg: Msg) {
        val (newModel, cmdSet) = muDef.update(_stateFlow.value, msg)
        _stateFlow.value = newModel
        cmdSet.forEach { cmd ->
            coroutineScope.launch {
                perform(cmd)
            }
        }
    }

    fun perform(cmd: Cmd) {
        coroutineScope.launch {
            perform(cmd, ::dispatch)
        }
    }

}

// TODO add model instance saving
abstract class MvuRuntime<Model, Msg, Cmd>(
    val mvuDef: MvuDef<Model, Msg, Cmd>,
    initialModel: Model? = null
) : MuRuntime<Model, Msg, Cmd>(mvuDef, initialModel) {

    @Composable
    fun Content() {
        val modelState = stateFlow.collectAsState()
        mvuDef.View(modelState.value, this::dispatch)
    }

}
