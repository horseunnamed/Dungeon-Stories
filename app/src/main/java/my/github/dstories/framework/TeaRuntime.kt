package my.github.dstories.framework

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class TeaRuntime<Model, Msg, Cmd>(
    initialModel: Model,
    initialCmd: (Model) -> Set<Cmd> = { emptySet() },
    private val update: (Model, Msg) -> Pair<Model, Set<Cmd>>
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _stateFlow = MutableStateFlow(initialModel)
    val stateFlow: StateFlow<Model> = _stateFlow
    val stateValue: Model
        get() = _stateFlow.value

    init {
        initialCmd(initialModel).forEach(::perform)
    }

    abstract suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit)

    fun dispatch(msg: Msg) {
        val (newModel, cmdSet) = update(_stateFlow.value, msg)
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

@Composable
fun <Model, Msg> TeaRuntime<Model, Msg, *>.DrawUi(
    content: @Composable (model: Model, dispatch: (Msg) -> Unit) -> Unit
) {
    val modelState = stateFlow.collectAsState()
    content(modelState.value, this::dispatch)
}

