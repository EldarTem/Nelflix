package com.kpu.shared.data.datasources

import com.kpu.shared.data.cache.sqldelight.daos.FavoriteMovieDao
import com.kpu.shared.data.mappers.toDomain
import com.kpu.shared.data.network.models.CastDto
import com.kpu.shared.data.network.models.MovieDetailsDto
import com.kpu.shared.data.network.models.MovieResultsDto
import com.kpu.shared.data.network.utils.safeApiCall
import com.kpu.shared.domain.models.Cast
import com.kpu.shared.domain.models.Movie
import com.kpu.shared.domain.models.MovieDetails
import com.kpu.shared.domain.repositories.MovieDetailsRepository
import com.kpu.shared.utils.ResultState
import com.kpu.shared.utils.toBoolean
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MovieDetailsRepositoryImpl(
    private val httpClient: HttpClient,
    private val favoriteMovieDao: FavoriteMovieDao
) : MovieDetailsRepository {

    override suspend fun fetchMovieDetails(movieId: Int): Flow<ResultState<MovieDetails>> {
        val isMovieCached = isMovieFavorite(movieId = movieId)

        return if (isMovieCached == true) {
            try {
                val cachedFavoriteMovie = getFavoriteMovie(movieId = movieId)
                flowOf(ResultState.Success(data = cachedFavoriteMovie))
            } catch (e: Exception) {
                flowOf(ResultState.Failure(exception = e))
            }
        } else {
            flowOf(
                safeApiCall {
                    val response =
                        httpClient.get(urlString = "movie/$movieId").body<MovieDetailsDto>()
                    response.toDomain()
                }
            )
        }
    }

    override suspend fun fetchMovieCast(movieId: Int): Flow<ResultState<Cast>> {
        return flowOf(
            safeApiCall {
                val response = httpClient.get(urlString = "movie/$movieId/credits").body<CastDto>()

                response.toDomain()
            }
        )
    }

    override suspend fun fetchSimilarMovies(
        movieId: Int,
        page: Int
    ): Flow<ResultState<List<Movie>?>> {
        return flowOf(
            safeApiCall {
                val response = httpClient.get(urlString = "movie/$movieId/similar") {
                    parameter("page", page)
                }.body<MovieResultsDto>()

                response.movies?.map { it.toDomain() }
            }
        )
    }

    override suspend fun saveFavoriteMovie(movie: MovieDetails) {
        favoriteMovieDao.saveFavoriteMovie(movie = movie)
    }

    override suspend fun getFavoriteMovie(movieId: Int): MovieDetails {
        return favoriteMovieDao.getFavoriteMovie(movieId = movieId).toDomain()
    }

    override suspend fun deleteFavoriteMovie(movieId: Int) {
        favoriteMovieDao.deleteFavouriteMovie(movieId = movieId)
    }

    override suspend fun isMovieFavorite(movieId: Int): Boolean? {
        return favoriteMovieDao.isMovieFavorite(movieId = movieId)?.toBoolean()
    }
}
