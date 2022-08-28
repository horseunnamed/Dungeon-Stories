package my.github.dstories.features.monsters.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import my.github.dstories.ui.component.ErrorUi

@Composable
fun MonstersLoadingError(onRetryClick: () -> Unit) {
    ErrorUi(
        modifier = Modifier.fillMaxWidth(),
        errorText = "Loading error, sorry :(",
        retryButtonText = "Retry Request",
        onRetryClick = onRetryClick
    )
}

