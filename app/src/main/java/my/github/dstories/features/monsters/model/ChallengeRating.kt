package my.github.dstories.features.monsters.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@JvmInline
value class ChallengeRating(val value: Double) : Parcelable {

    override fun toString() = when (value) {
        0.125 -> "1/8"
        0.25 -> "1/4"
        0.5 -> "1/2"
        else -> value.toInt().toString()
    }

    companion object {
        val AvailableValues: List<ChallengeRating> =
            (listOf(0.0, 0.125, 0.25, 0.5) + (1..30).map { it.toDouble() })
                .map(::ChallengeRating)
    }

}