package com.tutorial.earthquakemonitor

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize

@Entity(tableName = "eqTable")
data class Earthquake(@PrimaryKey val id: String, val place: String, val magnitude: Double, val time: Long,
                      val longitude: Double, val latitude: Double)