package my.github.dstories.ui.theme

import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

object TransparentTopAppBarColors : TopAppBarColors {

    @Composable
    override fun actionIconContentColor(scrollFraction: Float): State<Color> {
        return TopAppBarDefaults.smallTopAppBarColors().actionIconContentColor(scrollFraction)
    }

    @Composable
    override fun containerColor(scrollFraction: Float): State<Color> {
        return mutableStateOf(Color.Transparent)
    }

    @Composable
    override fun navigationIconContentColor(scrollFraction: Float): State<Color> {
        return TopAppBarDefaults.smallTopAppBarColors().navigationIconContentColor(scrollFraction)
    }

    @Composable
    override fun titleContentColor(scrollFraction: Float): State<Color> {
        return TopAppBarDefaults.smallTopAppBarColors().titleContentColor(scrollFraction)
    }

}