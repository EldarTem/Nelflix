package com.kpu.shared.domain.repositories

import com.kpu.shared.domain.models.MovieDetails
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    /**Returns a list of movies that are favourite from the database*/
    suspend fun getFavouriteMovies(): Flow<List<MovieDetails>>
}
