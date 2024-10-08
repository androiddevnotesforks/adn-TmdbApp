package com.example.tmdbapp.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import coil.compose.*
import com.example.tmdbapp.utils.*

@Composable
fun ItemSimpleUi(
  title: String,
  overview: String,
  posterPath: String?,
  voteAverage: Float,
  isFavorite: Boolean,
  modifier: Modifier = Modifier,
  onFavoriteClick: () -> Unit,
) {
  Card(
    modifier =
      modifier
        .fillMaxWidth()
        .height(150.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    shape = RoundedCornerShape(8.dp),
  ) {
    Box {
      Row(
        modifier =
          Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
      ) {
        AsyncImage(
          model = "${Constants.BASE_IMAGE_URL}$posterPath",
          contentDescription = title,
          contentScale = ContentScale.Crop,
          modifier =
            Modifier
              .width(100.dp)
              .fillMaxHeight()
              .clip(
                RoundedCornerShape(
                  topStart = 8.dp,
                  bottomStart = 8.dp,
                ),
              ),
        )
        Column(
          modifier =
            Modifier
              .weight(1f)
              .padding(16.dp),
        ) {
          Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = overview,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Spacer(modifier = Modifier.weight(1f))
          Row(
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Icon(
              imageVector = Icons.Filled.Star,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.secondary,
              modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = String.format("%.1f", voteAverage),
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }
      IconButton(
        onClick = onFavoriteClick,
        modifier =
          Modifier
            .align(Alignment.TopEnd)
            .padding(4.dp),
      ) {
        Icon(
          imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
          contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
          tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
          modifier = Modifier.size(24.dp),
        )
      }
    }
  }
}
