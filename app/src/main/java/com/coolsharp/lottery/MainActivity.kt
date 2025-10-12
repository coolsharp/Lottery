package com.coolsharp.lottery

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coolsharp.lottery.network.RetrofitInstance
import com.coolsharp.lottery.ui.ProfileLayout
import com.coolsharp.lottery.ui.SplashScreen
import com.coolsharp.lottery.viewmodel.SplashViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
//                ChangeSystemBarsTheme(!isSystemInDarkTheme())
                ChangeSystemBarsTheme(true)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
        MainScope().launch {
            val result = RetrofitInstance.lottoLatestApiService.getLatestLotto(
            )
            Log.d("posts: ", result.toString())
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
    private fun ChangeSystemBarsTheme(lightTheme: Boolean) {
        val barColor = MaterialTheme.colorScheme.background.toArgb()
        LaunchedEffect(lightTheme) {
            if (lightTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.light(
                        barColor, barColor,
                    ),
                    navigationBarStyle = SystemBarStyle.light(
                        barColor, barColor,
                    ),
                )
            } else {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.dark(
                        barColor,
                    ),
                    navigationBarStyle = SystemBarStyle.dark(
                        barColor,
                    ),
                )
            }
        }
    }
}