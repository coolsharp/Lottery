package com.coolsharp.lottery.config

object AppConfig {
    const val API_URL = "https://api.example.com"
    const val SPLASH_DELAY = 3000L
    var isDebugMode = false

    fun initialize() {
        println("AppConfig 초기화")
    }
}