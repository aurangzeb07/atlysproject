package com.atlys.movieatlysassignment.di

import android.content.Context
import com.atlys.movieatlysassignment.BuildConfig
import com.atlys.movieatlysassignment.data.api.ApiModule
import com.atlys.movieatlysassignment.data.api.RemoteDataSource
import com.atlys.movieatlysassignment.data.api.ApiService
import com.atlys.movieatlysassignment.data.database.MovieDatabase
import com.atlys.movieatlysassignment.data.database.MovieDao
import com.atlys.movieatlysassignment.data.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiModule.provideApiService()
    }
    
    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource {
        return RemoteDataSource(apiService)
    }
    
    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return MovieDatabase.getDatabase(context)
    }
    
    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }
    
    @Provides
    @Singleton
    fun provideMovieRepository(
        remoteDataSource: RemoteDataSource,
        movieDao: MovieDao,
        @ApplicationContext context: Context
    ): MovieRepository {
        return MovieRepository(
            remoteDataSource = remoteDataSource,
            movieDao = movieDao,
            context = context,
            apiKey = BuildConfig.TMDB_API_KEY
        )
    }
}


