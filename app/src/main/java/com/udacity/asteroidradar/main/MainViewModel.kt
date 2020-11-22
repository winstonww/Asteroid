package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repo.AsteroidRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

enum class LoadStatus {
    LOADING,
    DONE,
    ERROR
}

class MainViewModel(application: Application) : AndroidViewModel(application) {


    val repo = AsteroidRepository(getDatabase(application.applicationContext))
    val asteroidMains = repo.asteroidMains

    private var _loadAsteroidsStatus = MutableLiveData<LoadStatus>()
    val loadAsteroidStatus : LiveData<LoadStatus>
        get() = _loadAsteroidsStatus

    private var currentJob : Job? = null


    init {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            try {
                _loadAsteroidsStatus.value = LoadStatus.LOADING
                repo.refreshAsteroids()
                _loadAsteroidsStatus.value = LoadStatus.DONE

            } catch (e: IOException) {
                _loadAsteroidsStatus.value = LoadStatus.ERROR
                Log.i("MainViewModel", "IOException: ${e.toString()}")
            } catch ( e: HttpException) {
                _loadAsteroidsStatus.value = LoadStatus.ERROR
                Log.i("MainViewModel", "HttpException: ${e.toString()}")
            }

            Log.i("MainViewModel", asteroidMains.value.toString())
        }
    }

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}