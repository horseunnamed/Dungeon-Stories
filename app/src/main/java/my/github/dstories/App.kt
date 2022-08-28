package my.github.dstories

import android.app.Application
import android.content.Context
import com.github.terrakok.modo.*
import com.github.terrakok.modo.android.compose.AppReducer
import my.github.dstories.core.data.DndGraphQlApi
import my.github.dstories.core.data.DndRepository
import my.github.dstories.core.data.DndRestApi
import my.github.dstories.feature.CharacterEditorTea
import my.github.dstories.feature.CharactersListTea
import my.github.dstories.feature.CharactersStoreTea
import my.github.dstories.feature.DicesTea
import my.github.dstories.feature.monster.MonsterInfoTea
import my.github.dstories.feature.monsters.MonstersCatalogTea
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    val modo = Modo(
        LogReducer(
            AppReducer(
                context = this,
                origin = CustomReducer(
                    multiStackReducer = MultiReducer(),
                    fullScreenReducer = ModoReducer()
                )
            )
        )
    )

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                module {
                    single { modo }
                    single { DndRestApi.create() }
                    single { DndGraphQlApi() }
                    single { DndRepository(get(), get(), get()) }
                    single { CharactersListTea.Runtime(get(), get()) }
                    factory { params ->
                        CharacterEditorTea.Runtime(
                            characterId = params.get(),
                            modo = get(),
                            charactersStore = get(),
                            dndApi = get()
                        )
                    }
                    single { CharactersStoreTea.Runtime() }
                    single { MonstersCatalogTea.Runtime(get(), get()) }
                    single { DicesTea.Runtime() }
                    factory { params ->
                        MonsterInfoTea.Runtime(
                            shortMonster = params.get(),
                            dndGraphQlApi = get()
                        )
                    }
                }
            )
        }
    }

    private class CustomReducer(
        val multiStackReducer: MultiReducer,
        val fullScreenReducer: NavigationReducer
    ) : NavigationReducer {

        override fun invoke(action: NavigationAction, state: NavigationState): NavigationState {
            return when (action) {
                is SelectStack -> multiStackReducer(action, state)
                else -> fullScreenReducer(action, state)
            }
        }

    }

}


val Context.app: App
    get() = applicationContext as App
