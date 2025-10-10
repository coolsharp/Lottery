package com.coolsharp.lottery.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coolsharp.lottery.common.CircleWithStroke
import com.coolsharp.lottery.data.LottoNumber
import com.coolsharp.lottery.data.WinnerLottoNumber

@Composable
fun ProfileLayout(modifier: Modifier) {
    LazyColumn(modifier.fillMaxWidth()) {
        item {
            val winnerLottoNumber: WinnerLottoNumber = WinnerLottoNumber(
                number1 = 1,
                number2 = 12,
                number3 = 23,
                number4 = 34,
                number5 = 45,
                number6 = 6,
                bonusNumber = 2,
                1192
            )
            ProfileHeader(winnerLottoNumber)
        }
        stickyHeader {
            TabRow(selectedTabIndex = 0) {
                repeat(1) {
                    Tab(selected = it == 0, onClick = {}) {
                        Text(
                            "내 번호관리"
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                        )
                    }
                }
            }
        }
        items(100) {
            val lottoNumber: LottoNumber = LottoNumber(
                number1 = 1,
                number2 = 12,
                number3 = 23,
                number4 = 34,
                number5 = 45,
                number6 = 6
            )

            var margin = 10
            if (it == 0 || it == 99) {
                margin = 20
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(margin.dp)
            )
            DrawLottoNumber(lottoNumber)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )
        }
    }
}

@Composable
fun ProfileHeader(winnerLottoNumber: WinnerLottoNumber) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${winnerLottoNumber.draw}회 당첨번호",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color(0xFF6200EE)
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
        DrawWinnerLottoNumber(winnerLottoNumber)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )
    }
}

@Composable
fun DrawWinnerLottoNumber(winnerLottoNumber: WinnerLottoNumber) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleWithStroke(winnerLottoNumber.number1) {}
        Spacer(modifier = Modifier.width(3.dp))
        CircleWithStroke(winnerLottoNumber.number2) {}
        Spacer(modifier = Modifier.width(3.dp))
        CircleWithStroke(winnerLottoNumber.number3) {}
        Spacer(modifier = Modifier.width(3.dp))
        CircleWithStroke(winnerLottoNumber.number4) {}
        Spacer(modifier = Modifier.width(3.dp))
        CircleWithStroke(winnerLottoNumber.number5) {}
        Spacer(modifier = Modifier.width(3.dp))
        CircleWithStroke(winnerLottoNumber.number6) {}
        Spacer(modifier = Modifier.width(7.dp))
        Text("+")
        Spacer(modifier = Modifier.width(7.dp))
        CircleWithStroke(winnerLottoNumber.bonusNumber) {}
    }
}

@Composable
fun DrawLottoNumber(lottoNumber: LottoNumber) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleWithStroke(lottoNumber.number1) {}
        Spacer(modifier = Modifier.width(3.dp))
        CircleWithStroke(lottoNumber.number2) {}
        Spacer(modifier = Modifier.width(3.dp))
        CircleWithStroke(lottoNumber.number3) {}
        Spacer(modifier = Modifier.width(3.dp))
        CircleWithStroke(lottoNumber.number4) {}
        Spacer(modifier = Modifier.width(3.dp))
        CircleWithStroke(lottoNumber.number5) {}
        Spacer(modifier = Modifier.width(3.dp))
        CircleWithStroke(lottoNumber.number6) {}
    }
}