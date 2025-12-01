package com.atlys.movieatlysassignment.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.atlys.movieatlysassignment.data.api.RemoteDataSource
import com.atlys.movieatlysassignment.data.database.MovieDao
import com.atlys.movieatlysassignment.data.model.Movie
import com.atlys.movieatlysassignment.util.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class MovieRepository(
    private val remoteDataSource: RemoteDataSource,
    private val movieDao: MovieDao,
    private val context: Context,
    private val apiKey: String
) {
    fun getTrendingMovies(): Flow<ResultState<List<Movie>>> =
        if (isOnline()) {
            remoteDataSource.getTrendingMovies(apiKey = apiKey)
                .transform { state ->
                    when (state) {
                        is ResultState.Loading -> emit(ResultState.Loading)
                        is ResultState.Success -> {
                            val results = state.data.results
                            if (results.isNotEmpty()) {
                                movieDao.deleteAllMovies()
                                movieDao.insertMovies(results)
                                emit(ResultState.Success(results))
                            } else {
                                val cachedMovies = movieDao.getAllMovies().first()
                                if (cachedMovies.isNotEmpty()) {
                                    emit(ResultState.Success(cachedMovies))
                                } else {
                                    emit(ResultState.Error(Exception("No data available")))
                                }
                            }
                        }
                        is ResultState.Error -> {
                            val cachedMovies = movieDao.getAllMovies().first()
                            if (cachedMovies.isNotEmpty()) {
                                emit(ResultState.Success(cachedMovies))
                            } else {
                                emit(ResultState.Error(state.throwable))
                            }
                        }
                    }
                }
        } else {
            flow {
                emit(ResultState.Loading)
                val cachedMovies = movieDao.getAllMovies().first()
                if (cachedMovies.isNotEmpty()) {
                    emit(ResultState.Success(cachedMovies))
                } else {
                    emit(ResultState.Error(Exception("No internet connection and no cached data")))
                }
            }
        }
    
    fun searchMovies(query: String): Flow<ResultState<List<Movie>>> {
        return if (query.isBlank()) {
            getTrendingMovies()
        } else {
            movieDao.searchMovies(query)
                .map { movies -> ResultState.Success(movies) as ResultState<List<Movie>> }
                .catch { e -> emit(ResultState.Error(e)) }
                .transform { emit(ResultState.Loading); emit(it) }
        }
    }
    
    suspend fun getMovieById(movieId: Int): kotlin.Result<Movie> {
        return try {
            val movie = movieDao.getMovieById(movieId)
            if (movie != null) {
                kotlin.Result.success(movie)
            } else {
                kotlin.Result.failure(Exception("Movie not found"))
            }
        } catch (e: Exception) {
            kotlin.Result.failure(e)
        }
    }
    
    private fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities != null && (
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                )
    }
}

