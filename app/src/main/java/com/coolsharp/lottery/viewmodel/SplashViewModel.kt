package com.coolsharp.lottery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolsharp.lottery.AppState
import com.coolsharp.lottery.config.AppConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 스플래시 뷰 모델
class SplashViewModel : ViewModel() {
    // 앱 상태 저장
    private val _appState = MutableStateFlow<AppState>(AppState.Splash)
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    // 스플래시 보이기
    private var isSplashShown = false

    init {
        // 스플래시가 한 번만 실행되도록 제어
        if (!isSplashShown) {
            startSplashTimer()
            isSplashShown = true
        }
    }

    private fun startSplashTimer() {
        viewModelScope.launch {
            delay(AppConfig.SPLASH_DELAY) // 3초 대기
            _appState.value = AppState.Main
        }
    }

    // 수동으로 메인 화면으로 전환 (스킵 기능용)
    fun navigateToMain() {
        _appState.value = AppState.Main
    }

    // 테스트용: 스플래시 다시 보기
    fun resetToSplash() {
        _appState.value = AppState.Splash
        startSplashTimer()
    }
}