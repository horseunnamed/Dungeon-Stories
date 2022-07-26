package my.github.dstories

import android.app.Application
import android.content.Context
import com.github.terrakok.modo.*
import com.github.terrakok.modo.android.compose.AppReducer
import my.github.dstories.data.DndRestApi
import my.github.dstories.data.DndGraphQlApi
import my.github.dstories.data.DndRepository
import my.github.dstories.features.*
import my.github.dstories.features.monsters.MonstersCatalogTea
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
                    single { MonstersCatalogTea.Runtime(get()) }
                    single { DicesTea.Runtime() }
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
