package my.github.dstories.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import my.github.dstories.features.monsters.model.ChallengeRating
import my.github.dstories.features.monsters.model.ShortMonster
import my.github.dstories.graphql.MonsterQuery
import my.github.dstories.graphql.MonstersQuery
import my.github.dstories.graphql.type.MonsterType
import my.github.dstories.model.AbilityScoreValue
import my.github.dstories.model.AbilityScoresValues
import my.github.dstories.model.ImagePath

typealias DomainMonsterType = my.github.dstories.features.monsters.model.MonsterType
typealias DomainMonster = my.github.dstories.features.monster.model.Monster

class DndGraphQlApi(
    private val apolloClient: ApolloClient = ApolloClient.Builder()
        .serverUrl("https://www.dnd5eapi.co/graphql")
        .build()
) {

    suspend fun getMonsters(): ApolloResponse<MonstersQuery.Data> {
        return apolloClient.query(MonstersQuery(limit = 1000))
            .execute()
    }

    suspend fun getMonster(index: String, portrait: ImagePath?): DomainMonster {
        return apolloClient.query(MonsterQuery(index = index))
            .execute()
            .dataAssertNoErrors
            .let { it.monster!!.toDomain(portrait) }
    }

}

fun MonstersQuery.Monster.toDomain(portrait: ImagePath?): ShortMonster {
    return ShortMonster(
        index = index,
        name = name,
        type = type.toDomain(),
        hitPoints = hit_points,
        armorClass = armor_class,
        challengeRating = ChallengeRating(challenge_rating),
        portrait = portrait
    )
}

fun MonsterQuery.Monster.toDomain(portrait: ImagePath?): DomainMonster {
    return DomainMonster(
        index = index,
        name = name,
        type = type.toDomain(),
        hitPoints = hit_points,
        armorClass = armor_class,
        challengeRating = ChallengeRating(challenge_rating),
        portrait = portrait,
        hitDie = hit_dice,
        speed = speed.toString(),
        abilityScores = AbilityScoresValues(
            strength = AbilityScoreValue(strength),
            dexterity = AbilityScoreValue(dexterity),
            constitution = AbilityScoreValue(constitution),
            intelligence = AbilityScoreValue(intelligence),
            wisdom = AbilityScoreValue(wisdom),
            charisma = AbilityScoreValue(charisma)
        )
    )
}

private fun MonsterType.toDomain(): DomainMonsterType {
    return when (this) {
        MonsterType.ABERRATION -> DomainMonsterType.Aberration
        MonsterType.BEAST -> DomainMonsterType.Beast
        MonsterType.CELESTIAL -> DomainMonsterType.Celestial
        MonsterType.CONSTRUCT -> DomainMonsterType.Construct
        MonsterType.DRAGON -> DomainMonsterType.Dragon
        MonsterType.ELEMENTAL -> DomainMonsterType.Elemental
        MonsterType.FEY -> DomainMonsterType.Fey
        MonsterType.FIEND -> DomainMonsterType.Fiend
        MonsterType.GIANT -> DomainMonsterType.Giant
        MonsterType.HUMANOID -> DomainMonsterType.Humanoid
        MonsterType.MONSTROSITY -> DomainMonsterType.Monstrosity
        MonsterType.OOZE -> DomainMonsterType.Ooze
        MonsterType.PLANT -> DomainMonsterType.Plant
        MonsterType.SWARM -> DomainMonsterType.Swarm
        MonsterType.UNDEAD -> DomainMonsterType.Undead
        MonsterType.UNKNOWN__ -> DomainMonsterType.Other
    }
}
