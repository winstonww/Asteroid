package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.AsteroidDetails
import com.udacity.asteroidradar.main.MainViewModel
import com.udacity.asteroidradar.repo.AsteroidRepository
import kotlinx.coroutines.launch

class DetailViewModel(application: Application, asteroidId: Long) : AndroidViewModel(application) {
    val database = getDatabase(application.applicationContext)
    private val repo = AsteroidRepository(database)

    private var _asteroid = MutableLiveData<AsteroidDetails>()
    val asteroid : LiveData<AsteroidDetails>
        get() = _asteroid

    init {
        viewModelScope.launch {
//            _asteroid.value = repo.getAsteroidById(asteroidId)
        }
    }
    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application, val asteroidId: Long) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(app, asteroidId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}