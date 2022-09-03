package my.github.dstories

import android.app.Application
import android.content.Context
import com.github.terrakok.modo.*
import com.github.terrakok.modo.android.compose.AppReducer
import my.github.dstories.core.data.DndGraphQlApi
import my.github.dstories.core.data.DndRestApi
import my.github.dstories.feature.character_editor.old.OldCharacterEditorTea
import my.github.dstories.feature.characters_list.CharactersListTea
import my.github.dstories.feature.characters_list.CharactersStoreTea
import my.github.dstories.feature.dices.DicesTea
import my.github.dstories.feature.monster_info.MonsterInfoTea
import my.github.dstories.feature.monsters_catalog.MonstersCatalogTea
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
                    single { DndGraphQlApi(context = get()) }
                    single { CharactersListTea.Runtime(get(), get()) }
                    factory { params ->
                        OldCharacterEditorTea.Runtime(
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
                            monsterPreview = params.get(),
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
