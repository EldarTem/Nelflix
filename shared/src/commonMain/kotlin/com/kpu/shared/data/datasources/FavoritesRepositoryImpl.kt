package com.kpu.shared.data.datasources

import com.kpu.shared.data.cache.sqldelight.daos.FavoriteMovieDao
import com.kpu.shared.data.mappers.toDomain
import com.kpu.shared.domain.models.MovieDetails
import com.kpu.shared.domain.repositories.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(private val favoriteMovieDao: FavoriteMovieDao) :
    FavoritesRepository {

    override suspend fun getFavouriteMovies(): Flow<List<MovieDetails>> {
        return favoriteMovieDao.getAllFavoriteMovies()
            .map { it.map { movieDetail -> movieDetail.toDomain() } }
    }
}
