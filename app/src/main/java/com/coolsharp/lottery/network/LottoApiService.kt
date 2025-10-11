package com.coolsharp.lottery.network

import com.coolsharp.lottery.data.LottoApi
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LottoApiService {
    @GET("/api/lotto/draws")
    suspend fun getLotto(
        @Path("page") page: Int,
        @Query("limit") limit: Int = 20,
    ): LottoApi
}