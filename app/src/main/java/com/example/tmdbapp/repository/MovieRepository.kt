package com.example.tmdbapp.repository

import android.content.*
import com.example.tmdbapp.*
import com.example.tmdbapp.data.*
import com.example.tmdbapp.models.*
import com.example.tmdbapp.network.*
import com.example.tmdbapp.utils.*
import kotlinx.coroutines.flow.*

class MovieRepository(
  context: Context,
) {
  private val api = ApiService(KtorClient.httpClient)
  private val apiKey = BuildConfig.TMDB_API_KEY
  private val favoritePreferences = FavoritePreferences(context)
  private val sessionManager = SessionManager(context)

  suspend fun getPopularMovies(page: Int): Resource<MovieResponse> =
    try {
      val response = api.getPopularMovies(apiKey, page)
      val moviesWithFavoriteStatus =
        response.results.map { movie ->
          movie.copy(isFavorite = favoritePreferences.isFavorite(movie.id))
        }
      Resource.Success(response.copy(results = moviesWithFavoriteStatus))
    } catch (e: Exception) {
      Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
    }

  suspend fun getFavoriteMovies(): List<Movie> =
    try {
      val response = api.getPopularMovies(apiKey, 1)
      response.results
        .filter { movie ->
          favoritePreferences.isFavorite(movie.id)
        }.map { it.copy(isFavorite = true) }
    } catch (e: Exception) {
      emptyList()
    }

  fun toggleFavorite(movie: Movie) {
    val newFavoriteStatus = !movie.isFavorite
    favoritePreferences.setFavorite(movie.id, newFavoriteStatus)
  }

  suspend fun discoverMovies(
    page: Int,
    sortBy: String? = null,
    genres: List<Int>? = null,
    releaseYear: Int? = null,
    minRating: Float? = null,
  ): Resource<MovieResponse> =
    try {
      val response =
        api.discoverMovies(
          apiKey,
          page,
          sortBy,
          genres?.joinToString(","),
          releaseYear,
          minRating,
        )
      val moviesWithFavoriteStatus =
        response.results.map { movie ->
          movie.copy(isFavorite = favoritePreferences.isFavorite(movie.id))
        }
      Resource.Success(response.copy(results = moviesWithFavoriteStatus))
    } catch (e: Exception) {
      Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
    }

  suspend fun searchMovies(
    query: String,
    page: Int,
  ): Resource<MovieResponse> =
    try {
      val response = api.searchMovies(apiKey, query, page)
      val moviesWithFavoriteStatus =
        response.results.map { movie ->
          movie.copy(isFavorite = favoritePreferences.isFavorite(movie.id))
        }
      Resource.Success(response.copy(results = moviesWithFavoriteStatus))
    } catch (e: Exception) {
      Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
    }

  suspend fun getMovieDetails(movieId: Int): Movie? =
    try {
      val response = api.getMovieDetails(movieId, apiKey)
      response.copy(isFavorite = favoritePreferences.isFavorite(response.id))
    } catch (e: Exception) {
      null
    }

  suspend fun createRequestToken(): Resource<String> =
    try {
      val response = api.createRequestToken(apiKey)
      if (response.success) {
        Resource.Success(response.requestToken)
      } else {
        Resource.Error("Failed to create request token")
      }
    } catch (e: Exception) {
      Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
    }

  suspend fun createSession(requestToken: String): Resource<String> =
    try {
      val response = api.createSession(apiKey, CreateSessionRequest(requestToken))
      if (response.success) {
        sessionManager.saveSessionId(response.sessionId)
        Resource.Success(response.sessionId)
      } else {
        Resource.Error("Failed to create session")
      }
    } catch (e: Exception) {
      Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
    }

  suspend fun createList(
    name: String,
    description: String,
  ): Resource<Int> {
    return try {
      val sessionId =
        sessionManager.sessionIdFlow.first() ?: return Resource.Error("No active session")
      val response = api.createList(apiKey, sessionId, CreateListRequest(name, description))
      if (response.success) {
        Resource.Success(response.listId)
      } else {
        Resource.Error(response.statusMessage)
      }
    } catch (e: Exception) {
      Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
    }
  }
}
