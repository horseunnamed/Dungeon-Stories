package my.github.dstories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import my.github.dstories.core.ui.theme.DungeonStoriesTheme
import my.github.dstories.feature.Home

class AppActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            DungeonStoriesTheme {
                Home.Content()
            }
        }
    }

}
