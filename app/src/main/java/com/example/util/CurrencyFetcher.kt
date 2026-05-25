package com.example.util

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object CurrencyFetcher {
    private val ratesCache = mutableMapOf<String, Double>()
    private var lastFetchTime = 0L
    private val cacheDuration = 1000 * 60 * 60 // 1 hour

    suspend fun getRate(from: String, to: String): Double? = withContext(Dispatchers.IO) {
        val pair = "${from}-${to}"
        val now = System.currentTimeMillis()
        
        if (ratesCache.containsKey(pair) && now - lastFetchTime < cacheDuration) {
            return@withContext ratesCache[pair]
        }
        
        try {
            val urlStr = "https://open.er-api.com/v6/latest/\$from"
            Log.d("CurrencyFetcher", "Fetching: \$urlStr")
            val url = URL(urlStr)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val responseCode = connection.responseCode
            Log.d("CurrencyFetcher", "Response code: \$responseCode")
            
            if (responseCode == 200) {
                val jsonStr = connection.inputStream.bufferedReader().readText()
                val json = JSONObject(jsonStr)
                if (json.getString("result") == "success") {
                    val rates = json.getJSONObject("rates")
                    if (rates.has(to)) {
                        val rate = rates.getDouble(to)
                        Log.d("CurrencyFetcher", "Found rate for \$pair: \$rate")
                        ratesCache[pair] = rate
                        lastFetchTime = now
                        return@withContext rate
                    } else {
                        Log.e("CurrencyFetcher", "Rate for \$to not found in response")
                    }
                } else {
                    Log.e("CurrencyFetcher", "API error: " + json.optString("error-type"))
                }
            } else {
                Log.e("CurrencyFetcher", "Error response: \$responseCode")
            }
        } catch (e: Exception) {
            Log.e("CurrencyFetcher", "Exception: \${e.message}", e)
        }
        null
    }
}
