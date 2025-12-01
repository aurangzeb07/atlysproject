package com.atlys.movieatlysassignment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.atlys.movieatlysassignment.ui.screen.MovieDetailScreen
import com.atlys.movieatlysassignment.ui.screen.MovieListScreen

sealed class Screen(val route: String) {
    object MovieList : Screen("movie_plp")
    object MovieDetail : Screen("movie_pdp/{movieId}") {
        fun createRoute(movieId: Int) = "movie_pdp/$movieId"
    }
}

@Composable
fun HomeScreenComposable(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.MovieList.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.MovieList.route) {
            MovieListScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screen.MovieDetail.createRoute(movieId))
                }
            )
        }
        composable(
            route = Screen.MovieDetail.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            MovieDetailScreen(
                movieId = movieId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

