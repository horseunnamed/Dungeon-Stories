package my.github.dstories.features.monster.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import my.github.dstories.features.monster.MonsterInfoTea
import my.github.dstories.ui.component.ScrimOnScrollBehavior
import my.github.dstories.ui.component.ScrimSurface
import my.github.dstories.ui.component.rememberContentOffsetState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonsterInfoScaffold(
    model: MonsterInfoTea.Model,
    dispatch: (MonsterInfoTea.Msg) -> Unit
) {
    val contentOffsetState = rememberContentOffsetState()
    val scrimOnScrollBehavior = remember { ScrimOnScrollBehavior(contentOffsetState) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrimOnScrollBehavior.nestedScrollConnection),
        topBar = {
            ScrimSurface(contentOffsetState = contentOffsetState) {
                SmallTopAppBar(title = { Text(model.shortMonster.name) })
            }
        }
    ) { paddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(Modifier.padding(16.dp)) {
                MainMonsterInfoCard(
                    modifier = Modifier.fillMaxWidth(),
                    monsterPreview = model.shortMonster,
                    monsterInfo = model.monsterInfo
                )
            }
        }
    }
}

