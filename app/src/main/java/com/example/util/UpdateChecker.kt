package com.example.util

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class UpdateInfo(
    val isUpdateAvailable: Boolean,
    val latestVersion: String,
    val releaseUrl: String
)

object UpdateChecker {
    suspend fun checkForUpdates(): UpdateInfo? = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://api.github.com/repos/werflubs/Archimedes/releases/latest")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            
            // Accept header for GitHub API
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json")

            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                
                if (json.has("tag_name") && json.has("html_url")) {
                    val latestVersionTag = json.getString("tag_name").removePrefix("v").removePrefix("V")
                    val htmlUrl = json.getString("html_url")
                    
                    val currentVersion = BuildConfig.VERSION_NAME
                    val isUpdate = isNewerVersion(currentVersion, latestVersionTag)
                    
                    return@withContext UpdateInfo(isUpdate, latestVersionTag, htmlUrl)
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return@withContext null
    }

    private fun isNewerVersion(current: String, latest: String): Boolean {
        val currParts = current.split(".").map { it.toIntOrNull() ?: 0 }
        val latestParts = latest.split(".").map { it.toIntOrNull() ?: 0 }
        
        val maxLength = maxOf(currParts.size, latestParts.size)
        for (i in 0 until maxLength) {
            val c = currParts.getOrElse(i) { 0 }
            val l = latestParts.getOrElse(i) { 0 }
            if (l > c) return true
            if (l < c) return false
        }
        return false
    }
}
