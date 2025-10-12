package com.coolsharp.lottery.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.coolsharp.lottery.network.RetrofitInstance

class LottoRepository {
    private val lottoApiService = RetrofitInstance.lottoApiService

    fun getLotto(pageSize: Int = 20) = Pager(
        pagingSourceFactory = {
            LottoPagingSource(pageSize)
        },
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false,
            initialLoadSize = pageSize * 2
        )
    ).flow
}