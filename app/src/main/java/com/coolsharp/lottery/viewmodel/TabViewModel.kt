package com.coolsharp.lottery.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object TabViewModelHolder {
    private val _viewModel = TabViewModel()
    val viewModel: TabViewModel
        get() = _viewModel
}

class TabViewModel : ViewModel() {

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex.asStateFlow()

    fun selectTab(index: Int) {
        _selectedTabIndex.value = index
        // 탭 변경 시 실행할 추가 로직을 여기에 넣으세요
        when (index) {
            0 -> handleMyNumberTab()
            1 -> handleWinningNumbersTab()
        }
    }

    private fun handleMyNumberTab() {
        // "내 번호 관리" 탭 선택 시 실행할 로직
    }

    private fun handleWinningNumbersTab() {
        // "당첨 번호 조회" 탭 선택 시 실행할 로직
    }
}