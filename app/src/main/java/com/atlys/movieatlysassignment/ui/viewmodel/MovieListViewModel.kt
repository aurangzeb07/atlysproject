package com.atlys.movieatlysassignment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atlys.movieatlysassignment.data.model.Movie
import com.atlys.movieatlysassignment.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.atlys.movieatlysassignment.util.ResultState

data class MovieListUiState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

@OptIn(FlowPreview::class)
@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MovieListUiState(isLoading = true))
    val uiState: StateFlow<MovieListUiState> = _uiState.asStateFlow()
    
    private val searchQueryFlow = MutableStateFlow("")


    init {
        loadTrendingMovies()
        // Debounced search pipeline: 250ms, start after 2+ chars, clear => trending
        viewModelScope.launch {
            searchQueryFlow
                .debounce(SEARCH_DEBOUNCE)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isBlank()) {
                        loadTrendingMovies()
                        return@collectLatest
                    }
                    if (query.length < 2) {
                        return@collectLatest
                    }
                    repository.searchMovies(query).collect { state ->
                        when (state) {
                            is ResultState.Loading -> {
                                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                            }
                            is ResultState.Success -> {
                                _uiState.value = _uiState.value.copy(
                                    movies = state.data,
                                    isLoading = false,
                                    error = null
                                )
                            }
                            is ResultState.Error -> {
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    error = state.throwable.message ?: "Search failed"
                                )
                            }
                        }
                    }
                }
        }
    }
    
    fun loadTrendingMovies() {
        viewModelScope.launch {
            repository.getTrendingMovies().collect { state ->
                when (state) {
                    is ResultState.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    }
                    is ResultState.Success -> {
                        _uiState.value = _uiState.value.copy(
                            movies = state.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is ResultState.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = state.throwable.message ?: "Failed to load movies"
                        )
                    }
                }
            }
        }
    }
    
    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        searchQueryFlow.value = query
    }

    companion object {
        const val SEARCH_DEBOUNCE = 250L
    }
}

