package my.github.dstories.data

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import my.github.dstories.model.ImagePath
import my.github.dstories.features.monsters.model.ShortMonster
import java.util.stream.Collectors

class DndRepository(
    private val context: Context,
    private val graphQlApi: DndGraphQlApi,
    private val restApi: DndRestApi
) {

    suspend fun fetchMonsters(): List<ShortMonster> {
        val monsterPortraits = context
            .assets
            .open("monster_portraits.json")
            .bufferedReader().use {
                val contents = it.lines().collect(Collectors.joining())
                Json.decodeFromString<Map<String, String>>(contents)
            }
            .mapKeys { it.key.lowercase() }

        return graphQlApi.getMonsters().dataAssertNoErrors.monsters.map { networkMonster ->
            val portraitUrl = monsterPortraits[networkMonster.name!!.lowercase()]?.let {
                "$it/revision/latest/scale-to-width-down/500"
            }

            networkMonster.toDomain(portraitUrl?.let(::ImagePath))
        }
    }

}