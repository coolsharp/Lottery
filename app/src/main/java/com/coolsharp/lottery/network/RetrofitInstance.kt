package com.coolsharp.lottery.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.nerdythings.okhttp.profiler.OkHttpProfilerInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import kotlin.getValue
import kotlin.setValue

object RetrofitInstance {
    private const val BASE_URL = "https://coolsharp0.mycafe24.com/php/lotto_api.php"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(OkHttpProfilerInterceptor())
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val json = Json {
        ignoreUnknownKeys = true // JSON에 알 수 없는 키가 있어도 무시
        coerceInputValues = true // null 값이 들어올 때 기본값 사용
        explicitNulls = false // null 값이 명시되지 않으면 JSON에서 생략된 것으로 처리
    }

    private val contentType = "application/json".toMediaType()

    val lottoApiService: LottoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(LottoApiService::class.java)
    }
}