package com.example.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

object CurrencyFetcher {
    private val ratesCache = mutableMapOf<String, Double>()

    suspend fun getRate(from: String, to: String): Double? = withContext(Dispatchers.IO) {
        val pair = "${from}-${to}"
        if (ratesCache.containsKey(pair)) {
            return@withContext ratesCache[pair]
        }
        
        try {
            val url = URL("https://www.google.com/finance/quote/$from-$to")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("User-Agent", "Mozilla/5.0")
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            if (connection.responseCode == 200) {
                val html = connection.inputStream.bufferedReader().readText()
                
                // Try data-last-price first
                val pattern = Pattern.compile("data-last-price=\"([0-9.]+)\"")
                val matcher = pattern.matcher(html)
                if (matcher.find()) {
                    val rate = matcher.group(1)?.toDoubleOrNull()
                    if (rate != null) {
                        ratesCache[pair] = rate
                        return@withContext rate
                    }
                }
                
                // Fallback to div with class YMlKec fxKbKc
                val fallbackPattern = Pattern.compile("class=\"YMlKec fxKbKc\">([^<]+)</div>")
                val fallbackMatcher = fallbackPattern.matcher(html)
                if (fallbackMatcher.find()) {
                    val rateStr = fallbackMatcher.group(1)?.replace(",", "")
                    val rate = rateStr?.toDoubleOrNull()
                    if (rate != null) {
                        ratesCache[pair] = rate
                        return@withContext rate
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        null
    }
}
