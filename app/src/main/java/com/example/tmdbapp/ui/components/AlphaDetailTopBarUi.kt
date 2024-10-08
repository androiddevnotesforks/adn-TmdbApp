package com.example.tmdbapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import com.example.tmdbapp.R
import com.example.tmdbapp.utils.Constants

@Composable
fun AlphaDetailTopBarUi(
  onBackPress: () -> Unit,
  onFavoriteClick: () -> Unit,
  onDownloadClick: () -> Unit,
  onAskAiClick: () -> Unit,
  isFavorite: Boolean,
  textColor: Color,
) {
  TopAppBar(
    title = { },
    navigationIcon = {
      IconButton(onClick = onBackPress) {
        Icon(
          Filled.ArrowBack,
          contentDescription = stringResource(R.string.back),
          tint = textColor,
        )
      }
    },
    actions = {
      IconButton(onClick = onFavoriteClick) {
        Image(
          painter =
            if (isFavorite) {
              painterResource(
                id = R.drawable.cool_shape_fav,
              )
            } else {
              painterResource(id = R.drawable.cool_shape_placeholder)
            },
          contentDescription = stringResource(R.string.favorite),
          modifier = Modifier.size(Constants.ICON_SIZE_SMALL),
        )
      }
      IconButton(onClick = onDownloadClick) {
        Image(
          painter = painterResource(id = R.drawable.my_shape_poly12),
          contentDescription = stringResource(R.string.download_image),
          modifier = Modifier.size(Constants.ICON_SIZE_SMALL),
        )
      }
      IconButton(onClick = onAskAiClick) {
        Image(
          painter = painterResource(id = R.drawable.cool_shape_ai),
          contentDescription = stringResource(R.string.ask_ai_about_item),
          modifier = Modifier.size(Constants.ICON_SIZE_SMALL),
        )
      }
    },
    colors =
      TopAppBarDefaults.topAppBarColors(
        containerColor = Color.Transparent,
        titleContentColor = textColor,
        navigationIconContentColor = textColor,
        actionIconContentColor = textColor,
      ),
  )
}
