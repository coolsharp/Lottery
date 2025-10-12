package com.coolsharp.lottery.network

import com.coolsharp.lottery.data.LottoApiResult
import retrofit2.http.GET
import retrofit2.http.Query

interface LottoApiService {
    @GET("/php/lotto_api.php/api/lotto/draws")
    suspend fun getLotto(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 20,
    ): LottoApiResult
}