package com.tutorial.earthquakemonitor.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tutorial.earthquakemonitor.Earthquake

@Dao
interface EqDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(eqList: MutableList<Earthquake>)

    @Query("SELECT * FROM eqTable")
    fun getEarthquakes(): LiveData<MutableList<Earthquake>>

    @Query("SELECT * FROM eqTable WHERE magnitude > :mag")
    fun getEarthquakesFromMagnitude(mag: Double): MutableList<Earthquake>

    @Update
    fun updateEq(vararg eq: Earthquake)

    @Delete
    fun deleteEq(vararg eq: Earthquake)
}