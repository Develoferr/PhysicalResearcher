package com.tutorial.earthquakemonitor.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.tutorial.earthquakemonitor.Earthquake
import com.tutorial.earthquakemonitor.api.ApiResponseStatus
import com.tutorial.earthquakemonitor.database.getDatabase
import kotlinx.coroutines.*
import java.net.UnknownHostException

private val TAG = MainViewModel::class.java.simpleName
class MainViewModel(application: Application): AndroidViewModel(application) {
    private val database = getDatabase(application.applicationContext)
    private val repository = MainRepository(database)

    private val _status = MutableLiveData<ApiResponseStatus>()
    val status: LiveData<ApiResponseStatus>
        get() = _status

    private var _eqList = MutableLiveData<MutableList<Earthquake>>()
    val eqList: LiveData<MutableList<Earthquake>>
        get() = _eqList

    init {
        reloadEarthquakes (false)
    }

    private fun reloadEarthquakes(sortByMagnitude: Boolean) {
        viewModelScope.launch {
            try {
                _status.value = ApiResponseStatus.LOADING
                _eqList.value = repository.fetchEarthquake(sortByMagnitude)
                _status.value = ApiResponseStatus.DONE

            } catch (e: UnknownHostException) {
                _status.value = ApiResponseStatus.NO_INTERNET_CONNECTION
                Log.d(TAG, "No internet connection.", e)
            }
        }
    }

    fun reloadEarthquakesFromDb(sortByMagnitude: Boolean) {
        viewModelScope.launch {
            _eqList.value = repository.fetchEarthquakeFromDb(sortByMagnitude)
        }
    }
}