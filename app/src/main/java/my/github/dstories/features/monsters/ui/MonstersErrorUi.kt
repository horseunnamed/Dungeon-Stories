package my.github.dstories.features.monsters.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import my.github.dstories.R

@Composable
fun MonstersLoadingError(onRetryClick: () -> Unit) {
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

