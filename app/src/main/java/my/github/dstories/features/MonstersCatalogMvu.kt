package my.github.dstories.features

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.terrakok.modo.android.compose.ComposeScreen
import kotlinx.parcelize.Parcelize
import my.github.dstories.framework.*
import my.github.dstories.model.ShortMonster
import org.koin.androidx.compose.get

object MonstersCatalogMvu :
    MvuDef<MonstersCatalogMvu.Model, MonstersCatalogMvu.Msg, MonstersCatalogMvu.Cmd> {

    data class Model(
        val monsters: AsyncRes<List<ShortMonster>>
    )

    sealed class Msg {
        object Load : Msg()
        data class LoadingResult(val monsters: AsyncRes<List<ShortMonster>>) : Msg()
    }

    sealed class Cmd {
        object LoadMonsters : Cmd()
    }

    override fun update(model: Model, msg: Msg) = with(model) {
        when (msg) {
            Msg.Load -> this to setOf(Cmd.LoadMonsters)
            is Msg.LoadingResult -> copy(monsters = msg.monsters) to emptySet()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun View(model: Model, dispatch: (Msg) -> Unit) {
        Scaffold(
            topBar = {
                SmallTopAppBar(title = { Text("Monsters") })
            }
        ) { _ ->
            AsyncContent(
                res = model.monsters,
                onLoading = { MonstersLoadingColumn() },
                onValue = { MonstersContentColumn(monsters = it, onMonsterClick = { /*TODO*/ }) },
                onError = { MonstersLoadingError(onRetryClick = { /*TODO*/ })  }
            )
        }
    }

    @Composable
    private fun MonstersLoadingColumn() {

    }

    @Composable
    private fun MonstersContentColumn(
        monsters: List<ShortMonster>,
        onMonsterClick: (ShortMonster) -> Unit
    ) {

    }

    @Composable
    private fun MonstersLoadingError(onRetryClick: () -> Unit) {

    }

    class Runtime : MvuRuntime<Model, Msg, Cmd>(
        mvuDef = this,
        initialModel = Model(AsyncRes.Empty),
        initialCmd = { setOf(Cmd.LoadMonsters) }
    ) {
        override suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit) {
            /*TODO*/
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