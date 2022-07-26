package my.github.dstories.features.monsters

import androidx.compose.runtime.Composable
import com.github.terrakok.modo.android.compose.ComposeScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import my.github.dstories.data.DndRepository
import my.github.dstories.features.monsters.ui.MonstersCatalogScaffold
import my.github.dstories.framework.AsyncRes
import my.github.dstories.framework.MvuDef
import my.github.dstories.framework.MvuRuntime
import my.github.dstories.model.ShortMonster
import org.koin.androidx.compose.get

object MonstersCatalogMvu :
    MvuDef<MonstersCatalogMvu.Model, MonstersCatalogMvu.Msg, MonstersCatalogMvu.Cmd> {

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
        data class OnSearchInput(val text: String) : Msg()
    }

    sealed class Cmd {
        object LoadMonsters : Cmd()
    }

    override fun update(model: Model, msg: Msg) = with(model) {
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
        }
    }

    @Composable
    override fun View(model: Model, dispatch: (Msg) -> Unit) {
        MonstersCatalogScaffold(model, dispatch)
    }


    class Runtime(
        private val dndRepository: DndRepository
    ) : MvuRuntime<Model, Msg, Cmd>(
        mvuDef = this,
        initialModel = Model(
            monsters = AsyncRes.Empty,
            showSearchBar = false,
            shouldFocusSearchBar = false,
            searchText = "",
            filteredMonsters = null
        ),
        initialCmd = { setOf(Cmd.LoadMonsters) }
    ) {
        override suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit) {
            withContext(Dispatchers.IO) {
                AsyncRes.from(
                    action = { dndRepository.fetchMonsters() },
                    dispatch = { dispatch(Msg.LoadingResult(it)) }
                )
            }
        }
    }

    @Parcelize
    data class Screen(
        override val screenKey: String = "MonstersScreen"
    ) : ComposeScreen(screenKey) {

        @Composable
        override fun Content() {
            get<Runtime>().Content()
        }

    }

}