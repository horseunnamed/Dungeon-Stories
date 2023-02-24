package my.github.dstories.feature.monsters.catalog

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import my.github.dstories.core.data.DndGraphQlApi
import my.github.dstories.core.framework.AsyncRes
import my.github.dstories.core.framework.TeaRuntime
import my.github.dstories.core.model.ChallengeRating
import my.github.dstories.core.model.Monster

object MonstersCatalogTea {

    data class Model(
        val monsters: AsyncRes<List<Monster.Preview>>,
        val filter: Filter,
        val showSearchBar: Boolean,
        val shouldFocusSearchBar: Boolean,
        val searchText: String
    ) {

        data class Filter(
            val challengeRatingFrom: ChallengeRating?,
            val challengeRatingTo: ChallengeRating?,
            val monsterTypes: Set<Monster.Type>
        ) {

            val isEmpty: Boolean
                get() = challengeRatingFrom == null
                        && challengeRatingTo == null
                        && monsterTypes.isEmpty()

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

        val filteredMonsters: List<Monster.Preview>?
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
        data class LoadingResult(val monsters: AsyncRes<List<Monster.Preview>>) : Msg()
        object OnOpenSearchClick : Msg()
        object OnCloseSearchClick : Msg()
        object OnSearchBarFocus : Msg()
        object OnOpenFilterClick : Msg()
        object OnCloseFilterClick : Msg()
        data class OnMonsterClick(val monster: Monster.Preview) : Msg()
        data class OnSearchInput(val text: String) : Msg()

        sealed class Filter : Msg() {
            object Clear : Filter()
            data class OnFromChallengeRatingSelected(val value: ChallengeRating?) : Filter()
            data class OnToChallengeRatingSelected(val value: ChallengeRating?) : Filter()
            data class OnMonsterTypeClick(val value: Monster.Type) : Filter()
        }
    }

    sealed class Cmd {
        object LoadMonsters : Cmd()
        object OpenFilterScreen : Cmd()
        data class OpenMonsterInfoScreen(val monster: Monster.Preview) : Cmd()
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

            Msg.Filter.Clear -> updateFilter {
                copy(
                    challengeRatingTo = null,
                    challengeRatingFrom = null,
                    monsterTypes = emptySet()
                )
            } to emptySet()

            is Msg.OnMonsterClick -> model to setOf(Cmd.OpenMonsterInfoScreen(msg.monster))
        }
    }

    class Runtime(
        private val dndGraphQlApi: DndGraphQlApi,
    ) : TeaRuntime<Model, Msg, Cmd>(
        initialModel = Model(
            monsters = AsyncRes.Empty,
            filter = Model.Filter(null, null, emptySet()),
            showSearchBar = false,
            shouldFocusSearchBar = false,
            searchText = ""
        ),
        initialCmd = { setOf(Cmd.LoadMonsters) },
        update = MonstersCatalogTea::update
    ) {
        override suspend fun perform(cmd: Cmd, dispatch: (Msg) -> Unit) {
            when (cmd) {
                Cmd.LoadMonsters -> {
                    withContext(Dispatchers.IO) {
                        AsyncRes.from(
                            action = { dndGraphQlApi.fetchMonsters() },
                            onResult = { dispatch(Msg.LoadingResult(it)) }
                        )
                    }
                }

                Cmd.OpenFilterScreen -> {
                    // TODO migrate to navigation-compose
                    // modo.forward(MonstersFilterScreen())
                }

                Cmd.CloseFilterScreen -> {
                    // TODO migrate to navigation-compose
                    // modo.backTo("HomeScreen")
                }

                is Cmd.OpenMonsterInfoScreen -> {
                    // TODO migrate to navigation-compose
                    // modo.forward(MonsterInfoScreen(cmd.monster))
                }
            }
        }
    }

}
