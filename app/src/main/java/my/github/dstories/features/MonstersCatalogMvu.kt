package my.github.dstories.features

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.terrakok.modo.android.compose.ComposeScreen
import kotlinx.parcelize.Parcelize
import my.github.dstories.framework.AsyncContent
import my.github.dstories.framework.AsyncRes
import my.github.dstories.framework.MvuDef
import my.github.dstories.framework.MvuRuntime
import my.github.dstories.model.ShortMonster
import my.github.dstories.ui.component.Skeleton
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
        ) { paddingValues ->
            Box(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                AsyncContent(
                    res = model.monsters,
                    onLoading = { MonstersLoadingColumn() },
                    onValue = {
                        MonstersContentColumn(
                            monsters = it,
                            onMonsterClick = { /*TODO*/ })
                    },
                    onError = { MonstersLoadingError(onRetryClick = { /*TODO*/ }) }
                )
            }
        }
    }

    @Composable
    private fun MonstersLoadingColumn() {
        Column(Modifier.padding(16.dp)) {
            MonsterPlaceholderCard()
            Spacer(Modifier.height(8.dp))
            MonsterPlaceholderCard()
            Spacer(Modifier.height(8.dp))
            MonsterPlaceholderCard()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MonsterPlaceholderCard() {
        OutlinedCard(
            Modifier.fillMaxWidth()
        ) {
            Row(Modifier.wrapContentHeight()) {
                Column(
                    Modifier
                        .padding(16.dp)
                        .wrapContentHeight()
                        .weight(1f)
                ) {
                    Skeleton(
                        Modifier
                            .height(24.dp)
                            .width(57.dp)
                    )
                    Spacer(Modifier.height(4.dp))
                    Skeleton(
                        Modifier
                            .height(40.dp)
                            .width(164.dp)
                    )
                }
                Skeleton(
                    modifier = Modifier.size(100.dp),
                    cornerRadius = 0.dp
                )
            }
        }
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
        initialModel = Model(AsyncRes.Loading),
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