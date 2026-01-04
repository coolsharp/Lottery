package com.coolsharp.lottery.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.coolsharp.lottery.data.Lotto
import com.coolsharp.lottery.repository.LottoRepository

class LottoViewModel: ViewModel() {
    private val repository = LottoRepository()

    val lottoData = repository.getLotto().cachedIn(viewModelScope)

    private val _lotto = mutableStateOf<Lotto>(Lotto(0, "", 0, emptyList()))
    val lotto: State<Lotto> = _lotto

    private val _isLoad = mutableStateOf(false)
    val isLoad: State<Boolean> = _isLoad

    fun setLotto(lotto: Lotto) {
        _lotto.value = lotto
    }

    fun setLoad(load: Boolean) {
        _isLoad.value = load
    }
}