package my.github.dstories.data

import my.github.dstories.model.DndCharacter
import my.github.dstories.model.Id
import my.github.dstories.model.ImagePath

val DefaultCharacters = listOf(
    DndCharacter(
        id = Id("1"),
        name = "April Lovegate",
        portrait = ImagePath("https://cdna.artstation.com/p/assets/images/images/021/600/412/large/vika-yarova-snake-lady.jpg?1572293382"),
        race = DndCharacter.Race("Lizard"),
        dndClass = DndCharacter.DndClass("Rogue")
    )
)