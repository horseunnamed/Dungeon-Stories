package my.github.dstories

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.github.terrakok.modo.android.compose.ComposeRenderImpl
import com.github.terrakok.modo.android.compose.init
import com.github.terrakok.modo.android.compose.saveState
import com.github.terrakok.modo.back
import my.github.dstories.feature.Home
import my.github.dstories.core.ui.theme.DungeonStoriesTheme

class AppActivity : ComponentActivity() {

    private val render = ComposeRenderImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        app.modo.init(savedInstanceState) { Home.Screen() }

        setContent {
            DungeonStoriesTheme {
                render.Content()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        app.modo.render = render
    }

    override fun onPause() {
        super.onPause()
        app.modo.render = null
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        app.modo.saveState(outState)
    }

    override fun onBackPressed() {
        if (app.modo.state.chain.size <= 1) {
            super.onBackPressed()
        } else {
            app.modo.back()
        }
    }

}
