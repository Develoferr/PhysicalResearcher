package com.tutorial.earthquakemonitor.main

import com.tutorial.earthquakemonitor.Earthquake
import com.tutorial.earthquakemonitor.api.EqJsonResponse
import com.tutorial.earthquakemonitor.api.service
import com.tutorial.earthquakemonitor.database.EqDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository (private val dataBase: EqDatabase) {

    suspend fun fetchEarthquake(sortByMagnitude: Boolean): MutableList<Earthquake> {
        return withContext(Dispatchers.IO) {
            val eqJsonResponse: EqJsonResponse = service.getLastHourEarthquakes()
            val eqList = parseEqResult(eqJsonResponse)
            dataBase.eqDao.insertAll(eqList)

            fetchEarthquakeFromDb(sortByMagnitude)
        }
    }
    suspend fun fetchEarthquakeFromDb(sortByMagnitude: Boolean): MutableList<Earthquake> {
        return withContext(Dispatchers.IO) {
            if (sortByMagnitude) {
                dataBase.eqDao.getEarthquakesByMagnitude()
            } else {
                dataBase.eqDao.getEarthquakes()
            }
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