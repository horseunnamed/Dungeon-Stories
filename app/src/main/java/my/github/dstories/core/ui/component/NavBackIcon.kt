package my.github.dstories.core.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.github.terrakok.modo.LocalContainerScreen
import com.github.terrakok.modo.stack.Back

@Composable
fun NavBackIcon() {
    val parentScreen = LocalContainerScreen.current
    IconButton(onClick = { parentScreen.dispatch(Back) }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = null
        )
    }
}