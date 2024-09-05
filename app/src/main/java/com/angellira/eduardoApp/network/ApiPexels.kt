package com.angellira.eduardoApp.network

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.random.Random

class PexelsApi {

    private val client = OkHttpClient()

    fun getRandomPhotoUrl(query: String = "random", perPage: Int = 30000): String? {
        val request = Request.Builder()
            .url("https://api.pexels.com/v1/search?query=$query&per_page=$perPage")
            .addHeader("Authorization", "gPGH2MVmgsKK5AW40RgK6sKXFRgni6uJPKjfCdCmUtBu02UHrXB9chzt")
            .build()

        val response = client.newCall(request).execute()
        val jsonData = response.body?.string()

        if (jsonData != null) {
            val jsonObject = JSONObject(jsonData)
            val photos = jsonObject.getJSONArray("photos")

            if (photos.length() > 0) {
                val randomIndex = Random.nextInt(photos.length())
                val randomPhoto = photos.getJSONObject(randomIndex)

                return randomPhoto.getJSONObject("src").getString("original")
            }
        }
        return null
    }
}