package my.github.dstories

import android.app.Application
import android.content.Context
import my.github.dstories.core.data.DndGraphQlApi
import my.github.dstories.core.data.DndRestApi
import my.github.dstories.feature.character_editor.CharacterEditorTea
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

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                module {
                    single { DndRestApi.create() }
                    single { DndGraphQlApi(context = get()) }
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
                            monsterPreview = params.get(),
                            dndGraphQlApi = get()
                        )
                    }
                }
            )
        }
    }

}


val Context.app: App
    get() = applicationContext as App
