package my.github.dstories.features.monsters.model

import my.github.dstories.model.ImagePath

data class ShortMonster(
    val index: String,
    val name: String,
    val type: MonsterType,
    val portrait: ImagePath?,
    val hitPoints: Int,
    val armorClass: Int,
    val challengeRating: ChallengeRating
)
