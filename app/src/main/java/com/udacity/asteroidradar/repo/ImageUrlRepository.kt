package com.udacity.asteroidradar.repo

import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.database.AsteroidDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ImageUrlRepository(private val database: AsteroidDatabase) {

    suspend fun getImageOfTheDayUrl(): String {
        val url = withContext(Dispatchers.IO) {
            val json = AsteroidApi.service.getImageOfTheDayUrl()
            JSONObject(json).getString("url")
        }
        return url
    }
}