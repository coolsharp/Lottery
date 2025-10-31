package com.coolsharp.lottery.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coolsharp.lottery.data.LottoNumbers
import com.coolsharp.lottery.repository.NumbersDataStore
import kotlinx.coroutines.launch

@Composable
fun NumbersScreen(context: Context) {
    val numbersDataStore = remember { NumbersDataStore(context) }
    val numbersData by numbersDataStore.numbersDataFlow.collectAsState(initial = null)
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "저장된 번호 세트",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        numbersData?.let { data ->
            data.numbers.forEachIndexed { index, numbers ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("세트 ${index + 1}: ${numbers.joinToString(", ")}")

                        IconButton(onClick = {
                            scope.launch {
                                numbersDataStore.removeNumbersAt(index)
                            }
                        }) {
                            Icon(Icons.Default.Delete, "삭제")
                        }
                    }
                }
            }
        } ?: Text("저장된 데이터가 없습니다")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    val sampleData = LottoNumbers(
                        numbers = listOf(
                            listOf(3, 13, 15, 24, 33, 37),
                            listOf(3, 13, 15, 24, 33, 37)
                        )
                    )
                    numbersDataStore.saveNumbers(sampleData)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("샘플 데이터 저장")
        }

        Button(
            onClick = {
                scope.launch {
                    numbersDataStore.addNumbers(listOf(1, 2, 3, 4, 5, 6))
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("새 번호 추가")
        }

        Button(
            onClick = {
                scope.launch {
                    numbersDataStore.clearNumbers()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("전체 삭제")
        }
    }
}