package my.github.dstories

import android.app.Application
import android.content.Context
import my.github.dstories.core.data.DndGraphQlApi
import my.github.dstories.core.data.DndRestApi
import my.github.dstories.feature.characters.CharactersRepository
import my.github.dstories.feature.characters.catalog.CharactersCatalogViewModel
import my.github.dstories.feature.characters.catalog.CharactersStoreTea
import my.github.dstories.feature.characters.editor.CharacterEditorTea
import my.github.dstories.feature.monsters.catalog.MonstersCatalogTea
import my.github.dstories.feature.monsters.details.MonsterInfoTea
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
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
                    single { CharactersRepository() }
                    viewModel { CharactersCatalogViewModel(get()) }
                    factory { params ->
                        CharacterEditorTea.Runtime(
                            characterId = params.get(),
                            charactersStore = get(),
                            dndApi = get()
                        )
                    }
                    single { CharactersStoreTea.Runtime() }
                    single { MonstersCatalogTea.Runtime(get()) }
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
