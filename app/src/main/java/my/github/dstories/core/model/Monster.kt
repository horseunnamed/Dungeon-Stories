package my.github.dstories.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Monster(
    val index: String,
    val name: String,
    val type: Type,
    val portrait: ImagePath?,
    val hitPoints: Int,
    val armorClass: Int,
    val challengeRating: ChallengeRating,
    val hitDie: String,
    val speed: String,
    val abilityScores: AbilityScoresValues
) {

    @Parcelize
    data class Preview(
        val index: String,
        val name: String,
        val type: Type,
        val portrait: ImagePath?,
        val hitPoints: Int,
        val armorClass: Int,
        val challengeRating: ChallengeRating
    ) : Parcelable

    enum class Type {
        Giant,
        Plant,
        Ooze,
        Monstrosity,
        Fiend,
        Fey,
        Undead,
        Elemental,
        Dragon,
        Construct,
        Humanoid,
        Celestial,
        Beast,
        Aberration,
        Swarm,
        Other
    }

}