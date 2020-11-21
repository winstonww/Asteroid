package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonElement
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.NetworkAsteroid
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

enum class LoadStatus {
    LOADING,
    DONE,
    ERROR
}

class MainViewModel : ViewModel() {
    private var _asteroids = MutableLiveData<ArrayList<NetworkAsteroid>>()
    val asteroids : LiveData<ArrayList<NetworkAsteroid>>
        get() = _asteroids

    private var _loadAsteroidsStatus = MutableLiveData<LoadStatus>()
    val loadAsteroidStatus : LiveData<LoadStatus>
        get() = _loadAsteroidsStatus

    private var currentJob : Job? = null


    init {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            try {
                _loadAsteroidsStatus.value = LoadStatus.LOADING
                _asteroids.value = parseAsteroidsJsonResult(JSONObject(AsteroidApi.service.getAsteroids(
                    startDate = "2020-11-21",
                    endDate = "2020-11-28"
                )))
                _loadAsteroidsStatus.value = LoadStatus.DONE

            } catch (e: IOException) {
                _loadAsteroidsStatus.value = LoadStatus.ERROR
                Log.i("MainViewModel", "IOException: ${e.toString()}")
            } catch ( e: HttpException) {
                _loadAsteroidsStatus.value = LoadStatus.ERROR
                Log.i("MainViewModel", "HttpException: ${e.toString()}")
            }

            Log.i("MainViewModel", _asteroids.value.toString())
        }
    }
}