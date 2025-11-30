package com.coolsharp.lottery.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircleWithStroke(number: Int, content: @Composable () -> Unit) {
    var colorStroke = Color.Transparent
    when (number) {
        in 1..9 -> colorStroke = Color(0xFFFBBF24)
        in 10..19 -> colorStroke = Color(0xFF3B82F6)
        in 20..29 -> colorStroke = Color(0xFFEF4444)
        in 30..39 -> colorStroke = Color(0xFF6B7280)
        in 40..45 -> colorStroke = Color(0xFF10B981)
    }

    Box(
        modifier = Modifier
            .size(35.dp)
            .border(2.dp, colorStroke.darken(0.9f), shape = CircleShape)
            .background(colorStroke, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        TextWithOutlineExtension(
            text = "${number}",
            size = 17.sp,
            Color.White,
            colorStroke.darken(0.1f)
        )
    }
}

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