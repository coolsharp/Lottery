package com.coolsharp.lottery

sealed class AppState {
    object Splash : AppState()
    object Main : AppState()
}