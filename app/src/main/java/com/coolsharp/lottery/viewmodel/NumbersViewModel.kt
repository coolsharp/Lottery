package com.coolsharp.lottery.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.coolsharp.lottery.data.LottoNumbers
import com.coolsharp.lottery.repository.NumbersDataStore
import kotlinx.coroutines.launch

class NumbersViewModel(application: Application) : AndroidViewModel(application) {

    private val numbersDataStore = NumbersDataStore(application)

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
}