package my.github.dstories.feature.character_editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.github.terrakok.modo.android.compose.ComposeScreen
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.parcelize.Parcelize


@Parcelize
class CharacterEditorScreen(
    override val screenKey: String = "CharacterEditorScreenKey"
) : ComposeScreen(screenKey) {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val scrollBehavior = topBarScrollBehavior()
        var isRefreshing by remember { mutableStateOf(false) }

        SwipeRefresh(
            modifier = Modifier.statusBarsPadding(),
            indicatorPadding = PaddingValues(vertical = scrollBehavior.topBarHeight()),
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = { isRefreshing = true }
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopBar(
                        modifier = Modifier
                            .statusBarsPadding()
                            .fillMaxWidth(),
                        titleText = "Really Long Two Lines Title String",
                        scrollBehavior = scrollBehavior
                    )
                }
            ) { paddingValues ->
                LazyColumn(
                    contentPadding = paddingValues,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (i in 1..3000) {
                        item {
                            Text(modifier = Modifier.fillMaxWidth(), text = "$i")
                        }
                    }
                }

            }
        }
    }
}
