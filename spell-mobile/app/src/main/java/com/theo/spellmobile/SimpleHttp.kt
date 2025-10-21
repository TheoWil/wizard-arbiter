package com.theo.spellmobile

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object SimpleHttp {
    /**
     * Sends a simple HTTP request and returns true if successful
     * @param url The URL to send the request to
     * @param method HTTP method (GET, POST, etc.) - defaults to GET
     */
    suspend fun sendRequest(url: String, method: String = "GET"): Boolean = withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.apply {
                    requestMethod = method
                    connectTimeout = 6000
                    readTimeout = 6000
                    setRequestProperty("User-Agent", "SpellMobile-Android-App")
                    // Add these headers to help with CORS and ensure request goes through
                    setRequestProperty("Accept", "*/*")
                    setRequestProperty("Connection", "close")
                }

                //Sends the http request and reads the response code
                val responseCode = connection.responseCode
                //cleans up the connection
                connection.disconnect()

                // Consider 2xx and 3xx status codes as success
                responseCode in 200..399

            } catch (e: Exception) {
                System.out.println("Request failed: ${e.message}")
                e.printStackTrace()
                false
            }
    }
}