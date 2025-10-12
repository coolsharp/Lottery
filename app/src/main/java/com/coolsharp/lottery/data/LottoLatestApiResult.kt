package com.coolsharp.lottery.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LottoLatestApiResult(
    @SerialName("data")
    val data: Lotto,
    @SerialName("success")
    val success: Boolean
)