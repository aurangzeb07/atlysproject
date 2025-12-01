package com.atlys.movieatlysassignment.data.api

import com.atlys.movieatlysassignment.data.model.TrendingMoviesResponse
import com.atlys.movieatlysassignment.util.ResultState
import com.atlys.movieatlysassignment.util.resultStateFlow
import kotlinx.coroutines.flow.Flow

class RemoteDataSource(
    private val service: ApiService
) {
    companion object {
        private const val LANGUAGE = "en-US"
    }
    fun getTrendingMovies(
        apiKey: String,
        language: String = LANGUAGE
    ): Flow<ResultState<TrendingMoviesResponse>> {
        return resultStateFlow {
            service.getTrendingMovies(language = language, apiKey = apiKey)
        }
    }
}


