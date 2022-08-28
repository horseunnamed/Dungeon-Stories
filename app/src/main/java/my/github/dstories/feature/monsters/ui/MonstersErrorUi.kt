package my.github.dstories.feature.monsters.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import my.github.dstories.core.ui.component.ErrorUi

@Composable
fun MonstersLoadingError(onRetryClick: () -> Unit) {
    ErrorUi(
        modifier = Modifier.fillMaxWidth(),
        errorText = "Loading error, sorry :(",
        retryButtonText = "Retry Request",
        onRetryClick = onRetryClick
    )
}

