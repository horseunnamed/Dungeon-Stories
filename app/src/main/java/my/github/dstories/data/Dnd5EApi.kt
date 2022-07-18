package my.github.dstories.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface Dnd5EApi {

    // https://www.dnd5eapi.co/docs/#get-/api/races/-index-
    @GET("/api/races/{index}")
    fun getRaceInfo(@Path("index") index: String): Race

    @Serializable
    data class Race(
        val name: String,
        val speed: Int,
        val abilityBonuses: List<AbilityBonus>,
        val alignment: String,
    )

    @Serializable
    data class AbilityBonus(
        val abilityScore: AbilityScore,
        val bonus: Int
    )

    @Serializable
    data class AbilityScore(
        val index: String,
        val name: String
    )

    companion object {
        private val contentType = "application/json".toMediaType()

        @OptIn(ExperimentalSerializationApi::class)
        fun build() = Retrofit.Builder()
            .baseUrl("https://www.dnd5eapi.co")
            .addConverterFactory(Json.asConverterFactory(contentType))
    }

}

