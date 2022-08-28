package my.github.dstories.core.model

data class Monster(
    val index: String,
    val name: String,
    val type: MonsterType,
    val portrait: ImagePath?,
    val hitPoints: Int,
    val armorClass: Int,
    val challengeRating: ChallengeRating,
    val hitDie: String,
    val speed: String,
    val abilityScores: AbilityScoresValues
)