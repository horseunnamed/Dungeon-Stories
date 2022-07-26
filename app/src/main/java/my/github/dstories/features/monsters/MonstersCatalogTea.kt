package my.github.dstories.features.monsters

import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.forward
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import my.github.dstories.data.DndRepository
import my.github.dstories.framework.AsyncRes
import my.github.dstories.framework.TeaRuntime
import my.github.dstories.model.ShortMonster

object MonstersCatalogTea {

    data class Model(
        val monsters: AsyncRes<List<ShortMonster>>,
        val filteredMonsters: List<ShortMonster>?,
        val showSearchBar: Boolean,
        val shouldFocusSearchBar: Boolean,
        val searchText: String
    ) {

        fun filterMonsters(search: String): Model {
            val filteredMonsters = when (monsters) {
                is AsyncRes.Ok -> {
                    monsters.value.filter { it.name.lowercase().contains(search.lowercase()) }
                }
                else -> null
            }
            return copy(filteredMonsters = filteredMonsters)
        }

    }

    sealed class Msg {
        object Load : Msg()
        data class LoadingResult(val monsters: AsyncRes<List<ShortMonster>>) : Msg()
        object OnOpenSearchClick : Msg()
        object OnCloseSearchClick : Msg()
        object OnSearchBarFocus : Msg()
        object OnOpenFilterClick : Msg()
        data class OnSearchInput(val text: String) : Msg()
    }

    sealed class Cmd {
        object LoadMonsters : Cmd()
        object OpenFilterScreen : Cmd()
    }

    fun update(model: Model, msg: Msg) = with(model) {
        when (msg) {
            Msg.Load -> when (model.monsters) {
                is AsyncRes.Loading -> {
                    this to emptySet()
                }
                else -> this to setOf(
                    Cmd.LoadMonsters
                )
            }

            is Msg.LoadingResult -> copy(monsters = msg.monsters) to emptySet()

            Msg.OnOpenSearchClick -> copy(
                showSearchBar = true,
                shouldFocusSearchBar = true
            ) to emptySet()

            Msg.OnCloseSearchClick -> copy(
                searchText = "",
                filteredMonsters = null,
                showSearchBar = false
            ) to emptySet()

            is Msg.OnSearchInput ->
                copy(searchText = msg.text).filterMonsters(msg.text) to emptySet()

            Msg.OnSearchBarFocus -> copy(shouldFocusSearchBar = false) to emptySet()

            Msg.OnOpenFilterClick -> this to setOf(Cmd.OpenFilterScreen)
        }
    }

    class Runtime(
        private val dndRepository: DndRepository,
        private val modo: Modo
    ) : TeaRuntime<Model, Msg, Cmd>(
        initialModel = Model(
            monsters = AsyncRes.Empty,
            showSearchBar = false,
            shouldFocusSearchBar = false,
            searchText = "",
            filteredMonsters = null
        ),
        initialCmd = { setOf(Cmd.LoadMonsters) },
        update = ::update
    ) {
        override suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit) {
            when (cmd) {
                Cmd.LoadMonsters -> {
                    withContext(Dispatchers.IO) {
                        AsyncRes.from(
                            action = { dndRepository.fetchMonsters() },
                            onResult = { dispatch(Msg.LoadingResult(it)) }
                        )
                    }
                }

                Cmd.OpenFilterScreen -> {
                    modo.forward(MonstersFilterScreen())
                }
            }
        }
    }

}
