package com.coolsharp.lottery.network

import com.coolsharp.lottery.data.LottoLatestApiResult
import retrofit2.http.GET

interface LottoLatestApiService {
    @GET("/php/lotto_api.php/api/lotto/latest")
    suspend fun getLatestLotto(
    ): LottoLatestApiResult
}