package com.example.tmdbapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import com.example.tmdbapp.ui.components.CommonTopBar
import com.example.tmdbapp.ui.components.ItemSimpleUi
import com.example.tmdbapp.ui.viewmodel.AlphaViewModel
import com.example.tmdbapp.utils.Constants

@Composable
fun AlphaListFavoriteScreenUi(
  viewModel: AlphaViewModel,
  onItemClick: (Int) -> Unit,
  onBackPress: () -> Unit,
) {
  val favorites by viewModel.favorites.collectAsState()

  LaunchedEffect(Unit) {
    viewModel.loadFavorites()
  }

  Scaffold(
    topBar = {
      CommonTopBar(
        title = Constants.SCREEN_TITLE_FAVORITES,
        onBackPress = onBackPress,
      )
    },
  ) { paddingValues ->
    if (favorites.isEmpty()) {
      Box(
        modifier =
          Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center,
      ) {
        Text(Constants.MESSAGE_NO_FAVORITES)
      }
    } else {
      LazyColumn(
        contentPadding = PaddingValues(Constants.PADDING_MEDIUM),
        verticalArrangement = Arrangement.spacedBy(Constants.PADDING_SMALL),
        modifier = Modifier.padding(paddingValues),
      ) {
        items(favorites) { item ->
          ItemSimpleUi(
            title = item.title,
            overview = item.overview,
            posterPath = item.posterPath,
            voteAverage = item.voteAverage,
            isFavorite = item.isFavorite,
            modifier = Modifier.clickable { onItemClick(item.id) },
            onFavoriteClick = { viewModel.toggleFavorite(item) },
          )
        }
      }
    }
  }
}
