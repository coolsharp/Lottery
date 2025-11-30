package com.coolsharp.lottery.ui

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.coolsharp.lottery.R
import com.coolsharp.lottery.common.NumberBall
import com.coolsharp.lottery.data.ButtonType
import com.coolsharp.lottery.data.Lotto
import com.coolsharp.lottery.data.LottoLatestApiResult
import com.coolsharp.lottery.data.LottoNumbers
import com.coolsharp.lottery.data.ShowError
import com.coolsharp.lottery.data.TabType
import com.coolsharp.lottery.data.UiStateLatestLotto
import com.coolsharp.lottery.viewmodel.LottoViewModel
import com.coolsharp.lottery.viewmodel.MainViewModel
import com.coolsharp.lottery.viewmodel.NumbersViewModel
import com.coolsharp.lottery.viewmodel.TabViewModel
import com.coolsharp.qrcode.QRActivity
import kotlinx.coroutines.launch


@Composable
fun MainLayout(
    context: Context,
    modifier: Modifier,
    lottoViewModel: LottoViewModel,
    mainViewModel: MainViewModel,
    numberViewModel: NumbersViewModel,
    tabViewModel: TabViewModel,
    onNavigateToLottoNumberGenerator: () -> Unit
) {
    val selectedTabIndex by tabViewModel.selectedTabIndex.collectAsState()
    val lottoApi = lottoViewModel.lotto.collectAsLazyPagingItems()
    val uiState by mainViewModel.uiState.collectAsState()
    val numbersData by numberViewModel.numbersLiveData.observeAsState()
    numberViewModel.loadNumbers()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // 하드웨어 백키 클릭 핸들러
    BackHandler(enabled = numberViewModel.isEditMode.value) {
        // 에디트 모드 해제
        numberViewModel.toggleEditMode()
    }

    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier
                .fillMaxWidth()
                .weight(1f),
            state = listState
        ) {
            item {
                when (uiState) {
                    is UiStateLatestLotto.Loading -> {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiStateLatestLotto.Success -> {
                        val lotto = (uiState as UiStateLatestLotto.Success).data
                        MainHeader(lotto)
                    }

                    is UiStateLatestLotto.Error -> {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("에러 발생: ${(uiState as UiStateLatestLotto.Error).message}")
                        }
                    }
                }
            }
            stickyHeader {
                TabRow(selectedTabIndex = selectedTabIndex) {
                    repeat(TabType.entries.size) { index ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = {
                                tabViewModel.selectTab(index)
                            }
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                            )
                            var text = ""
                            when (index) {
                                TabType.MyLottoNumber.index -> text = stringResource(R.string.my_number_management)
                                TabType.WinnerLottoNumber.index -> text = stringResource(R.string.winning_numbers_by_draw)
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
                TabType.MyLottoNumber.index -> {
                    coroutineScope.launch {listState.animateScrollToItem(0)}
                    lottoNumberContent(numbersData, numberViewModel)
                }

                TabType.WinnerLottoNumber.index -> {
                    coroutineScope.launch {listState.animateScrollToItem(0)}
                    winnerLottoNumberContent(lottoApi)
                }
            }
        }

        Row() {
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                onClick = onNavigateToLottoNumberGenerator
            ) {
                Text("새 번호 생성")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                onClick = {
                    val intent: Intent = Intent(context, QRActivity::class.java)
                    context.startActivity(intent)
                }
            ) {
                Text("QR 당첨확인")
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}

fun LazyListScope.lottoNumberContent(
    lottoNumber: LottoNumbers?,
    numberViewModel: NumbersViewModel
) {
    lottoNumber?.let {
        items(it.numbers.size) { index ->
            it.numbers[index].let { lotto ->
                val margin = if (index == 0) 20 else 10

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = margin.dp, bottom = 10.dp)
                        .combinedClickable(
                            onClick = { },
                            onLongClick = { numberViewModel.toggleEditMode() }
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DrawLottoNumber(lotto, numberViewModel)
                    if (numberViewModel.isEditMode.value) {
                        Spacer(modifier = Modifier.width(3.dp))
                        CustomCircleButton(
                            icon = Icons.Default.Close,
                            backgroundColor = Color(0xFFFF499B),
                            size = 36,
                            onClick = { numberViewModel.removeNumbers(index) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomCircleButton(
    icon: ImageVector,
    backgroundColor: Color = Color.Blue,
    iconColor: Color = Color.White,
    size: Int = 56,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "원형 버튼",
            tint = iconColor,
            modifier = Modifier.size((size * 0.5).dp)
        )
    }
}

fun LazyListScope.winnerLottoNumberContent(lottoApi: LazyPagingItems<Lotto>) {
    items(lottoApi.itemCount) { index ->
        lottoApi[index]?.let { lotto ->

            val margin = if (index == 0) 20 else 10

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(margin.dp)
            )
            Box(
                modifier = Modifier
                    .background(Color(0xFFFFEE91), shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth(),
            ) {
                Text(
                    text = " ${lotto.drawNo}회 당첨번호(추첨일 : ${lotto.drawDate}) ",
                    color = Color(0xFF57595B),
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Color.Black,
                        shadow = Shadow(
                            color = Color.Gray,
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    )
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            )
            DrawWinnerLottoNumber(lotto)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )
        }
    }
}

@Composable
fun MainHeader(lottoLatestApiResult: LottoLatestApiResult) {
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
                "${lottoLatestApiResult.data.drawNo}회 당첨번호",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_down_24),
                contentDescription = null,
                tint = Color(0xFF6200EE)
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "추첨일 : ${lottoLatestApiResult.data.drawDate}",
            fontSize = 15.sp
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )
        DrawWinnerLottoNumber(lottoLatestApiResult.data)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )
    }
}

@Composable
fun DrawWinnerLottoNumber(lotto: Lotto) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var margin = 3.dp
        if (5 < lotto.numbers.size) {
            for (i in 0..<lotto.numbers.size) {
                NumberBall(
                    number = lotto.numbers[i],
                    ButtonType.Selected,
                    onClick = { },
                    onLongClick = { },
                    modifier = Modifier.weight(1f)
                )
                if (5 > i) margin = 7.dp
                Spacer(modifier = Modifier.width(margin))
            }
        }
        Text("+")
        Spacer(modifier = Modifier.width(margin))
        NumberBall(
            number = lotto.bonusNumber,
            ButtonType.Selected,
            onClick = { },
            onLongClick = { },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun DrawLottoNumber(lotto: List<Int>, numberViewModel: NumbersViewModel) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.width(300.dp)
    ) {
        if (5 < lotto.size) {
            for (i in 0..5) {
                NumberBall(
                    number = lotto[i],
                    ButtonType.Selected,
                    onClick = { },
                    onLongClick = { numberViewModel.toggleEditMode() },
                    modifier = Modifier.weight(1f)
                )
                if (5 > i) Spacer(modifier = Modifier.width(3.dp))
            }
        }
    }
}