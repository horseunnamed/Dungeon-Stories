package my.github.dstories.features

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.github.terrakok.modo.android.compose.ComposeScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import my.github.dstories.R
import my.github.dstories.data.DndRepository
import my.github.dstories.framework.AsyncContent
import my.github.dstories.framework.AsyncRes
import my.github.dstories.framework.MvuDef
import my.github.dstories.framework.MvuRuntime
import my.github.dstories.model.ShortMonster
import my.github.dstories.ui.component.Skeleton
import my.github.dstories.ui.component.forEachWithSpacers
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
            Msg.Load -> when (model.monsters) {
                is AsyncRes.Loading -> {
                    this to emptySet()
                }
                else -> this to setOf(
                    Cmd.LoadMonsters
                )
            }
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
                            onMonsterClick = { /*TODO*/ }
                        )
                    },
                    onError = { MonstersLoadingError(onRetryClick = { /*TODO*/ }) },
                    onEmpty = { MonstersLoadingColumn() }
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

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun MonstersContentColumn(
        monsters: List<ShortMonster>,
        onMonsterClick: (ShortMonster) -> Unit
    ) {
        if (monsters.isEmpty()) {
            MonstersEmptyContent()
        } else {
            LazyColumn {
                item("top_spacer") {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                monsters.forEachWithSpacers(
                    onSpacer = { prevIndex, _ ->
                        item("${prevIndex}_spacer") {
                            Spacer(Modifier.height(8.dp))
                        }
                    },
                    onItem = { monster ->
                        item(monster.index) {
                            MonsterCard(monster = monster, onClick = { onMonsterClick(monster) })
                        }
                    }
                )
                item("bottom_spacer") {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    private fun formatChallengeRating(challengeRating: Double) = when (challengeRating) {
        0.125 -> "1/8"
        0.25 -> "1/4"
        0.5 -> "1/2"
        else -> challengeRating.toInt().toString()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MonsterCard(monster: ShortMonster, onClick: () -> Unit) {
        val monsterDescription = with(monster) {
            "${type.replaceFirstChar { it.uppercase() }} • " +
                    "${formatChallengeRating(challengeRating)} Challenge\n" +
                    "$hitPoints HP • $armorClass AC"
        }

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = { onClick() }
        ) {
            Row(Modifier.wrapContentHeight()) {
                Column(
                    Modifier
                        .padding(16.dp)
                        .wrapContentHeight()
                        .weight(1f)
                ) {
                    Text(monster.name, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text(monsterDescription, style = MaterialTheme.typography.bodyMedium)
                }
                if (monster.portrait != null) {
                    AsyncImage(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.White)
                            .border(width = 1.dp, color = MaterialTheme.colorScheme.outline),
                        model = monster.portrait.value,
                        contentScale = ContentScale.Fit,
                        contentDescription = null
                    )
                }
            }
        }
    }

    @Composable
    private fun MonstersEmptyContent() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.puffing_dragon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "No monsters found :(",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Try to change search",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    @Composable
    private fun MonstersLoadingError(onRetryClick: () -> Unit) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.sad_dragon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Loading error, sorry :(",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(16.dp))
                Button(onClick = onRetryClick) {
                    Text("Retry Request")
                }
            }
        }
    }

    class Runtime(
        private val dndRepository: DndRepository
    ) : MvuRuntime<Model, Msg, Cmd>(
        mvuDef = this,
        initialModel = Model(AsyncRes.Empty),
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