package my.github.dstories.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import my.github.dstories.graphql.MonstersQuery

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