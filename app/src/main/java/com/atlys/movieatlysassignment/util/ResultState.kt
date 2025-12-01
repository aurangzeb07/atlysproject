package com.atlys.movieatlysassignment.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

sealed class ResultState<out T> {
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error(val throwable: Throwable) : ResultState<Nothing>()
    data object Loading : ResultState<Nothing>()
}

// Wrap a suspend API call as a Flow<ResultState<T>> with Loading/Success/Error
fun <T> resultStateFlow(call: suspend () -> T): Flow<ResultState<T>> {
    return flow {
        emit(ResultState.Loading)
        val data = call()
        emit(ResultState.Success(data))
    }.catch { e ->
        emit(ResultState.Error(e))
    }
}


