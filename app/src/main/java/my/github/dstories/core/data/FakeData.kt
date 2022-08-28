package my.github.dstories.core.data

import my.github.dstories.core.model.AbilityScoresValues
import my.github.dstories.core.model.DndCharacter
import my.github.dstories.core.model.Id
import my.github.dstories.core.model.ImagePath

val FakeCharacters = listOf(
    DndCharacter(
        id = Id("123"),
        name = "April Lovegate",
        portrait = ImagePath("https://cdna.artstation.com/p/assets/images/images/021/600/412/large/vika-yarova-snake-lady.jpg?1572293382"),
        race = DndCharacter.Race.Available.first(),
        dndClass = DndCharacter.DndClass("Rogue"),
        abilityScoresValues = AbilityScoresValues.Default
    )
)

val FakeNames = listOf(
    "April Lovegate",
    "Pat Simmons",
    "Elvis Presley",
    "Dan Paul",
    "Brian Karmak"
)
