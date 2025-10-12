package com.coolsharp.lottery.repository

import com.coolsharp.lottery.data.LottoLatestApiResult
import com.coolsharp.lottery.network.LottoLatestApiService

class LottoLatestRepository(private val api: LottoLatestApiService) {
    suspend fun fetch(): LottoLatestApiResult {
        return api.getLatestLotto()
    }
}