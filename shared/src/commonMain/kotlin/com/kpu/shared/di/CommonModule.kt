package com.kpu.shared.di

import com.kpu.shared.BuildKonfig
import com.kpu.shared.data.cache.sqldelight.daos.FavoriteMovieDao
import com.kpu.shared.data.datasources.FavoritesRepositoryImpl
import com.kpu.shared.data.datasources.MovieDetailsRepositoryImpl
import com.kpu.shared.data.datasources.MoviesRepositoryImpl
import com.kpu.shared.data.datasources.SettingsRepositoryImpl
import com.kpu.shared.domain.repositories.FavoritesRepository
import com.kpu.shared.domain.repositories.MovieDetailsRepository
import com.kpu.shared.domain.repositories.MoviesRepository
import com.kpu.shared.domain.repositories.SettingsRepository
import com.kpu.shared.domain.utils.Constants.BASE_URL
import com.kpu.shared.domain.utils.Constants.URL_PATH
import com.kpu.shared.presentation.ui.screens.home.HomeViewModel
import com.kpu.shared.presentation.ui.screens.main.MainViewModel
import com.kpu.shared.ui.screens.settings.SettingsViewModel
import com.kpu.shared.ui.screens.details.DetailsViewModel
import com.kpu.shared.ui.screens.favorites.FavoritesViewModel
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.addDefaultResponseValidation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun commonModule(enableNetworkLogs: Boolean) = module {
    /**
     * Creates a http client for Ktor that is provided to the
     * API client via constructor injection
     */
    single {
        HttpClient {
            expectSuccess = true
            addDefaultResponseValidation()

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = BASE_URL
                    path(URL_PATH)
                    parameters.append("api_key", BuildKonfig.API_KEY)
                    parameters.append("language", "ru-RU")
                }
            }

            if (enableNetworkLogs) {
                install(Logging) {
                    level = LogLevel.HEADERS
                    logger = object : Logger {
                        override fun log(message: String) {
                            Napier.i(tag = "Http Client", message = message)
                        }
                    }
                }.also {
                    Napier.base(DebugAntilog())
                }
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
                )
            }
        }
    }



    single { FavoriteMovieDao(databaseDriverFactory = get()) }

    single<MoviesRepository> { MoviesRepositoryImpl(httpClient = get()) }
    single<MovieDetailsRepository> {
        MovieDetailsRepositoryImpl(httpClient = get(), favoriteMovieDao = get())
    }
    single<FavoritesRepository> { FavoritesRepositoryImpl(favoriteMovieDao = get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(observableSettings = get()) }

    singleOf(::MainViewModel)
    singleOf(::HomeViewModel)
    factoryOf(::DetailsViewModel)
    singleOf(::SettingsViewModel)
    singleOf(::FavoritesViewModel)
}

expect fun platformModule(): Module
