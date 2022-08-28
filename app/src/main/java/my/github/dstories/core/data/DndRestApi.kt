package my.github.dstories.core.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import my.github.dstories.core.model.DndCharacter
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface DndRestApi {

    // https://www.dnd5eapi.co/docs/#get-/api/races/-index-
    @GET("api/races/{index}")
    suspend fun getRaceInfo(@Path("index") index: String): Race

    @Serializable
    data class Race(
        val name: String,
        val speed: Int,
        @SerialName("ability_bonuses") val abilityBonuses: List<AbilityBonus>,
        val alignment: String,
    ) {
        fun toDomain() = DndCharacter.RaceInfo(
            name = name,
            alignment = alignment,
            speed = speed,
            abilityBonuses = abilityBonuses
                .associateBy { it.abilityScore.toDomain() }
                .mapValues { it.value.bonus }
        )
    }

    @Serializable
    data class AbilityBonus(
        @SerialName("ability_score") val abilityScore: AbilityScore,
        val bonus: Int
    )

    @Serializable
    data class AbilityScore(
        val index: String,
        val name: String
    ) {
        fun toDomain() = when (name) {
            "CHA" -> my.github.dstories.core.model.AbilityScore.Cha
            "CON" -> my.github.dstories.core.model.AbilityScore.Con
            "DEX" -> my.github.dstories.core.model.AbilityScore.Dex
            "INT" -> my.github.dstories.core.model.AbilityScore.Int
            "STR" -> my.github.dstories.core.model.AbilityScore.Str
            "WIS" -> my.github.dstories.core.model.AbilityScore.Wis
            else -> error("No matching ability score for \"$name\"")
        }
    }

    companion object {
        private val contentType = "application/json".toMediaType()

        private val json = Json(builderAction = { ignoreUnknownKeys = true })

        @OptIn(ExperimentalSerializationApi::class)
        fun create(): DndRestApi = Retrofit.Builder()
            .baseUrl("https://www.dnd5eapi.co")
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(DndRestApi::class.java)

    }

}

