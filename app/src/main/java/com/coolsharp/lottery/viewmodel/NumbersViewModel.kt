package com.coolsharp.lottery.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.coolsharp.lottery.data.LottoNumbers
import com.coolsharp.lottery.repository.NumbersDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class NumbersViewModel(application: Application) : AndroidViewModel(application) {

    private val numbersDataStore = NumbersDataStore(application)

    private val _isEditMode = mutableStateOf(false)
    val isEditMode: State<Boolean> = _isEditMode

    val numbersLiveData: LiveData<LottoNumbers?> =
        numbersDataStore.numbersDataFlow.asLiveData()

    fun saveNumbers(numbers: List<List<Int>>) {
        viewModelScope.launch {
            numbersDataStore.saveNumbers(LottoNumbers(numbers))
        }
    }

    fun addNumbers(numbers: List<Int>) {
        viewModelScope.launch {
            numbersDataStore.addNumbers(numbers)
        }
    }

    fun removeNumbers(index: Int) {
        viewModelScope.launch {
            numbersDataStore.removeNumbersAt(index)
        }
    }

    fun loadNumbers() {
        viewModelScope.launch {
            val data = numbersDataStore.getNumbers()
            data?.let {
                Log.d("coolsharp","총 ${it.numbers.size}개의 번호 세트")
                Log.d("coolsharp", numbersLiveData.value.toString())
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            numbersDataStore.clearNumbers()
        }
    }

    fun toggleEditMode() {
        _isEditMode.value = !_isEditMode.value
    }

    fun setEditMode(enabled: Boolean) {
        _isEditMode.value = enabled
    }
}