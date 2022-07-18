package my.github.dstories.framework

import androidx.compose.runtime.Composable

sealed class AsyncRes<out T> {

    object Empty : AsyncRes<Nothing>()
    object Loading : AsyncRes<Nothing>()
    data class Ok<T>(val value: T) : AsyncRes<T>()
    data class Error(val error: Throwable) : AsyncRes<Nothing>()

    fun getOrNull() = (this as? Ok)?.value

    companion object {
        suspend fun <T> from(
            action: suspend () -> T,
            dispatch: (AsyncRes<T>) -> Unit
        ) {
            dispatch(Loading)
            try {
                val result = action()
                dispatch(Ok(result))
            } catch (error: Throwable) {
                dispatch(Error(error))
            }
        }
    }

}

@Composable
inline fun <T> AsyncContent(
    res: AsyncRes<T>,
    onLoading: @Composable () -> Unit,
    onValue: @Composable (T) -> Unit,
    onError: @Composable (Throwable) -> Unit,
) {
    when (res) {
        AsyncRes.Empty -> { /* draw nothing */ }
        is AsyncRes.Error -> onError(res.error)
        AsyncRes.Loading -> onLoading()
        is AsyncRes.Ok -> onValue(res.value)
    }
}