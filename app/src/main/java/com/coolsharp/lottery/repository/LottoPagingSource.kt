package com.coolsharp.lottery.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.coolsharp.lottery.data.Lotto
import com.coolsharp.lottery.data.LottoApiResult
import com.coolsharp.lottery.network.RetrofitInstance

class LottoPagingSource(
    private val pageSize: Int = 20,
) : PagingSource<Int, Lotto>() {
    override fun getRefreshKey(state: PagingState<Int, Lotto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Lotto> {
        return try {
            var response: LottoApiResult

            val page = params.key ?: 0

            response = RetrofitInstance.lottoApiService.getLotto(
                limit = 20,
                page = page + 1
            )

            LoadResult.Page(
                data = response.data,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}