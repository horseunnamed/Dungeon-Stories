package my.github.dstories.framework

import android.util.Log
import androidx.compose.runtime.Composable

sealed class AsyncRes<out T> {

    object Empty : AsyncRes<Nothing>()
    object Loading : AsyncRes<Nothing>()
    data class Ok<T>(val value: T) : AsyncRes<T>()
    data class Error(val error: Throwable) : AsyncRes<Nothing>()

    fun getOrNull() = (this as? Ok)?.value

    fun <R> map(transform: (T) -> R): AsyncRes<R> {
        return when (this) {
            Empty -> Empty
            Loading -> Loading
            is Error -> this
            is Ok -> Ok(transform(value))
        }
    }

    val isReady: Boolean
        get() = this is Ok

    companion object {
        suspend fun <T> from(
            action: suspend () -> T,
            onResult: (AsyncRes<T>) -> Unit
        ) {
            onResult(Loading)
            try {
                val result = action()
                onResult(Ok(result))
            } catch (error: Throwable) {
                Log.e(null, error.message ?: "Error :(")
                onResult(Error(error))
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
    onEmpty: @Composable () -> Unit = {}
) {
    when (res) {
        AsyncRes.Empty -> onEmpty()
        is AsyncRes.Error -> onError(res.error)
        AsyncRes.Loading -> onLoading()
        is AsyncRes.Ok -> onValue(res.value)
    }
}
