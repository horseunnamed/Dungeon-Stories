package my.github.dstories.features.monster.model

import my.github.dstories.features.monsters.model.ChallengeRating
import my.github.dstories.features.monsters.model.MonsterType
import my.github.dstories.model.ImagePath

data class Monster(
    val index: String,
    val name: String,
    val type: MonsterType,
    val portrait: ImagePath?,
    val hitPoints: Int,
    val armorClass: Int,
    val challengeRating: ChallengeRating,
    val hitDie: String,
    val speed: String
)