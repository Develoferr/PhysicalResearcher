package com.tutorial.earthquakemonitor.main

import androidx.lifecycle.LiveData
import com.tutorial.earthquakemonitor.Earthquake
import com.tutorial.earthquakemonitor.api.EqJsonResponse
import com.tutorial.earthquakemonitor.api.service
import com.tutorial.earthquakemonitor.database.EqDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository (private val dataBase: EqDatabase) {

    val eqList: LiveData<MutableList<Earthquake>> = dataBase.eqDao.getEarthquakes()

    suspend fun fetchEarthquake(): MutableList<Earthquake> {
        return withContext(Dispatchers.IO) {
            val eqJsonResponse: EqJsonResponse = service.getLastHourEarthquakes()
            val eqList = parseEqResult(eqJsonResponse)
            dataBase.eqDao.insertAll(eqList)

            return@withContext eqList
        }
    }

    private fun parseEqResult(eqJsonResponse: EqJsonResponse): MutableList<Earthquake> {
        val eqList = mutableListOf<Earthquake>()
        val featureList = eqJsonResponse.features

        for (feature in featureList){
            val properties = feature.properties

            val id = feature.id
            val magnitude = properties.mag
            val place = properties.place
            val time = properties.time

            val geometry = feature.geometry
            val longitude = geometry.longitude
            val latitude = geometry.latitude

            eqList.add(Earthquake(id, place, magnitude, time, longitude, latitude))
        }

        return eqList
    }
}