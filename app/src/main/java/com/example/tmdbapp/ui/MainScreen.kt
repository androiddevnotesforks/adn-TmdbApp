package com.example.tmdbapp.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.tmdbapp.models.Movie
import com.example.tmdbapp.ui.theme.LocalThemeMode
import com.example.tmdbapp.ui.theme.ThemeMode
import com.example.tmdbapp.utils.Constants
import com.example.tmdbapp.viewmodel.MovieViewModel

sealed class Screen {
    object List : Screen()
    object Favorites : Screen()
    data class Detail(val movie: Movie) : Screen()
}

@Composable
fun MainScreen(
    movieViewModel: MovieViewModel,
    onThemeChange: (ThemeMode) -> Unit
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.List) }
    var previousScreen by remember { mutableStateOf<Screen>(Screen.List) }
    val selectedMovie by movieViewModel.selectedMovie.collectAsState()
    var viewType by remember { mutableStateOf(Constants.VIEW_TYPE_GRID) }
    val currentThemeMode = LocalThemeMode.current

    BackHandler(enabled = currentScreen != Screen.List) {
        when (currentScreen) {
            is Screen.Detail -> {
                movieViewModel.clearSelectedMovie()
                currentScreen = previousScreen
            }

            is Screen.Favorites -> {
                currentScreen = Screen.List
                previousScreen = Screen.List
            }

            is Screen.List -> {}
        }
    }

    when (currentScreen) {
        is Screen.List -> MovieListScreen(
            viewModel = movieViewModel,
            onMovieClick = {
                movieViewModel.selectMovie(it)
                previousScreen = Screen.List
                currentScreen = Screen.Detail(it)
            },
            onFavoritesClick = {
                currentScreen = Screen.Favorites
                previousScreen = Screen.List
            },
            screenTitle = Constants.SCREEN_TITLE_DISCOVER,  // This will now be "ADN"
            viewType = viewType,
            onViewTypeChange = { newViewType -> viewType = newViewType },
            onThemeChange = {
                val newThemeMode = when (currentThemeMode) {
                    ThemeMode.LIGHT -> ThemeMode.DARK
                    ThemeMode.DARK -> ThemeMode.SYSTEM
                    ThemeMode.SYSTEM -> ThemeMode.LIGHT
                }
                onThemeChange(newThemeMode)
            },
            currentThemeMode = currentThemeMode
        )

        is Screen.Favorites -> FavoritesScreen(
            viewModel = movieViewModel,
            onMovieClick = { movieId ->
                val movie = movieViewModel.getMovieById(movieId)
                if (movie != null) {
                    movieViewModel.selectMovie(movie)
                    previousScreen = Screen.Favorites
                    currentScreen = Screen.Detail(movie)
                }
            },
            onBackPress = {
                currentScreen = Screen.List
                previousScreen = Screen.List
            }
        )

        is Screen.Detail -> MovieDetailScreen(
            viewModel = movieViewModel,
            onBackPress = {
                movieViewModel.clearSelectedMovie()
                currentScreen = previousScreen
            }
        )
    }
}