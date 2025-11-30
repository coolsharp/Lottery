package com.coolsharp.lottery.common

import android.util.Log

fun safeRemoveColumn(numbers: List<List<Int>>, columnIndex: Int): List<List<Int>> {
    if (numbers.isEmpty()) return numbers
    if (columnIndex < 0 || columnIndex >= numbers[0].size) {
        Log.d("coolsharp", "경고: 유효하지 않은 열 인덱스")
        return numbers
    }

    return numbers.map { row ->
        row.toMutableList().apply {
            Log.d("coolsharp", "delete column $columnIndex")
            removeAt(columnIndex)
        }
    }
}
