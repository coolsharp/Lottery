package com.coolsharp.lottery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolsharp.lottery.data.UiStateLatestLotto
import com.coolsharp.lottery.network.RetrofitInstance
import com.coolsharp.lottery.repository.LottoLatestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel
class MainViewModel : ViewModel() {
    private val repository = LottoLatestRepository(RetrofitInstance.lottoLatestApiService)

    private val _uiState = MutableStateFlow<UiStateLatestLotto>(UiStateLatestLotto.Loading)
    val uiState: StateFlow<UiStateLatestLotto> = _uiState

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _uiState.value = UiStateLatestLotto.Loading
            try {
                val data = repository.fetch()
                _uiState.value = UiStateLatestLotto.Success(data)
            } catch (e: Exception) {
                _uiState.value = UiStateLatestLotto.Error("데이터 불러오기 실패: ${e.message}")
            }
        }
    }
}