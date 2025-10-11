package com.coolsharp.lottery.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coolsharp.lottery.R
import com.coolsharp.lottery.common.CircleWithStroke
import com.coolsharp.lottery.data.LottoNumber
import com.coolsharp.lottery.data.WinnerLottoNumber
import com.coolsharp.lottery.viewmodel.TabViewModel
import com.coolsharp.lottery.viewmodel.TabViewModelHolder

@Composable
fun ProfileLayout(modifier: Modifier) {
    val viewModel = TabViewModelHolder.viewModel
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier.fillMaxWidth().weight(1f)) {
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
                TabRow(selectedTabIndex = selectedTabIndex) {
                    repeat(2) { index ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = {
                                viewModel.selectTab(index)
                            }
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                            )
                            var text = ""
                            when (index) {
                                0 -> text = stringResource(R.string.my_number_management)
                                1 -> text = stringResource(R.string.winning_numbers_by_draw)
                            }
                            Text(text)
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                            )
                        }
                    }
                }
            }
            when (selectedTabIndex) {
                0 -> {
                    myLottoNumberContent()
                }
                1 -> {

                }
            }
        }

        Row() {
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                onClick = {
                    // 버튼 클릭 시 실행할 로직
                }
            ) {
                Text("새 번호 생성")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                onClick = {
                    // 버튼 클릭 시 실행할 로직
                }
            ) {
                Text("QR 당첨확인")
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

fun LazyListScope.myLottoNumberContent() {
    items(100) { index ->
        val lottoNumber: LottoNumber = LottoNumber(
            number1 = 1,
            number2 = 12,
            number3 = 23,
            number4 = 34,
            number5 = 45,
            number6 = 6
        )

        val margin = if (index == 0) 20 else 10

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

@Composable
fun ProfileHeader(winnerLottoNumber: WinnerLottoNumber) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
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