package my.github.dstories.features.monsters.model

@JvmInline
value class ChallengeRating(val value: Double) {

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