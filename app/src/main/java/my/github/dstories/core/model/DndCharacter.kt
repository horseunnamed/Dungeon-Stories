package my.github.dstories.core.model

data class DndCharacter(
    val id: Id,
    val name: String,
    val portrait: ImagePath?,
    val race: Race,
    val dndClass: DndClass,
    val abilityScoresValues: AbilityScoresValues
) {

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


}
