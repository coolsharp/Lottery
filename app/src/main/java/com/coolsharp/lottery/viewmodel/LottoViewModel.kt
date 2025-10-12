package com.coolsharp.lottery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.coolsharp.lottery.repository.LottoRepository

class LottoViewModel: ViewModel() {
    private val repository = LottoRepository()

    val lotto = repository.getLotto().cachedIn(viewModelScope)
}