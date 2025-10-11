package com.coolsharp.lottery.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Lotto(
    @SerialName("bonus_number")
    val bonusNumber: Int,
    @SerialName("draw_date")
    val drawDate: String,
    @SerialName("draw_no")
    val drawNo: Int,
    @SerialName("numbers")
    val numbers: List<Int>
)