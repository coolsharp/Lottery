package com.coolsharp.lottery.data

class WinnerLottoNumber(
    number1: Int,
    number2: Int,
    number3: Int,
    number4: Int,
    number5: Int,
    number6: Int,
    val bonusNumber: Int,
    val draw: Int
): LottoNumber(
    number1,
    number2,
    number3,
    number4,
    number5,
    number6
) {
}