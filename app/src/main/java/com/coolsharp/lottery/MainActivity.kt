package com.coolsharp.lottery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coolsharp.lottery.ui.ProfileLayout
import com.coolsharp.lottery.ui.SplashScreen
import com.coolsharp.lottery.viewmodel.SplashViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun AppNavigation(modifier: Modifier, viewModel: SplashViewModel = viewModel()) {
        // StateFlow를 Compose State로 변환
        val appState by viewModel.appState.collectAsState()

        when (appState) {
            is AppState.Splash -> SplashScreen(modifier)
            is AppState.Main -> ProfileLayout(modifier)
        }
    }

    @Composable
    fun MyAppTheme(content: @Composable () -> Unit) {
        MaterialTheme(
            content = content
        )
    }
}