package my.github.dstories.model

import kotlin.math.max

data class DndCharacter(
    val id: Id,
    val name: String,
    val portrait: ImagePath?,
    val race: Race,
    val dndClass: DndClass,
    val abilityScoresValues: AbilityScoresValues
) {

    companion object {
        val SampleNames = listOf(
            "April Lovegate",
            "Pat Simmons",
            "Elvis Presley",
            "Dan Paul",
            "Brian Karmak"
        )
    }

    data class Race(
        val name: String,
        val apiIndex: String
    ) {
        companion object {
            val Available = listOf(
                Race("Dragonborn", "dragonborn"),
                Race("Dwarf", "dwarf"),
                Race("Elf", "elf"),
                Race("Gnome", "gnome"),
                Race("Halfling", "halfling"),
                Race("Half-Elf", "half-elf"),
                Race("Half-Orc", "half-orc"),
                Race("Human", "human"),
                Race("Tiefling", "tiefling")
            )
        }
    }

    data class RaceInfo(
        val name: String,
        val speed: Int,
        val alignment: String,
        val abilityBonuses: Map<AbilityScore, Int>
    )

    data class DndClass(
        val name: String
    ) {
        companion object {
            val Available = listOf(
                DndClass("Artificier"),
                DndClass("Barbarian"),
                DndClass("Bard"),
                DndClass("Cleric"),
                DndClass("Druid"),
                DndClass("Fighter"),
                DndClass("Monk"),
                DndClass("Paladin"),
            )
        }
    }

    enum class AbilityScore {
        Str, Dex, Con, Int, Wis, Cha
    }

    data class AbilityScoreValue(
        val base: Int,
        val raceBonus: Int
    ) {
        val modifier: Int
            get() = (base - 10).floorDiv(2)

        val totalBonus: Int
            get() = raceBonus

        val total: Int
            get() = base + raceBonus

        val increaseCost: Int
            get() = pointBuyCost(base + 1)

        val currentCost: Int
            get() = pointBuyCost(base)

        val isMax: Boolean
            get() = base >= 15

        val isMin: Boolean
            get() = base <= 8

        companion object {

            fun pointBuyCost(value: Int): Int = when {
                value < 9 -> 0
                value in (9..13) -> value - 8
                value in (14..15) -> (value - 8) + (value - 13)
                else -> Int.MAX_VALUE
            }

        }

    }

    data class AbilityScoresValues(
        val strength: AbilityScoreValue,
        val dexterity: AbilityScoreValue,
        val constitution: AbilityScoreValue,
        val intelligence: AbilityScoreValue,
        val wisdom: AbilityScoreValue,
        val charisma: AbilityScoreValue
    ) {

        private val pointsCost: Int
            get() = AbilityScore.values().sumOf { abilityScore ->
                this[abilityScore].currentCost
            }

        val freePoints: Int
            get() = max(27 - pointsCost, 0)

        fun canIncrease(abilityScore: AbilityScore) =
            !this[abilityScore].isMax && freePoints > this[abilityScore].increaseCost

        fun canDecrease(abilityScore: AbilityScore) = !this[abilityScore].isMin

        fun increase(abilityScore: AbilityScore) = update(abilityScore) {
            if (canIncrease(abilityScore)) {
                copy(base = base + 1)
            } else {
                this
            }
        }

        fun decrease(abilityScore: AbilityScore) = update(abilityScore) {
            if (canDecrease(abilityScore)) {
                copy(base = base - 1)
            } else {
                this
            }
        }

        fun applyRaceBonuses(raceBonuses: Map<AbilityScore, Int>): AbilityScoresValues {
            return AbilityScore.values().scan(this) { abilityScoreValues, abilityScore ->
                abilityScoreValues.update(abilityScore) {
                    copy(raceBonus = raceBonuses[abilityScore] ?: 0)
                }
            }.last()
        }

        fun update(
            abilityScore: AbilityScore,
            updateValue: AbilityScoreValue.() -> AbilityScoreValue
        ): AbilityScoresValues {
            return when (abilityScore) {
                AbilityScore.Str -> copy(strength = updateValue(strength))
                AbilityScore.Dex -> copy(dexterity = updateValue(dexterity))
                AbilityScore.Con -> copy(constitution = updateValue(constitution))
                AbilityScore.Int -> copy(intelligence = updateValue(intelligence))
                AbilityScore.Wis -> copy(wisdom = updateValue(wisdom))
                AbilityScore.Cha -> copy(charisma = updateValue(charisma))
            }
        }

        operator fun get(abilityScore: AbilityScore): AbilityScoreValue {
            return when (abilityScore) {
                AbilityScore.Str -> strength
                AbilityScore.Dex -> dexterity
                AbilityScore.Con -> constitution
                AbilityScore.Int -> intelligence
                AbilityScore.Wis -> wisdom
                AbilityScore.Cha -> charisma
            }
        }

        companion object {
            val Default = AbilityScoresValues(
                strength = AbilityScoreValue(8, 0),
                dexterity = AbilityScoreValue(8, 0),
                constitution = AbilityScoreValue(8, 0),
                intelligence = AbilityScoreValue(8, 0),
                wisdom = AbilityScoreValue(8, 0),
                charisma = AbilityScoreValue(8, 0)
            )
        }
    }

}
