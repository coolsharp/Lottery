package com.coolsharp.lottery.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.coolsharp.lottery.data.LottoNumbers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

// DataStore 인스턴스 생성
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "numbers_datastore")

class NumbersDataStore(private val context: Context) {

    companion object {
        private val NUMBERS_KEY = stringPreferencesKey("numbers_json")
    }

    /**
     * 2차원 배열 데이터를 JSON으로 변환하여 DataStore에 저장
     */
    suspend fun saveNumbers(numbersData: LottoNumbers) {
        try {
            // JSON 객체 생성
            val jsonObject = JSONObject()
            val numbersArray = JSONArray()

            // 2차원 배열 생성
            numbersData.numbers.forEach { numberList ->
                val innerArray = JSONArray()
                numberList.forEach { number ->
                    innerArray.put(number)
                }
                numbersArray.put(innerArray)
            }

            jsonObject.put("numbers", numbersArray)

            // DataStore에 저장
            context.dataStore.edit { preferences ->
                preferences[NUMBERS_KEY] = jsonObject.toString()
            }

            println("DataStore에 저장 완료: ${jsonObject.toString()}")
        } catch (e: Exception) {
            e.printStackTrace()
            println("저장 실패: ${e.message}")
        }
    }

    /**
     * DataStore에서 JSON을 읽어 NumbersData로 변환하는 Flow
     */
    val numbersDataFlow: Flow<LottoNumbers?> = context.dataStore.data
        .map { preferences ->
            try {
                val jsonString = preferences[NUMBERS_KEY] ?: return@map null
                parseJsonToNumbersData(jsonString)
            } catch (e: Exception) {
                e.printStackTrace()
                println("불러오기 실패: ${e.message}")
                null
            }
        }

    /**
     * DataStore에서 데이터를 한 번만 불러오기
     */
    suspend fun getNumbers(): LottoNumbers? {
        return try {
            val preferences = context.dataStore.data.first()
            val jsonString = preferences[NUMBERS_KEY] ?: return null
            parseJsonToNumbersData(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * JSON 문자열을 NumbersData 객체로 파싱
     */
    private fun parseJsonToNumbersData(jsonString: String): LottoNumbers {
        val jsonObject = JSONObject(jsonString)
        val numbersArray = jsonObject.getJSONArray("numbers")

        val numbersList = mutableListOf<List<Int>>()

        // 2차원 배열 파싱
        for (i in 0 until numbersArray.length()) {
            val innerArray = numbersArray.getJSONArray(i)
            val innerList = mutableListOf<Int>()

            for (j in 0 until innerArray.length()) {
                innerList.add(innerArray.getInt(j))
            }

            numbersList.add(innerList)
        }

        return LottoNumbers(numbersList)
    }

    /**
     * 새로운 숫자 배열 추가 (기존 데이터에 append)
     */
    suspend fun addNumbers(newNumbers: List<Int>) {
        try {
            val currentData = getNumbers()
            val updatedList = currentData?.numbers?.toMutableList() ?: mutableListOf()
            updatedList.add(newNumbers)

            saveNumbers(LottoNumbers(updatedList))
            println("새로운 숫자 배열 추가 완료")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 특정 인덱스의 숫자 배열 삭제
     */
    suspend fun removeNumbersAt(index: Int) {
        try {
            val currentData = getNumbers() ?: return
            val updatedList = currentData.numbers.toMutableList()

            if (index in updatedList.indices) {
                updatedList.removeAt(index)
                saveNumbers(LottoNumbers(updatedList))
                println("인덱스 $index 의 숫자 배열 삭제 완료")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 저장된 데이터 삭제
     */
    suspend fun clearNumbers() {
        try {
            context.dataStore.edit { preferences ->
                preferences.remove(NUMBERS_KEY)
            }
            println("DataStore 데이터 삭제 완료")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}