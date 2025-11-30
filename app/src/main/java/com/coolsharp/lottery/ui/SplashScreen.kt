package com.coolsharp.lottery.ui

import android.content.Context
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.coolsharp.lottery.R
import com.coolsharp.lottery.config.AppConfig
import kotlinx.coroutines.delay

@Composable
fun SplashContent(onTimeout: () -> Unit) {
    // 애니메이션 설정
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "alpha"
    )

    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )

    // 3초 후 메인 화면으로 전환
    LaunchedEffect(Unit) {
        delay(AppConfig.SPLASH_DELAY)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6200EE)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box() {
                // 배경
                Image(
                    painter = painterResource(id = R.drawable.splash_back),
                    contentDescription = "Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(alpha)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                )
                // 로고
                Image(
                    painter = painterResource(id = R.drawable.splash),
                    contentDescription = "Logo",
                    contentScale = ContentScale.FillWidth, // 넓이 꽉 채움
                    alignment = Alignment.TopCenter, // 상단정렬
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(alpha)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .align(Alignment.Center)
                )
            }
        }
    }
}

// 스플래시 스크린
@Composable
fun SplashScreen(context: Context, modifier: Modifier) {
    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        SplashContent {
            showSplash = false
        }
    } else {
        MainLayout(context = context, modifier)
    }
}
