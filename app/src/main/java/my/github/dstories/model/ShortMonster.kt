package my.github.dstories.model

data class ShortMonster(
    val index: String,
    val name: String,
    val type: String,
    val portrait: ImagePath?,
    val hitPoints: Int,
    val armorClass: Int,
    val challengeRating: Double
)