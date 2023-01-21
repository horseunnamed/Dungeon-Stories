package my.github.dstories

import com.github.terrakok.modo.stack.StackNavModel
import com.github.terrakok.modo.stack.StackScreen
import kotlinx.parcelize.Parcelize

@Parcelize
internal class RootScreen(
    private val stackNavModel: StackNavModel
) : StackScreen(stackNavModel)
