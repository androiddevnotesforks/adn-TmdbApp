@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tmdbapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tmdbapp.viewmodel.MovieViewModel
import com.example.tmdbapp.ui.components.MovieItem
import androidx.compose.material3.SmallTopAppBar as SmallTopAppBar
import androidx.compose.material3.TopAppBarDefaults as TopAppBarDefaults

@Composable
fun FavoritesScreen(
    viewModel: MovieViewModel,
    onMovieClick: (Int) -> Unit,
    onBackPress: () -> Unit
) {
    val favorites by viewModel.favorites.collectAsState()

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Favorites", style = typography.headlineMedium) },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = colorScheme.surface,
                    titleContentColor = colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No favorites yet")
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(paddingValues)
            ) {
                items(favorites) { movie ->
                    MovieItem(
                        movie = movie,
                        modifier = Modifier.clickable { onMovieClick(movie.id) },
                        onFavoriteClick = { viewModel.toggleFavorite(movie) }
                    )
                }
            }
        }
    }
}