package my.github.dstories.model

data class DndCharacter(
    val id: Id,
    val name: String,
    val portrait: ImagePath?,
    val race: Race,
    val dndClass: DndClass
) {

    companion object {
        val RandomNames = listOf(
            "April Lovegate",
            "Pat Simmons",
            "Elvis Presley",
            "Dan Paul",
            "Brian Karmak"
        )
    }

    data class Race(
        val name: String
    ) {
        companion object {
            val Available = listOf(
                Race("Human"),
                Race("Elf"),
                Race("Dragonborn"),
                Race("Dwarf"),
                Race("Gnome"),
                Race("Half-Elf"),
                Race("Halfling"),
                Race("Half-Orc"),
            )
        }
    }

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
