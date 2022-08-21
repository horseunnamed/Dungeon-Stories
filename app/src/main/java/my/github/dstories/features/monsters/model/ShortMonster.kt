package my.github.dstories.features.monsters.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import my.github.dstories.model.ImagePath

@Parcelize
data class ShortMonster(
    val index: String,
    val name: String,
    val type: MonsterType,
    val portrait: ImagePath?,
    val hitPoints: Int,
    val armorClass: Int,
    val challengeRating: ChallengeRating
) : Parcelable
