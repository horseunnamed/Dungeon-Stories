package my.github.dstories.framework

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