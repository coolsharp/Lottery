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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.coolsharp.lottery.ui.LottoNumberGeneratorScreen
import com.coolsharp.lottery.network.RetrofitInstance
import com.coolsharp.lottery.ui.MainLayout
import com.coolsharp.lottery.ui.SplashScreen
import com.coolsharp.lottery.viewmodel.LottoViewModel
import com.coolsharp.lottery.viewmodel.MainViewModel
import com.coolsharp.lottery.viewmodel.NumbersViewModel
import com.coolsharp.lottery.viewmodel.SplashViewModel
import com.coolsharp.lottery.viewmodel.TabViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var backPressedTime: Long = 0
    private lateinit var lottoViewModel: LottoViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var numberViewModel: NumbersViewModel
    private lateinit var tabViewModel: TabViewModel

    @Composable
    fun initViewModel(modifier: Modifier = Modifier) {
        lottoViewModel = viewModel()
        mainViewModel = viewModel()
        numberViewModel = viewModel()
        tabViewModel = viewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backPressed()
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                initViewModel()
//                ChangeSystemBarsTheme(!isSystemInDarkTheme())
                ChangeSystemBarsTheme(true)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyNavController(modifier = Modifier.padding(innerPadding))
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
    fun MyNavController(modifier: Modifier) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "ScreenMain"
        ) {
            composable("ScreenMain") {
                ScreenMain(
                    modifier = modifier,
                    onNavigateToLottoNumberGenerator = { navController.navigate("ScreenLottoNumberGenerator") },
                    navController = navController
                )
            }

            composable("ScreenLottoNumberGenerator") {
                ScreenLottoNumberGenerator(
                    onReturnResult = { value ->
                        // B → A 결과 보내기
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("resultKey", value)

                        navController.popBackStack()
                    }
                )
            }
        }
    }

    @Composable
    fun ScreenMain(
        onNavigateToLottoNumberGenerator: () -> Unit,
        navController: NavHostController,
        modifier: Modifier
    ) {
        // StateFlow를 Compose State로 변환
        val splashViewModel: SplashViewModel = viewModel()
        val appState by splashViewModel.appState.collectAsState()

        when (appState) {
            is AppState.Splash -> SplashScreen(context = this,modifier)
            is AppState.Main -> {
                MainLayout(context = this, modifier,
                    lottoViewModel = lottoViewModel,
                    mainViewModel = mainViewModel,
                    numberViewModel = numberViewModel,
                    tabViewModel = tabViewModel,
                    onNavigateToLottoNumberGenerator = onNavigateToLottoNumberGenerator)
            }
        }

        val context = LocalContext.current

        // B 에서 전달한 값 관찰
        val result = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow<Set<Int>?>("resultKey", null)
            ?.collectAsState()

        LaunchedEffect(result?.value) {
            result?.value?.let {
                numberViewModel.addNumbers(it.toList().sorted())
            }
        }
    }

    @Composable
    fun ScreenLottoNumberGenerator(
        onReturnResult: (Set<Int>) -> Unit
    ) {
        LottoNumberGeneratorScreen(onReturnResult)
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