package my.github.dstories

import android.app.Application
import android.content.Context
import com.github.terrakok.modo.*
import com.github.terrakok.modo.android.compose.AppReducer
import my.github.dstories.data.Dnd5EApi
import my.github.dstories.features.CharacterCreationMvu
import my.github.dstories.features.CharactersListMvu
import my.github.dstories.features.CharactersStoreMu
import my.github.dstories.features.DicesMvu
import okhttp3.OkHttpClient
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
                    single { Dnd5EApi.build() }
                    single { CharactersListMvu.Runtime(get(), get()) }
                    factory { CharacterCreationMvu.Runtime(get(), get()) }
                    single { CharactersStoreMu.Runtime() }
                    single { DicesMvu.Runtime() }
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
