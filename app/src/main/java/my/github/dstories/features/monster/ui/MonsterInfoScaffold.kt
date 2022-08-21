package my.github.dstories.features.monster.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import my.github.dstories.features.monster.MonsterInfoTea
import my.github.dstories.framework.AsyncContent
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
                Text("Monster Info")
            }
        }
    ) { paddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            AsyncContent(
                res = model.monsterInfo,
                onLoading = { },
                onValue = { },
                onError = { },
                onEmpty = { }
            )
        }
    }
}

