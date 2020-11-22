package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repo.AsteroidRepository
import com.udacity.asteroidradar.repo.ImageUrlRepository
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

    val imageUrlRepo = ImageUrlRepository(getDatabase(application.applicationContext))
    val asteroidRepo = AsteroidRepository(getDatabase(application.applicationContext))
    val asteroidMains = asteroidRepo.asteroidMains

    private var _imageOfTheDayURl = MutableLiveData<String>()
    val imageOfTheDayUrl : LiveData<String>
        get() = _imageOfTheDayURl

    private val _loadImageOfTheDayStatus = MutableLiveData<LoadStatus>()
    val loadImageOfTheDayStatus : LiveData<LoadStatus>
        get() = _loadImageOfTheDayStatus


    private var _loadAsteroidsStatus = MutableLiveData<LoadStatus>()
    val loadAsteroidStatus : LiveData<LoadStatus>
        get() = _loadAsteroidsStatus


    private var _navigateDetails = MutableLiveData<Long?>()
    val navigateDetails : LiveData<Long?>
        get() = _navigateDetails



    init {
        viewModelScope.launch {
            try {
                _loadAsteroidsStatus.value = LoadStatus.LOADING
                asteroidRepo.refreshAsteroids()
                _loadAsteroidsStatus.value = LoadStatus.DONE

            } catch (e: IOException) {
                _loadAsteroidsStatus.value = LoadStatus.ERROR
                Log.i("MainViewModel", "IOException: ${e.toString()}")
            } catch (e: HttpException) {
                _loadAsteroidsStatus.value = LoadStatus.ERROR
                Log.i("MainViewModel", "HttpException: ${e.toString()}")
            }
        }

        _navigateDetails.value = null

        viewModelScope.launch {
            try {
                _loadImageOfTheDayStatus.value = LoadStatus.LOADING
                Log.i("MainViewModel", "Get Image URL")
                _imageOfTheDayURl.value = imageUrlRepo.getImageOfTheDayUrl()
                _loadImageOfTheDayStatus.value = LoadStatus.DONE
            } catch (e: IOException) {
                _loadImageOfTheDayStatus.value = LoadStatus.ERROR
                Log.i("MainViewModel", "IOException: ${e.toString()}")
            } catch ( e: HttpException) {
                _loadImageOfTheDayStatus.value = LoadStatus.ERROR
                Log.i("MainViewModel", "HttpException: ${e.toString()}")
            }
        }
    }

    fun navigateToDetails(id: Long) {
        _navigateDetails.value = id
    }

    fun onNavigateToDetailsComplete() {
        _navigateDetails.value = null
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