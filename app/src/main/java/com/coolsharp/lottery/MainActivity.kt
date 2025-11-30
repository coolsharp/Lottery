package com.coolsharp.lottery

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coolsharp.lottery.network.RetrofitInstance
import com.coolsharp.lottery.ui.MainLayout
import com.coolsharp.lottery.ui.SplashScreen
import com.coolsharp.lottery.viewmodel.NumbersViewModel
import com.coolsharp.lottery.viewmodel.SplashViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backPressed()
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
            is AppState.Splash -> SplashScreen(context = this,modifier)
            is AppState.Main -> MainLayout(context = this, modifier)
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

    private fun backPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - backPressedTime >= 2000) {
                    backPressedTime = System.currentTimeMillis()
                    Toast.makeText(this@MainActivity, "뒤로 버튼을 한번 더 누르면 앱을 종료합니다.", Toast.LENGTH_SHORT).show()
                } else if (System.currentTimeMillis() - backPressedTime < 2000) {
                    finish()
                }
            }
        }

        this.onBackPressedDispatcher.addCallback(this, callback)
    }
}