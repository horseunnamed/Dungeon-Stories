package my.github.dstories.features.monsters

import com.github.terrakok.modo.Modo
import com.github.terrakok.modo.backTo
import com.github.terrakok.modo.forward
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import my.github.dstories.data.DndRepository
import my.github.dstories.features.monsters.model.ChallengeRating
import my.github.dstories.features.monsters.model.MonsterType
import my.github.dstories.features.monsters.model.ShortMonster
import my.github.dstories.framework.AsyncRes
import my.github.dstories.framework.TeaRuntime

object MonstersCatalogTea {

    data class Model(
        val monsters: AsyncRes<List<ShortMonster>>,
        val filter: Filter,
        val showSearchBar: Boolean,
        val shouldFocusSearchBar: Boolean,
        val searchText: String
    ) {

        data class Filter(
            val challengeRatingFrom: ChallengeRating?,
            val challengeRatingTo: ChallengeRating?,
            val monsterTypes: Set<MonsterType>
        ) {

            val availableChallengeRatingTo: List<ChallengeRating>
                get() = when (challengeRatingFrom) {
                    null -> ChallengeRating.AvailableValues
                    else -> ChallengeRating.AvailableValues.filter {
                        it.value >= challengeRatingFrom.value
                    }
                }

            val availableChallengeRatingFrom: List<ChallengeRating>
                get() = when (challengeRatingTo) {
                    null -> ChallengeRating.AvailableValues
                    else -> ChallengeRating.AvailableValues.filter {
                        it.value <= challengeRatingTo.value
                    }
                }
        }

        val filteredMonsters: List<ShortMonster>?
            get() = monsters.getOrNull()
                // filter by search input
                ?.filter { monster ->
                    when {
                        searchText.isNotEmpty() -> monster.name.lowercase()
                            .contains(searchText.lowercase())
                        else -> true
                    }
                }
                // filter by challenge rating
                ?.filter { monster ->
                    val challengeRatingFrom = filter.challengeRatingFrom?.value ?: 0.0
                    val challengeRatingTo = filter.challengeRatingTo?.value ?: Double.MAX_VALUE
                    (challengeRatingFrom <= monster.challengeRating.value)
                            && (monster.challengeRating.value <= challengeRatingTo)
                }
                // filter by monster type
                ?.filter {
                    when {
                        filter.monsterTypes.isNotEmpty() -> it.type in filter.monsterTypes
                        else -> true
                    }
                }

        fun updateFilter(update: Filter.() -> Filter) = copy(filter = filter.update())

    }

    sealed class Msg {
        object Load : Msg()
        data class LoadingResult(val monsters: AsyncRes<List<ShortMonster>>) : Msg()
        object OnOpenSearchClick : Msg()
        object OnCloseSearchClick : Msg()
        object OnSearchBarFocus : Msg()
        object OnOpenFilterClick : Msg()
        object OnCloseFilterClick : Msg()
        data class OnSearchInput(val text: String) : Msg()

        sealed class Filter : Msg() {
            data class OnFromChallengeRatingSelected(val value: ChallengeRating?) : Filter()
            data class OnToChallengeRatingSelected(val value: ChallengeRating?) : Filter()
            data class OnMonsterTypeClick(val value: MonsterType) : Filter()
        }
    }

    sealed class Cmd {
        object LoadMonsters : Cmd()
        object OpenFilterScreen : Cmd()
        object CloseFilterScreen : Cmd()
    }

    fun update(model: Model, msg: Msg) = with(model) {
        when (msg) {
            Msg.Load -> when (model.monsters) {
                is AsyncRes.Loading -> {
                    this to emptySet()
                }
                else -> this to setOf(
                    Cmd.LoadMonsters
                )
            }

            is Msg.LoadingResult -> copy(monsters = msg.monsters) to emptySet()

            Msg.OnOpenSearchClick -> copy(
                showSearchBar = true,
                shouldFocusSearchBar = true
            ) to emptySet()

            Msg.OnCloseSearchClick -> copy(
                searchText = "",
                showSearchBar = false
            ) to emptySet()

            is Msg.OnSearchInput ->
                copy(searchText = msg.text) to emptySet()

            Msg.OnSearchBarFocus -> copy(shouldFocusSearchBar = false) to emptySet()

            Msg.OnOpenFilterClick -> this to setOf(Cmd.OpenFilterScreen)

            Msg.OnCloseFilterClick -> this to setOf(Cmd.CloseFilterScreen)

            is Msg.Filter.OnFromChallengeRatingSelected -> updateFilter {
                copy(challengeRatingFrom = msg.value)
            } to emptySet()

            is Msg.Filter.OnToChallengeRatingSelected -> updateFilter {
                copy(challengeRatingTo = msg.value)
            } to emptySet()

            is Msg.Filter.OnMonsterTypeClick -> updateFilter {
                if (msg.value in monsterTypes) {
                    copy(monsterTypes = monsterTypes - msg.value)
                } else {
                    copy(monsterTypes = monsterTypes + msg.value)
                }
            } to emptySet()
        }
    }

    class Runtime(
        private val dndRepository: DndRepository,
        private val modo: Modo
    ) : TeaRuntime<Model, Msg, Cmd>(
        initialModel = Model(
            monsters = AsyncRes.Empty,
            filter = Model.Filter(null, null, emptySet()),
            showSearchBar = false,
            shouldFocusSearchBar = false,
            searchText = ""
        ),
        initialCmd = { setOf(Cmd.LoadMonsters) },
        update = ::update
    ) {
        override suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit) {
            when (cmd) {
                Cmd.LoadMonsters -> {
                    withContext(Dispatchers.IO) {
                        AsyncRes.from(
                            action = { dndRepository.fetchMonsters() },
                            onResult = { dispatch(Msg.LoadingResult(it)) }
                        )
                    }
                }

                Cmd.OpenFilterScreen -> {
                    modo.forward(MonstersFilterScreen())
                }

                Cmd.CloseFilterScreen -> {
                    modo.backTo("HomeScreen")
                }
            }
        }
    }

}
