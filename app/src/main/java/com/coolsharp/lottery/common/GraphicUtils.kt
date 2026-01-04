package com.coolsharp.lottery.common

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coolsharp.lottery.data.ButtonType

fun Color.darken(factor: Float = 0.8f): Color {
    return Color(
        red = (this.red * factor).coerceIn(0f, 1f),
        green = (this.green * factor).coerceIn(0f, 1f),
        blue = (this.blue * factor).coerceIn(0f, 1f),
        alpha = this.alpha
    )
}

@Composable
fun TextWithOutlineExtension(text: String, size: TextUnit, textColor: Color, strokeColor: Color) {
    Text(
        text = text,
        fontSize = size,
        style = TextStyle(
            color = textColor,
            fontWeight = FontWeight.Bold,
            shadow = androidx.compose.ui.graphics.Shadow(
                color = strokeColor,
                offset = androidx.compose.ui.geometry.Offset(1f, 1f),
                blurRadius = 0f
            )
        )
    )
}

// ============================================
// 번호 볼 컴포넌트
// ============================================

@Composable
fun NumberBall(
    number: Int,
    buttonType: ButtonType,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale",
        finishedListener = {
            if (isPressed) {
                isPressed = false
            }
        }
    )

    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
            .clip(CircleShape)
            .background(
                if (buttonType != ButtonType.None) {
                    Brush.linearGradient(
                        colors = listOf(
                            getNumberColor(number),
                            getNumberColor(number).copy(alpha = 0.8f)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFF9FAFB), Color(0xFFF3F4F6))
                    )
                }
            )
            .border(
                width = when (buttonType) {
                    ButtonType.Selected -> 3.dp
                    ButtonType.Winner -> 3.dp
                    else -> 1.5.dp
                },
                color = when (buttonType) {
                    ButtonType.Selected -> Color.White.copy(alpha = 0.6f)
                    ButtonType.Winner -> Color.White.copy(alpha = 0.6f)
                    else -> Color(0xFFE5E7EB)
                },
                shape = CircleShape
            )
//            .background(
//                brush = Brush.radialGradient(
//                    colors = listOf(Color(0xFFD1D5DB), Color(0xFF9CA3AF), Color(0xFF6B7280)).map {
//                        it.copy(alpha = glowAlpha)
//                    }
//                ),
//                shape = CircleShape
//            )
            .combinedClickable(
                onClick = {
                    isPressed = true
                    onClick()
                },
                onLongClick = {
                    isPressed = true
                    onLongClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            fontSize = 16.sp,
            fontWeight = when (buttonType) {
                ButtonType.Selected -> FontWeight.ExtraBold
                ButtonType.Winner -> FontWeight.ExtraBold
                else -> FontWeight.SemiBold
            },
            color = when (buttonType) {
                ButtonType.Selected -> Color.White
                ButtonType.Winner -> Color.Red
                else -> Color(0xFF6B7280)
            },
            textAlign = TextAlign.Center,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.White,
                    offset = Offset(0f, 0f),
                    blurRadius = 10f
                )
            )
        )
    }
}

// ============================================
// 번호별 색상
// ============================================

fun getNumberColor(number: Int): Color {
    return when (number) {
        in 1..10 -> Color(0xFFFBBF24)   // 노란색
        in 11..20 -> Color(0xFF3B82F6)  // 파란색
        in 21..30 -> Color(0xFFEF4444)  // 빨간색
        in 31..40 -> Color(0xFF6B7280)  // 회색
        else -> Color(0xFF10B981)       // 초록색
    }
}