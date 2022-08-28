package my.github.dstories.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@JvmInline
value class ImagePath(val value: String) : Parcelable

@Parcelize
@JvmInline
value class Id(val value: String) : Parcelable {
    init {
        require(value.isNotEmpty())
    }

    companion object {
        fun random() = Id(UUID.randomUUID().toString())
    }
}
