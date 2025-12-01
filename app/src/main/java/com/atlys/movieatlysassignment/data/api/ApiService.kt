package com.atlys.movieatlysassignment.data.api

import com.atlys.movieatlysassignment.data.model.TrendingMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): TrendingMoviesResponse
}


