package com.heftreng.kurdi.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object NetworkModule {

    // raw.githubusercontent.com — birincil (hızlı, CDN olmadan doğrudan)
    private const val PRIMARY_URL  = "https://raw.githubusercontent.com/heftreng49/kurdi/main/"
    // GitHub Pages — yedek
    private const val FALLBACK_URL = "https://heftreng49.github.io/kurdi/"

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private fun buildClient() = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private fun buildRetrofit(baseUrl: String): KurdiApiService =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(buildClient())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(KurdiApiService::class.java)

    val primaryApi:  KurdiApiService by lazy { buildRetrofit(PRIMARY_URL)  }
    val fallbackApi: KurdiApiService by lazy { buildRetrofit(FALLBACK_URL) }
}
