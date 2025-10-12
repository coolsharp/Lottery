package com.coolsharp.lottery.data

sealed class UiStateLatestLotto {
    object Loading : UiStateLatestLotto()
    data class Success(val data: LottoLatestApiResult) : UiStateLatestLotto()
    data class Error(val message: String) : UiStateLatestLotto()
}