package my.github.dstories.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import my.github.dstories.graphql.MonstersQuery
import my.github.dstories.model.ShortMonster

class Dnd5eGraphQlApi(
    private val apolloClient: ApolloClient = ApolloClient.Builder()
        .serverUrl("https://www.dnd5eapi.co/graphql")
        .build()
) {

    suspend fun getMonsters(): ApolloResponse<MonstersQuery.Data> {
        return apolloClient.query(MonstersQuery(limit = Optional.Present(1000)))
            .execute()
    }

}

fun MonstersQuery.Data.toDomain(): List<ShortMonster> {
    return monsters.map { networkMonster ->
        ShortMonster(
            index = networkMonster.index!!,
            name = networkMonster.name!!,
            type = networkMonster.type!!,
            hitPoints = networkMonster.hit_points!!.toInt(),
            armorClass = networkMonster.armor_class!!.toInt(),
            challengeRating = networkMonster.challenge_rating!!,
            portrait = null
        )
    }
}