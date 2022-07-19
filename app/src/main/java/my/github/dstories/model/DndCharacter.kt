package my.github.dstories.model

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
        val alignment: String
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
    }

    data class AbilityScoresValues(
        val strength: AbilityScoreValue,
        val dexterity: AbilityScoreValue,
        val constitution: AbilityScoreValue,
        val intelligence: AbilityScoreValue,
        val wisdom: AbilityScoreValue,
        val charisma: AbilityScoreValue
    ) {

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
