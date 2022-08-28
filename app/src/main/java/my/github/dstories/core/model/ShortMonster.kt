package my.github.dstories.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
