package com.kpu.shared.ui.screens.favorites

import com.kpu.shared.domain.repositories.FavoritesRepository
import com.kpu.shared.utils.FavouritesUiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository
) : KoinComponent {

    private val _favoriteMoviesState = MutableStateFlow(FavouritesUiState())
    val favoriteMoviesState = _favoriteMoviesState.asStateFlow()

    private val viewModelScope = CoroutineScope(Dispatchers.Default)

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _favoriteMoviesState.update { it.copy(isLoading = false, error = exception.message) }
    }

    init {
        getFavoriteMovie()
    }

    fun getFavoriteMovie() = viewModelScope.launch(coroutineExceptionHandler) {
        favoritesRepository.getFavouriteMovies().collect { favoriteMoviesResult ->
            _favoriteMoviesState.update { it.copy(favoriteMovies = favoriteMoviesResult) }
        }
    }
}
