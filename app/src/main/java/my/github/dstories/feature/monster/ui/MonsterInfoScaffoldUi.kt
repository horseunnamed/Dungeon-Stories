package my.github.dstories.feature.monster.ui

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
import my.github.dstories.core.ui.component.*
import my.github.dstories.feature.monster.MonsterInfoTea
import my.github.dstories.core.framework.AsyncRes


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
                SmallTopAppBar(
                    navigationIcon = { NavBackIcon() },
                    title = { Text(model.monsterPreview.name) }
                )
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
                    monsterPreview = model.monsterPreview,
                    monsterInfo = model.monsterInfo
                )
                VerticalSpacer(height = 32.dp)
                if (model.monsterInfo !is AsyncRes.Error) {
                    AbilityScoresUi(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        abilityScoresValues = model.monsterInfo.res?.abilityScores
                    )
                } else {
                    ErrorUi(
                        modifier = Modifier.fillMaxWidth(),
                        errorText = "Loading error, sorry :(",
                        retryButtonText = "Retry Request",
                        onRetryClick = { dispatch(MonsterInfoTea.Msg.RetryLoading) }
                    )
                }
            }
        }
    }
}

