package my.github.dstories.core.data

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import my.github.dstories.core.model.*
import my.github.dstories.graphql.MonsterQuery
import my.github.dstories.graphql.MonstersQuery
import my.github.dstories.graphql.type.MonsterType
import java.util.stream.Collectors

typealias DomainMonsterType = Monster.Type
typealias DomainMonster = Monster

class DndGraphQlApi(
    private val context: Context,
    private val apolloClient: ApolloClient = ApolloClient.Builder()
        .serverUrl("https://www.dnd5eapi.co/graphql")
        .build()
) {

    suspend fun fetchMonsters(): List<Monster.Preview> {
        val monsterPortraits = fetchMonsterPortraits()

        return apolloClient.query(MonstersQuery(limit = 1000))
            .execute()
            .dataAssertNoErrors
            .monsters!!
            .map { networkMonster ->
                val portraitUrl = monsterPortraits[networkMonster.name.lowercase()]?.let {
                    "$it/revision/latest/scale-to-width-down/300"
                }

                networkMonster.toDomain(portraitUrl?.let(::ImagePath))
            }
    }

    suspend fun fetchMonster(index: String, portrait: ImagePath?): DomainMonster {
        return apolloClient.query(MonsterQuery(index = index))
            .execute()
            .dataAssertNoErrors
            .let { it.monster!!.toDomain(portrait) }
    }

    private fun fetchMonsterPortraits(): Map<String, String> {
        return context
            .assets
            .open("monster_portraits.json")
            .bufferedReader().use {
                val contents = it.lines().collect(Collectors.joining())
                Json.decodeFromString<Map<String, String>>(contents)
            }
            .mapKeys { it.key.lowercase() }
    }

}

fun MonstersQuery.Monster.toDomain(portrait: ImagePath?): Monster.Preview {
    return Monster.Preview(
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
        speed = speed.walk ?: "0 ft.",
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
        MonsterType.ABERRATION -> Monster.Type.Aberration
        MonsterType.BEAST -> Monster.Type.Beast
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
