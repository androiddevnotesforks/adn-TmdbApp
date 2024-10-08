package com.example.tmdbapp.network

import android.util.*
import com.example.tmdbapp.utils.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

object KtorClient {
  private const val TIME_OUT = 60_000

  val httpClient =
    HttpClient(Android) {
      install(ContentNegotiation) {
        json(
          Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true // Add this line if not already present
          },
        )
      }

      install(Logging) {
        logger =
          object : Logger {
            override fun log(message: String) {
              Log.v("Ktor", message)
            }
          }
        level = LogLevel.ALL
      }

      install(HttpTimeout) {
        requestTimeoutMillis = TIME_OUT.toLong()
        connectTimeoutMillis = TIME_OUT.toLong()
        socketTimeoutMillis = TIME_OUT.toLong()
      }

      defaultRequest {
        url(Constants.BASE_API_URL)
      }
    }
}
