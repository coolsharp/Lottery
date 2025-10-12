package com.coolsharp.lottery.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LottoApiResult(
    @SerialName("data")
    val data: List<Lotto>,
    @SerialName("pagination")
    val pagination: Pagination,
    @SerialName("success")
    val success: Boolean
)