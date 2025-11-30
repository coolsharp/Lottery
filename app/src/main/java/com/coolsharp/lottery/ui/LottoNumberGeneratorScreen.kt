package com.coolsharp.lottery.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import com.coolsharp.lottery.common.NumberBall
import com.coolsharp.lottery.common.getNumberColor
import com.coolsharp.lottery.data.ShowError
import kotlinx.coroutines.delay

@Composable
fun LottoNumberGeneratorScreen(onReturnResult: (Set<Int>) -> Unit) {
    // 선택된 번호
    var selectedNumbers by remember { mutableStateOf(setOf<Int>()) }
    // 에러 보이기
    var showError by remember { mutableStateOf(ShowError(show = false)) }

    // 번호 선택/해제
    fun toggleNumber(number: Int) {
        // 선택된 번호가 선택되었으면 선택된 번호 제거
        selectedNumbers = if (number in selectedNumbers) {
            selectedNumbers - number
        } else {
            // 선택된 번호들이 6개보다 작으면 선택된 번호 추가
            if (selectedNumbers.size < 6) {
                selectedNumbers + number
            // 6개보다 크면 에러 보이기
            } else {
                showError = showError.copy(show = true, message = "⚠️ 최대 6개까지만 선택할 수 있습니다.")
                selectedNumbers
            }
        }
    }

    // 랜덤 생성
    fun generateRandom() {
        selectedNumbers = (1..45).shuffled().take(6).toSet()
        showError = showError.copy(show = false)
    }

    // 초기화
    fun reset() {
        selectedNumbers = emptySet()
        showError = showError.copy(
            show = false,
            message = ""
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFEFF6FF), Color(0xFFDEEEFF))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 상단 카드 - 선택된 번호 표시
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // 제목
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "로또 번호 생성",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF312E81)
                            )
                            Text(
                                text = "번호를 선택하세요",
                                fontSize = 14.sp,
                                color = Color(0xFF6B7280)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 진행 상황
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "선택된 번호",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF374151)
                        )

                        Text(
                            text = "${selectedNumbers.size}/6",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedNumbers.size == 6) Color(0xFF10B981) else Color(0xFF4F46E5)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 진행 바
                    LinearProgressIndicator(
                        progress = { selectedNumbers.size / 6f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (selectedNumbers.size == 6) Color(0xFF10B981) else Color(0xFF4F46E5),
                        trackColor = Color(0xFFE5E7EB),
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // 선택된 번호 표시
                    SelectedNumbersDisplay(selectedNumbers = selectedNumbers.sorted())

                    Spacer(modifier = Modifier.height(20.dp))

                    // 에러 메시지
                    AnimatedVisibility(
                        visible = showError.show,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFEE2E2)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = showError.message,
                                color = Color(0xFFDC2626),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(12.dp)
                            )
                        }

                        LaunchedEffect(Unit) {
                            delay(2000)
                            showError = showError.copy(show = false)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column() {
                        // 버튼들
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { generateRandom() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4F46E5)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "랜덤 생성",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = { reset() },
                                modifier = Modifier.height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFF6B7280)
                                )
                            ) {
                                Text(
                                    "초기화",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (selectedNumbers.size == 6) {
                                    onReturnResult(selectedNumbers)
                                }
                                else {
                                    showError = showError.copy(show = true, message = "⚠️ 번호 6개를 선택하여야 저장할 수 있습니다.")
                                }
                                      },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFEE6550)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "저장하기",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 번호 선택 그리드
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "번호를 선택하세요",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 번호 그리드 (1-45) - 스크롤 추가
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        NumberGrid(
                            selectedNumbers = selectedNumbers,
                            onNumberClick = { toggleNumber(it) }
                        )
                    }
                }
            }
        }
    }
}

// ============================================
// 선택된 번호 표시 컴포넌트
// ============================================

@Composable
fun SelectedNumbersDisplay(selectedNumbers: List<Int>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(6) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(
                        if (index < selectedNumbers.size) {
                            Brush.linearGradient(
                                colors = listOf(
                                    getNumberColor(selectedNumbers[index]),
                                    getNumberColor(selectedNumbers[index]).copy(alpha = 0.8f)
                                )
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFF3F4F6), Color(0xFFE5E7EB))
                            )
                        }
                    )
                    .border(
                        width = 2.dp,
                        color = if (index < selectedNumbers.size) Color.White.copy(alpha = 0.5f) else Color(0xFFD1D5DB),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (index < selectedNumbers.size) {
                    Text(
                        text = selectedNumbers[index].toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                } else {
                    Text(
                        text = (index + 1).toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFD1D5DB)
                    )
                }
            }
        }
    }
}

// ============================================
// 번호 그리드
// ============================================

@Composable
fun NumberGrid(
    selectedNumbers: Set<Int>,
    onNumberClick: (Int) -> Unit
) {
    // 9x5 그리드로 표시 (45개 번호) - 스크롤 가능
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(end = 4.dp), // 스크롤바 여유 공간
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (row in 0..8) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // 고정 높이
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (col in 0..4) {
                    val number = row * 5 + col + 1
                    if (number <= 45) {
                        NumberBall(
                            number = number,
                            isSelected = number in selectedNumbers,
                            onClick = { onNumberClick(number) },
                            onLongClick = {  },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // 하단 여유 공간
        Spacer(modifier = Modifier.height(8.dp))
    }
}
