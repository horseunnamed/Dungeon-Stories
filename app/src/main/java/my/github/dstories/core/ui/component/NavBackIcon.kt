package my.github.dstories.core.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.github.terrakok.modo.Back
import my.github.dstories.app

@Composable
fun NavBackIcon() {
    val modo = LocalContext.current.app.modo

    IconButton(onClick = { modo.dispatch(Back) }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = null
        )
    }
}