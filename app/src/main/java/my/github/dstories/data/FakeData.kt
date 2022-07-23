package my.github.dstories.data

import my.github.dstories.model.DndCharacter
import my.github.dstories.model.Id
import my.github.dstories.model.ImagePath
import my.github.dstories.model.ShortMonster

val FakeCharacters = listOf(
    DndCharacter(
        id = Id("123"),
        name = "April Lovegate",
        portrait = ImagePath("https://cdna.artstation.com/p/assets/images/images/021/600/412/large/vika-yarova-snake-lady.jpg?1572293382"),
        race = DndCharacter.Race.Available.first(),
        dndClass = DndCharacter.DndClass("Rogue"),
        abilityScoresValues = DndCharacter.AbilityScoresValues.Default
    )
)

val FakeShortMonsters = listOf(
    ShortMonster(
        index = "aboleth",
        name = "Aboleth",
        type = "aberration",
        hitPoints = 135,
        armorClass = 17,
        challengeRating = 10,
        portrait = ImagePath("https://static.wikia.nocookie.net/forgottenrealms/images/5/58/Monster_Manual_5e_-_Aboleth_-p13.jpg/revision/latest?cb=20200313153917")
    )
)
