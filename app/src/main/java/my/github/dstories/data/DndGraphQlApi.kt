package my.github.dstories.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import my.github.dstories.graphql.MonstersQuery
import my.github.dstories.model.ImagePath
import my.github.dstories.model.ShortMonster

class DndGraphQlApi(
    private val apolloClient: ApolloClient = ApolloClient.Builder()
        .serverUrl("https://www.dnd5eapi.co/graphql")
        .build()
) {

    suspend fun getMonsters(): ApolloResponse<MonstersQuery.Data> {
        return apolloClient.query(MonstersQuery(limit = Optional.Present(1000)))
            .execute()
    }

}

fun MonstersQuery.Monster.toDomain(portrait: ImagePath?): ShortMonster {
    return ShortMonster(
        index = index!!,
        name = name!!,
        type = type!!,
        hitPoints = hit_points!!.toInt(),
        armorClass = armor_class!!.toInt(),
        challengeRating = challenge_rating!!,
        portrait = portrait
    )
}
