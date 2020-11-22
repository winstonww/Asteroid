package com.udacity.asteroidradar.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModelAsteroidDetails
import com.udacity.asteroidradar.database.asDomainModelAsteroidMain
import com.udacity.asteroidradar.domain.AsteroidDetails
import com.udacity.asteroidradar.domain.AsteroidMain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroidMains : LiveData<List<AsteroidMain>> = Transformations.map(database.dao.getAsteroids()) {
        it.asDomainModelAsteroidMain()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val (startTime, endTime) = getStartEndTime()
            val asteroids = parseAsteroidsJsonResult(
                JSONObject(AsteroidApi.service.getAsteroids(startDate = startTime, endDate = endTime)))
            Log.i("AsteroidRepository", asteroids.toString())
            database.dao.insertAsteroids(*asteroids.asDatabaseModel())
        }
    }

//    suspend fun getAsteroidById(id: Long) : AsteroidDetails {
//        val asteroid = withContext(Dispatchers.IO) {
//             database.dao.getAsteroidById(id).first().asDomainModelAsteroidDetails()
//        }
//        return asteroid
//    }

    fun getStartEndTime() : Array<String> {
        val res : MutableList<String> = mutableListOf()
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        for (i in 0..1) {
            res.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DAY_OF_YEAR, 7)
        }
        return res.toTypedArray()

    }
}